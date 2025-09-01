package mc.jeryn.dev.angels.client;

import mc.jeryn.dev.angels.registry.AngelVariants;
import mc.jeryn.dev.angels.registry.entity.AngelEmotion;
import mc.jeryn.dev.angels.registry.entity.WeepingAngel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class WeepingAngelRenderState extends LivingEntityRenderState {
    public AngelVariants variant;
    public AngelEmotion emotion;
    public ResourceLocation texture;
    public WeepingAngel weepingAngel;

    public WeepingAngelRenderState() {
        this.variant = AngelVariants.STONE;
        this.emotion = AngelEmotion.IDLE;
     //   this.texture = AngelVariants.getModelFor(variant).texture(emotion, variant);
    }
}
