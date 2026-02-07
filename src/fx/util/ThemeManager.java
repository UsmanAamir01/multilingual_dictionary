package fx.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Theme Manager for JavaFX application
 * Manages dark/light mode switching with CSS
 */
public class ThemeManager {
    
    private static final ThemeManager INSTANCE = new ThemeManager();
    
    private static final String LIGHT_THEME = "/fx/css/theme-light.css";
    private static final String DARK_THEME = "/fx/css/theme-dark.css";
    
    private final BooleanProperty darkMode = new SimpleBooleanProperty(false);
    private final List<Scene> managedScenes = new ArrayList<>();
    
    private ThemeManager() {}
    
    public static ThemeManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Register a scene to be managed by the theme manager
     */
    public void registerScene(Scene scene) {
        if (!managedScenes.contains(scene)) {
            managedScenes.add(scene);
            applyTheme(scene);
        }
    }
    
    /**
     * Unregister a scene
     */
    public void unregisterScene(Scene scene) {
        managedScenes.remove(scene);
    }
    
    /**
     * Toggle between dark and light mode
     */
    public void toggleTheme() {
        setDarkMode(!darkMode.get());
    }
    
    /**
     * Set dark mode state
     */
    public void setDarkMode(boolean isDark) {
        darkMode.set(isDark);
        managedScenes.forEach(this::applyTheme);
    }
    
    /**
     * Check if dark mode is active
     */
    public boolean isDarkMode() {
        return darkMode.get();
    }
    
    /**
     * Get dark mode property for bindings
     */
    public BooleanProperty darkModeProperty() {
        return darkMode;
    }
    
    /**
     * Apply current theme to a scene
     */
    private void applyTheme(Scene scene) {
        scene.getStylesheets().removeIf(s -> s.contains("theme-"));
        
        String themePath = darkMode.get() ? DARK_THEME : LIGHT_THEME;
        String themeUrl = getClass().getResource(themePath).toExternalForm();
        
        // Add theme as first stylesheet (base)
        scene.getStylesheets().add(0, themeUrl);
    }
    
    /**
     * Get the current theme CSS path
     */
    public String getCurrentThemePath() {
        return darkMode.get() ? DARK_THEME : LIGHT_THEME;
    }
}
