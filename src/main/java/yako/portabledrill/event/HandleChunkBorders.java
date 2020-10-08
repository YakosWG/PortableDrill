package yako.portabledrill.event;

import java.lang.reflect.Method;

import blusunrize.immersiveengineering.client.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yako.portabledrill.PortableDrill;
import yako.portabledrill.items.ItemDrill;

@EventBusSubscriber(modid = PortableDrill.MODID)
public class HandleChunkBorders {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void renderLast(RenderWorldLastEvent event) {

		EntityPlayerSP player = Minecraft.getMinecraft().player;

		if (player.getHeldItemMainhand().getItem() instanceof ItemDrill
				|| player.getHeldItemOffhand().getItem() instanceof ItemDrill) {

			// Caling IE render code. This is super broken, super bad and a massive bodge so
			// this will probably break. I really wish this method wasn't private
			try {
				Method genChunkBorders = ClientEventHandler.class.getDeclaredMethod("renderChunkBorders",
						EntityPlayer.class, int.class, int.class);
				genChunkBorders.setAccessible(true);
				genChunkBorders.invoke(ClientEventHandler.class, player, (int) player.posX >> 4 << 4,
						(int) player.posZ >> 4 << 4);

			} catch (Exception e) {
				e.printStackTrace();
				PortableDrill.logger.error("Method renderChunkBorders broke please report this to the author of PortableDrill");
			}
		}

	}

}
