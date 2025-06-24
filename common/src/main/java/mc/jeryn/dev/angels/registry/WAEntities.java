package mc.jeryn.dev.angels.registry;

import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.platform.services.RegisterHelper;
import mc.jeryn.dev.angels.registry.entity.WeepingAngel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class WAEntities {

    private static final Map<ResourceLocation, EntityType<?>> TO_REGISTER = new LinkedHashMap<>();

    private static <T extends EntityType<?>> T define(String name, Function<ResourceKey<EntityType<?>>, T> entityFactory) {
        ResourceLocation id =  ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, name);
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, id);
        T entity = entityFactory.apply(key);
        TO_REGISTER.put(id, entity);
        return entity;
    }

    public static final EntityType<WeepingAngel> WEEPING_ANGEL = define("weeping_angel", key ->
            EntityType.Builder.of(new EntityType.EntityFactory<WeepingAngel>() {
                        @Override
                        public @Nullable WeepingAngel create(EntityType<WeepingAngel> entityType, Level level) {
                            return new WeepingAngel(level);
                        }
                    }, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build(key)
    );

    public static void registerEntities(RegisterHelper<EntityType<?>> entityHelper) {
        TO_REGISTER.forEach(entityHelper::register);
    }
}
