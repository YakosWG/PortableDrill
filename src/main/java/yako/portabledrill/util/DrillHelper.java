package yako.portabledrill.util;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralWorldInfo;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler.OilWorldInfo;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler.ReservoirType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import yako.portabledrill.init.Config;
import yako.portabledrill.init.ModItems;

public class DrillHelper {

	public static boolean isValid(ItemStack stack) {

		if (stack == null || stack.getItem() != ModItems.DRILL || !stack.hasCapability(CapabilityEnergy.ENERGY, null)) {

			return false;

		} else
			return true;

	}

	public static boolean tryConsumePower(ItemStack stack) {

		int remaining = stack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored() - Config.CONSUMPTION;

		if (remaining >= 0) {

			// Consume Energy
			stack.getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(-Config.CONSUMPTION, false);
			return true;

		}

		return false;
	}

	// Code Taken from Immersive Engineering
	public static ItemStack genMineralSample(ItemStack sample, MineralWorldInfo mineral) {
		
		MineralMix mix = getValidMineral(mineral);

		if (mix != null) {

			ItemNBTHelper.setString(sample, "mineral", mix.name);

			if (ExcavatorHandler.mineralVeinCapacity < 0 || mineral.depletion < 0)
				ItemNBTHelper.setBoolean(sample, "infinite", true);
			else
				ItemNBTHelper.setInt(sample, "depletion", mineral.depletion);

		}

		return sample;

	}

	public static ItemStack genOilSample(ItemStack sample, OilWorldInfo oil) {

		if (getValidReservoir(oil) != null) {

			ItemNBTHelper.setString(sample, "resType", oil.getType().name);
			ItemNBTHelper.setInt(sample, "oil", oil.current);

		}

		return sample;

	}

	public static MineralMix getValidMineral(MineralWorldInfo info) {

		MineralMix mix = null;

		if (info != null) {
			if (info.mineral != null)
				mix = info.mineral;

			if (info.mineralOverride != null)
				mix = info.mineralOverride;

		}

		return mix;

	}

	public static ReservoirType getValidReservoir(OilWorldInfo info) {

		ReservoirType reservoirType = null;

		if (info != null) {
			if (info.getType() != null)
				reservoirType = info.getType();
			if (info.overrideType != null)
				reservoirType = info.overrideType;

		}

		return reservoirType;

	}

}
