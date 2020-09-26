package yako.portabledrill.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import yako.portabledrill.init.Config;

public class CapabilityHandlerDrill implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IEnergyStorage.class)
	private static final Capability<IEnergyStorage> ENERGY_CAPABILITY = null;
	private EnergyStorageDrill ENERGY_STORAGE = new EnergyStorageDrill(Config.MAX_ENERGY, Config.MAX_INSERT, 0, getNBTEnergy());

	private ItemStack drill;

	public CapabilityHandlerDrill(ItemStack drill) {

		this.drill = drill;

	}
	
	private int getNBTEnergy() {
		
		return drill != null && drill.hasTagCompound() && drill.getTagCompound().hasKey("Energy")
				? drill.getTagCompound().getInteger("Energy")
				: 0;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

		if (capability == CapabilityEnergy.ENERGY) {

			return true;

		}

		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

		if (capability == CapabilityEnergy.ENERGY) {

			return CapabilityEnergy.ENERGY.cast(ENERGY_STORAGE);

		}

		return null;
	}

	public class EnergyStorageDrill extends EnergyStorage {

		public EnergyStorageDrill(int capacity, int maxReceive, int maxExtract, int energy) {
			super(capacity, maxReceive, maxExtract, energy);

		}
		
		public void setEnergy(int energy) {
			
			this.energy = energy;
			onEnergyChanged();
			
		}

		@Override
		public boolean canExtract() {
			return false;
		}

		public void onEnergyChanged() {
			NBTTagCompound nbt = drill.hasTagCompound() ? drill.getTagCompound() : new NBTTagCompound();

			nbt.setInteger("Energy", getEnergyStored());
			drill.setTagCompound(nbt);			
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {

			int received = super.receiveEnergy(maxReceive, simulate);
			onEnergyChanged();
			return received;
		}

	}

	@Override
	public NBTBase serializeNBT() {
//		return drill.hasTagCompound() && drill.getTagCompound().hasKey("Energy")
//				? new NBTTagInt(drill.getTagCompound().getInteger("Energy"))
//				: new NBTTagInt(0);
		
		return ENERGY_CAPABILITY.writeNBT(ENERGY_STORAGE, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {

//		NBTTagInt nbtTag = drill.hasTagCompound() && drill.getTagCompound().hasKey("Energy")
//				? new NBTTagInt(drill.getTagCompound().getInteger("Energy"))
//				: new NBTTagInt(0);
//				
//		ENERGY_STORAGE.setEnergy(nbtTag.getInt());
//
//		ENERGY_CAPABILITY.readNBT(ENERGY_STORAGE, null, nbtTag);
		
		ENERGY_CAPABILITY.readNBT(ENERGY_STORAGE, null, nbt);
		
	}

}
