package mc.jeryn.dev.angels;

import mc.jeryn.dev.angels.registry.WAEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;

public class WeepingAngelsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        entityRenders();
    }

    private void entityRenders() {
        EntityRendererRegistry.register(WAEntities.WEEPING_ANGEL, NoopRenderer::new);
    }
}
