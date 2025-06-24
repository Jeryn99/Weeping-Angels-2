package mc.jeryn.dev.angels.client;

/*
import com.mojang.blaze3d.vertex.PoseStack;
import mc.jeryn.dev.angels.registry.entity.WeepingAngel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WeepingAngelRenderer extends AgeableMobRenderer<WeepingAngel, WeepingAngelRenderState, AngelModel> {
    public WeepingAngelRenderer(EntityRendererProvider.Context context) {
        super(context,
                ModelRegistration.getModelFor(AngelVariant.STONE), // Default adult model
                ModelRegistration.getModelFor(AngelVariant.STONE), // Reuse same model for "baby", or customize
                0.0F // Shadow size
        );

    }

    @Override
    public ResourceLocation getTextureLocation(WeepingAngelRenderState state) {
        return state.texture;
    }

    @Override
    public WeepingAngelRenderState createRenderState() {
        return new WeepingAngelRenderState();
    }

    @Override
    public void extractRenderState(WeepingAngel angel, WeepingAngelRenderState state, float partialTick) {

        state.variant = angel.getVariant();
        state.emotion = angel.getEmotion();
        state.texture = ModelRegistration.getModelFor(state.variant).texture(state.emotion, state.variant);
        state.deathTime = angel.deathTime;
    }

    @Override
    protected void setupRotations(WeepingAngelRenderState renderState, PoseStack poseStack, float bodyRot, float scale) {
        if (renderState.deathTime > 0) return;
        super.setupRotations(renderState, poseStack, bodyRot, scale);
    }

}
*/
