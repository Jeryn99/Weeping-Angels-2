package mc.jeryn.dev.angels.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mc.jeryn.dev.angels.client.model.AliceAngelModel;
import mc.jeryn.dev.angels.client.model.BaseAngelModel;
import mc.jeryn.dev.angels.client.model.ModelRegistration;
import mc.jeryn.dev.angels.registry.entity.WeepingAngel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class WeepingAngelRenderer extends AgeableMobRenderer<WeepingAngel, WeepingAngelRenderState, BaseAngelModel> {
    public WeepingAngelRenderer(EntityRendererProvider.Context context) {
        super(context,
                new AliceAngelModel(context.bakeLayer(ModelRegistration.ALICE_ANGEL)),
                new AliceAngelModel(context.bakeLayer(ModelRegistration.ALICE_ANGEL)),
                0.0F
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

        super.extractRenderState(angel, state, partialTick);

        state.variant = angel.getVariant();
        state.emotion = angel.getEmotion();
        state.texture = model.texture(state.emotion, state.variant);
        state.deathTime = angel.deathTime;
        state.weepingAngel = angel;
    }

    @Override
    protected void setupRotations(WeepingAngelRenderState renderState, PoseStack poseStack, float bodyRot, float scale) {

        if (renderState.deathTime > 0) {
            float progress = Math.min(renderState.deathTime / 20.0F, 1.0F);
            float shake = (float) Math.sin(renderState.ageInTicks * 10.0F) * 2.0F * (1.0F - progress);

            poseStack.mulPose(Axis.XP.rotationDegrees(-progress * 45.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(shake + (float) Math.sin(progress * Math.PI) * 15.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) Math.sin(progress * Math.PI * 2.0F) * 10.0F));
            poseStack.translate(0.0F, -progress * 0.5F, 0.0F);
        } else {
            super.setupRotations(renderState, poseStack, bodyRot, scale);
        }

    }


}

