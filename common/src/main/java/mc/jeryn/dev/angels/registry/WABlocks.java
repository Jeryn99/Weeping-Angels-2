package mc.jeryn.dev.angels.registry;

import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.platform.services.RegisterHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class WABlocks {

    private static final Map<ResourceLocation, Block> TO_REGISTER = new LinkedHashMap<>();

    private static Block define(String name, Function<ResourceKey<Block>, Block> blockFactory) {
        ResourceLocation id =  ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name);
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
        Block block = blockFactory.apply(key);
        TO_REGISTER.put(id, block);
        return block;
    }

    // === Static block fields ===
    public static final Block PAUL_MCGANN = define("paul_mcgann", key ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK).setId(key))
    );

    public static void registerBlocks(RegisterHelper<Block> blockHelper) {
        TO_REGISTER.forEach(blockHelper::register);
    }
}
