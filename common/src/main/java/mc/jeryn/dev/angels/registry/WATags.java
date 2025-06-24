package mc.jeryn.dev.angels.registry;

import mc.jeryn.dev.angels.WAConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class WATags {

    // Item Tags
    public static final TagKey<Item> ANGEL_THEFT = itemTag("angel_theft");

    private static TagKey<Block> blockTag(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name));
    }

    private static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name));
    }
}
