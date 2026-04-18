package io.github.ssppeeeedd.upgrade.registry;

import io.github.ssppeeeedd.upgrade.SsppeeeeddUpgradeMod;
import io.github.ssppeeeedd.upgrade.entity.PeterRabbitEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntityTypes {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SsppeeeeddUpgradeMod.MOD_ID);

    public static final RegistryObject<EntityType<PeterRabbitEntity>> PETER_RABBIT = ENTITY_TYPES.register(
            "peter_rabbit",
            () -> EntityType.Builder.of(PeterRabbitEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 0.7F)
                    .clientTrackingRange(8)
                    .build("peter_rabbit")
    );

    private ModEntityTypes() {
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
