package mc.jeryn.dev.angels.data;

import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.registry.WAMusic;
import mc.jeryn.dev.angels.registry.WASounds;
import mc.jeryn.dev.angels.registry.damage.WADamageTypes;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class WorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, context -> {
                context.register(
                        WADamageTypes.GENERATOR,
                        new DamageType("generator", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 1)
                );
                context.register(
                        WADamageTypes.PUNCH_STONE,
                        new DamageType("punch_stone", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 1)
                );
                context.register(
                        WADamageTypes.SNAPPED_NECK,
                        new DamageType("snapped_neck", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 1)
                );
            })
            .add(Registries.JUKEBOX_SONG, context -> {
                context.register(
                        WAMusic.MUSIC_DISC_SALLY,
                        new JukeboxSong(
                                Holder.direct(WASounds.DISC_SALLY),
                                Component.translatable(Util.makeDescriptionId("jukebox_song", WAMusic.MUSIC_DISC_SALLY.location())),
                                65f,
                                1
                        )
                );
                context.register(
                        WAMusic.MUSIC_DISC_TIME_PREVAILS,
                        new JukeboxSong(
                                Holder.direct(WASounds.DISC_TIME_PREVAILS),
                                Component.translatable(Util.makeDescriptionId("jukebox_song", WAMusic.MUSIC_DISC_TIME_PREVAILS.location())),
                                16f,
                                1
                        )
                );
            });

    public WorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BUILDER, Set.of(WAConstants.MOD_ID));
    }



}