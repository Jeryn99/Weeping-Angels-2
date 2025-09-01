package mc.jeryn.dev.angels.registry.entity;

import com.google.common.collect.ImmutableList;
import mc.jeryn.dev.angels.CommonClass;
import mc.jeryn.dev.angels.registry.*;
import mc.jeryn.dev.angels.registry.damage.WADamageTypes;
import mc.jeryn.dev.angels.util.HurtUtil;
import mc.jeryn.dev.angels.util.WATeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class WeepingAngel extends AbstractWeepingAngel {

    public AnimationState POSE_ANIMATION_STATE = new AnimationState();

    private static final double COMMUNICATION_RADIUS = 16.0;
    private static final double FORMATION_SPACING = 2.5;

    private int fakeAnimation = -1;

    public int getFakeAnimation() {
        return fakeAnimation;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (tickCount % 20 == 0) {
            coordinateWithNearbyAngels();
        }
    }

    private void coordinateWithNearbyAngels() {
        if (level().isClientSide()) return;

        List<WeepingAngel> nearbyAngels = level().getEntitiesOfClass(
                WeepingAngel.class,
                getBoundingBox().inflate(COMMUNICATION_RADIUS),
                angel -> angel != this && !angel.isRemoved()
        );

        if (nearbyAngels.isEmpty()) return;

        Vec3 center = getCenterOfMass(nearbyAngels);
        double angleOffset = 360.0 / (nearbyAngels.size() + 1);

        for (int i = 0; i < nearbyAngels.size(); i++) {
            WeepingAngel angel = nearbyAngels.get(i);
            double angle = Math.toRadians(i * angleOffset);
            double offsetX = FORMATION_SPACING * Math.cos(angle);
            double offsetZ = FORMATION_SPACING * Math.sin(angle);
            BlockPos targetPos = new BlockPos((int) (center.x + offsetX), (int) center.y, (int) (center.z + offsetZ));

            angel.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
        }

        // Position this angel too (optional if you want the leader in formation)
        getNavigation().moveTo(center.x, center.y, center.z, 1.0);
    }

    private Vec3 getCenterOfMass(List<WeepingAngel> angels) {
        double sumX = getX();
        double sumY = getY();
        double sumZ = getZ();

        for (WeepingAngel angel : angels) {
            sumX += angel.getX();
            sumY += angel.getY();
            sumZ += angel.getZ();
        }

        int total = angels.size() + 1; // include this angel
        return new Vec3(sumX / total, sumY / total, sumZ / total);
    }

    public void setFakeAnimation(int fakeAnimation) {
        this.fakeAnimation = fakeAnimation;
    }

    public WeepingAngel(Level worldIn) {
        super(WAEntities.WEEPING_ANGEL, worldIn);
        int id = 0;

        // Goals
        goalSelector.addGoal(id++, new OpenDoorGoal(this, false));
        goalSelector.addGoal(id++, new MeleeAttackGoal(this, 0.5f, true));
        goalSelector.addGoal(id++, new ClimbOnTopOfPowderSnowGoal(this, this.level()));

        // Targeting
        targetSelector.addGoal(id++, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(id++, (new HurtByTargetGoal(this)).setAlertOthers(WeepingAngel.class));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData spawnGroupData) {
        setVariant(AngelVariants.getVariantForPos(this));
        return super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
    }


    @Nullable
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(WAItems.ANGEL_SPAWNER);
    }


    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity pEntity) {
        if (!(pEntity instanceof Player player))
            return false;

        float attackDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);

        // Teleporting
        if (WATeleporter.tryRandomTeleport(pEntity, serverLevel, random)) {
            return true;
        }

        // Theft
        stealItems(player);

        // Hurt
        boolean didHurt = pEntity.hurtOrSimulate(WADamageTypes.getSource(serverLevel, WADamageTypes.SNAPPED_NECK), attackDamage);
        this.setLastHurtMob(pEntity);
        return didHurt;
    }


    @Override
    public boolean killedEntity(ServerLevel serverLevel, LivingEntity livingEntity) {
        boolean wasKilled = super.killedEntity(serverLevel, livingEntity);
        if (wasKilled) {
            playSound(WASounds.NECK_SNAP);
        }
        return wasKilled;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!state.liquid()) {
            BlockState blockState = Blocks.STONE.defaultBlockState();
            SoundType soundType = blockState.getSoundType();
            this.playSound(soundType.getStepSound(), soundType.getVolume() * 0.15F, soundType.getPitch());
        }
    }

