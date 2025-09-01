package mc.jeryn.dev.angels.data.model.donators;

public record Donator(
        String mcName,
        String uuid,
        String vipType,
        String wingsModel,
        long updatedAt
) {
}
