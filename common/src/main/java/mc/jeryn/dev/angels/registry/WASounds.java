package mc.jeryn.dev.angels.registry;

import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.platform.services.RegisterHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class WASounds {

    private static final Map<ResourceLocation, SoundEvent> TO_REGISTER = new LinkedHashMap<>();

    private static SoundEvent define(String name, Function<ResourceKey<SoundEvent>, SoundEvent> soundFactory) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name);
        ResourceKey<SoundEvent> key = ResourceKey.create(Registries.SOUND_EVENT, id);
        SoundEvent sound = soundFactory.apply(key);
        TO_REGISTER.put(id, sound);
        return sound;
    }

    private static SoundEvent variableRange(String name) {
        return define(name, key -> SoundEvent.createVariableRangeEvent(key.location()));
    }

    // === Static sound fields ===
    public static final SoundEvent DING = variableRange("ding");
    public static final SoundEvent BLOW = variableRange("blow");
    public static final SoundEvent NECK_SNAP = variableRange("neck_snap");
    public static final SoundEvent PROJECTOR = variableRange("projector");
    public static final SoundEvent ANGEL_MOCKING = variableRange("angel_mocking");
    public static final SoundEvent TARDIS_TAKEOFF = variableRange("tardis_takeoff");
    public static final SoundEvent DISC_SALLY = variableRange("disc_sally");
    public static final SoundEvent DISC_TIME_PREVAILS = variableRange("disc_time_prevails");
    public static final SoundEvent KNOCK = variableRange("knock");
    public static final SoundEvent LOCKED = variableRange("locked");
    public static final SoundEvent CRUMBLING = variableRange("crumbling");
    public static final SoundEvent CATACOMB = variableRange("catacomb");
    public static final SoundEvent TELEPORT = variableRange("teleport");
    public static final SoundEvent ANGEL_NOISE = variableRange("angel_noise");

    public static void registerSounds(RegisterHelper<SoundEvent> soundHelper) {
        TO_REGISTER.forEach(soundHelper::register);
    }
}
