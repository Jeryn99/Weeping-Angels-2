package mc.jeryn.dev.angels.platform;

import mc.jeryn.dev.angels.platform.services.IModelRegister;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NeoForgeModelRegister implements IModelRegister {

    private static final Map<ModelLayerLocation, Supplier<LayerDefinition>> DEFINITIONS = new HashMap<>();

    public NeoForgeModelRegister() {
        NeoForge.EVENT_BUS.register(this);
    }

    @Override
    public ModelLayerLocation register(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        DEFINITIONS.put(location, definition);
        return location;
    }

    @SubscribeEvent
    public void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        DEFINITIONS.forEach(event::registerLayerDefinition);
    }
}
