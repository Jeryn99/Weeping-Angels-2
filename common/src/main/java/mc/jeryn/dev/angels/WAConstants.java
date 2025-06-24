package mc.jeryn.dev.angels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WAConstants {

	public static final String MOD_ID = "weeping_angels";
	public static final String MOD_NAME = "Weeping Angels";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static class NBT {
		public static final String IS_SEEN = "IsSeen";
		public static final String TIME_SEEN = "TimeSeen";
		public static final String EMOTION = "Emotion";
		public static final String IS_HOOKED = "IsHooked";
		public static final String DROPS_LOOT = "DropsLoot";
		public static final String VARIANT = "Variant";
	}
}
