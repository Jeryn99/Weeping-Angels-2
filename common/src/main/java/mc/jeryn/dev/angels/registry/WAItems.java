package mc.jeryn.dev.angels.registry;

import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.platform.services.RegisterHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class WAItems {

    private static final Map<ResourceLocation, Item> TO_REGISTER = new LinkedHashMap<>();

    private static Item define(String name, Function<ResourceKey<Item>, Item> itemFactory) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        Item item = itemFactory.apply(key);
        TO_REGISTER.put(id, item);
        return item;
    }

    public static final Item ANGEL_SPAWNER = define("angel_spawner", key ->
            new SpawnEggItem(WAEntities.WEEPING_ANGEL, new Properties().setId(key))
    );

    public static final Item MUSIC_DISC_SALLY = define("music_disc_sally", key ->
            new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(WAMusic.MUSIC_DISC_SALLY).setId(key)));

    public static final Item MUSIC_DISC_TIME_PREVAILS = define("music_disc_time_prevails", key ->
            new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(WAMusic.MUSIC_DISC_TIME_PREVAILS).setId(key)));

   /* public static final Item TEST = define("test", key ->
            new TestItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(WAMusic.MUSIC_DISC_TIME_PREVAILS).setId(key)));
*/
    public static final Item DETECTOR = define("timey_wimey_detector", key ->
            new DetectorItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).setId(key)));


    public static void registerItems(RegisterHelper<Item> itemHelper) {
        TO_REGISTER.forEach(itemHelper::register);
    }
}
