package mc.jeryn.dev.angels.data;

import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.registry.WASounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class WASoundProvider extends SoundDefinitionsProvider {

    public WASoundProvider(PackOutput output) {
        super(output, WAConstants.MOD_ID);
    }

    @Override
    public void registerSounds() {
        add(WASounds.DING, streamingSimple(WASounds.DING));
        add(WASounds.BLOW, streamingSimple(WASounds.BLOW));
        add(WASounds.NECK_SNAP, streamingSimple(WASounds.NECK_SNAP));
        add(WASounds.PROJECTOR, streamingSimple(WASounds.PROJECTOR));
        add(WASounds.ANGEL_MOCKING, streamingSimple(WASounds.ANGEL_MOCKING));
        add(WASounds.TARDIS_TAKEOFF, streamingSimple(WASounds.TARDIS_TAKEOFF));
        add(WASounds.DISC_SALLY, streamingSimple(WASounds.DISC_SALLY));
        add(WASounds.DISC_TIME_PREVAILS, streamingSimple(WASounds.DISC_TIME_PREVAILS));
        add(WASounds.KNOCK, streamingSimple(WASounds.KNOCK));
        add(WASounds.LOCKED, streamingSimple(WASounds.LOCKED));
        add(WASounds.CRUMBLING, streamingSimple(WASounds.CRUMBLING));
        add(WASounds.CATACOMB, streamingSimple(WASounds.CATACOMB));
        add(WASounds.TELEPORT, streamingSimple(WASounds.TELEPORT));
        add(WASounds.ANGEL_NOISE, streamingSimple(WASounds.ANGEL_NOISE));
    }

    private SoundDefinition simple(SoundEvent sound) {
        ResourceLocation id = sound.location();
        return definition()
                .with(sound(id.toString()));
    }

    private SoundDefinition streamingSimple(SoundEvent sound) {
        ResourceLocation id = sound.location();
        return definition()
                .with(sound(id.toString()).stream(true));
    }
}
