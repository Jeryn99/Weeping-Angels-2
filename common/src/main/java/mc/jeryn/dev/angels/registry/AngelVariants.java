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

import java.util.*;
import java.util.stream.Collectors;

public class AngelVariants {

    private static final Map<ResourceLocation, AngelVariants> ORE_VARIANTS = new Object2ObjectOpenHashMap<>();
    private static final Map<ResourceLocation, AngelVariants> VARIANTS = new Object2ObjectOpenHashMap<>();

    public static AngelVariants STONE, BASALT, DIRT, COPPER, MOSSY, RUSTED;
    public static AngelVariants RUSTED_NO_ARM, RUSTED_NO_WING, RUSTED_NO_HEAD;
    public static AngelVariants QUARTZ, LAPIS_LAZULI, IRON, GOLD, EMERALD, DIAMOND;
   // public static AngelVariants GAS_STONE, GAS_RUSTED, A_DIZZLE, DOCTOR;

    private final ItemStack drops;
    private final ResourceLocation regName;

    public AngelVariants(ResourceLocation resourceLocation, ItemStack drops) {
        this.regName = resourceLocation;
        this.drops = drops;
    }

    public ResourceLocation location() {
        return regName;
    }

    public ItemStack getDrops() {
        return drops;
    }

    public static void init() {
        STONE = register("normal", Blocks.STONE, false);
     //   DOCTOR = register("doctor", Blocks.STONE, false);
        BASALT = register("basalt", Blocks.BASALT, false);
        COPPER = register("copper", Blocks.COPPER_ORE, true);
        DIRT = register("dirt", Blocks.DIRT, false);
        MOSSY = register("mossy", Blocks.MOSSY_COBBLESTONE, false);
        RUSTED = register("rusted", Blocks.MOSSY_COBBLESTONE, false);
        RUSTED_NO_ARM = register("rusted_no_arm", Blocks.GRANITE, false);
        RUSTED_NO_WING = register("rusted_no_wing", Blocks.GRANITE, false);
        RUSTED_NO_HEAD = register("rusted_no_head", Blocks.GRANITE, false);
        QUARTZ = register("quartz", Blocks.QUARTZ_PILLAR, false);
        LAPIS_LAZULI = register("lapis_lazuli", Blocks.LAPIS_ORE, true);
        IRON = register("iron", Blocks.IRON_ORE, true);
        GOLD = register("gold", Blocks.GOLD_ORE, true);
        EMERALD = register("emerald", Blocks.EMERALD_ORE, true);
        DIAMOND = register("diamond", Blocks.DIAMOND_ORE, true);
  //      GAS_RUSTED = register("gas_rusted", Blocks.STONE, false);
   //     GAS_STONE = register("gas_stone", Blocks.GRANITE, false);
  //      A_DIZZLE = register("a_dizzle", Blocks.GRANITE, false);
    }

    private static AngelVariants register(String name, net.minecraft.world.level.block.Block block, boolean isOre) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name);
        AngelVariants variant = new AngelVariants(id, new ItemStack(block));
        WAConstants.LOG.info("Registered: {}", id);

        if (isOre) ORE_VARIANTS.put(id, variant);
        VARIANTS.put(id, variant);
        return variant;
    }

    public static AngelVariants getVariant(ResourceLocation id) {
        return VARIANTS.getOrDefault(id, STONE);
    }


    public static AngelVariants getVariantForPos(WeepingAngel angel) {
        Level level = angel.level();
        RandomSource random = level.random;

        var pos = angel.blockPosition();
        boolean isUnderground = pos.getY() < 50 && !level.canSeeSky(pos);

        Holder<Biome> biome = level.getBiome(pos);
        if (biome.is(BiomeTags.IS_JUNGLE)) return MOSSY;
        if (biome.is(BiomeTags.IS_NETHER)) return random.nextBoolean() ? QUARTZ : BASALT;

        if (isUnderground && random.nextInt(100) < 10) {
            return getRandomVariant(ORE_VARIANTS, random);
        }

        List<AngelVariants> filtered = VARIANTS.values().stream()
                .filter(v -> v != QUARTZ && v != MOSSY && v != BASALT && !ORE_VARIANTS.containsKey(v.location()))
                .collect(Collectors.toList());

        return filtered.isEmpty() ? STONE : filtered.get(random.nextInt(filtered.size()));
    }

    public static AngelVariants getRandomVariant(Map<ResourceLocation, AngelVariants> map, RandomSource random) {
        if (map.isEmpty()) return STONE;
        return new ArrayList<>(map.values()).get(random.nextInt(map.size()));
    }
}
