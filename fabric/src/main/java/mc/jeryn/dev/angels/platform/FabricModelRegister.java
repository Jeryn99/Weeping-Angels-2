package mc.jeryn.dev.angels.platform;

import mc.jeryn.dev.angels.platform.services.IModelRegister;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public class FabricModelRegister implements IModelRegister {
    @Override
    public ModelLayerLocation register(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        EntityModelLayerRegistry.registerModelLayer(location, definition::get);
        return location;
    }
}
