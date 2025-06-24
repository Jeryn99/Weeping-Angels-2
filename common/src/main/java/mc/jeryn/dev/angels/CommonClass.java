package mc.jeryn.dev.angels;

import mc.jeryn.dev.angels.config.WAConfig;
import mc.jeryn.dev.angels.data.model.Donator;
import mc.jeryn.dev.angels.registry.AngelVariants;

import java.util.List;

import static mc.jeryn.dev.angels.data.model.VIPCacheManager.getVIPs;

public class CommonClass {

    public static final WAConfig CONFIG = WAConfig.createAndLoad();


    public static void init() {
        AngelVariants.init();

        List<Donator> vips = getVIPs();
        for (Donator donator : vips) {
            WAConstants.LOG.info(donator.toString());
        }
    }
}