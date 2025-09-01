package mc.jeryn.dev.angels.client.screen;

import com.mojang.authlib.yggdrasil.ProfileResult;
import mc.jeryn.dev.angels.data.model.donators.Donator;
import mc.jeryn.dev.angels.data.model.donators.VIPCacheManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class VIPListScreen extends Screen {

    private VIPGrid vipGrid;

    public VIPListScreen() {
        super(Component.literal("VIP Donators"));
    }

    @Override
    protected void init() {
        List<Donator> donators = VIPCacheManager.getVIPs();
        vipGrid = new VIPGrid(minecraft, donators, width, height);
        setFocused(vipGrid);
        addRenderableWidget(vipGrid);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        vipGrid.render(gfx, mouseX, mouseY, delta);
        gfx.drawCenteredString(font, "VIP Donators", width / 2, 15, 0xFFFFFF);
        super.render(gfx, mouseX, mouseY, delta);
    }

    private static class VIPGrid extends net.minecraft.client.gui.components.AbstractWidget implements GuiEventListener {

        private final Minecraft mc;
        private final List<Donator> donators;
        private final int width;
        private final int height;

        private final int itemSize = 64;
        private final int padding = 8;
        private final int columns;
        private int scrollOffset = 0;
        private int maxScroll;

        public VIPGrid(Minecraft mc, List<Donator> donators, int screenWidth, int screenHeight) {
            super(0, 0, screenWidth, screenHeight, Component.empty());
            this.mc = mc;
            this.donators = donators;
            this.width = screenWidth;
            this.height = screenHeight;

            this.columns = Math.max(1, (width - padding) / (itemSize + padding));
            int rows = (int) Math.ceil(donators.size() / (double) columns);
            int contentHeight = rows * (itemSize + padding);
            maxScroll = Math.max(0, contentHeight - (height - 40));
        }

        @Override
        protected void renderWidget(GuiGraphics gfx, int mousex, int mousey, float delta) {
            int startX = padding;
            int startY = 40 - scrollOffset;

            int rows = (int) Math.ceil(donators.size() / (double) columns);

            for (int i = 0; i < donators.size(); i++) {
                int col = i % columns;
                int row = i / columns;

                int x = startX + col * (itemSize + padding);
                int y = startY + row * (itemSize + padding);

                if (y + itemSize < 40 || y > height) continue;

                try {
                    renderEntry(gfx, donators.get(i), x, y, itemSize, itemSize);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // Draw scrollbar
            if (maxScroll > 0) {
                int scrollbarX = width - 10;
                int scrollbarY = 40;
                int scrollbarHeight = height - 40;

                gfx.fill(scrollbarX, scrollbarY, scrollbarX + 6, scrollbarY + scrollbarHeight, 0xFFAAAAAA); // track

                // Thumb
                float scrollRatio = (float) scrollOffset / maxScroll;
                int thumbHeight = Math.max(20, scrollbarHeight * (height - 40) / (height - 40 + maxScroll));
                int thumbY = scrollbarY + (int) ((scrollbarHeight - thumbHeight) * scrollRatio);
                gfx.fill(scrollbarX, thumbY, scrollbarX + 6, thumbY + thumbHeight, 0xFF888888);
            }
        }

        private void renderEntry(GuiGraphics gfx, Donator donator, int x, int y, int width, int height) throws ExecutionException, InterruptedException {
            int faceSize = 20; // smaller face size
            int faceX = x + (width - faceSize) / 2;
            int faceY = y;

            renderPlayerFace(gfx, faceX, faceY, faceSize, VIPEntry.parseUUID(donator.uuid()));

            int nameY = faceY + faceSize + 2; // tighter spacing
            String name = donator.mcName();
            int nameWidth = mc.font.width(name);
            int nameX = x + (width - nameWidth) / 2;

            gfx.drawString(mc.font, name, nameX, nameY, 0xFFFFFF);
        }

        public static void renderPlayerFace(GuiGraphics guiGraphics, int x, int y, int size, UUID playerUuid) {
            Minecraft minecraft = Minecraft.getInstance();
            ProfileResult profileresult = minecraft.getMinecraftSessionService().fetchProfile(playerUuid, false);
            PlayerSkin playerskin = profileresult != null ? minecraft.getSkinManager().getInsecureSkin(profileresult.profile()) : DefaultPlayerSkin.get(playerUuid);
            PlayerFaceRenderer.draw(guiGraphics, playerskin, x, y, size);
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
            scrollOffset -= (int) (scrollY * (itemSize + padding));
            if (scrollOffset < 0) scrollOffset = 0;
            if (scrollOffset > maxScroll) scrollOffset = maxScroll;
            return true;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

}

    private static class VIPEntry {
        public static java.util.UUID parseUUID(String uuidStr) {
            if (uuidStr == null) return null;
            if (uuidStr.contains("-")) {
                return java.util.UUID.fromString(uuidStr);
            } else if (uuidStr.length() == 32) {
                String formatted = uuidStr.substring(0, 8) + "-" +
                        uuidStr.substring(8, 12) + "-" +
                        uuidStr.substring(12, 16) + "-" +
                        uuidStr.substring(16, 20) + "-" +
                        uuidStr.substring(20, 32);
                return java.util.UUID.fromString(formatted);
            } else {
                throw new IllegalArgumentException("Invalid UUID string length: " + uuidStr);
            }
        }
    }
}
