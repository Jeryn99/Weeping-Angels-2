package mc.jeryn.dev.angels.registry;

import mc.jeryn.dev.angels.util.AngelUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DetectorItem extends Item {

    public DetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel serverLevel, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
        super.inventoryTick(itemStack, serverLevel, entity, equipmentSlot);

        if (!entity.level().isClientSide) {
            List<Entity> angels = AngelUtil.getAnomaliesAroundEntity(entity, 64);

            if (entity instanceof Player player) {
                boolean isSelected = player.getMainHandItem() == itemStack || player.getOffhandItem() == itemStack;

                if (!angels.isEmpty() && isSelected && entity.tickCount % 20 == 0) {
                    serverLevel.playSound(
                            null,
                            player.getX(), player.getY(), player.getZ(),
                            WASounds.DING,
                            SoundSource.PLAYERS,
                            0.2F,
                            1.0F
                    );
                }
            }
        }
    }


}