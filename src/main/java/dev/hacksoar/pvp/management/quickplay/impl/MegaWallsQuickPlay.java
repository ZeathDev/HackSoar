package dev.hacksoar.pvp.management.quickplay.impl;

import dev.hacksoar.pvp.management.quickplay.QuickPlay;
import dev.hacksoar.pvp.management.quickplay.QuickPlayCommand;

import java.util.ArrayList;

public class MegaWallsQuickPlay extends QuickPlay{

	public MegaWallsQuickPlay() {
		super("MegaWalls", "soar/mods/quickplay/MegaWalls.png");
	}

	@Override
	public void addCommands() {
		ArrayList<QuickPlayCommand> commands = new ArrayList<QuickPlayCommand>();
		
		commands.add(new QuickPlayCommand("Standard", "/play mw_standard"));
		commands.add(new QuickPlayCommand("Faceoff", "/play mw_face_off"));
		
		this.setCommands(commands);
	}
}
