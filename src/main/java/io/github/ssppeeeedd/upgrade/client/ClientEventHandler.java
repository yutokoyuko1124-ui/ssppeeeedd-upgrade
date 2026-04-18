package io.github.ssppeeeedd.upgrade.client;

import io.github.ssppeeeedd.upgrade.SsppeeeeddUpgradeMod;
import io.github.ssppeeeedd.upgrade.registry.ModEntityTypes;
import net.minecraft.client.renderer.entity.RabbitRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SsppeeeeddUpgradeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEventHandler {
    private ClientEventHandler() {
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.PETER_RABBIT.get(), RabbitRenderer::new);
    }
}
