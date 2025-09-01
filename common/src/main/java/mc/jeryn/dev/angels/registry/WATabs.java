package mc.jeryn.dev.angels.registry;

import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.platform.services.RegisterHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class WATabs {

    private static final Map<ResourceLocation, CreativeModeTab> TO_REGISTER = new LinkedHashMap<>();

    private static CreativeModeTab define(String name, Function<ResourceKey<CreativeModeTab>, CreativeModeTab> tabFactory) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name);
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        CreativeModeTab tab = tabFactory.apply(key);
        TO_REGISTER.put(id, tab);
        return tab;
    }

    // === Static tab fields ===
    public static final CreativeModeTab TAB = define("main", key ->
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 10)
                    .title(Component.translatable("itemGroup." + key.location().getNamespace() + "." + key.location().getPath()))
                    .icon(() -> new ItemStack(WAItems.ANGEL_SPAWNER))
                    .displayItems((parameters, output) -> {
                        BuiltInRegistries.ITEM.entrySet().forEach(resourceKeyItemEntry -> {
                            if(resourceKeyItemEntry.getKey().location().getNamespace().equals(WAConstants.MOD_ID)) {
                                output.accept(resourceKeyItemEntry.getValue());
                            }
                        });
                    })
                    .build()
    );

    public static void registerTabs(RegisterHelper<CreativeModeTab> tabHelper) {
        TO_REGISTER.forEach(tabHelper::register);
    }
}
