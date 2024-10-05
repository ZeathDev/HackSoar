package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import net.minecraft.scoreboard.ScoreObjective;

public class EventRenderScoreboard extends Event{
	
	private ScoreObjective objective;
	
	public EventRenderScoreboard(ScoreObjective objective) {
		this.objective = objective;
	}

	public ScoreObjective getObjective() {
		return objective;
	}
}