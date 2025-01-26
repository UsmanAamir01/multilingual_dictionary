package pl;

import java.util.ArrayList;
import java.util.List;

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

    public interface ThemeListener {
        void themeChanged(boolean isDarkMode);
    }
}