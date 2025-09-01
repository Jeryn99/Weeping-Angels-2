package mc.jeryn.dev.angels.data;

import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.registry.WAEntities;
import mc.jeryn.dev.angels.registry.WAItems;
import mc.jeryn.dev.angels.registry.WAMusic;
import mc.jeryn.dev.angels.registry.damage.WADamageTypes;
import mc.jeryn.dev.angels.util.HurtUtil;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Locale;

public class WAEnglish extends LanguageProvider {

    public WAEnglish(PackOutput output) {
        super(output, WAConstants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

        // Items
        add(WAItems.ANGEL_SPAWNER, "Weeping Angel Spawn Egg");
        add(WAItems.MUSIC_DISC_SALLY, "Music Disc");
        add(WAItems.MUSIC_DISC_TIME_PREVAILS, "Music Disc");
        add(WAItems.DETECTOR, "Timey Wimey Detector");

        // Config section headers
        addConfigHeader("behaviour", "Behaviour");
        addConfigHeader("teleporting", "Teleporting");
        addConfigHeader("seasonal", "Seasonal");

        // Config entries
        addConfigEntry("stalkRange", "Stalk Range");
        addConfigEntry("blockBreaking", "Allow angels to break light-emitting blocks?");
        addConfigEntry("interdimensionalTeleporting", "Allow teleporting across dimensions?");
        addConfigEntry("angelTheft", "Allow Angel theft?");
        addConfigEntry("teleportRange", "Teleportation Range");
        addConfigEntry("teleportChance", "Chance of Teleportation");
        addConfigEntry("bannedDimensions", "Banned Dimensions for Teleportation");
        addConfigEntry("santaHats", "Show Santa hats on angels at Xmas?");
        addConfigEntry("hurtType", "Attack Type");

        // Hurt Type Config
        addHurtTypeConfig(HurtUtil.HurtType.PICKAXE.name().toLowerCase(Locale.ROOT), "Pickaxe");
        addHurtTypeConfig(HurtUtil.HurtType.PICKAXE_AND_GENERATOR.name().toLowerCase(Locale.ROOT), "Pickaxe/Generator");
        addHurtTypeConfig(HurtUtil.HurtType.NONE.name().toLowerCase(Locale.ROOT), "Invulnerable");
        addHurtTypeConfig(HurtUtil.HurtType.GENERATOR.name().toLowerCase(Locale.ROOT), "Generator");
        addHurtTypeConfig(HurtUtil.HurtType.ANYTHING.name().toLowerCase(Locale.ROOT), "Anything");

        // Entities
        add(WAEntities.WEEPING_ANGEL, "Weeping Angel");

        // Item Groups
        addItemGroup("main", WAConstants.MOD_NAME);

        add(Util.makeDescriptionId("jukebox_song", WAMusic.MUSIC_DISC_TIME_PREVAILS.location()), "Time Prevails");
        add(Util.makeDescriptionId("jukebox_song", WAMusic.MUSIC_DISC_SALLY.location()), "Sally Sparrow");

        // Config Title
        add("text.config." + WAConstants.MOD_ID + ".title", WAConstants.MOD_NAME);

        // Damage Types
        damage(WADamageTypes.GENERATOR, "Chronodyne Generator overloaded and exploded in a temporal blaze!");
        damage(WADamageTypes.PUNCH_STONE, "%s tried to shatter stone with brute force... and paid the price.");
        damage(WADamageTypes.SNAPPED_NECK, "%s's neck was silently snapped by a lurking Weeping Angel.");

        //Tags
        add("tag.item.weeping_angels.stealable_items", "Stealable Items");
        add("tag.entity.weeping_angels.anomalies", "Anomalous Entities");

    }

    private void damage(ResourceKey<DamageType> damageTypes, String translation) {
        add("death.attack." + damageTypes.location().getPath(), translation);
    }

    private void addConfigEntry(String key, String translation) {
        add("text.config." + WAConstants.MOD_ID + ".option." + key, translation);
    }

    private void addConfigHeader(String key, String translation) {
        add("text.config." + WAConstants.MOD_ID + ".section." + key, translation);
    }

    private void addItemGroup(String key, String translation) {
        add("itemGroup." + WAConstants.MOD_ID + "." + key, translation);
    }

    private void addHurtTypeConfig(String enumValue, String translation) {
        String key = "text.config.weeping_angels.option.hurtType.value." + enumValue.toLowerCase();
        add(key, translation);
    }


}
