package yako.portabledrill;

import java.io.File;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yako.portabledrill.init.Config;
import yako.portabledrill.proxy.CommonProxy;

@Mod(modid = PortableDrill.MODID, name = PortableDrill.NAME, version = PortableDrill.VERSION, dependencies = "required-after:immersiveengineering; after:immersivepetroleum")
public class PortableDrill {
	public static final String MODID = "portabledrill";
	public static final String NAME = "Portable Drill";
	public static final String VERSION = "1.0";

	public static Logger logger;
	
	@SidedProxy(clientSide = "yako.portabledrill.proxy.ClientProxy", serverSide = "yako.portabledrill.proxy.ServerProxy")
	public static CommonProxy proxy;

	public static boolean immersivePetroleumPresent = false;
	
	@Instance
	public static PortableDrill instance;
	public static Configuration config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		
		File directory = event.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "portabledrill.cfg"));
		Config.readConfig();
		
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (Loader.isModLoaded("immersivepetroleum"))
			immersivePetroleumPresent = true;
		
		proxy.postInit(event);
	}
}
