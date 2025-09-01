package mc.jeryn.dev.angels.registry.damage;

import mc.jeryn.dev.angels.WAConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class WADamageTypes {

    private static final List<ResourceKey<DamageType>> DAMAGE_TYPES = new ArrayList<>();

    private static ResourceKey<DamageType> create(String name) {
        ResourceKey<DamageType> key = ResourceKey.create(Registries.DAMAGE_TYPE,  ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name));
        DAMAGE_TYPES.add(key);
        return key;
    }

    // === Defined damage types ===
    public static final ResourceKey<DamageType> GENERATOR = create("generator");
    public static final ResourceKey<DamageType> PUNCH_STONE = create("punch_stone");
    public static final ResourceKey<DamageType> SNAPPED_NECK = create("snapped_neck");

    public static List<ResourceKey<DamageType>> getAll() {
        return Collections.unmodifiableList(DAMAGE_TYPES);
    }

    public static DamageSource getSource(ServerLevel serverLevel, ResourceKey<DamageType> damageTypeKey) {
        Registry<DamageType> damageTypeRegistry = serverLevel.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(damageTypeRegistry.getOrThrow(damageTypeKey));
    }
}
