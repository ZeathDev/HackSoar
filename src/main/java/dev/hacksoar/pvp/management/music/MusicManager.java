package dev.hacksoar.pvp.management.music;

import dev.hacksoar.HackSoar;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class MusicManager {

	private ArrayList<Music> musics = new ArrayList<Music>();
	
	private int prevMusics;
	
	private double scrollY;
	private Music currentMusic;
	
	public MusicManager() {
		this.loadMusic();
	}
	
	public Music getMusicByName(String name) {
		return musics.stream().filter(music -> music.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	public void loadMusic() {
		
		if(prevMusics != HackSoar.instance.fileManager.getMusicDir().listFiles().length) {
			
			prevMusics = HackSoar.instance.fileManager.getMusicDir().listFiles().length;
			
			musics.clear();
			
			scrollY = 0;

			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File file, String str){
					if (str.endsWith("mp3") || str.endsWith("wav")){
						return true;
					}else{
						return false;
					}
				}
			};
			
			File fileArray[] = HackSoar.instance.fileManager.getMusicDir().listFiles(filter);

			for(File f : fileArray) {
				musics.add(new Music(f.getName().replace(".mp3", "").replace(".wav", ""), f.getName()));
			}
		}
	}

	public ArrayList<Music> getMusics() {
		return musics;
	}

	public double getScrollY() {
		return scrollY;
	}

	public void setScrollY(double scrollY) {
		this.scrollY = scrollY;
	}

	public Music getCurrentMusic() {
		return currentMusic;
	}

	public void setCurrentMusic(Music currentMusic) {
		this.currentMusic = currentMusic;
	}
}
