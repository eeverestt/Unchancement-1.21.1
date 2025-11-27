package net.acoyt.unchancement;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unchancement implements ModInitializer {
	public static final String MOD_ID = "unchancement";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public void onInitialize() {
		//
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}