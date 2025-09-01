package mc.jeryn.dev.angels.platform.services;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public interface IModelRegister {

    ModelLayerLocation register(ModelLayerLocation location, Supplier<LayerDefinition> definition);

}
