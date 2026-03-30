package io.github.ssppeeeedd.upgrade;

import io.github.ssppeeeedd.upgrade.integration.MekanismIntegrationHandler;
import io.github.ssppeeeedd.upgrade.registry.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SsppeeeeddUpgradeMod.MOD_ID)
public class SsppeeeeddUpgradeMod {
    public static final String MOD_ID = "ssppeeeeddupgrade";

    public SsppeeeeddUpgradeMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new MekanismIntegrationHandler());
    }
}
