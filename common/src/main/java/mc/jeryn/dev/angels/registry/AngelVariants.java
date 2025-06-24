package mc.jeryn.dev.angels.registry;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.registry.entity.WeepingAngel;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;

import java.util.Collection;
import java.util.Map;

public class AngelVariants {

    // Ore Variants
    public static final Map<ResourceLocation, AngelVariants> ORE_VARIANTS = Util.make(new Object2ObjectOpenHashMap<>(), (objectOpenHashMap) -> objectOpenHashMap.defaultReturnValue(AngelVariants.IRON));
    public static AngelVariants STONE, BASALT, DIRT, COPPER, MOSSY, RUSTED, RUSTED_NO_ARM, RUSTED_NO_WING, RUSTED_NO_HEAD, QUARTZ, LAPIS_LAZULI, IRON, GOLD, EMERALD, DIAMOND;
    // Main Variant Registry
    public static final Map<ResourceLocation, AngelVariants> VARIANTS = Util.make(new Object2ObjectOpenHashMap<>(), (objectOpenHashMap) -> objectOpenHashMap.defaultReturnValue(AngelVariants.STONE));
    public static AngelVariants GAS_STONE, GAS_RUSTED, A_DIZZLE, DOCTOR;

    public static void init() {
        STONE = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "normal"), new ItemStack(Blocks.STONE), false);
        DOCTOR = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "doctor"), new ItemStack(Blocks.STONE), false);
        BASALT = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "basalt"), new ItemStack(Blocks.BASALT), false);
        COPPER = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "copper"), new ItemStack(Blocks.COPPER_ORE), true);
        DIRT = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "dirt"), new ItemStack(Blocks.DIRT), false);
        MOSSY = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "mossy"), new ItemStack(Blocks.MOSSY_COBBLESTONE), false);
        RUSTED = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "rusted"), new ItemStack(Blocks.MOSSY_COBBLESTONE), false);
        RUSTED_NO_ARM = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "rusted_no_arm"), new ItemStack(Blocks.GRANITE), false);
        RUSTED_NO_WING = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "rusted_no_wing"), new ItemStack(Blocks.GRANITE), false);
        RUSTED_NO_HEAD = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "rusted_no_head"), new ItemStack(Blocks.GRANITE), false);
        QUARTZ = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "quartz"), new ItemStack(Blocks.QUARTZ_PILLAR), false);
        LAPIS_LAZULI = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "lapis_lazuli"), new ItemStack(Blocks.LAPIS_ORE), true);
        IRON = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "iron"), new ItemStack(Blocks.IRON_ORE), true);
        GOLD = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "gold"), new ItemStack(Blocks.GOLD_ORE), true);
        EMERALD = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "emerald"), new ItemStack(Blocks.EMERALD_ORE), true);
        DIAMOND = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "diamond"), new ItemStack(Blocks.DIAMOND_ORE), true);

        GAS_RUSTED = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "gas_rusted"), new ItemStack(Blocks.STONE), false);
        GAS_STONE = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "gas_stone"), new ItemStack(Blocks.GRANITE), false);
        A_DIZZLE = registerVariant(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "a_dizzle"), new ItemStack(Blocks.GRANITE), false);
    }

    private final ItemStack drops;
    private final ResourceLocation regName;

    public AngelVariants(ResourceLocation resourceLocation, ItemStack drops) {
        this.drops = drops;
        this.regName = resourceLocation;
    }

    public ResourceLocation location() {
        return regName;
    }

    public ItemStack getDrops() {
        return drops;
    }


    // TODO Nicer way
    public static AngelVariants getVariantForPos(WeepingAngel weepingAngel) {
        Level level = weepingAngel.level();
        RandomSource randomSource = level.random;

        boolean isOrePosition = weepingAngel.blockPosition().getY() < 50 && !level.canSeeSky(weepingAngel.blockPosition());

        Holder<Biome> currentBiome = level.getBiome(weepingAngel.blockPosition());
        boolean isNether = currentBiome.is(BiomeTags.IS_NETHER);
        boolean isJungle = currentBiome.is(BiomeTags.IS_JUNGLE);

        if (isJungle) {
            return MOSSY;
        }

        // Nether Related
        if (isNether) {
            return randomSource.nextBoolean() ? QUARTZ : BASALT;
        }

        // Ores
        if (isOrePosition && randomSource.nextInt(100) < 10) {
            return getRandomVariant(ORE_VARIANTS, randomSource);
        }

        // Random value after conditions
        Collection<AngelVariants> variants = VARIANTS.values();
        variants.removeIf(angelTextureVariant -> angelTextureVariant == QUARTZ || angelTextureVariant == MOSSY || angelTextureVariant == BASALT || ORE_VARIANTS.containsKey(angelTextureVariant.regName));
        return variants.stream().skip((int) (variants.size() * Math.random())).findFirst().get();
    }

    public static AngelVariants getRandomVariant(Map<ResourceLocation, AngelVariants> variantMap, RandomSource randomSource) {
        int index = randomSource.nextInt(variantMap.size());
        return variantMap.values().toArray(new AngelVariants[0])[index];
    }

    public static AngelVariants getVariant(ResourceLocation resourceLocation) {
        if (VARIANTS.containsKey(resourceLocation)) {
            return VARIANTS.get(resourceLocation);
        }
        return STONE;
    }

    public static AngelVariants registerVariant(ResourceLocation resourceLocation, ItemStack itemStack, boolean isOre) {
        WAConstants.LOG.info("Registered: {}", resourceLocation);
        return registerVariant(resourceLocation, new AngelVariants(resourceLocation, itemStack), isOre);
    }

    public static AngelVariants registerVariant(ResourceLocation resourceLocation, AngelVariants angelVariant, boolean isOre) {

        if (isOre) {
            ORE_VARIANTS.put(resourceLocation, angelVariant);
        }

        if (VARIANTS.containsKey(resourceLocation)) {
            VARIANTS.replace(resourceLocation, angelVariant);
        }
        VARIANTS.put(resourceLocation, angelVariant);
        return angelVariant;
    }


}