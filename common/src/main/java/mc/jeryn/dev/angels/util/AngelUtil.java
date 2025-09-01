package mc.jeryn.dev.angels.util;

import mc.jeryn.dev.angels.registry.WATags;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.function.Predicate;

public class AngelUtil {

    public static Predicate<? super Entity> ANOMALY_ENTITIES = input -> input.getType().is(WATags.ANOMALIES);

    public static List<Entity> getAnomaliesAroundEntity(Entity entity, int radius) {
        return entity.level().getEntities((Entity) null, entity.getBoundingBox().inflate(radius, radius, radius), ANOMALY_ENTITIES);
    }

}
