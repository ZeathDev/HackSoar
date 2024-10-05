package dev.hacksoar.modules.impl.render;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRendererLivingEntity;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleTag
public class Chams extends Module {
    public Chams() {
        super("Chams","Like esp in csgo", ModuleCategory.Render);
    }

    public ListValue modeValue = new ListValue("Mode",new String[]{"Normal","Colored"},"Normal");
    public BoolValue teamColor = new BoolValue("TeamColor",false);
    public BoolValue flatValue = new BoolValue("Flat",false);

    @EventTarget
    public void onRenderLivingEntity(EventRendererLivingEntity evt) {
        Color color = Color.RED;
        String mode;
        mode = modeValue.get();
        boolean flat;
        flat = flatValue.get();
        boolean teamCol;
        teamCol = teamColor.get();

        if (evt.getEntity() != mc.thePlayer) {
            if (evt.isPre()) {
                if (mode.equals("Colored")) {
                    evt.setCancelled(true);
                    try {
                        Render renderObject = mc.getRenderManager().getEntityRenderObject(evt.getEntity());
                        if (renderObject != null && mc.getRenderManager().renderEngine != null && renderObject instanceof RendererLivingEntity) {
                            GL11.glPushMatrix();
                            GL11.glDisable(GL11.GL_DEPTH_TEST);
                            GL11.glBlendFunc(770, 771);
                            GL11.glDisable(GL11.GL_TEXTURE_2D);
                            GL11.glEnable(GL11.GL_BLEND);
                            Color teamColor = null;

                            if (flat) {
                                GlStateManager.disableLighting();
                            }

                            if (teamCol) {
                                String text = evt.getEntity().getDisplayName().getFormattedText();
                                for (int i = 0; i < text.length(); i++) {
                                    if ((text.charAt(i) == (char) 0x00A7) && (i + 1 < text.length())) {
                                        char oneMore = Character.toLowerCase(text.charAt(i + 1));
                                        int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
                                        if (colorCode < 16) {
                                            try {
                                                Color newCol = teamColor = new Color(mc.fontRendererObj.colorCode[colorCode]);
                                                GL11.glColor4f(newCol.getRed() / 255f, newCol.getGreen() / 255f, newCol.getBlue() / 255f, 1f);
                                            } catch (ArrayIndexOutOfBoundsException exception) {
                                                GL11.glColor4f(1, 0, 0, 1f);
                                            }
                                        }
                                    }
                                }
                            } else {
                                Color c = color;
                                GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 500f, c.getBlue() / 500f, 1f);
                            }

                            ((RendererLivingEntity) renderObject).renderModel(evt.getEntity(), evt.getLimbSwing(), evt.getLimbSwingAmount(), evt.getAgeInTicks(), evt.getRotationYawHead(), evt.getRotationPitch(), evt.getOffset());
                            GL11.glEnable(GL11.GL_DEPTH_TEST);

                            if (teamCol && teamColor != null) {
                                GL11.glColor4f(teamColor.getRed() / 255f, teamColor.getGreen() / 255f, teamColor.getBlue() / 255f, 1f);
                            } else {
                                Color c = color;
                                GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1f);
                            }

                            ((RendererLivingEntity) renderObject).renderModel(evt.getEntity(), evt.getLimbSwing(), evt.getLimbSwingAmount(), evt.getAgeInTicks(), evt.getRotationYawHead(), evt.getRotationPitch(), evt.getOffset());
                            GL11.glEnable(GL11.GL_TEXTURE_2D);
                            GL11.glDisable(GL11.GL_BLEND);
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                            if (flat) {
                                GlStateManager.enableLighting();
                            }

                            GL11.glPopMatrix();
                            ((RendererLivingEntity) renderObject).renderLayers(evt.getEntity(), evt.getLimbSwing(), evt.getLimbSwingAmount(), mc.timer.renderPartialTicks, evt.getAgeInTicks(), evt.getRotationYawHead(), evt.getRotationPitch(), evt.getOffset());
                            GL11.glPopMatrix();
                        }
                    } catch (Exception ex) {}
                } else {
                    GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                    GL11.glPolygonOffset(1.0F, -1100000.0F);
                }
            } else if (!mode.equals("Colored") && evt.isPost()) {
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPolygonOffset(1.0F, 1100000.0F);
            }
        }
    }
}
