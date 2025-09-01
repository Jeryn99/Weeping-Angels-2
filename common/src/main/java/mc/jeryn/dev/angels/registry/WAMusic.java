package mc.jeryn.dev.angels.registry;

import mc.jeryn.dev.angels.WAConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.JukeboxSong;

public class WAMusic {

    public static ResourceKey<JukeboxSong> MUSIC_DISC_TIME_PREVAILS = create("music_disc_time_prevails");
    public static ResourceKey<JukeboxSong> MUSIC_DISC_SALLY = create("music_disc_sally");

    private static ResourceKey<JukeboxSong> create(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name));
    }

}
