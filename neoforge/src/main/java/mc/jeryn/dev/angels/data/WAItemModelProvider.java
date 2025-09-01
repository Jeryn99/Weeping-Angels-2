package mc.jeryn.dev.angels.data;

import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.registry.WAItems;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;

public class WAItemModelProvider extends ItemModelGenerators {

    public WAItemModelProvider(ItemModelOutput itemModelOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
    }

    @Override
    public void run() {
        this.generateFlatItem(WAItems.ANGEL_SPAWNER, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(WAItems.MUSIC_DISC_SALLY, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(WAItems.MUSIC_DISC_TIME_PREVAILS, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(WAItems.TEST, ModelTemplates.FLAT_ITEM);

        this.generateCustomModel(WAItems.DETECTOR, "item/timey_wimey_detector");
    }

    /**
     * Registers a custom item model using an existing .json model file.
     *
     * @param item      The item to register.
     * @param modelPath The model path (e.g., "item/your_model_name") relative to assets/weeping_angels/models/.
     */
    private void generateCustomModel(Item item, String modelPath) {
        ResourceLocation modelLocation = modLoc(modelPath);
        this.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelLocation));
    }

    private ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, path);
    }
}
