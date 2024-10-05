package dev.hacksoar.utils.color.impl;

import java.awt.*;

public class ChristmasColorUtils {

	public Color getGreen(int opacity) {
		return new Color(41, 165, 131, opacity);
	}
	
	public Color getGreen() {
		return getGreen(255);
	}
	
	public Color getRed(int opacity) {
		return new Color(252, 25, 52, opacity);
	}
	
	public Color getRed() {
		return getRed(255);
	}
	
	public Color getDarkRed(int opacity) {
		return new Color(201, 4, 11, opacity);
	}
	
	public Color getDarkRed() {
		return getDarkRed(255);
	}
}
