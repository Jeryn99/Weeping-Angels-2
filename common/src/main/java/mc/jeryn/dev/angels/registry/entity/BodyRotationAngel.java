package mc.jeryn.dev.angels.registry.entity;

import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class BodyRotationAngel extends BodyRotationControl {

    private final AbstractWeepingAngel angel;

    public BodyRotationAngel(AbstractWeepingAngel angel) {
        super(angel);
        this.angel = angel;
    }

    @Override
    public void clientTick() {
        if (!angel.isSeen()) {
            super.clientTick();
        }
    }
}
