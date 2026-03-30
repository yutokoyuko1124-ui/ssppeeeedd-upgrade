package io.github.ssppeeeedd.upgrade.integration;

import java.lang.reflect.Method;
import java.util.Arrays;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class MekanismUpgradeBridge {

    private static volatile boolean mekanismPresent;
    private static volatile boolean mekanismChecked;

    private MekanismUpgradeBridge() {
    }

    public static void detectMekanism() {
        if (mekanismChecked) {
            return;
        }
        mekanismChecked = true;
        try {
            Class.forName("mekanism.api.Upgrade");
            mekanismPresent = true;
        } catch (ClassNotFoundException ignored) {
            mekanismPresent = false;
        }
    }

    public static InteractionResult tryApplyUpgradeCard(UseOnContext context) {
        detectMekanism();
        if (!mekanismPresent || context.getLevel().isClientSide) {
            return InteractionResult.PASS;
        }

        BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if (blockEntity == null || !blockEntity.getClass().getName().startsWith("mekanism.")) {
            return InteractionResult.PASS;
        }

        boolean applied = tryReflectiveApply(blockEntity);
        if (applied) {
            ItemStack stack = context.getItemInHand();
            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        if (context.getPlayer() instanceof ServerPlayer player) {
            player.displayClientMessage(Component.translatable("message.ssppeeeeddupgrade.apply_failed")
                    .withStyle(ChatFormatting.RED), true);
        }
        return InteractionResult.FAIL;
    }

    private static boolean tryReflectiveApply(BlockEntity blockEntity) {
        // Best-effort bridge: works with Mekanism methods when available,
        // while keeping this addon resilient to minor API/package changes.
        try {
            Class<?> upgradeClass = Class.forName("mekanism.api.Upgrade");
            Object speedUpgrade = Enum.valueOf((Class<Enum>) upgradeClass.asSubclass(Enum.class), "SPEED");

            Method[] methods = blockEntity.getClass().getMethods();

            Method supportsMethod = Arrays.stream(methods)
                    .filter(m -> m.getName().equals("supportsUpgrade"))
                    .filter(m -> m.getParameterCount() == 1)
                    .findFirst()
                    .orElse(null);

            if (supportsMethod != null) {
                Object supports = supportsMethod.invoke(blockEntity, speedUpgrade);
                if (supports instanceof Boolean bool && !bool) {
                    return false;
                }
            }

            Method getCount = Arrays.stream(methods)
                    .filter(m -> m.getName().equals("getUpgradeCount"))
                    .filter(m -> m.getParameterCount() == 1)
                    .findFirst()
                    .orElse(null);
            Method setCount = Arrays.stream(methods)
                    .filter(m -> m.getName().equals("setUpgradeCount"))
                    .filter(m -> m.getParameterCount() == 2)
                    .findFirst()
                    .orElse(null);

            if (getCount != null && setCount != null) {
                Object count = getCount.invoke(blockEntity, speedUpgrade);
                if (count instanceof Integer current && current < 64) {
                    setCount.invoke(blockEntity, speedUpgrade, current + 1);
                    blockEntity.setChanged();
                    return true;
                }
            }
        } catch (ReflectiveOperationException ignored) {
            // No-op: handled by caller with user feedback.
        }
        return false;
    }
}
