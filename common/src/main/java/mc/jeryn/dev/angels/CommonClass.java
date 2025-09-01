package mc.jeryn.dev.angels;

import mc.jeryn.dev.angels.config.WAConfig;
import mc.jeryn.dev.angels.data.model.donators.Donator;
import mc.jeryn.dev.angels.registry.AngelVariants;
import mc.jeryn.dev.angels.registry.entity.BlockReactions;

import java.util.List;
import java.util.stream.Collectors;

import static mc.jeryn.dev.angels.data.model.donators.VIPCacheManager.getVIPs;

public class CommonClass {

    public static final WAConfig CONFIG = WAConfig.createAndLoad();


    public static void init() {
        AngelVariants.init();

        List<Donator> vips = getVIPs();
        if (vips.isEmpty()) {
            WAConstants.LOG.info("No VIP donators found.");
        } else {
            String vipList = vips.stream()
                    .map(Donator::toString)
                    .collect(Collectors.joining("\n - ", "\nVIP Donators:\n - ", ""));
            WAConstants.LOG.info(vipList);
        }
    }

}