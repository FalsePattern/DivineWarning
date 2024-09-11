package divinewarning;

import lombok.val;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.FMLLaunchHandler;

@Mod(modid = Tags.MOD_ID,
     name = Tags.MOD_NAME,
     version = Tags.MOD_VERSION,
     acceptedMinecraftVersions = "[1.7.10]",
     acceptableRemoteVersions = "*")
public class Divine {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        if (FMLLaunchHandler.side().isClient()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    private double lastInt = 0;

    @SubscribeEvent
    public void onTick(RenderGameOverlayEvent.Post e) {
        if (e.type != RenderGameOverlayEvent.ElementType.ALL)
            return;

        val mc = Minecraft.getMinecraft();
        if (mc.theWorld == null)
            return;
        if (mc.thePlayer == null)
            return;
        val health = mc.thePlayer.getHealth();
        if (health > 6)
            return;

        double frequency = (2/3d) + health / 3;
        double time = ((double)e.partialTicks + mc.theWorld.getTotalWorldTime()) / 20d;
        double periodic = time % frequency;
        double duration = 2;
        double intensity = Math.max(duration - periodic, 0) / duration;
        if (intensity > lastInt) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation(Tags.MOD_ID, "ui.divine.warning"), 1.0F));
        }
        lastInt = intensity;

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        val tex = new ResourceLocation(Tags.MOD_ID, "him.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
        GL11.glColor4f(1F, 1F, 1F, (float) intensity);
        val tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(0, e.resolution.getScaledHeight(), 0, 0, 1);
        tess.addVertexWithUV(e.resolution.getScaledWidth(), e.resolution.getScaledHeight(), 0, 1, 1);
        tess.addVertexWithUV(e.resolution.getScaledWidth(), 0, 0, 1, 0);
        tess.addVertexWithUV(0, 0, 0, 0, 0);
        tess.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}
