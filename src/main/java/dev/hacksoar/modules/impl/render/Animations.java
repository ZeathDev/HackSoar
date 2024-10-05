package dev.hacksoar.modules.impl.render;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventPreMotion;
import dev.hacksoar.api.events.impl.EventRenderItem;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.api.value.impl.FloatValue;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class Animations extends Module {
    public Animations() {
        super("Animation","Blocking anima", ModuleCategory.Render);
    }

    private final ListValue blockAnimation = new ListValue("Block Animation",new String[]{"None", "1.7", "Smooth", "Spin", "Leaked", "Old", "Exhibition", "Wood", "Swong", "Chill", "Komorebi", "Rhys", "Allah", "?", "Stab",},"1.7");
    private final ListValue swingAnimation = new ListValue("Swing Animation",new String[]{"None","Punch","Shove","Smooth","1.9+"},"None");
    // private final IntValue swingSpeed = new IntValue("Swing Speed",1, -200, 50);
    private final FloatValue x = new FloatValue("X",0.0f,-2.0f,2f);
    private final FloatValue y = new FloatValue("Y",0.0f,-2.0f,2f);
    private final FloatValue z = new FloatValue("Z",0.0f,-2.0f,2f);
    private final BoolValue alwaysShow = new BoolValue("Always Show",false);

    private int attacks, swing;
    private Entity target;

    @Override
    public void onInitialize() {
        this.setToggled(true);
    }

    @Override
    public void onDisable() {
        // this.setToggled(true);
    }

    @EventTarget
    public void onRenderItem(EventRenderItem event) {
        if (event.getItemToRender().getItem() instanceof ItemMap) {
            return;
        }

        final EnumAction itemAction = event.getEnumAction();
        final ItemRenderer itemRenderer = mc.getItemRenderer();
        final float animationProgression = alwaysShow.get() && event.isUseItem() ? 0.0F : event.getAnimationProgression();
        final float swingProgress = event.getSwingProgress();
        final float convertedProgress = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);

        if (animationProgression > 0) {
            this.attacks = 0;
        }
        if (event.isUseItem() && itemAction == EnumAction.BLOCK) {
            GlStateManager.translate(x.get(), y.get(), z.get());
            switch (blockAnimation.get()) {
                case "None": {
                    itemRenderer.transformFirstPersonItem(animationProgression, 0.0F);
                    itemRenderer.blockTransformation();

                    break;
                }

                case "1.7": {
                    itemRenderer.transformFirstPersonItem(animationProgression, swingProgress);
                    itemRenderer.blockTransformation();

                    break;
                }

                case "Smooth": {
                    itemRenderer.transformFirstPersonItem(animationProgression, 0.0F);
                    final float y = -convertedProgress * 2.0F;
                    GlStateManager.translate(0.0F, y / 10.0F + 0.1F, 0.0F);
                    GlStateManager.rotate(y * 10.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(250, 0.2F, 1.0F, -0.6F);
                    GlStateManager.rotate(-10.0F, 1.0F, 0.5F, 1.0F);
                    GlStateManager.rotate(-y * 20.0F, 1.0F, 0.5F, 1.0F);

                    break;
                }

                case "Stab": {
                    final float spin = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);

                    GlStateManager.translate(0.6f, 0.3f, -0.6f + -spin * 0.7);
                    GlStateManager.rotate(6090, 0.0f, 0.0f, 0.1f);
                    GlStateManager.rotate(6085, 0.0f, 0.1f, 0.0f);
                    GlStateManager.rotate(6110, 0.1f, 0.0f, 0.0f);
                    itemRenderer.transformFirstPersonItem(0.0F, 0.0f);
                    itemRenderer.blockTransformation();
                    break;
                }

                case "Spin": {
                    itemRenderer.transformFirstPersonItem(animationProgression, 0.0F);
                    GlStateManager.translate(0, 0.2F, -1);
                    GlStateManager.rotate(-59, -1, 0, 3);
                    // Don't make the /2 a float it causes the animation to break
                    GlStateManager.rotate(-((float) System.currentTimeMillis() / 2 % 360), 1, 0, 0.0F);
                    GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
                    break;
                }

                case "Leaked": {
                    itemRenderer.transformFirstPersonItem(animationProgression, 0.0F);
                    GlStateManager.translate(0.0f, 0.1F, 0.0F);
                    itemRenderer.blockTransformation();
                    GlStateManager.rotate(convertedProgress * 35.0F / 2.0F, 0.0F, 1.0F, 1.5F);
                    GlStateManager.rotate(-convertedProgress * 135.0F / 4.0F, 1.0f, 1.0F, 0.0F);

                    break;
                }

                case "Old": {
                    GlStateManager.translate(0.0F, 0.18F, 0.0F);
                    itemRenderer.transformFirstPersonItem(animationProgression / 2.0F, swingProgress);
                    itemRenderer.blockTransformation();

                    break;
                }

                case "Exhibition": {
                    itemRenderer.transformFirstPersonItem(animationProgression / 2.0F, 0.0F);
                    GlStateManager.translate(0.0F, 0.3F, -0.0F);
                    GlStateManager.rotate(-convertedProgress * 31.0F, 1.0F, 0.0F, 2.0F);
                    GlStateManager.rotate(-convertedProgress * 33.0F, 1.5F, (convertedProgress / 1.1F), 0.0F);
                    itemRenderer.blockTransformation();

                    break;
                }

                case "Wood": {
                    itemRenderer.transformFirstPersonItem(animationProgression / 2.0F, 0.0F);
                    GlStateManager.translate(0.0F, 0.3F, -0.0F);
                    GlStateManager.rotate(-convertedProgress * 30.0F, 1.0F, 0.0F, 2.0F);
                    GlStateManager.rotate(-convertedProgress * 44.0F, 1.5F, (convertedProgress / 1.2F), 0.0F);
                    itemRenderer.blockTransformation();

                    break;
                }

                case "Swong": {
                    itemRenderer.transformFirstPersonItem(animationProgression / 2.0F, swingProgress);
                    GlStateManager.rotate(convertedProgress * 30.0F / 2.0F, -convertedProgress, -0.0F, 9.0F);
                    GlStateManager.rotate(convertedProgress * 40.0F, 1.0F, -convertedProgress / 2.0F, -0.0F);
                    GlStateManager.translate(0.0F, 0.2F, 0.0F);
                    itemRenderer.blockTransformation();

                    break;
                }

                case "Chill": {
                    itemRenderer.transformFirstPersonItem(animationProgression / 1.5F, 0.0F);
                    itemRenderer.blockTransformation();
                    GlStateManager.translate(-0.05F, 0.3F, 0.3F);
                    GlStateManager.rotate(-convertedProgress * 140.0F, 8.0F, 0.0F, 8.0F);
                    GlStateManager.rotate(convertedProgress * 90.0F, 8.0F, 0.0F, 8.0F);

                    break;
                }

                case "Komorebi": {
                    itemRenderer.transformFirstPersonItem(-0.25F, 1.0F + convertedProgress / 10.0F);
                    GL11.glRotated(-convertedProgress * 25.0F, 1.0F, 0.0F, 0.0F);
                    itemRenderer.blockTransformation();

                    break;
                }

                case "Rhys": {
                    GlStateManager.translate(0.41F, -0.25F, -0.5555557F);
                    GlStateManager.translate(0.0F, 0, 0.0F);
                    GlStateManager.rotate(35.0F, 0f, 1.5F, 0.0F);

                    final float racism = MathHelper.sin(swingProgress * swingProgress / 64 * (float) Math.PI);

                    GlStateManager.rotate(racism * -5.0F, 0.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(convertedProgress * -12.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(convertedProgress * -65.0F, 1.0F, 0.0F, 0.0F);
                    itemRenderer.blockTransformation();

                    break;
                }

                case "Allah": {
                    itemRenderer.transformFirstPersonItem(animationProgression, swingProgress);
                    itemRenderer.blockTransformation();
                    GlStateManager.translate(-0.3F, -0.1F, -0.0F);

                    break;
                }

                case "?": {
                    itemRenderer.transformFirstPersonItem(animationProgression, swingProgress);

                    GL11.glTranslatef(-0.35F, 0.1F, 0.0F);
                    GL11.glTranslatef(-0.05F, -0.1F, 0.1F);

                    itemRenderer.blockTransformation();

                    break;
                }
            }

            event.setCancelled(true);

        } else if (!event.isUseItem() && (event.getItemToRender().getItem() instanceof ItemSword || event.getItemToRender().getItem() instanceof ItemAxe)) {

            switch (swingAnimation.get()) {
                case "None":
                    itemRenderer.func_178105_d(swingProgress);
                    itemRenderer.transformFirstPersonItem(animationProgression, swingProgress);
                    break;

                case "Punch": {
                    itemRenderer.transformFirstPersonItem(animationProgression, swingProgress);
                    itemRenderer.func_178105_d(swingProgress);
                    break;
                }

                case "1.9+": {
                    itemRenderer.func_178105_d(swingProgress);
                    itemRenderer.transformFirstPersonItem(animationProgression, swingProgress);
                    GlStateManager.translate(0, -((swing - 1) -
                            (swing == 0 ? 0 : mc.timer.renderPartialTicks)) / 5f, 0);
                    break;
                }

                case "Shove": {
                    itemRenderer.transformFirstPersonItem(animationProgression, animationProgression);
                    itemRenderer.func_178105_d(swingProgress);
                    break;
                }

                case "Smooth": {
                    itemRenderer.transformFirstPersonItem(animationProgression, swingProgress);
                    itemRenderer.func_178105_d(animationProgression);
                    break;
                }
            }

            event.setCancelled(true);
        }
    }

/*    @EventTarget
    public void onAttack(EventAttackEntity event) {
        this.attacks++;
        target = event.getEntity();

        if (swingAnimation.get().equals("1.9+")) {
            final ItemStack currentItem = mc.thePlayer.getCurrentEquippedItem();

            if (currentItem != null && target != null) {
                currentItem.setRepairCost((int) (Math.random() * 100));

                PacketUtil.receiveNoEvent(new S2FPacketSetSlot(0, 36 + mc.thePlayer.inventory.currentItem, currentItem));
            }
        }
    }*/

    @EventTarget
    public void onPreMotionEvent(EventPreMotion event) {

        final ItemStack currentItem = mc.thePlayer.getCurrentEquippedItem();

/*        if (currentItem != null && target != null && target.hurtResistantTime >= 16 && attacks > 1) {
//            mc.entityRenderer.itemRenderer.resetEquippedProgress2();
            currentItem.setRepairCost((int) (Math.random() * 100));

            PacketUtil.receiveNoEvent(new S2FPacketSetSlot(0, 36 + mc.thePlayer.inventory.currentItem, currentItem));

            attacks = 0;
        }*/

        if (mc.thePlayer.swingProgressInt == 1) {
            swing = 9;
        } else {
            swing = Math.max(0, swing - 1);
        }
    }
}
