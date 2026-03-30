package io.github.ssppeeeedd.upgrade.integration;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MekanismIntegrationHandler {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        MekanismUpgradeBridge.detectMekanism();
    }
}
