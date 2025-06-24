package mc.jeryn.dev.angels.registry;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class TestItem extends Item {
    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
       // Minecraft.getInstance().setScreen(new DonatorScreen(VIPCacheManager.getVIPs(), null));
        return super.useOn(context);
    }
}
