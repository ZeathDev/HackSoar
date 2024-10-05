package dev.hacksoar.utils;

import dev.hacksoar.HackSoar;
import dev.hacksoar.pvp.management.mods.impl.HUDMod;

import java.util.Calendar;
import java.util.Date;

public class DayEventUtils {

	private static Calendar getCalendar() {
		
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        
        return calendar;
	}
	
	public static boolean isHalloween() {
		
		if (getCalendar().get(2) + 1 == 10 && (getCalendar().get(5) == 29 || getCalendar().get(5) == 30 || getCalendar().get(5) == 31)) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isChristmas() {
		
		if (getCalendar().get(2) + 1 == 12 && (getCalendar().get(5) == 23 || getCalendar().get(5) == 24 || getCalendar().get(5) == 25)) {
			return true;
		}
		
		return false;
	}
	
	public static void resetHudDesign() {
		
		if(!isHalloween()) {
			if(HackSoar.instance.settingsManager.getSettingByClass(HUDMod.class, "Design").getValString().equals("Halloween")) {
				HackSoar.instance.settingsManager.getSettingByClass(HUDMod.class, "Design").setValString("Color");
			}
		}
		
		if(!isChristmas()) {
			if(HackSoar.instance.settingsManager.getSettingByClass(HUDMod.class, "Design").getValString().equals("Christmas")) {
				HackSoar.instance.settingsManager.getSettingByClass(HUDMod.class, "Design").setValString("Color");
			}
		}
	}
}
