package dev.pvpstudio.client.gui;

import dev.pvpstudio.config.ConfigManager;
import dev.pvpstudio.config.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.Locale;

/** Stage 1 shell: tab bar, themed panel, placeholder body. Later stages fill in each tab. */
public class MenuScreen extends Screen {
    private enum Tab {
        TEXTURES, PACKS, PVP, HUD, UI, PROFILES;
        Text label() { return Text.translatable("pvpstudio.tab." + name().toLowerCase(Locale.ROOT)); }
    }

    private final Screen parent;
    private Tab tab = Tab.TEXTURES;

    public MenuScreen(Screen parent) {
        super(Text.translatable("pvpstudio.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int tabW = 70, gap = 4, y = 22;
        int total = Tab.values().length * (tabW + gap) - gap;
        int x = (width - total) / 2;
        for (Tab t : Tab.values()) {
            addDrawableChild(ButtonWidget.builder(t.label(), b -> tab = t)
                    .dimensions(x, y, tabW, 20).build());
            x += tabW + gap;
        }
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), b -> close())
                .dimensions(width / 2 - 50, height - 28, 100, 20).build());
    }

    /** Replaces vanilla's blurred background with our themed fill. */
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        ModConfig.Ui ui = ConfigManager.get().ui;
        context.fill(0, 0, width, height, ui.background);
        context.fill(0, 48, width, 49, ui.accent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 8, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, tab.label(), width / 2, 60, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.translatable("pvpstudio.placeholder"),
                width / 2, 80, 0xFFAAAAAA);
    }

    @Override
    public void close() {
        ConfigManager.save();
        if (client != null) client.setScreen(parent);
    }
}
