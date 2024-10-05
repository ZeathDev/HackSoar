package dev.hacksoar.pvp.clickgui.category;

import dev.hacksoar.pvp.clickgui.category.impl.*;

import java.util.ArrayList;

public class CategoryManager {

	private final ArrayList<Category> categories = new ArrayList<Category>();

	public CategoryManager() {
		categories.add(new FeatureCategory());
		categories.add(new ConfigCategory());
		categories.add(new CosmeticCategory());
		categories.add(new EditHUDCategory());
		categories.add(new MusicPlayerCategory());
		categories.add(new SettingsCategory());
	}
	
	public ArrayList<Category> getCategories() {
		return categories;
	}
	
	public Category getCategoryByName(String name) {
		return categories.stream().filter(category -> category.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	public Category getCategoryByClass(Class<?> categoryClass) {
		return categories.stream().filter(category -> category.getClass().equals(categoryClass)).findFirst().orElse(null);
	}
}
