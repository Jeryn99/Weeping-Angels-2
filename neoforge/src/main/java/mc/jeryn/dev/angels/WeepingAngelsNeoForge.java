package mc.jeryn.dev.angels;


import mc.jeryn.dev.angels.data.*;
import mc.jeryn.dev.angels.platform.services.RegisterHelper;
import mc.jeryn.dev.angels.registry.*;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Mod(WAConstants.MOD_ID)
@EventBusSubscriber(modid = WAConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class WeepingAngelsNeoForge {



    public WeepingAngelsNeoForge(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        WAConstants.LOG.info("Hello NeoForge world!");
        CommonClass.init();

    }

    private static <T> void registerHelper(RegisterEvent event, Registry<T> register, Consumer<RegisterHelper<T>> consumer) {
        event.register(register.key(), registry -> consumer.accept(registry::register));
    }


    @SubscribeEvent
    public static void register(RegisterEvent event) {
        registerHelper(event, BuiltInRegistries.BLOCK, WABlocks::registerBlocks);
        registerHelper(event, BuiltInRegistries.ITEM, WAItems::registerItems);
        registerHelper(event, BuiltInRegistries.SOUND_EVENT, WASounds::registerSounds);
        registerHelper(event, BuiltInRegistries.ENTITY_TYPE, WAEntities::registerEntities);
        registerHelper(event, BuiltInRegistries.CREATIVE_MODE_TAB, WATabs::registerTabs);
    }

    @SubscribeEvent
    public static void clientData(GatherDataEvent.Client client) {
        DataGenerator generator = client.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = client.getLookupProvider();

        // Register all data providers
        client.addProvider(new WASoundProvider(output));
        client.addProvider(new WAModelProviders(output));
        client.addProvider(new WAEntityTagsProvider(output, lookupProvider));
        client.addProvider(new WAEnglish(output));
        client.addProvider(new WAItemTags(
                output,
                lookupProvider,
                CompletableFuture.completedFuture(TagsProvider.TagLookup.empty())
        ));
        client.addProvider(new WABlocktags(
                output,
                lookupProvider
        ));
        client.addProvider(new WorldGenProvider(output, lookupProvider));
    }

    @SubscribeEvent
    public static void serverData(GatherDataEvent.Server server) {
        DataGenerator generator = server.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = server.getLookupProvider();

        server.addProvider(new WASoundProvider(generator.getPackOutput()));
        server.addProvider(new WAEnglish(generator.getPackOutput()));
        server.addProvider(new WAItemTags(
                generator.getPackOutput(),
                lookupProvider,
                CompletableFuture.completedFuture(TagsProvider.TagLookup.empty())
        ));
        server.addProvider(new WorldGenProvider(generator.getPackOutput(), lookupProvider));


    }




}