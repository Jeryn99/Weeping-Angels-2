package mc.jeryn.dev.angels.data;

import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.registry.WATags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class WABlocktags extends BlockTagsProvider {

    public WABlocktags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, WAConstants.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(WATags.NO_BREAK).add(Blocks.GLOWSTONE);
    }
}
