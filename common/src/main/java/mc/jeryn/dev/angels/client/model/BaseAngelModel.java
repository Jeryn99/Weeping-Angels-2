package mc.jeryn.dev.angels.client.model;

import mc.jeryn.dev.angels.client.WeepingAngelRenderState;
import mc.jeryn.dev.angels.registry.AngelVariants;
import mc.jeryn.dev.angels.registry.entity.AngelEmotion;
import mc.jeryn.dev.angels.registry.entity.WeepingAngel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

import static mc.jeryn.dev.angels.client.model.AliceAngelModel.IDLE2;

public abstract class BaseAngelModel extends EntityModel<WeepingAngelRenderState> {

    public BaseAngelModel(ModelPart root) {
        super(root);
    }

    public abstract Iterable<ModelPart> getWings();

    public abstract ModelPart getHead();

    public static boolean isBlockPosBehindPlayer(Player player, BlockPos blockPos) {
        // Get the player's facing direction as a normalized vector
        Vec3 playerFacing = player.getLookAngle().normalize();

        // Get the vector from the player to the BlockPos
        Vec3 playerPos = player.position();
        Vec3 toBlockPos = new Vec3(blockPos.getX() + 0.5 - playerPos.x, blockPos.getY() + 0.5 - playerPos.y, blockPos.getZ() + 0.5 - playerPos.z).normalize();

        // Calculate the dot product of the two vectors
        double dotProduct = playerFacing.dot(toBlockPos);

        // If the dot product is less than 0, the BlockPos is behind the player
        return dotProduct < 0;
    }

    public ResourceLocation texture(AngelEmotion angelEmotion, AngelVariants angelVariant) {
        return DefaultPlayerSkin.getDefaultSkin().texture();
    }

    public AnimationDefinition poseForId(int index) {
        return getAnimationDefinition(index);
    }

    public static AnimationDefinition getAnimationDefinition(int index) {
        return switch (index) {
            case 1 -> AliceAngelModel.IDLE1;
            case 2 -> AliceAngelModel.IDLE2;
            case 3 -> AliceAngelModel.IDLE3;
            case 4 -> AliceAngelModel.IDLE4;
            case 5 -> AliceAngelModel.IDLE5;
            case 6 -> AliceAngelModel.IDLE6;
            case 7 -> AliceAngelModel.IDLE7;
            case 8 -> AliceAngelModel.ANGRY1;
            case 9 -> AliceAngelModel.ANGRY2;
            case 10 -> AliceAngelModel.ANGRY3;
            case 11 -> AliceAngelModel.ANGRY4;
            case 12 -> AliceAngelModel.ANGRY5;
            case 0 -> AliceAngelModel.ANGRY6;
            default -> IDLE2;
        };
    }
}
