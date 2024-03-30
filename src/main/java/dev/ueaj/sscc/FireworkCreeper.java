package dev.ueaj.sscc;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FireworkCreeper implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("firework-creeper");
	public static final GameRules.Key<EnumRule<CreeperExplosionType>> EXPLODE_INTO_FIREWORK = GameRuleRegistry.register(
		"creeperExplosionBehavior", GameRules.Category.MOBS, GameRuleFactory.createEnumRule(CreeperExplosionType.DEFAULT));

	@Override
	public void onInitialize() {
		LOGGER.info("Firework Creeper Mod Installed!");
	}
}