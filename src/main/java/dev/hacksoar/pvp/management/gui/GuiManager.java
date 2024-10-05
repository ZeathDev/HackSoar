package dev.hacksoar.pvp.management.gui;

import dev.hacksoar.pvp.GuiQuickPlay;
import dev.hacksoar.pvp.clickgui.ClickGUI;
import dev.hacksoar.pvp.mainmenu.GuiSoarMainMenu;
import dev.hacksoar.pvp.screenshot.GuiScreenshotViewer;
import dev.hacksoar.ui.clickgui.ClickGui;

public class GuiManager {

	private ClickGUI clickGUI;
	private ClickGui gclickGUI;
	private GuiSoarMainMenu guiMainMenu;
	private GuiQuickPlay guiQuickPlay;
	private GuiScreenshotViewer guiScreenshotViewer;
	
	public GuiManager() {
		clickGUI = new ClickGUI();
		gclickGUI = new ClickGui();
		guiMainMenu = new GuiSoarMainMenu();
		guiQuickPlay = new GuiQuickPlay();
		guiScreenshotViewer = new GuiScreenshotViewer();
	}

	public ClickGUI getClickGUI() {
		return clickGUI;
	}

	public ClickGui getgClickGUI() {
		return gclickGUI;
	}

	public GuiSoarMainMenu getGuiMainMenu() {
		return guiMainMenu;
	}

	public GuiQuickPlay getGuiQuickPlay() {
		return guiQuickPlay;
	}

	public GuiScreenshotViewer getGuiScreenshotViewer() {
		return guiScreenshotViewer;
	}
}
