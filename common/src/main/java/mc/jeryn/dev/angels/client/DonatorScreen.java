package mc.jeryn.dev.angels.client;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.data.model.Donator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DonatorScreen extends BaseUIModelScreen<FlowLayout> {

    private static final DataSource MODEL = DataSource.asset(ResourceLocation.fromNamespaceAndPath(WAConstants.MOD_ID, "donators"));

    private final List<Donator> donators;
    private final @Nullable DonatorScreen parent;

    public DonatorScreen(List<Donator> donators, @Nullable DonatorScreen parent) {
        super(FlowLayout.class, MODEL);
        this.donators = donators;
        this.parent = parent;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        // Set screen title
        LabelComponent title = rootComponent.childById(LabelComponent.class, "title");
        if (title != null) {
            title.text(Component.literal("💎 Donators"));
        }

        FlowLayout donatorList = rootComponent.childById(FlowLayout.class, "donator-list");
        if (donatorList == null) {
            // fallback to root if container missing
            donatorList = rootComponent;
        }

        // Hide all predefined donator labels first
        for (int i = 1; i <= 10; i++) {
            LabelComponent label = rootComponent.childById(LabelComponent.class, "donator-" + i);
           // if (label != null) label.visible(false);
        }

        System.out.println(donators + " don don");

        if (donators.isEmpty()) {
            LabelComponent noDonatorsLabel = rootComponent.childById(LabelComponent.class, "no-donators-label");
            if (noDonatorsLabel != null) {
                noDonatorsLabel.text(Component.literal("No donators found."));
                noDonatorsLabel.remove();
             //   noDonatorsLabel.visible(true);
            }
            return;
        }

        // Hide "no donators" label if present
        LabelComponent noDonatorsLabel = rootComponent.childById(LabelComponent.class, "no-donators-label");
        if (noDonatorsLabel != null) {
           // noDonatorsLabel.visible(false);
        }

        // Show donators up to available labels (e.g., 10 max)
        for (int i = 0; i < donators.size() && i < 10; i++) {
            LabelComponent label = rootComponent.childById(LabelComponent.class, "donator-" + (i + 1));
            if (label != null) {
                label.text(Component.literal(donators.get(i).mc_name()));
               // label.visible(true);
            }
        }
    }

    @Override
    public void onClose() {
        if (parent != null) {
            this.minecraft.setScreen(parent);
        } else {
            super.onClose();
        }
    }
}
