package io.github.ssppeeeedd.upgrade.item;

import io.github.ssppeeeedd.upgrade.entity.PeterRabbitEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BlueJacketItem extends Item {
    public static final String JACKET_TAG = "WearingBlueJacket";

    public BlueJacketItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, net.minecraft.world.InteractionHand hand) {
        if (!(target instanceof Rabbit rabbit) || target instanceof PeterRabbitEntity) {
            return InteractionResult.PASS;
        }

        if (!target.getPersistentData().getBoolean(JACKET_TAG)) {
            target.getPersistentData().putBoolean(JACKET_TAG, true);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(target.level().isClientSide);
    }
}
