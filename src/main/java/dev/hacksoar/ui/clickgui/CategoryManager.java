package dev.hacksoar.ui.clickgui;

import dev.hacksoar.ui.clickgui.impl.ConfigCategory;
import dev.hacksoar.ui.clickgui.impl.FeatureCategory;
import dev.hacksoar.ui.clickgui.impl.features.CombatModules;
import dev.hacksoar.ui.clickgui.impl.features.MovementModules;
import dev.hacksoar.ui.clickgui.impl.features.RenderModules;
import dev.hacksoar.ui.clickgui.impl.features.UtiltyModules;

import java.util.ArrayList;

public class CategoryManager {
    private ArrayList<Category> categories = new ArrayList<Category>();

    public CategoryManager() {
        categories.add(new FeatureCategory());
        categories.add(new CombatModules());
        categories.add(new MovementModules());
        categories.add(new RenderModules());
        categories.add(new UtiltyModules());
        categories.add(new ConfigCategory());
//        categories.add(new EditHUDCategory());
//        categories.add(new ConfigCategory());
//        categories.add(new CosmeticCategory());
//        categories.add(new EditHUDCategory());
//        categories.add(new MusicPlayerCategory());
//        categories.add(new SettingsCategory());
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

    public boolean isModule(Category category,CategoryManager categoryManager) {
        return category instanceof FeatureCategory;
    }
}
