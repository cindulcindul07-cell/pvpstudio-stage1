package dev.pvpstudio.client;

import dev.pvpstudio.client.gui.MenuScreen;
import dev.pvpstudio.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PvpStudioClient implements ClientModInitializer {
    public static final String MOD_ID = "pvpstudio";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        ConfigManager.load();

        // 1.21.9+ keybinds take a Category object instead of a string.
        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of(MOD_ID, "main"));
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpstudio.open_menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, category));

        ClientTickEvents.END_CLIENT_TICK.register(PvpStudioClient::onTick);
        LOGGER.info("PvP Studio initialised");
    }

    private static void onTick(MinecraftClient client) {
        try {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new MenuScreen(null));
            }
        } catch (RuntimeException e) {
            LOGGER.error("Tick handler failed", e);
        }
    }
}
