package io.github.ssppeeeedd.upgrade.registry;

import io.github.ssppeeeedd.upgrade.SsppeeeeddUpgradeMod;
import io.github.ssppeeeedd.upgrade.item.SsppeeeeddUpgradeCardItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SsppeeeeddUpgradeMod.MOD_ID);

    public static final RegistryObject<Item> SSPPEEEEDD_UPGRADE_CARD = ITEMS.register(
            "ssppeeeedd_upgrade_card",
            () -> new SsppeeeeddUpgradeCardItem(new Item.Properties().stacksTo(64))
    );

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
