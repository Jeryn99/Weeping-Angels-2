package mc.jeryn.dev.angels.registry.entity;

import mc.jeryn.dev.angels.CommonClass;
import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.registry.AngelVariants;
import mc.jeryn.dev.angels.util.ViewUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractWeepingAngel extends Monster implements Enemy {
    private static final EntityDataAccessor<Integer> TIME_VIEWED = SynchedEntityData.defineId(AbstractWeepingAngel.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> EMOTION = SynchedEntityData.defineId(AbstractWeepingAngel.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> IS_HOOKED = SynchedEntityData.defineId(AbstractWeepingAngel.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(AbstractWeepingAngel.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> SHOULD_DROP_LOOT = SynchedEntityData.defineId(AbstractWeepingAngel.class, EntityDataSerializers.BOOLEAN);

    private long timeSincePlayedSound = 0;

    public AbstractWeepingAngel(EntityType<? extends Monster> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.8D)
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    public void tick() {
        super.tick();
        if (getSeenTime() == 0) {
            setNoAi(false);
        }
    }

    @Override
    public void aiStep() {
        if (!getMainHandItem().isEmpty()) {
            setPersistenceRequired();
        }

        super.aiStep();
        if (!level().isClientSide) {
            List<Player> players = level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(CommonClass.CONFIG.stalkRange()));
            players.removeIf(player -> player.isSpectator() || player.isInvisible() || player.isSleeping() || player.level() != level());

            if (players.isEmpty()) {
                setSeenTime(0);
                setSpeed(0.5F);
                return;
            }

            Player targetPlayer = null;
            for (Player player : players) {
                if (ViewUtil.isInSight(player, this)) {
                    setSeenTime(getSeenTime() + 1);
                    invokeSeen(player);
                    return;
                }
                if (targetPlayer == null) {
                    targetPlayer = player;
                    setSeenTime(0);
                    setSpeed(0.5F);
                }
            }

            if (isSeen()) return;
            snapLookToPlayer(targetPlayer);
            moveTowards(targetPlayer);
        }
    }

    @Override
    public void makeStuckInBlock(BlockState state, @NotNull Vec3 motionMultiplierIn) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplierIn);
        }
    }

    private void snapLookToPlayer(Player targetPlayer) {
        Vec3 pos = position();
        Vec3 playerPos = targetPlayer.position();
        double dx = pos.x - playerPos.x;
        double dz = pos.z - playerPos.z;
        float angle = (float) Math.toDegrees(Math.atan2(dz, dx));
        yHeadRot = yBodyRot = (angle > 180 ? angle : angle + 90);
    }

    public void moveTowards(LivingEntity targetPlayer) {
        getNavigation().moveTo(targetPlayer, getSpeed());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TIME_VIEWED, 0);
        builder.define(EMOTION, AngelEmotion.IDLE.getId());
        builder.define(IS_HOOKED, false);
        builder.define(VARIANT, AngelVariants.BASALT.location().toString());
        builder.define(SHOULD_DROP_LOOT, true);
    }


    @Override
    protected boolean shouldDropLoot() {
        return getEntityData().get(SHOULD_DROP_LOOT);
    }

    public AngelVariants getVariant() {
        return AngelVariants.getVariant(ResourceLocation.parse(getEntityData().get(VARIANT)));
    }

    public void setVariant(AngelVariants variant) {
        getEntityData().set(VARIANT, variant.location().toString());
    }

    public AngelEmotion getEmotion() {
        String emotion = getEntityData().get(EMOTION);
        for (AngelEmotion angelEmotion : AngelEmotion.values()) {
            if (emotion.equalsIgnoreCase(angelEmotion.getId())) {
                return angelEmotion;
            }
        }
        return AngelEmotion.IDLE;
    }

    public void setEmotion(AngelEmotion emotion) {
        getEntityData().set(EMOTION, emotion.getId());
    }

    public boolean isHooked() {
        return getEntityData().get(IS_HOOKED);
    }

    public void setHooked(boolean hooked) {
        getEntityData().set(IS_HOOKED, hooked);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        compound.getInt(WAConstants.NBT.TIME_SEEN).ifPresent(this::setSeenTime);
        compound.getString(WAConstants.NBT.EMOTION).ifPresent(s -> setEmotion(AngelEmotion.find(s.toUpperCase())));
        compound.getBoolean(WAConstants.NBT.IS_HOOKED).ifPresent(this::setHooked);
        compound.getBoolean(WAConstants.NBT.DROPS_LOOT).ifPresent(this::setDrops);
        compound.getString(WAConstants.NBT.VARIANT)
                .ifPresent(s -> setVariant(AngelVariants.getVariant(ResourceLocation.parse(s))));
    }



    public void setDrops(boolean drops) {
        getEntityData().set(SHOULD_DROP_LOOT, drops);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean(WAConstants.NBT.IS_SEEN, isSeen());
        compound.putInt(WAConstants.NBT.TIME_SEEN, getSeenTime());
        compound.putString(WAConstants.NBT.EMOTION, getEmotion().getId());
        compound.putBoolean(WAConstants.NBT.DROPS_LOOT, shouldDropLoot());
        compound.putString(WAConstants.NBT.VARIANT, getVariant().location().toString());
    }


    public SoundEvent getSeenSound() {
        AngelVariants angelVariant = getVariant();
        ItemStack itemStack = angelVariant.getDrops();
        if (itemStack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            return block.defaultBlockState().getSoundType().getBreakSound();
        }
        return SoundEvents.STONE_PLACE;
    }


    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    public long getTimeSincePlayedSound() {
        return timeSincePlayedSound;
    }

    public void setTimeSincePlayedSound(long timeSincePlayedSound) {
        this.timeSincePlayedSound = timeSincePlayedSound;
    }

    public boolean isSeen() {
        return getSeenTime() > 0;
    }

    public int getSeenTime() {
        return getEntityData().get(TIME_VIEWED);
    }

    public void setSeenTime(int time) {
        getEntityData().set(TIME_VIEWED, time);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        // Disabled knockback, intentional for angel behavior
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new BodyRotationAngel(this);
    }

    public void invokeSeen(Player player) {
        getNavigation().moveTo((Path) null, 0);
        setNoAi(true);
    }

    @Override
    protected boolean isImmobile() {
        return getSeenTime() > 0;
    }
}
