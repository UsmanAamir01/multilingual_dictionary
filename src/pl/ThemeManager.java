package pl;

import pl.ui.ComponentFactory;
import pl.ui.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced Theme Manager with full dark/light mode support
 */
public class ThemeManager {
    private static ThemeManager instance;
    private static boolean darkMode = false;
    private final static List<ThemeListener> listeners = new ArrayList<>();

    private ThemeManager() {}

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public static void toggleDarkMode() {
        darkMode = !darkMode;
        ComponentFactory.setDarkMode(darkMode);
        updateLookAndFeel();
        notifyListeners();
    }

    public static void setDarkMode(boolean dark) {
        darkMode = dark;
        ComponentFactory.setDarkMode(darkMode);
        updateLookAndFeel();
        notifyListeners();
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    public void addListener(ThemeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ThemeListener listener) {
        listeners.remove(listener);
    }

    private static void notifyListeners() {
        for (ThemeListener listener : listeners) {
            listener.themeChanged(darkMode);
        }
    }

    private static void updateLookAndFeel() {
        // Update UI defaults for consistent theming
        UIManager.put("Panel.background", UIConstants.getBackground(darkMode));
        UIManager.put("Label.foreground", UIConstants.getTextPrimary(darkMode));
        UIManager.put("TextField.background", UIConstants.getSurface(darkMode));
        UIManager.put("TextField.foreground", UIConstants.getTextPrimary(darkMode));
        UIManager.put("TextArea.background", UIConstants.getSurface(darkMode));
        UIManager.put("TextArea.foreground", UIConstants.getTextPrimary(darkMode));
        UIManager.put("ComboBox.background", UIConstants.getSurface(darkMode));
        UIManager.put("ComboBox.foreground", UIConstants.getTextPrimary(darkMode));
        UIManager.put("Table.background", UIConstants.getSurface(darkMode));
        UIManager.put("Table.foreground", UIConstants.getTextPrimary(darkMode));
        UIManager.put("Table.selectionBackground", UIConstants.getPrimary(darkMode));
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("ScrollPane.background", UIConstants.getBackground(darkMode));
        UIManager.put("OptionPane.background", UIConstants.getSurface(darkMode));
        UIManager.put("OptionPane.messageForeground", UIConstants.getTextPrimary(darkMode));
    }

    // Helper methods for getting theme colors
    public static Color getPrimaryColor() {
        return UIConstants.getPrimary(darkMode);
    }

    public static Color getBackgroundColor() {
        return UIConstants.getBackground(darkMode);
    }

    public static Color getSurfaceColor() {
        return UIConstants.getSurface(darkMode);
    }

    public static Color getTextColor() {
        return UIConstants.getTextPrimary(darkMode);
    }

    public static Color getSecondaryTextColor() {
        return UIConstants.getTextSecondary(darkMode);
    }

    public static Color getBorderColor() {
        return UIConstants.getBorder(darkMode);
    }

    public static Color getCardColor() {
        return UIConstants.getCard(darkMode);
    }

    public interface ThemeListener {
        void themeChanged(boolean isDarkMode);
    }
}