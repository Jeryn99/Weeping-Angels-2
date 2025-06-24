package mc.jeryn.dev.angels.platform.services;

import net.minecraft.resources.ResourceLocation;

public interface RegisterHelper<T> {
    void register(ResourceLocation name, T value);
}