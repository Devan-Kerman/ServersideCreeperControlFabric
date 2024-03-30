package dev.ueaj.sscc.mixin;

import dev.ueaj.sscc.CreeperExplosionTypes;
import dev.ueaj.sscc.FireworkCreeper;
import dev.ueaj.sscc.FireworkManufacturer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin (CreeperEntity.class)
public abstract class CreeperEntityMixin_Neuter {
	@Shadow
	public abstract boolean shouldRenderOverlay();

	@Redirect (method = "explode",
	           at = @At (value = "INVOKE",
	                     target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;" +
	                              "DDDFLnet/minecraft/world/World$ExplosionSourceType;)" +
	                              "Lnet/minecraft/world/explosion/Explosion;"))
	private Explosion neuterCreeper(
		World world,
		@Nullable Entity entity,
		double x,
		double y,
		double z,
		float power,
		World.ExplosionSourceType explosionSourceType
	) {
		CreeperExplosionTypes types = world.getGameRules().get(FireworkCreeper.EXPLODE_INTO_FIREWORK).get();
		if (types.firework) {
			boolean charged = this.shouldRenderOverlay();
			world.addFireworkParticle(x, y, z, 0, 0, 0, FireworkManufacturer.generate(charged));
			if (charged) {
				FireworkManufacturer.generateRandomSpecial();
				world.playSound(
					null, x, y, z, SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, SoundCategory.HOSTILE, 8.0F, 2.0F);
			} else {
				world.playSound(
					null, x, y, z, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.HOSTILE, 8.0F, 2.0F);
			}
		}

		if (types.explode.damage) {
			return world.createExplosion(entity, Explosion.createDamageSource(world, entity), null, x, y, z, power, false,
				types.explode.destroy ? explosionSourceType : World.ExplosionSourceType.NONE,
				types.explode.particles, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER,
				SoundEvents.ENTITY_GENERIC_EXPLODE
			);
		} else {
			return null;
		}
	}
}