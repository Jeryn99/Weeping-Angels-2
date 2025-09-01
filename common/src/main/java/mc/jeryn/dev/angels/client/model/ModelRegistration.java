package mc.jeryn.dev.angels.client.model;

import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.platform.Services;
import mc.jeryn.dev.angels.registry.AngelVariants;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ModelRegistration {

    public static ModelLayerLocation SANTA_HAT;
    public static ModelLayerLocation COFFIN, MERCY_WINGS, PORTAL, ALICE_ANGEL, TARDIS, A_DIZZLE_ANGEL, DOCTOR_ANGEL, GENERATOR, SNOW_ARM, SNOW_BODY, SNOW_WINGS, SNOW_HEAD;
    private static BaseAngelModel ALICE_MODEL, A_DIZZLE_MODEL, DOCTOR_ANGEL_MODEL;

    public static void init() {
        ALICE_ANGEL = register(new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "model"), "alice_angel"), AliceAngelModel::meshLayer);
    }

    public static void regModels(BlockEntityRendererProvider.Context context) {
        EntityModelSet entityModels = context.getModelSet();
        ALICE_MODEL = new AliceAngelModel(entityModels.bakeLayer(ALICE_ANGEL));
    }

    public static BaseAngelModel getModelFor(AngelVariants angelVariant) {

     /*   if (angelVariant == AngelVariants.DOCTOR) {
            return DOCTOR_ANGEL_MODEL;
        }

        if (angelVariant == AngelVariants.A_DIZZLE) {
            return A_DIZZLE_MODEL;
        }*/

        return ALICE_MODEL;
    }


    public static ModelLayerLocation register(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        return Services.MODEL.register(location, definition);
    }


}