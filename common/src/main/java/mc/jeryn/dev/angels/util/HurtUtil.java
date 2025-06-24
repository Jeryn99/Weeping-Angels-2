package mc.jeryn.dev.angels.util;

import mc.jeryn.dev.angels.CommonClass;
import mc.jeryn.dev.angels.registry.WASounds;
import mc.jeryn.dev.angels.registry.damage.WADamageTypes;
import mc.jeryn.dev.angels.registry.entity.WeepingAngel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class HurtUtil {

    public static boolean validatePickaxe(Player player, WeepingAngel weepingAngel, Predicate<ItemStack> predicate) {
        ItemStack heldItem = player.getItemBySlot(EquipmentSlot.MAINHAND);
        if (weepingAngel.getVariant().getDrops().getItem() instanceof BlockItem blockItem) {
            return predicate.test(heldItem) && heldItem.getItem().isCorrectToolForDrops(heldItem, blockItem.getBlock().defaultBlockState());
        }
        return false;
    }

    public static boolean handleAngelHurt(WeepingAngel weepingAngel, DamageSource pSource, float pAmount) {

        if (pSource.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            return true; // Required for /kill command else...yeah
        }

        HurtType hurtType = CommonClass.CONFIG.hurtType();
        switch (hurtType) {
            case NONE -> {
                return false;
            }
            case PICKAXE -> {
                return hasPickAxe(weepingAngel, pSource, itemStack -> true);
            }
            case GENERATOR -> {
                return pSource.is(WADamageTypes.GENERATOR);
            }
            case PICKAXE_AND_GENERATOR -> {
                return pSource.is(WADamageTypes.GENERATOR) || hasPickAxe(weepingAngel, pSource, itemStack -> true);
            }
            case ANYTHING -> {
                return true;
            }
        }
        return false;
    }

    private static boolean hasPickAxe(WeepingAngel weepingAngel, DamageSource pSource, Predicate<ItemStack> predicate) {
        if (pSource.getEntity() instanceof ServerPlayer player) {
            boolean hasPickAxe = validatePickaxe(player, weepingAngel, predicate);
            if (!hasPickAxe) {
                if (weepingAngel.level().random.nextInt(100) <= 10) {
                    weepingAngel.playSound(WASounds.ANGEL_MOCKING);
                }
                if (player.level() instanceof ServerLevel serverLevel) {
                    player.hurt(WADamageTypes.getSource(serverLevel, WADamageTypes.PUNCH_STONE), weepingAngel.level().random.nextInt(5));
                }
                return false;
            }
            ItemStack stack = player.getItemBySlot(EquipmentSlot.MAINHAND);
            stack.hurtAndBreak(weepingAngel.level().random.nextInt(4),
                    player.serverLevel(), player, item -> weepingAngel.playSound(WASounds.ANGEL_MOCKING));


            return true;
        }
        return false;
    }

    public enum HurtType {
        PICKAXE, PICKAXE_AND_GENERATOR, NONE, GENERATOR, ANYTHING
    }
}