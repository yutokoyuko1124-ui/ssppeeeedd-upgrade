package io.github.ssppeeeedd.upgrade.entity;

import io.github.ssppeeeedd.upgrade.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class PeterRabbitEntity extends Rabbit {
    private static final EntityDataAccessor<Boolean> TAMED = SynchedEntityData.defineId(PeterRabbitEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(PeterRabbitEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final String TAG_TAMED = "PeterTamed";
    private static final String TAG_OWNER = "PeterOwner";
    private int cropHarvestCooldown;

    public PeterRabbitEntity(EntityType<? extends Rabbit> entityType, Level level) {
        super(entityType, level);
        this.setRabbitType(Rabbit.Variant.BROWN);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2D, stack -> stack.is(Items.GOLDEN_CARROT), false));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TAMED, false);
        this.entityData.define(OWNER_UUID, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean(TAG_TAMED, this.isTamed());
        if (this.getOwnerUUID() != null) {
            tag.putUUID(TAG_OWNER, this.getOwnerUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setTamed(tag.getBoolean(TAG_TAMED));
        if (tag.hasUUID(TAG_OWNER)) {
            this.setOwnerUUID(tag.getUUID(TAG_OWNER));
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (held.is(Items.GOLDEN_CARROT) && !this.isTamed()) {
            if (!this.level().isClientSide) {
                this.setTamed(true);
                this.setOwnerUUID(player.getUUID());
                this.navigation.stop();
                this.level().broadcastEntityEvent(this, (byte) 7);
                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            for (int i = 0; i < 7; i++) {
                this.level().addParticle(ParticleTypes.HEART,
                        this.getRandomX(0.5D),
                        this.getRandomY() + 0.2D,
                        this.getRandomZ(0.5D),
                        0.0D,
                        0.0D,
                        0.0D);
            }
            return;
        }
        super.handleEntityEvent(id);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            return;
        }

        LivingEntity owner = this.getOwner();
        if (owner != null) {
            this.supportOwnerCombat(owner);
            this.followOwner(owner);
        }
        this.tryCarrotAttack();
        this.tryHarvestCrops();
    }

    private void supportOwnerCombat(LivingEntity owner) {
        LivingEntity ownerTarget = owner.getLastHurtMob();
        if (ownerTarget != null && ownerTarget.isAlive() && ownerTarget != this) {
            this.setTarget(ownerTarget);
        }
    }

    private void followOwner(LivingEntity owner) {
        double distanceSqr = this.distanceToSqr(owner);
        if (distanceSqr > 100.0D) {
            this.teleportTo(owner.getX(), owner.getY(), owner.getZ());
        } else if (distanceSqr > 9.0D) {
            this.getNavigation().moveTo(owner, 1.25D);
        }
    }

    private void tryCarrotAttack() {
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }
        if (this.random.nextFloat() < 0.02F && this.distanceToSqr(target) < 4.0D) {
            target.hurt(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
            this.level().playSound(null, this.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.NEUTRAL, 0.6F, 1.8F);
        }
    }

    private void tryHarvestCrops() {
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return;
        }

        if (this.cropHarvestCooldown > 0) {
            this.cropHarvestCooldown--;
            return;
        }

        BlockPos center = this.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-3, -1, -3), center.offset(3, 1, 3))) {
            BlockState state = this.level().getBlockState(pos);
            if (!(state.getBlock() instanceof CropBlock crop) || !crop.isMaxAge(state)) {
                continue;
            }

            this.level().destroyBlock(pos, true, this);
            ItemStack base = crop.getCloneItemStack(this.level(), pos, state);
            if (!base.isEmpty()) {
                Block.popResource(this.level(), pos, new ItemStack(base.getItem(), 2));
            }
            this.cropHarvestCooldown = 60;
            return;
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(damageSource, looting, recentlyHit);
        this.spawnAtLocation(ModItems.BLUE_JACKET.get());
    }

    @Override
    @Nullable
    public Rabbit getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return this.getType().create(level);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevel level, DifficultyInstance difficulty, MobSpawnType reason,
                                        @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        this.setRabbitType(Rabbit.Variant.BROWN);
        this.setCustomName(Component.literal("Peter"));
        this.setCustomNameVisible(true);
        return data;
    }

    public boolean isTamed() {
        return this.entityData.get(TAMED);
    }

    public void setTamed(boolean tamed) {
        this.entityData.set(TAMED, tamed);
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID owner) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(owner));
    }

    @Nullable
    public LivingEntity getOwner() {
        UUID ownerUUID = this.getOwnerUUID();
        if (ownerUUID == null || !(this.level() instanceof ServerLevel serverLevel)) {
            return null;
        }
        Entity entity = serverLevel.getEntity(ownerUUID);
        return entity instanceof LivingEntity living ? living : null;
    }
}
