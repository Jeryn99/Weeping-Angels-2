package mc.jeryn.dev.angels.util;

import mc.jeryn.dev.angels.registry.entity.AbstractWeepingAngel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.function.Predicate;

public class ViewUtil {

    private static final float HEAD_SIZE = 0.15f;

    public static boolean isInFrontOfEntity(LivingEntity entity, Entity target, boolean vr) {
        Vec3 targetPos = target.position();
        Vec3 lookVec = entity.getLookAngle();

        if (entity instanceof Player player && vr) {
            lookVec = manipulateVrRotation(player, lookVec);
        }

        Vec3 directionToEntity = targetPos.vectorTo(entity.position()).normalize();
        directionToEntity = new Vec3(directionToEntity.x, 0.0D, directionToEntity.z);
        return directionToEntity.dot(lookVec) < 0.0;
    }

    public static Vec3 manipulateVrRotation(Player player, Vec3 vec3) {
        return vec3;
    }

    public static Vec3 manipulateVrPosition(Player player, Vec3 vec3) {
        return vec3;
    }

    public static boolean isVrPlayer(Player player) {
        return false;
    }

    public static boolean isInSight(LivingEntity viewer, AbstractWeepingAngel angel) {
        if (isPlayerBlind(viewer) || viewBlocked(viewer, angel)) return false;

        boolean vr = viewer instanceof Player player && isVrPlayer(player);
        return isInFrontOfEntity(viewer, angel, vr);
    }

    public static boolean viewBlocked(LivingEntity viewer, LivingEntity angel) {
        AABB viewerBB = viewer.getBoundingBox();
        AABB angelBB = angel.getBoundingBox();

        Vec3[] viewerPoints = generateViewPoints(viewer, viewerBB);
        Vec3[] angelPoints = generateBoundingBoxPoints(angelBB);

        for (int i = 0; i < viewerPoints.length; i++) {
            Vec3 from = viewerPoints[i];
            Vec3 to = angelPoints[i];

            if (viewer.level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, viewer)).getType() == HitResult.Type.MISS) {
                return false;
            }

