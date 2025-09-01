package mc.jeryn.dev.angels.fabric.events;

import mc.jeryn.dev.angels.registry.entity.BlockReactions;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class WAFabricServerEvents {

    public static void init(){
        ServerLifecycleEvents.SERVER_STARTED.register(server -> BlockReactions.init());
    }

}
