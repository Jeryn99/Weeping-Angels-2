package mc.jeryn.dev.angels.registry;

import mc.jeryn.dev.angels.client.screen.VIPListScreen;
import mc.jeryn.dev.angels.data.model.donators.VIPCacheManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class TestItem extends Item {
    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().isClientSide) {
            Minecraft.getInstance().setScreen(new VIPListScreen());
        }
        return super.useOn(context);
    }
}