            if (rayTraceBlocks(viewer, viewer.level(), from, to, pos -> {
                BlockState state = viewer.level().getBlockState(pos);
                return !canSeeThrough(state, viewer.level(), pos);
            }) == null) {
                return false;
            }
        }

        if (angel.tickCount % 1200 == 0 && angel.distanceTo(viewer) < 15) {
            viewer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 15));
        }

        return true;
    }

    private static Vec3[] generateViewPoints(LivingEntity viewer, AABB viewerBB) {
        Vec3[] points = {
                new Vec3(viewerBB.minX, viewerBB.minY, viewerBB.minZ),
                new Vec3(viewerBB.minX, viewerBB.minY, viewerBB.maxZ),
                new Vec3(viewerBB.minX, viewerBB.maxY, viewerBB.minZ),
                new Vec3(viewerBB.minX, viewerBB.maxY, viewerBB.maxZ),
                new Vec3(viewerBB.maxX, viewerBB.maxY, viewerBB.minZ),
                new Vec3(viewerBB.maxX, viewerBB.maxY, viewerBB.maxZ),
                new Vec3(viewerBB.maxX, viewerBB.minY, viewerBB.maxZ),
                new Vec3(viewerBB.maxX, viewerBB.minY, viewerBB.minZ),
        };

        if (viewer instanceof Player player) {
            Vec3 eyePos = new Vec3(viewer.getX(), viewer.getY() + 1.62f, viewer.getZ());
            if (isVrPlayer(player)) {
                eyePos = manipulateVrPosition(player, eyePos);
            }

            points = new Vec3[] {
                    eyePos.add(-HEAD_SIZE, -HEAD_SIZE, -HEAD_SIZE),
                    eyePos.add(-HEAD_SIZE, -HEAD_SIZE,  HEAD_SIZE),
                    eyePos.add(-HEAD_SIZE,  HEAD_SIZE, -HEAD_SIZE),
                    eyePos.add(-HEAD_SIZE,  HEAD_SIZE,  HEAD_SIZE),
                    eyePos.add( HEAD_SIZE,  HEAD_SIZE, -HEAD_SIZE),
                    eyePos.add( HEAD_SIZE,  HEAD_SIZE,  HEAD_SIZE),
                    eyePos.add( HEAD_SIZE, -HEAD_SIZE,  HEAD_SIZE),
                    eyePos.add( HEAD_SIZE, -HEAD_SIZE, -HEAD_SIZE)
            };
        }

        return points;
    }

    private static Vec3[] generateBoundingBoxPoints(AABB box) {
        return new Vec3[]{
                new Vec3(box.minX, box.minY, box.minZ),
                new Vec3(box.minX, box.minY, box.maxZ),
                new Vec3(box.minX, box.maxY, box.minZ),
                new Vec3(box.minX, box.maxY, box.maxZ),
                new Vec3(box.maxX, box.maxY, box.minZ),
                new Vec3(box.maxX, box.maxY, box.maxZ),
                new Vec3(box.maxX, box.minY, box.maxZ),
                new Vec3(box.maxX, box.minY, box.minZ),
        };
    }

    public static boolean isDarkForPlayer(AbstractWeepingAngel angel, LivingEntity living) {
        return !living.hasEffect(MobEffects.NIGHT_VISION)
                && angel.level().getLightEmission(angel.blockPosition()) <= 0
                && !angel.level().dimensionType().hasCeiling();
    }

    public static boolean isPlayerBlind(LivingEntity living) {
        return living.hasEffect(MobEffects.BLINDNESS);
    }

    private static HitResult rayTraceBlocks(LivingEntity livingEntity, Level world, Vec3 startVec, Vec3 endVec, Predicate<BlockPos> stopOn) {
        if (hasNaN(startVec) || hasNaN(endVec)) {
            return null;
        }

        int startX = Mth.floor(startVec.x);
        int startY = Mth.floor(startVec.y);
        int startZ = Mth.floor(startVec.z);
        int endX = Mth.floor(endVec.x);
        int endY = Mth.floor(endVec.y);
        int endZ = Mth.floor(endVec.z);

        BlockPos currentPos = new BlockPos(startX, startY, startZ);
        if (stopOn.test(currentPos)) {
            HitResult result = world.clip(new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity));
            if (result != null) return result;
        }

        int maxIterations = 200;
        Direction direction;

        while (maxIterations-- >= 0) {
            if (startX == endX && startY == endY && startZ == endZ) {
                return null;
            }

            boolean stepX = true, stepY = true, stepZ = true;
            double nextX = Double.POSITIVE_INFINITY;
            double nextY = Double.POSITIVE_INFINITY;
            double nextZ = Double.POSITIVE_INFINITY;

            if (endX > startX) nextX = startX + 1.0D;
            else if (endX < startX) nextX = startX;
            else stepX = false;

            if (endY > startY) nextY = startY + 1.0D;
            else if (endY < startY) nextY = startY;
            else stepY = false;

            if (endZ > startZ) nextZ = startZ + 1.0D;
            else if (endZ < startZ) nextZ = startZ;
            else stepZ = false;

            double deltaX = endVec.x - startVec.x;
            double deltaY = endVec.y - startVec.y;
            double deltaZ = endVec.z - startVec.z;

            double ratioX = stepX ? (nextX - startVec.x) / deltaX : Double.POSITIVE_INFINITY;
            double ratioY = stepY ? (nextY - startVec.y) / deltaY : Double.POSITIVE_INFINITY;
            double ratioZ = stepZ ? (nextZ - startVec.z) / deltaZ : Double.POSITIVE_INFINITY;

            if (ratioX == -0.0D) ratioX = -1.0E-4D;
            if (ratioY == -0.0D) ratioY = -1.0E-4D;
            if (ratioZ == -0.0D) ratioZ = -1.0E-4D;

            if (ratioX < ratioY && ratioX < ratioZ) {
                direction = endX > startX ? Direction.WEST : Direction.EAST;
                startVec = new Vec3(nextX, startVec.y + deltaY * ratioX, startVec.z + deltaZ * ratioX);
            } else if (ratioY < ratioZ) {
                direction = endY > startY ? Direction.DOWN : Direction.UP;
                startVec = new Vec3(startVec.x + deltaX * ratioY, nextY, startVec.z + deltaZ * ratioY);
            } else {
                direction = endZ > startZ ? Direction.NORTH : Direction.SOUTH;
                startVec = new Vec3(startVec.x + deltaX * ratioZ, startVec.y + deltaY * ratioZ, nextZ);
            }

            startX = Mth.floor(startVec.x) - (direction == Direction.EAST ? 1 : 0);
            startY = Mth.floor(startVec.y) - (direction == Direction.UP ? 1 : 0);
            startZ = Mth.floor(startVec.z) - (direction == Direction.SOUTH ? 1 : 0);

            currentPos = new BlockPos(startX, startY, startZ);
            if (stopOn.test(currentPos)) {
                HitResult result = world.clip(new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity));
                if (result != null) return result;
            }
        }

        return null;
    }


    private static boolean hasNaN(Vec3 vec) {
        return Double.isNaN(vec.x) || Double.isNaN(vec.y) || Double.isNaN(vec.z);
    }

    public static boolean canSeeThrough(BlockState state, Level level, BlockPos pos) {
        if (!state.canOcclude() || !state.isSolidRender()) return true;

        Block block = state.getBlock();

        if (block instanceof DoorBlock door) {
            return state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER;
        }

        if (!block.defaultBlockState().canOcclude()) return true;

        return state.getCollisionShape(level, pos).isEmpty();
    }
}
