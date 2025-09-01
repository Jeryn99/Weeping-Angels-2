package mc.jeryn.dev.angels.registry;

import mc.jeryn.dev.angels.WAConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class WATags {

    // Item Tags
    public static final TagKey<Item> STEALABLE_ITEMS = itemTag("stealable_items");
    public static final TagKey<EntityType<?>> ANOMALIES = entityTypeTag("anomalies");
    public static final TagKey<Block> NO_BREAK = blockTag("no_break");

    private static TagKey<Block> blockTag(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name));
    }

    private static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name));
    }

    private static TagKey<EntityType<?>> entityTypeTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name));
    }
}
