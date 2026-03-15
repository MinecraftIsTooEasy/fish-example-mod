package com.example.register;

import com.example.ExampleMod;
import huix.glacier.api.entrypoint.IGameRegistry;
import huix.glacier.api.registry.MinecraftRegistry;

public class ExampleRegistryInit implements IGameRegistry {
	// Registrar instance, using this mod's modid as the namespace
	public static final MinecraftRegistry registry = new MinecraftRegistry(ExampleMod.MOD_ID).initAutoItemRegister();

	// Create an instance of the item
	// public static Item EXAMPLE_ITEM;

	@Override
	public void onGameRegistry() {
		// Register items, bind texture and create localized key
		// registry.registerItem(ExampleMod.MOD_ID + ":example_item", "exampleItem", EXAMPLE_ITEM);
	}
}
