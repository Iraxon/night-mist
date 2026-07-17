/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.iraxon.nightmist.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import net.iraxon.nightmist.block.NightMistBlock;
import net.iraxon.nightmist.NightMistMod;

public class NightMistModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, NightMistMod.MODID);
	public static final RegistryObject<Block> NIGHT_MIST;
	static {
		NIGHT_MIST = REGISTRY.register("night_mist", NightMistBlock::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}