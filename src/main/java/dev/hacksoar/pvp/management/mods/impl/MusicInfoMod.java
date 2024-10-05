package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.pvp.management.music.Music;
import dev.hacksoar.utils.TimerUtils;
import dev.hacksoar.utils.animation.simple.SimpleAnimation;
import dev.hacksoar.utils.font.FontUtils;
import dev.hacksoar.utils.render.RenderUtils;
import dev.hacksoar.utils.render.RoundedUtils;
import dev.hacksoar.utils.render.StencilUtils;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class MusicInfoMod extends Mod {

	public static MusicInfoMod instance = new MusicInfoMod();
	
	public float addX = 0;
	private TimerUtils timer = new TimerUtils();
	private TimerUtils timer2 = new TimerUtils();
	private boolean back = false;
	
	public static float[] visualizer = {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0
	};
	
	public static SimpleAnimation[] animation = {
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F),
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F),
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F),
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F),
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F),
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F),
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F),
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F),
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F),
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F),
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F),
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F),
			new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F), new SimpleAnimation(0.0F)
	};
	
	public MusicInfoMod() {
		super("Music Info", "Display current music infomation", ModCategory.HUD);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {

		float fontWidth = 0;
		
		//�w�i�̕`��
		this.drawBackground(this.getX(), this.getY(), 155, 100);
		
		//�����̉��̔����d�؂�̕`��
		RoundedUtils.drawRound(this.getX(), this.getY() + 20, 155, 1, 0, this.getFontColor());
		
		//�����g�`�̕`��
		for(int i = 0; i < 29; i++) {
			animation[i].setAnimation(MusicInfoMod.visualizer[i], 100);
			RoundedUtils.drawRound(this.getX() + 6 + (i * 5), this.getY() + animation[i].getValue() + 86, 2.5F, -animation[i].getValue(), 1.2F, this.getFontColor());
			RoundedUtils.drawRound(this.getX() + 6 + (i * 5), this.getY() + animation[i].getValue() + 80, 3, 3, 1, this.getFontColor());
		}
		
		if(HackSoar.instance.musicManager.getCurrentMusic() != null && HackSoar.instance.musicManager.getCurrentMusic().mediaPlayer != null) {
			Music music = HackSoar.instance.musicManager.getCurrentMusic();
			MediaPlayer mediaPlayer = music.mediaPlayer;

			String musicName = "Playing: " + music.getName();
			fontWidth =  (float) FontUtils.regular_bold26.getStringWidth(musicName);
					
			//�Ȗ�����яo�Ȃ��悤�ɔ͈͎w��
            StencilUtils.initStencilToWrite();
            RenderUtils.drawRect(this.getX(), this.getY(), this.getWidth() - this.getX(), this.getHeight() - this.getY(), -1);
            StencilUtils.readStencilBuffer(1);
			
			FontUtils.regular_bold26.drawString(musicName, this.getX() + 5 + addX, this.getY() + 5, this.getFontColor().getRGB());

            StencilUtils.uninitStencilBuffer();

			if(fontWidth > this.getWidth() - this.getX()) {
				if(timer.delay(30)) {
					if(((this.getWidth() - this.getX()) - fontWidth) - 10 < addX && !back) {
						addX = addX - 1;
					}else if(back && addX != 0) {
						addX = addX + 1;
					}
					timer.reset();
				}
				
				if(addX <= ((this.getWidth() - this.getX()) - fontWidth) - 10) {
					if(timer2.delay(3000)) {
						back = true;
					}
				}else {
					if(!back) {
						timer2.reset();
					}
				}
				
				if(back){
					if(addX == 0) {
						if(timer2.delay(3000)) {
							back = false;
						}
					}else {
						if(back) {
							timer2.reset();
						}
					}
				}
				
			}else {
				addX = 0;
				back = false;
			}
			
			if(mediaPlayer.getStatus().equals(Status.PAUSED) || mediaPlayer.getStatus().equals(Status.STOPPED)) {
				//�A�j���[�V���������Z�b�g
				for(int i = 0; i < 29; i++) {
					MusicInfoMod.visualizer[i] = 0.0F;
				}
			}
			
			float current = (float) mediaPlayer.getCurrentTime().toSeconds();
			float end = (float) music.getMedia().getDuration().toSeconds();
			
			//�Đ���Ԃ̕`��
			RoundedUtils.drawRound(this.getX() + 5, this.getY() + 92,  (current / end) * 145 - 4, 1.5F, 1.3F, this.getFontColor());
			
			//��
			RoundedUtils.drawRound(this.getX() + 5 + ((current / end) * 145) - 2, this.getY() + 91.2F, 3, 3F, 1, this.getFontColor());
			
			RoundedUtils.drawRound(this.getX() + 5 + ((current / end) * 145) + 3, this.getY() + 92, 145 - ((current / end) * 150), 1.5F, 1.3F, this.getFontColor());
			
		}else {
			RoundedUtils.drawRound(this.getX() + 5, this.getY() + 92,  145, 1.5F, 1.3F, this.getFontColor());
			
			FontUtils.regular_bold26.drawString("Nothing is playing.", this.getX() + 5, this.getY() + 5, this.getFontColor().getRGB());
		}
		
		this.setWidth(155);
		this.setHeight(100);
	}
	
	@EventTarget
	public void onRenderShadow(EventRenderShadow event) {
		this.drawShadow(this.getX(), this.getY(), this.getWidth() - this.getX(), this.getHeight() - this.getY());
	}
}
