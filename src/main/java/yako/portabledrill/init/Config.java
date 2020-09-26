package yako.portabledrill.init;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import yako.portabledrill.PortableDrill;

public class Config {

	public static int MAX_ENERGY;
	public static int MAX_INSERT;
	public static int CONSUMPTION;

	private final static String CATEGORY_DRILL = "Drill";

	public static void readConfig() {
		Configuration cfg = PortableDrill.config;
		try {
			cfg.load();
			initDrillConfig(cfg);
		} catch (Exception e1) {
			PortableDrill.logger.log(Level.ERROR, "Problem loading config file!", e1);
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}

	private static void initDrillConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_DRILL, "General configuration");

		MAX_ENERGY = cfg.getInt("MAX_ENERGY", CATEGORY_DRILL, 10000, 0, Integer.MAX_VALUE,
				"Energy that the portable drill can store");
		MAX_INSERT = cfg.getInt("MAX_INSERT", CATEGORY_DRILL, 2000, 0, Integer.MAX_VALUE,
				"Rate at which energy can be inserted into the item");
		CONSUMPTION = cfg.getInt("CONSUMPTION", CATEGORY_DRILL, 500, 0, Integer.MAX_VALUE,
				"Energy that will be consumed per scan");

	}

}
