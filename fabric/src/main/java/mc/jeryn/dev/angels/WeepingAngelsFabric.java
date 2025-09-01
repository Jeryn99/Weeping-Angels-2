package mc.jeryn.dev.angels;

import mc.jeryn.dev.angels.fabric.events.WAFabricServerEvents;
import mc.jeryn.dev.angels.platform.services.RegisterHelper;
import mc.jeryn.dev.angels.registry.*;
import mc.jeryn.dev.angels.registry.entity.AbstractWeepingAngel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Consumer;

import static mc.jeryn.dev.angels.registry.WAEntities.WEEPING_ANGEL;

public class WeepingAngelsFabric implements ModInitializer {

    private static <T> void registerHelper(Registry<T> register, Consumer<RegisterHelper<T>> consumer) {
        consumer.accept((name, value) -> Registry.register(register, name, value));
    }

    @Override
    public void onInitialize() {

        WAConstants.LOG.info("Hello Fabric world!");
        CommonClass.init();

        registerHelper(BuiltInRegistries.BLOCK, WABlocks::registerBlocks);
        registerHelper(BuiltInRegistries.ITEM, WAItems::registerItems);
        registerHelper(BuiltInRegistries.SOUND_EVENT, WASounds::registerSounds);
        registerHelper(BuiltInRegistries.ENTITY_TYPE, WAEntities::registerEntities);
        registerHelper(BuiltInRegistries.CREATIVE_MODE_TAB, WATabs::registerTabs);

        entityAttributes();

        WAFabricServerEvents.init();
    }

    private void entityAttributes() {
        FabricDefaultAttributeRegistry.register(WEEPING_ANGEL, AbstractWeepingAngel.createAttributes());
    }
}
