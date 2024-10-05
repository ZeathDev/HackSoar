package dev.hacksoar.pvp.management.quickplay;

import dev.hacksoar.utils.animation.simple.SimpleAnimation;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class QuickPlay {

	private String name;
	
	private ResourceLocation icon;
	
	private ArrayList<QuickPlayCommand> commands = new ArrayList<QuickPlayCommand>();
	
	public SimpleAnimation animation = new SimpleAnimation(0.0F);
	
	public QuickPlay(String name, String iconLocation) {
		this.name = name;
		this.icon = new ResourceLocation(iconLocation);
		this.addCommands();
	}
	
	public void addCommands() {}

	public ResourceLocation getIcon() {
		return icon;
	}

	public void setIcon(ResourceLocation icon) {
		this.icon = icon;
	}

	public ArrayList<QuickPlayCommand> getCommands() {
		return commands;
	}

	public void setCommands(ArrayList<QuickPlayCommand> commands) {
		this.commands = commands;
	}

	public String getName() {
		return name;
	}
}