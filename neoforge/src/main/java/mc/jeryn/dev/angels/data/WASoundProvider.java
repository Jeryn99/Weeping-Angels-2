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
        add(WASounds.DING, simple(WASounds.DING));
        add(WASounds.BLOW, simple(WASounds.BLOW));
        add(WASounds.NECK_SNAP, simple(WASounds.NECK_SNAP));
        add(WASounds.PROJECTOR, simple(WASounds.PROJECTOR));
        add(WASounds.ANGEL_MOCKING, simple(WASounds.ANGEL_MOCKING));
        add(WASounds.TARDIS_TAKEOFF, simple(WASounds.TARDIS_TAKEOFF));
        add(WASounds.DISC_SALLY, simple(WASounds.DISC_SALLY));
        add(WASounds.DISC_TIME_PREVAILS, simple(WASounds.DISC_TIME_PREVAILS));
        add(WASounds.KNOCK, simple(WASounds.KNOCK));
        add(WASounds.LOCKED, simple(WASounds.LOCKED));
        add(WASounds.CRUMBLING, simple(WASounds.CRUMBLING));
        add(WASounds.CATACOMB, simple(WASounds.CATACOMB));
        add(WASounds.TELEPORT, simple(WASounds.TELEPORT));
        add(WASounds.ANGEL_NOISE, simple(WASounds.ANGEL_NOISE));
    }

    private SoundDefinition simple(SoundEvent sound) {
        ResourceLocation id = sound.location();
        return definition()
                .with(sound(id.toString()));
    }
}
