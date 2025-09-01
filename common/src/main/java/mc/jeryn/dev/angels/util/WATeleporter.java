package mc.jeryn.dev.angels.util;

import mc.jeryn.dev.angels.CommonClass;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

public class WATeleporter {

    private static final int MAX_ATTEMPTS = 10;

    /**
     * Tries to teleport the given entity to a random nearby location in the current or a random dimension.
     */
    public static boolean tryRandomTeleport(Entity entity, ServerLevel serverLevel, RandomSource random) {
        if (random.nextInt(100) >= CommonClass.CONFIG.teleportChance()) return false;

        ServerLevel chosenLevel = CommonClass.CONFIG.interdimensionalTeleporting()
                ? getRandomDimension(random, serverLevel.getServer())
                : serverLevel;

        int teleportRange = CommonClass.CONFIG.teleportRange();

        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            int x = Mth.floor(entity.getX() + (random.nextDouble() - 0.5) * teleportRange);
            int z = Mth.floor(entity.getZ() + (random.nextDouble() - 0.5) * teleportRange);
            BlockPos attemptPos = new BlockPos(x, random.nextInt(161) - 40, z);

            BlockPos safePos = findClosestValidPosition(chosenLevel, attemptPos);
            if (safePos == null) continue;

            if (performTeleport(entity, chosenLevel, x, safePos.getY(), z, entity.getYRot(), entity.getXRot(), true)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets a random dimension from the server.
     */
    public static ServerLevel getRandomDimension(RandomSource random, MinecraftServer server) {
        List<ResourceKey<Level>> levels = server.levelKeys().stream().collect(Collectors.toList());
        ResourceKey<Level> chosenKey = levels.get(random.nextInt(levels.size()));
        return server.getLevel(chosenKey);
    }

    /**
     * Finds the nearest valid ground position by scanning down and up from the given point.
     */
    public static BlockPos findClosestValidPosition(ServerLevel level, BlockPos origin) {
        for (int yOffset = 0; yOffset <= 32; yOffset++) {
            BlockPos below = origin.below(yOffset);
            if (isSafeTeleportLocation(level, below)) return below.above();

            BlockPos above = origin.above(yOffset);
            if (isSafeTeleportLocation(level, above)) return above.above();
        }

        return null;
    }

    /**
     * Checks if a position is a valid teleport spot: solid ground + 2 blocks air above.
     */
    private static boolean isSafeTeleportLocation(ServerLevel level, BlockPos pos) {
        BlockState ground = level.getBlockState(pos);
        BlockState above = level.getBlockState(pos.above());
        BlockState above2 = level.getBlockState(pos.above(2));

        boolean solid = ground.isSolidRender();
        boolean airAbove = above.isAir() && above2.isAir();

        return solid && airAbove;
    }

    /**
     * Performs the actual teleport.
     */
    public static boolean performTeleport(Entity entity, ServerLevel level, double x, double y, double z, float yaw, float pitch, boolean resetMotion) {
        if (!Level.isInSpawnableBounds(BlockPos.containing(x, y, z))) return false;

        float wrappedYaw = Mth.wrapDegrees(yaw);
        float wrappedPitch = Mth.wrapDegrees(pitch);

        Set<Relative> flags = EnumSet.noneOf(Relative.class);

        if (entity.teleportTo(level, x, y, z, flags, wrappedYaw, wrappedPitch, true)) {

            if (resetMotion) {
                entity.setDeltaMovement(Vec3.ZERO);
            } else {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0, 1));
            }

            if (entity instanceof LivingEntity living) {
                if (!living.isFallFlying()) {
                    entity.setOnGround(true);
                }
            }

            if (entity instanceof PathfinderMob mob) {
                mob.getNavigation().stop();
            }

            return true;
        }

        return false;
    }
}
