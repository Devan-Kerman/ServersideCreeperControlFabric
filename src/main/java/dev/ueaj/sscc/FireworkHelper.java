package dev.ueaj.sscc;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

/**
 * Taken from <a href="https://github.com/DragonsPlusMinecraft/CreeperFirework/blob/multiloader/1.20.4/common/src/main/java/plus/dragons/creeperfirework/misc/FireworkManufacturer.java">CreeperFirework</a>
 */
public class FireworkHelper {
    public static void createFireworkExplosion(World world, Entity entity, double x, double y, double z, NbtCompound generate) {
        ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
        stack.setSubNbt("Fireworks", generate);
        FireworkRocketEntity firework = new FireworkRocketEntity(world, stack, entity, x, y, z, false);

        NbtCompound nbt = new NbtCompound();
        firework.writeCustomDataToNbt(nbt);
        nbt.putInt("LifeTime", 0);
        firework.readCustomDataFromNbt(nbt);

        world.spawnEntity(firework);
        firework.tick();
    }

    @Nullable
    public static Explosion createCreeperExplosion(
        World world,
        @Nullable Entity entity,
        double x,
        double y,
        double z,
        float power,
        World.ExplosionSourceType explosionSourceType,
        CreeperExplosionType type
    ) {
        if (type.explode.damage && !world.isClient) {
            Explosion explosion = world.createExplosion(entity, Explosion.createDamageSource(world, entity), null, x
                , y, z, power, false, type.explode.destroy ? explosionSourceType : World.ExplosionSourceType.NONE,
                false, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER,
                SoundEvents.ENTITY_GENERIC_EXPLODE
            );
            if (type.realExplodeOnClient()) {
                for (PlayerEntity player : world.getPlayers()) {
                    if (player.squaredDistanceTo(x, y, z) < 4096.0 && player instanceof ServerPlayerEntity serverPlayer) {
                        serverPlayer.networkHandler.sendPacket(
                            new ExplosionS2CPacket(x, y, z, power, explosion.getAffectedBlocks(),
                                explosion.getAffectedPlayers().get(serverPlayer), explosion.getDestructionType(),
                                explosion.getParticle(), explosion.getEmitterParticle(), explosion.getSoundEvent()
                            ));
                    }
                }
            }
            return explosion;
        } else {
            return null;
        }
    }

    public static NbtCompound generate(boolean powered) {
        NbtCompound fireworkInfoNbt = getFireworkInfoNbt();
        fireworkInfoNbt.putBoolean("Flicker", powered);
        fireworkInfoNbt.putByte("Type", (byte) (powered ? 1 : 0));
        fireworkInfoNbt.putBoolean("Trail", powered);
        NbtList mimicFireworkItemNbtStructureContainer = new NbtList();
        mimicFireworkItemNbtStructureContainer.add(fireworkInfoNbt);

        NbtCompound ret = new NbtCompound();
        ret.put("Explosions", mimicFireworkItemNbtStructureContainer);
        return ret;
    }

    public static NbtCompound generateRandomSpecial() {
        NbtCompound fireworkInfoNbt = getFireworkInfoNbt();
        fireworkInfoNbt.putByte("Type", (byte) (new Random().nextInt(3) + 2));
        NbtList mimicFireworkItemNbtStructureContainer = new NbtList();
        mimicFireworkItemNbtStructureContainer.add(fireworkInfoNbt);

        NbtCompound ret = new NbtCompound();
        ret.put("Explosions", mimicFireworkItemNbtStructureContainer);
        return ret;
    }

    @NotNull
    private static NbtCompound getFireworkInfoNbt() {
        NbtCompound fireworkInfoNbt = new NbtCompound();

        // Generate Color From DyeColor
        // Maximum is 8 dyes for a firework star
        List<Integer> list = Lists.newArrayList();
        Random rand = new Random();
        int dyeCount = rand.nextInt(6) + 3;
        for (int i = 0; i < dyeCount; i++)
            list.add(DyeColor.values()[rand.nextInt(DyeColor.values().length)].getFireworkColor());

        // Transform color array into required structure
        int[] colours = new int[list.size()];
        for (int i = 0; i < colours.length; i++)
            colours[i] = list.get(i);

        fireworkInfoNbt.putIntArray("Colors", colours);
        return fireworkInfoNbt;
    }
}