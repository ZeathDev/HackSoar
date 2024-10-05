package dev.hacksoar.pvp.management.quickplay.impl;

import dev.hacksoar.pvp.management.quickplay.QuickPlay;
import dev.hacksoar.pvp.management.quickplay.QuickPlayCommand;

import java.util.ArrayList;

public class SkywarsQuickPlay extends QuickPlay{

	public SkywarsQuickPlay() {
		super("Skywars", "soar/mods/quickplay/Skywars.png");
	}

	@Override
	public void addCommands() {
		ArrayList<QuickPlayCommand> commands = new ArrayList<QuickPlayCommand>();
		
		commands.add(new QuickPlayCommand("Lobby", "/l s"));
		commands.add(new QuickPlayCommand("Solo Normal", "/play solo_normal"));
		commands.add(new QuickPlayCommand("Solo Insane", "/play solo_insane"));
		commands.add(new QuickPlayCommand("Teams Normal", "/play teams_normal"));
		commands.add(new QuickPlayCommand("Teams Insane", "/play teams_insane"));
		commands.add(new QuickPlayCommand("Mega", "/play mega_normal"));
		
		this.setCommands(commands);
	}
}
