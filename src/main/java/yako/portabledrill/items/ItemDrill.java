package yako.portabledrill.items;

import java.util.List;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralWorldInfo;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler.OilWorldInfo;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler.ReservoirType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yako.portabledrill.PortableDrill;
import yako.portabledrill.init.Config;
import yako.portabledrill.init.ModItems;
import yako.portabledrill.util.CapabilityHandlerDrill.EnergyStorageDrill;

public class ItemDrill extends Item {

	public ItemDrill() {

		setRegistryName(PortableDrill.MODID, "portable_drill");
		setUnlocalizedName(PortableDrill.MODID + ".portable_drill");

		setMaxDamage(0);
		setMaxStackSize(1);
		setNoRepair();
		setCreativeTab(ImmersiveEngineering.creativeTab);

	}

	@SideOnly(Side.CLIENT)
	public void initModel() {

		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));

	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (worldIn.isRemote) {
			// Run Calculations Server side only
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}

		ItemStack stack = playerIn.getHeldItem(handIn);

		if (stack == null || stack.getItem() != ModItems.DRILL || !stack.hasCapability(CapabilityEnergy.ENERGY, null))
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));

		// Creative ignores power consumption
		if (!playerIn.isCreative()) {

			int remaining = stack.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored() - Config.CONSUMPTION;

			if (remaining < 0) {

				playerIn.sendMessage(new TextComponentString(TextFormatting.GOLD
						+ "Not enough energy! You need at least " + TextFormatting.AQUA + Config.CONSUMPTION));

				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
			}

			// Consume Energy
			stack.getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(-Config.CONSUMPTION, false);

		}

		// Mineral
		MineralWorldInfo worldInfo = ExcavatorHandler.getMineralWorldInfo(worldIn, playerIn.chunkCoordX,
				playerIn.chunkCoordZ);

		if (worldInfo != null && worldInfo.mineral != null) {
			MineralMix mix = worldInfo.mineral;

			if (worldInfo.mineralOverride != null)
				mix = worldInfo.mineralOverride;

			playerIn.sendMessage(new TextComponentString(
					TextFormatting.GOLD + "Mineral-Mix present: " + TextFormatting.AQUA + mix.name));

		} else
			playerIn.sendMessage(new TextComponentString(TextFormatting.GOLD + "No minerals found!"));

		if (PortableDrill.immersivePetroleumPresent) {
			// Reservoir
			OilWorldInfo worldInfoOil = PumpjackHandler.getOilWorldInfo(worldIn, playerIn.chunkCoordX,
					playerIn.chunkCoordZ);

			if (worldInfoOil != null && worldInfoOil.getType() != null) {
				ReservoirType res = worldInfoOil.getType();

				// Capitalize first letter because it looks better

				playerIn.sendMessage(new TextComponentString(TextFormatting.GOLD + "Fluid-Reservoir present: "
						+ TextFormatting.AQUA + res.name.substring(0, 1).toUpperCase() + res.name.substring(1)));

			} else
				playerIn.sendMessage(new TextComponentString(TextFormatting.GOLD + "No reservoir found!"));

		}

		playerIn.sendMessage(
				new TextComponentString(TextFormatting.DARK_GRAY + "------------------------------------"));

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		int energy = stack.hasTagCompound() && stack.getTagCompound().hasKey("Energy")
				? stack.getTagCompound().getInteger("Energy")
				: 0;

		tooltip.add(TextFormatting.GOLD + "Energy: " + TextFormatting.AQUA + energy + TextFormatting.DARK_GRAY + "/"
				+ TextFormatting.DARK_AQUA + Config.MAX_ENERGY);

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

		if (tab.getTabLabel() == ImmersiveEngineering.MODID) {

			ItemStack it = new ItemStack(this);
			((EnergyStorageDrill) it.getCapability(CapabilityEnergy.ENERGY, null)).setEnergy(Config.MAX_ENERGY);

			items.add(it);

		}
		super.getSubItems(tab, items);
	}

}