/*
    @Override
    public void push(Entity entity) {
        super.push(entity);
        doHurtTarget(entity);
    }

    @Override
    protected void doPush(Entity entity) {
        super.doPush(entity);
        doHurtTarget(entity);
    }
*/

    @Override
    public void tick() {
        super.tick();

        Level level = level();

      /*  if (!level.isClientSide()) {
            if (CatacombTracker.isInCatacomb(this)) {
                Warden.applyDarknessAround((ServerLevel) level, this.position(), this, 20);
            }
        }*/

        if (!POSE_ANIMATION_STATE.isStarted()) {
            POSE_ANIMATION_STATE.start(tickCount - random.nextInt(10000));
        }

        // Ensure angels do not lock in the air or walk through water
        if (isSeen() && (!onGround() || level.containsAnyLiquid(getBoundingBox())) && !isHooked()) {
            setSeenTime(0);
            setNoAi(false);
        }

        if (tickCount % 400 == 0) {
            if (isHooked()) {
                setHooked(false);
            }
            if (isSeen()) {
                investigateBlocks();
            }
        }
    }

    public void stealItems(Player player) {
        if (!CommonClass.CONFIG.angelTheft()) return;
        if (!getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) return;

        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack item = inventory.getItem(i);

            if (item.is(WATags.STEALABLE_ITEMS) && item != player.getOffhandItem()) {
                setItemInHand(InteractionHand.MAIN_HAND, item.copy());
                setGuaranteedDrop(EquipmentSlot.MAINHAND);
                inventory.setItem(i, ItemStack.EMPTY);
                break;
            }
        }
    }



    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level worldIn) {
        WallClimberNavigation navigator = new WallClimberNavigation(this, worldIn);
        navigator.setCanFloat(false);
        navigator.setCanOpenDoors(true);
        navigator.setAvoidSun(false);
        navigator.setSpeedModifier(1.0D);

        return navigator;
    }

    @Override
    public void kill(ServerLevel serverLevel) {
        remove(RemovalReason.KILLED);
    }

    @Override
    public boolean onClimbable() {
        return horizontalCollision;
    }

    @Override
    public void invokeSeen(Player player) {
        super.invokeSeen(player);
        if (getSeenTime() == 1 && System.currentTimeMillis() - getTimeSincePlayedSound() > 5000) {
            setEmotion(AngelEmotion.randomEmotion(random));
            playSound(SoundEvents.STONE_PLACE);

            if (player instanceof ServerPlayer serverPlayer && player.distanceTo(this) < 15) {
                setTimeSincePlayedSound(System.currentTimeMillis());
                serverPlayer.connection.send(new ClientboundSoundPacket(Holder.direct(getSeenSound()), SoundSource.BLOCKS, player.getX(), player.getY(), player.getZ(), 0.25F, 1.0F, this.random.nextLong()));
            }
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.STONE_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return WASounds.CRUMBLING;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float amount) {
        Entity directEntity = damageSource.getDirectEntity();

        // Check if the angel was hit by a projectile
        if (directEntity instanceof Projectile projectile) {
            Entity owner = projectile.getOwner();
            if (owner instanceof LivingEntity target) {
                shootBackProjectile(projectile, target);
                return false;
            }
        }

        boolean isHurt = HurtUtil.handleAngelHurt(this, damageSource, amount);
        if (isHurt) {
            if (getVariant().getDrops().getItem() instanceof BlockItem blockItem) {
                BlockState defaultState = blockItem.getBlock().defaultBlockState();
                playSound(defaultState.getSoundType().getHitSound());
            }
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()), getX(), getY(0.5D), getZ(), 5, 0.1D, 0.0D, 0.1D, 0.2D);
            return super.hurtServer(serverLevel, damageSource, amount);
        }
        return false;
    }

    private void shootBackProjectile(Projectile original, LivingEntity target) {
        if (level().isClientSide() || target == null) return;

        Entity reflectedEntity = original.getType().create(level(), EntitySpawnReason.TRIGGERED);
        if (!(reflectedEntity instanceof Projectile reflected)) return;

        reflected.setOwner(this);
        reflected.shoot(getX(), getEyeY() - 0.1, getZ(), getYRot(), getXRot());

        Vec3 from = getEyePosition();
        Vec3 to = target.getEyePosition();
        Vec3 direction = to.subtract(from).normalize();

        float speed = 1.6f;
        float inaccuracy = 0.01f;

        reflected.shoot(direction.x, direction.y, direction.z, speed, inaccuracy);

        if (reflected instanceof AbstractArrow arrow && original instanceof AbstractArrow originalArrow) {
            arrow.setCritArrow(originalArrow.isCritArrow());
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
        }

        level().addFreshEntity(reflected);
    }

    @Override
    protected void tickDeath() {
        Level level = level();
        ++this.deathTime;

        if (!level.isClientSide()) {
            if (deathTime <= 20) {
                ((ServerLevel) level).sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()),
                        getX(), getY(), getZ(),
                        40, 0.5, 0.5, 0.5, 0.1
                );

                if (deathTime == 1) {
                    level.playSound(null, blockPosition(), SoundEvents.STONE_BREAK, SoundSource.HOSTILE, 1.0F, 0.6F);
                }
            }

            if (deathTime >= 20) {
                if (shouldDropLoot()) {
                    ItemEntity itemEntity = new ItemEntity(EntityType.ITEM, level);
                    itemEntity.setItem(getVariant().getDrops());
                    itemEntity.setPos(getX(), getY(), getZ());
                    level.addFreshEntity(itemEntity);
                }
                remove(RemovalReason.KILLED);
            }
        }
    }


    public void investigateBlocks() {
        if (level() instanceof ServerLevel level) {
            if (level.isClientSide() || !level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || !CommonClass.CONFIG.blockBreaking())
                return;
            for (Iterator<BlockPos> iterator = BlockPos.withinManhattanStream(blockPosition(), 25, 3, 25).iterator(); iterator.hasNext(); ) {
                BlockPos pos = iterator.next();
                BlockState blockState = level.getBlockState(pos);
                BlockReactions.BlockReaction blockBehaviour = BlockReactions.BLOCK_BEHAVIOUR.get(blockState.getBlock());
                boolean completed = blockBehaviour.interact(this, blockState, level, pos);
                if (completed) {
                    Warden.applyDarknessAround(level, Vec3.atBottomCenterOf(blockPosition()), this, 64);
                    return;
                }
            }
        }
    }

    public Crackiness getCrackiness() {
        return WeepingAngel.Crackiness.byFraction(this.getHealth() / this.getMaxHealth());
    }

    public enum Crackiness {
        NONE(1.0F),
        LOW(0.75F),
        MEDIUM(0.5F),
        HIGH(0.25F);

        private static final List<WeepingAngel.Crackiness> BY_DAMAGE = Stream.of(values()).sorted(Comparator.comparingDouble((crackiness) -> crackiness.fraction)).collect(ImmutableList.toImmutableList());
        private final float fraction;

        Crackiness(float pFraction) {
            this.fraction = pFraction;
        }

        public static WeepingAngel.Crackiness byFraction(float pFraction) {
            for (WeepingAngel.Crackiness crackiness : BY_DAMAGE) {
                if (pFraction < crackiness.fraction) {
                    return crackiness;
                }
            }

            return NONE;
        }
    }
}