package mc.jeryn.dev.angels.registry.entity;

import net.minecraft.util.RandomSource;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AngelEmotion {
    ANGRY, IDLE, SCREAM;

    private static final AngelEmotion[] VALUES = values();
    private static final Map<String, AngelEmotion> ID_MAP = Stream.of(VALUES)
            .collect(Collectors.toMap(
                    e -> e.name().toLowerCase(Locale.ENGLISH),
                    e -> e
            ));

    public String getId() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    public static AngelEmotion randomEmotion(RandomSource random) {
        return VALUES[random.nextInt(VALUES.length)];
    }

    public static AngelEmotion find(String id) {
        if (id == null) return ANGRY;
        return ID_MAP.getOrDefault(id.toLowerCase(Locale.ENGLISH), ANGRY);
    }
}
