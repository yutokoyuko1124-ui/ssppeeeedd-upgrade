package io.github.ssppeeeedd.upgrade.registry;

import io.github.ssppeeeedd.upgrade.SsppeeeeddUpgradeMod;
import io.github.ssppeeeedd.upgrade.entity.PeterRabbitEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SsppeeeeddUpgradeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntityAttributes {
    private ModEntityAttributes() {
    }

    @SubscribeEvent
    public static void onEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.PETER_RABBIT.get(), PeterRabbitEntity.createAttributes().build());
    }
}
