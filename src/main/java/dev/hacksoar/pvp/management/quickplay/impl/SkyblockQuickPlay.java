package dev.hacksoar.pvp.management.quickplay.impl;

import dev.hacksoar.pvp.management.quickplay.QuickPlay;
import dev.hacksoar.pvp.management.quickplay.QuickPlayCommand;

import java.util.ArrayList;

public class SkyblockQuickPlay extends QuickPlay{

	public SkyblockQuickPlay() {
		super("Skyblock", "soar/mods/quickplay/Skyblock.png");
	}
	
	@Override
	public void addCommands() {
		ArrayList<QuickPlayCommand> commands = new ArrayList<QuickPlayCommand>();
		
		commands.add(new QuickPlayCommand("Skyblock", "/skyblock"));
		
		this.setCommands(commands);
	}

}
