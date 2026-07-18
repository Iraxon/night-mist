package net.iraxon.nightmist.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import net.iraxon.nightmist.init.NightMistModBlocks;

import javax.annotation.Nullable;

import java.util.Map;

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
				while (sy <= 2) {
					sz = -1;
					while (sz <= 1) {
						if (sx * sx + sz * sz < 1.1 * 1.1 && (world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == NightMistModBlocks.NIGHT_MIST.get()) {
							found = true;
						}
						sz = sz + 1;
					}
					sy = sy + 1;
				}
				sx = sx + 1;
			}
			if (found == true) {
				entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("night_mist:mist")))), 1);
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 5, 0.125, 0.125, 0.125, 1);
			}
		}
		searchCubeRadius = 64;
		numBlocks = Math.pow(2 * searchCubeRadius, 3);
		for (int index3 = 0; index3 < (int) (((world.getLevelData().getGameRules().getInt(GameRules.RULE_RANDOMTICKING)) / Math.pow(16, 3)) * numBlocks); index3++) {
			tx = x + Mth.nextInt(RandomSource.create(), (int) (0 - searchCubeRadius), (int) searchCubeRadius);
			ty = y + Mth.nextInt(RandomSource.create(), (int) (0 - searchCubeRadius), (int) searchCubeRadius);
			tz = z + Mth.nextInt(RandomSource.create(), (int) (0 - searchCubeRadius), (int) searchCubeRadius);
			if ((world.getBlockState(BlockPos.containing(tx, ty, tz))).is(BlockTags.create(new ResourceLocation("minecraft:replaceable")))) {
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
								if (sx * sx + sy * sy + sz * sz < 1.1 * 1.1 && (world.getBlockState(BlockPos.containing(tx + sx, ty + sy, tz + sz))).getBlock() == NightMistModBlocks.NIGHT_MIST.get()) {
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
					{
						BlockPos _bp = BlockPos.containing(tx, ty, tz);
						BlockState _bs = NightMistModBlocks.NIGHT_MIST.get().defaultBlockState();
						BlockState _bso = world.getBlockState(_bp);
						for (Map.Entry<Property<?>, Comparable<?>> entry : _bso.getValues().entrySet()) {
							Property _property = _bs.getBlock().getStateDefinition().getProperty(entry.getKey().getName());
							if (_property != null && _bs.getValue(_property) != null)
								try {
									_bs = _bs.setValue(_property, (Comparable) entry.getValue());
								} catch (Exception e) {
								}
						}
						world.setBlock(_bp, _bs, 3);
					}
				}
			}
		}
	}
}