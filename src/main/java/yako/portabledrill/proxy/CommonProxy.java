package yako.portabledrill.proxy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yako.portabledrill.PortableDrill;
import yako.portabledrill.init.ModItems;
import yako.portabledrill.util.CapabilityHandlerDrill;

@EventBusSubscriber
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {

	}

	public void init(FMLInitializationEvent e) {

	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	@SubscribeEvent
	public static void registerItems(Register<Item> e) {

		e.getRegistry().register(ModItems.DRILL);

	}

	@SubscribeEvent
	public static void attachItemCapabilites(AttachCapabilitiesEvent<ItemStack> e) {

		if (e.getObject().getItem() == ModItems.DRILL) {

			e.addCapability(new ResourceLocation(PortableDrill.MODID, "energy"),
					new CapabilityHandlerDrill(e.getObject()));

		}

	}
}