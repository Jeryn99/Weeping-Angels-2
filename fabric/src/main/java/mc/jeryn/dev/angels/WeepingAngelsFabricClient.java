package mc.jeryn.dev.angels;

import mc.jeryn.dev.angels.client.WeepingAngelRenderer;
import mc.jeryn.dev.angels.client.model.ModelRegistration;
import mc.jeryn.dev.angels.registry.WAEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class WeepingAngelsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModelRegistration.init();
        entityRenders();
    }

    private void entityRenders() {
        EntityRendererRegistry.register(WAEntities.WEEPING_ANGEL, WeepingAngelRenderer::new);
    }
}
