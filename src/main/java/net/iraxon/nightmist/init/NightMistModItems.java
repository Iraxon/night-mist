/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.iraxon.nightmist.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import net.iraxon.nightmist.item.SpectralFlareItem;
import net.iraxon.nightmist.NightMistMod;

public class NightMistModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, NightMistMod.MODID);
	public static final RegistryObject<Item> SPECTRAL_FLARE;
	static {
		SPECTRAL_FLARE = REGISTRY.register("spectral_flare", SpectralFlareItem::new);
	}
	// Start of user code block custom items
	// End of user code block custom items
}