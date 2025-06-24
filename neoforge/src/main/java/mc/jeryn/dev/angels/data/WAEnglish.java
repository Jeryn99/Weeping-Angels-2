package mc.jeryn.dev.angels.data;

import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.registry.WAEntities;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class WAEnglish extends LanguageProvider {

    public WAEnglish(PackOutput output) {
        super(output, WAConstants.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
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

        // Entities
        add(WAEntities.WEEPING_ANGEL, "Weeping Angel");


        addItemGroup("main", WAConstants.MOD_NAME);

        add("text.config." + WAConstants.MOD_ID + ".title", WAConstants.MOD_NAME);
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

}
