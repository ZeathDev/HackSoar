package dev.hacksoar.pvp.management.quickplay;

import dev.hacksoar.pvp.management.quickplay.impl.*;

import java.util.ArrayList;

public class QuickPlayManager {

	private ArrayList<QuickPlay> quickPlays = new ArrayList<QuickPlay>();

	public QuickPlayManager() {
		quickPlays.add(new ArcadeQuickPlay());
		quickPlays.add(new BedwarsQuickPlay());
		quickPlays.add(new BlitzSGQuickPlay());
		quickPlays.add(new BuildBattleQuickPlay());
		quickPlays.add(new DuelsQuickPlay());
		quickPlays.add(new MainLobbyQuickPlay());
		quickPlays.add(new MegaWallsQuickPlay());
		quickPlays.add(new MurderMysteryQuickPlay());
		quickPlays.add(new SkyblockQuickPlay());
		quickPlays.add(new SkywarsQuickPlay());
		quickPlays.add(new TNTQuickPlay());
		quickPlays.add(new UHCQuickPlay());
	}

	public ArrayList<QuickPlay> getQuickPlays() {
		return quickPlays;
	}
}