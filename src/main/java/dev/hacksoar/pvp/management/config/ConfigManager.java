package dev.hacksoar.pvp.management.config;

import dev.hacksoar.HackSoar;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.settings.Setting;

import java.io.*;
import java.util.ArrayList;

public class ConfigManager {

	private ArrayList<File> configs = new ArrayList<File>();
	
	private int prevConfigs;
	private double scrollY;
		
	public ConfigManager() {
		this.loadConfigs();
		this.load();
	}
	
	public void loadConfigs() {
		if(prevConfigs != HackSoar.instance.fileManager.getConfigDir().listFiles().length) {
			
			prevConfigs = HackSoar.instance.fileManager.getConfigDir().listFiles().length;
			
			configs.clear();
			
			scrollY = 0;
			
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File file, String str){
					if (str.endsWith("txt")){
						return true;
					}else{
						return false;
					}
				}
			};
			
			File fileArray[] = HackSoar.instance.fileManager.getConfigDir().listFiles(filter);
			
			for(File f : fileArray) {
				configs.add(f);
			}
		}
	}
	
	public void save(File file) {
		
		ArrayList<String> toSave = new ArrayList<String>();
		
		for (Mod mod : HackSoar.instance.modManager.getMods()) {
			if(!mod.isHide()) {
				toSave.add("MOD:" + mod.getName() + ":" + mod.isToggled());
				toSave.add("POS:" + mod.getName() + ":" + mod.getX() + ":" + mod.getY() + ":" + mod.getWidth() + ":" + mod.getHeight());
			}
		}
		
		for (Setting set : HackSoar.instance.settingsManager.getSettings()) {
			if (set.isCheck()) {
				toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getName() + ":" + set.getValBoolean());
			}
			if (set.isCombo()) {
				toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getName() + ":" + set.getValString());
			}
			if (set.isSlider()) {
				toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getName() + ":" + set.getValDouble());
			}
		}
		
		toSave.add("AccentColor:" + HackSoar.instance.colorManager.getClientColor().getName());
		toSave.add("Color:" + HackSoar.instance.colorManager.getAccentColorByName(HackSoar.instance.colorManager.getClientColor().getName()).getColor1().getRed() + ":" + HackSoar.instance.colorManager.getAccentColorByName(HackSoar.instance.colorManager.getClientColor().getName()).getColor1().getGreen() + ":" + HackSoar.instance.colorManager.getAccentColorByName(HackSoar.instance.colorManager.getClientColor().getName()).getColor1().getBlue());
		toSave.add("Color2:" + HackSoar.instance.colorManager.getAccentColorByName(HackSoar.instance.colorManager.getClientColor().getName()).getColor2().getRed() + ":" + HackSoar.instance.colorManager.getAccentColorByName(HackSoar.instance.colorManager.getClientColor().getName()).getColor2().getGreen() + ":" + HackSoar.instance.colorManager.getAccentColorByName(HackSoar.instance.colorManager.getClientColor().getName()).getColor2().getBlue());
		
		toSave.add("ClickPOS:" + HackSoar.instance.guiManager.getClickGUI().getX() + ":" + HackSoar.instance.guiManager.getClickGUI().getY() + ":" + HackSoar.instance.guiManager.getClickGUI().getWidth() + ":" + HackSoar.instance.guiManager.getClickGUI().getHeight());
		
		try {
			PrintWriter pw = new PrintWriter(file);
			for (String str : toSave) {
				pw.println(str);
			}
			pw.close();
		} catch (FileNotFoundException e) {
			// Don't give crackers clues...
			if (HackSoar.instance.DEVELOPMENT_SWITCH)
				e.printStackTrace();
		}
		
	}
	
	public void load(File file) {
		
		ArrayList<String> lines = new ArrayList<String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			// Don't give crackers clues...
			if (HackSoar.instance.DEVELOPMENT_SWITCH)
				e.printStackTrace();
		}
		
		for (String s : lines) {
			
			String[] args = s.split(":");
			
			if (s.toLowerCase().startsWith("mod:")) {
				Mod m = HackSoar.instance.modManager.getModByName(args[1]);
				if (m != null) {
					m.setToggled(Boolean.parseBoolean(args[2]));
				}
			} else if (s.toLowerCase().startsWith("set:")) {
				Mod m = HackSoar.instance.modManager.getModByName(args[2]);
				if (m != null) {
					Setting set = HackSoar.instance.settingsManager.getSettingByName(m, args[1]);
					if (set != null) {
						if (set.isCheck()) {
							set.setValBoolean(Boolean.parseBoolean(args[3]));
						}
						if (set.isCombo()) {
							set.setValString(args[3]);
						}
						if (set.isSlider()) {
							set.setValDouble(Double.parseDouble(args[3]));
						}
					}
				}
			}
			
			if (s.toLowerCase().startsWith("accentcolor:")) {
				HackSoar.instance.colorManager.setClientColor(HackSoar.instance.colorManager.getAccentColorByName(args[1]));
			}
			
			if (s.toLowerCase().startsWith("pos:")) {
				Mod m = HackSoar.instance.modManager.getModByName(args[1]);
				if (m != null) {
					m.setX(Integer.parseInt(args[2]));
					m.setY(Integer.parseInt(args[3]));
					m.setWidth(Integer.parseInt(args[4]));
					m.setHeight(Integer.parseInt(args[5]));
				}
			}
			
			if (s.toLowerCase().startsWith("clickpos:")) {
				HackSoar.instance.guiManager.getClickGUI().setX(Float.parseFloat(args[1]));
				HackSoar.instance.guiManager.getClickGUI().setY(Float.parseFloat(args[2]));
				HackSoar.instance.guiManager.getClickGUI().setWidth(Float.parseFloat(args[3]));
				HackSoar.instance.guiManager.getClickGUI().setHeight(Float.parseFloat(args[4]));
			}
		}
	}
	
	public void save() {
		this.save(HackSoar.instance.fileManager.getConfigFile());
	}
	
	public void load() {
		this.load(HackSoar.instance.fileManager.getConfigFile());
	}
	
	public ArrayList<File> getConfigs() {
		return configs;
	}

	public double getScrollY() {
		return scrollY;
	}

	public void setScrollY(double scrollY) {
		this.scrollY = scrollY;
	}
}
