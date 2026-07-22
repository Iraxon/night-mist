package net.iraxon.nightmist.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class PlayerTickUpdateProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level(), event.player.getX(), event.player.getY(), event.player.getZ(), event.player);
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		double searchCubeRadius = 0;
		double numBlocks = 0;
		double tx = 0;
		double ty = 0;
		double tz = 0;
		boolean found = false;
		boolean shouldBecomeMist = false;
		if (true) {
			sx = -1;
			found = false;
			while (sx <= 1) {
				sy = 0;
				while (sy <= 1) {
					sz = -1;
					while (sz <= 1) {
						if (sx * sx + sz * sz < 1.1 * 1.1 && (world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).is(BlockTags.create(new ResourceLocation("night_mist:night_mist")))) {
							found = true;
						}
						sz = sz + 1;
					}
					sy = sy + 1;
				}
				sx = sx + 1;
			}
			if (found == true) {
				if (entity instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(MobEffects.GLOWING)) {
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.SMOKE, x, y, z, 5, 0.125, 0.125, 0.125, 1);
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.GLOW_SQUID_INK, x, y, z, 2, 0.1, 0.1, 0.1, 0.5);
				} else {
					entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("night_mist:mist")))), 1);
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 5, 0.125, 0.125, 0.125, 1);
				}
			}
		}
		searchCubeRadius = 64;
		numBlocks = Math.pow(2 * searchCubeRadius, 3);
		for (int index3 = 0; index3 < (int) (((3 * (world.getLevelData().getGameRules().getInt(GameRules.RULE_RANDOMTICKING))) / Math.pow(16, 3)) * numBlocks); index3++) {
			tx = x + Mth.nextInt(RandomSource.create(), (int) (0 - searchCubeRadius), (int) searchCubeRadius);
			ty = y + Mth.nextInt(RandomSource.create(), (int) (0 - searchCubeRadius), (int) searchCubeRadius);
			tz = z + Mth.nextInt(RandomSource.create(), (int) (0 - searchCubeRadius), (int) searchCubeRadius);
			if ((world.getBlockState(BlockPos.containing(tx, ty, tz))).is(BlockTags.create(new ResourceLocation("minecraft:replaceable")))
					&& !(world.getBlockState(BlockPos.containing(tx, ty, tz))).is(BlockTags.create(new ResourceLocation("night_mist:night_mist")))) {
				if (ty <= Mth.nextInt(RandomSource.create(), 0, 50)) {
					shouldBecomeMist = true;
				} else {
					shouldBecomeMist = false;
					sx = -1;
					while (sx <= 1) {
						sy = -1;
						while (sy <= 1) {
							sz = -1;
							while (sz <= 1) {
								if (sx * sx + sy * sy + sz * sz < 1.1 * 1.1 && (world.getBlockState(BlockPos.containing(tx + sx, ty + sy, tz + sz))).is(BlockTags.create(new ResourceLocation("night_mist:night_mist")))) {
									shouldBecomeMist = true;
									break;
								}
								sz = sz + 1;
							}
							sy = sy + 1;
						}
						sx = sx + 1;
					}
				}
				if (shouldBecomeMist) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(tx, ty, tz), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"setblock ~ ~ ~ night_mist:night_mist destroy");
				}
			}
		}
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"fill ~-15 ~-15 ~-15 ~15 ~15 ~15 night_mist:night_mist replace night_mist:translucent_night_mist");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"fill ~-7 ~-7 ~-7 ~7 ~7 ~7 night_mist:translucent_night_mist replace night_mist:night_mist");
	}
}