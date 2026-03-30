package io.github.ssppeeeedd.upgrade.item;

import io.github.ssppeeeedd.upgrade.integration.MekanismUpgradeBridge;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class SsppeeeeddUpgradeCardItem extends Item {

    public SsppeeeeddUpgradeCardItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return MekanismUpgradeBridge.tryApplyUpgradeCard(context);
    }
}
