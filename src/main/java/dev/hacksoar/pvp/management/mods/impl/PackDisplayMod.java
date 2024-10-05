package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.api.events.impl.EventSwitchTexture;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.utils.font.FontUtils;
import dev.hacksoar.utils.render.RoundedUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class PackDisplayMod extends Mod {

	private IResourcePack pack;
	private ResourceLocation currentPack;
	private final ResourcePackRepository resourcePackRepository = mc.getResourcePackRepository();
	private List<ResourcePackRepository.Entry> packs = resourcePackRepository.getRepositoryEntries();
	
	public PackDisplayMod() {
		super("Pack Display", "Display the Pack you are using", ModCategory.HUD);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.loadTexture();
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		GlStateManager.pushMatrix();
		
		if(pack == null) {
			pack = this.getCurrentPack();
		}
		
		this.drawBackground(this.getX(), this.getY(), (float) (46 + (FontUtils.regular_bold22.getStringWidth(this.convertNormalText(pack.getPackName())))), 38);
		mc.getTextureManager().bindTexture(this.currentPack);
		RoundedUtils.drawRoundTextured(this.getX() + 4.5F, this.getY() + 4.5F, 29, 29, 4, 1.0F);
		
		FontUtils.regular_bold22.drawString(this.convertNormalText(pack.getPackName()), this.getX() + 40, this.getY() + (29 / 2), this.getFontColor().getRGB());
		
		GlStateManager.popMatrix();
		
		this.setWidth((int) (46 + FontUtils.regular_bold22.getStringWidth(this.convertNormalText(pack.getPackName()))));
		this.setHeight(38);
	}
	
	@EventTarget
	public void onRenderShadow(EventRenderShadow event) {
		if(pack != null) {
			this.drawShadow(this.getX(), this.getY(), (float) (46 + (FontUtils.regular_bold22.getStringWidth(this.convertNormalText(pack.getPackName())))), 38);
		}
	}
	
	@EventTarget
	public void onSwitchTexture(EventSwitchTexture event) {
		packs = resourcePackRepository.getRepositoryEntries();
		pack = this.getCurrentPack();
		this.loadTexture();
	}
	
	private String convertNormalText(String text) {
		return text.replaceAll("\\u00a7" + "1", "").replaceAll("\\u00a7" + "2", "").replaceAll("\\u00a7" + "3", "")
				.replaceAll("\\u00a7" + "4", "").replaceAll("\\u00a7" + "5", "").replaceAll("\\u00a7" + "6", "")
				.replaceAll("\\u00a7" + "7", "").replaceAll("\\u00a7" + "8", "").replaceAll("\\u00a7" + "9", "")
				.replaceAll("\\u00a7" + "a", "").replaceAll("\\u00a7" + "b", "").replaceAll("\\u00a7" + "c", "")
				.replaceAll("\\u00a7" + "d", "").replaceAll("\\u00a7" + "e", "").replaceAll("\\u00a7" + "f", "")
				.replaceAll("\\u00a7" + "g", "").replaceAll("\\u00a7" + "k", "").replaceAll("\\u00a7" + "l", "")
				.replaceAll("\\u00a7" + "m", "").replaceAll("\\u00a7" + "n", "").replaceAll("\\u00a7" + "o", "")
				.replaceAll("\\u00a7" + "r", "").replace(".zip", "");
	}
	
	private void loadTexture() {
		DynamicTexture dynamicTexture;
		try {
			dynamicTexture = new DynamicTexture(getCurrentPack().getPackImage());
		} catch (Exception e) {
			dynamicTexture = TextureUtil.missingTexture;
		}
		this.currentPack = mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamicTexture);
	}
	
	private IResourcePack getCurrentPack() {
		if (packs.size() > 0) {
			final IResourcePack last = packs.get(packs.size() - 1).getResourcePack();
			return last;
		}
		return (mc).getMcDefaultResourcePack();
	}
}
