package net.iraxon.nightmist.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.iraxon.nightmist.init.NightMistModBlocks;

public class NightMistSpreadProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == NightMistModBlocks.NIGHT_MIST.get()) && (world.getBlockState(BlockPos.containing(x, y, z))).is(BlockTags.create(new ResourceLocation("minecraft:replaceable")))
				&& (y < 0 || y < Mth.nextInt(RandomSource.create(), 0, 50) || true)) {
			world.setBlock(BlockPos.containing(x, y, z), NightMistModBlocks.NIGHT_MIST.get().defaultBlockState(), 3);
		}
	}
}