package pl.ui;

import java.awt.*;

/**
 * Premium Design System - Centralized UI Constants
 * A refined, elegant design system for a professional dictionary application
 */
public class UIConstants {
    
    // ==================== COLOR PALETTE ====================
    
    // Light Mode Colors - Darker, premium aesthetic
    public static final Color LIGHT_PRIMARY = new Color(30, 64, 175);         // #1E40AF - Deep royal blue
    public static final Color LIGHT_PRIMARY_DARK = new Color(23, 37, 84);     // #172554 - Navy blue
    public static final Color LIGHT_PRIMARY_LIGHT = new Color(59, 130, 246);  // #3B82F6 - Bright blue
    public static final Color LIGHT_SECONDARY = new Color(13, 148, 136);      // #0D9488 - Deep teal
    public static final Color LIGHT_ACCENT = new Color(217, 119, 6);          // #D97706 - Deep amber
    public static final Color LIGHT_BACKGROUND = new Color(203, 213, 225);    // #CBD5E1 - Darker slate
    public static final Color LIGHT_SURFACE = new Color(226, 232, 240);       // #E2E8F0 - Slate surface
    public static final Color LIGHT_CARD = new Color(241, 245, 249);          // #F1F5F9 - Light card
    public static final Color LIGHT_TEXT_PRIMARY = new Color(15, 23, 42);     // #0F172A - Rich slate
    public static final Color LIGHT_TEXT_SECONDARY = new Color(71, 85, 105);  // #475569 - Darker muted
    public static final Color LIGHT_BORDER = new Color(148, 163, 184);        // #94A3B8 - Visible border
    public static final Color LIGHT_DIVIDER = new Color(203, 213, 225);       // #CBD5E1 - Visible divider
    public static final Color LIGHT_HOVER = new Color(226, 232, 240);         // #E2E8F0 - Deeper hover
    
    // Dark Mode Colors - Ultra-deep and sophisticated
    public static final Color DARK_PRIMARY = new Color(99, 179, 237);         // #63B3ED - Vibrant sky blue
    public static final Color DARK_PRIMARY_DARK = new Color(66, 153, 225);    // #4299E1 - Rich blue
    public static final Color DARK_SECONDARY = new Color(56, 178, 172);       // #38B2AC - Vibrant teal
    public static final Color DARK_ACCENT = new Color(246, 173, 85);          // #F6AD55 - Warm orange
    public static final Color DARK_BACKGROUND = new Color(10, 15, 28);        // #0A0F1C - Deep navy
    public static final Color DARK_SURFACE = new Color(20, 28, 45);           // #141C2D - Rich surface
    public static final Color DARK_CARD = new Color(30, 41, 59);              // #1E293B - Card slate
    public static final Color DARK_TEXT_PRIMARY = new Color(237, 242, 247);   // #EDF2F7 - Crisp white
    public static final Color DARK_TEXT_SECONDARY = new Color(160, 174, 192); // #A0AEC0 - Soft muted
    public static final Color DARK_BORDER = new Color(45, 55, 72);            // #2D3748 - Subtle border
    public static final Color DARK_DIVIDER = new Color(35, 45, 62);           // #232D3E - Deep divider
    public static final Color DARK_HOVER = new Color(45, 55, 72);             // #2D3748 - Hover state
    
    // Semantic Colors - Refined and accessible
    public static final Color SUCCESS = new Color(34, 197, 94);               // #22C55E - Fresh green
    public static final Color SUCCESS_LIGHT = new Color(74, 222, 128);        // #4ADE80 - Light green
    public static final Color SUCCESS_DARK = new Color(22, 163, 74);          // #16A34A - Deep green
    public static final Color WARNING = new Color(245, 158, 11);              // #F59E0B - Warm amber
    public static final Color WARNING_LIGHT = new Color(251, 191, 36);        // #FBBF24 - Light amber
    public static final Color ERROR = new Color(239, 68, 68);                 // #EF4444 - Soft red
    public static final Color ERROR_LIGHT = new Color(248, 113, 113);         // #F87171 - Light red
    public static final Color INFO = new Color(59, 130, 246);                 // #3B82F6 - Blue
    
    // Sidebar Colors - Premium dark design
    public static final Color SIDEBAR_LIGHT = new Color(226, 232, 240);       // #E2E8F0 - Slate sidebar
    public static final Color SIDEBAR_DARK = new Color(15, 23, 42);           // #0F172A - Deep sidebar
    public static final Color SIDEBAR_ITEM_HOVER_LIGHT = new Color(203, 213, 225);  // #CBD5E1 - Visible hover
    public static final Color SIDEBAR_ITEM_HOVER_DARK = new Color(30, 41, 59);      // #1E293B
    public static final Color SIDEBAR_ITEM_ACTIVE_LIGHT = new Color(30, 64, 175);   // #1E40AF - Deep blue
    public static final Color SIDEBAR_ITEM_ACTIVE_DARK = new Color(99, 179, 237);   // #63B3ED - Bright blue
    
    // Premium Gradient Colors
    public static final Color GRADIENT_BLUE_START = new Color(37, 99, 235);   // #2563EB
    public static final Color GRADIENT_BLUE_END = new Color(59, 130, 246);    // #3B82F6
    public static final Color GRADIENT_TEAL_START = new Color(20, 184, 166);  // #14B8A6
    public static final Color GRADIENT_TEAL_END = new Color(45, 212, 191);    // #2DD4BF
    public static final Color GRADIENT_PURPLE_START = new Color(139, 92, 246);// #8B5CF6
    public static final Color GRADIENT_PURPLE_END = new Color(167, 139, 250); // #A78BFA
    public static final Color GRADIENT_AMBER_START = new Color(245, 158, 11); // #F59E0B
    public static final Color GRADIENT_AMBER_END = new Color(251, 191, 36);   // #FBBF24
    
    // Dark mode gradients
    public static final Color GRADIENT_DARK_START = new Color(30, 58, 138);   // #1E3A8A
    public static final Color GRADIENT_DARK_END = new Color(37, 99, 235);     // #2563EB
    
    // ==================== TYPOGRAPHY ====================
    
    public static final String FONT_FAMILY = "Segoe UI";
    public static final String FONT_FAMILY_MONO = "Consolas";
    public static final String FONT_FAMILY_ARABIC = "Arial";
    
    // Premium Typography Scale
    public static final Font FONT_DISPLAY = new Font(FONT_FAMILY, Font.BOLD, 32);
    public static final Font FONT_TITLE = new Font(FONT_FAMILY, Font.BOLD, 26);
    public static final Font FONT_HEADING = new Font(FONT_FAMILY, Font.BOLD, 20);
    public static final Font FONT_HEADING_MEDIUM = new Font(FONT_FAMILY, Font.PLAIN, 18);
    public static final Font FONT_SUBTITLE = new Font(FONT_FAMILY, Font.PLAIN, 15);
    public static final Font FONT_BODY = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_BODY_MEDIUM = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font FONT_CAPTION = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font(FONT_FAMILY, Font.PLAIN, 11);
    public static final Font FONT_TINY = new Font(FONT_FAMILY, Font.PLAIN, 10);
    
    // Arabic/RTL Fonts
    public static final Font FONT_ARABIC_DISPLAY = new Font(FONT_FAMILY_ARABIC, Font.BOLD, 28);
    public static final Font FONT_ARABIC_TITLE = new Font(FONT_FAMILY_ARABIC, Font.BOLD, 24);
    public static final Font FONT_ARABIC_BODY = new Font(FONT_FAMILY_ARABIC, Font.PLAIN, 16);
    
    // ==================== SPACING ====================
    
    public static final int SPACING_XXS = 2;
    public static final int SPACING_XS = 4;
    public static final int SPACING_SM = 8;
    public static final int SPACING_MD = 16;
    public static final int SPACING_LG = 24;
    public static final int SPACING_XL = 32;
    public static final int SPACING_XXL = 48;
    public static final int SPACING_XXXL = 64;
    
    // ==================== DIMENSIONS ====================
    
    public static final int SIDEBAR_WIDTH = 260;
    public static final int SIDEBAR_COLLAPSED_WIDTH = 72;
    public static final int HEADER_HEIGHT = 64;
    public static final int BUTTON_HEIGHT = 42;
    public static final int BUTTON_HEIGHT_SMALL = 34;
    public static final int BUTTON_HEIGHT_LARGE = 50;
    public static final int INPUT_HEIGHT = 46;
    public static final int CARD_MIN_WIDTH = 300;
    
    // ==================== BORDERS & RADIUS ====================
    
    public static final int RADIUS_XS = 4;
    public static final int RADIUS_SM = 6;
    public static final int RADIUS_MD = 10;
    public static final int RADIUS_LG = 14;
    public static final int RADIUS_XL = 20;
    public static final int RADIUS_XXL = 28;
    public static final int RADIUS_ROUND = 999;
    
    public static final int BORDER_WIDTH = 1;
    public static final int BORDER_WIDTH_FOCUS = 2;
    
    // ==================== SHADOWS ====================
    
    // Soft shadow for subtle elevation
    public static final Color SHADOW_SOFT = new Color(0, 0, 0, 8);
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 15);
    public static final Color SHADOW_MEDIUM = new Color(0, 0, 0, 20);
    public static final Color SHADOW_STRONG = new Color(0, 0, 0, 30);
    public static final Color SHADOW_COLOR_DARK = new Color(0, 0, 0, 45);
    
    // Colored shadows for premium feel
    public static final Color SHADOW_PRIMARY = new Color(37, 99, 235, 25);
    public static final Color SHADOW_SUCCESS = new Color(34, 197, 94, 25);
    public static final Color SHADOW_WARNING = new Color(245, 158, 11, 25);
    public static final Color SHADOW_ERROR = new Color(239, 68, 68, 25);
    
    // ==================== EFFECTS ====================
    
    // Glassmorphism
    public static final Color GLASS_LIGHT = new Color(255, 255, 255, 180);
    public static final Color GLASS_DARK = new Color(30, 41, 59, 200);
    public static final Color GLASS_BORDER_LIGHT = new Color(255, 255, 255, 100);
    public static final Color GLASS_BORDER_DARK = new Color(255, 255, 255, 20);
    
    // Overlay colors
    public static final Color OVERLAY_LIGHT = new Color(0, 0, 0, 40);
    public static final Color OVERLAY_MEDIUM = new Color(0, 0, 0, 60);
    public static final Color OVERLAY_DARK = new Color(0, 0, 0, 80);
    
    // ==================== ANIMATIONS ====================
    
    public static final int ANIMATION_INSTANT = 100;
    public static final int ANIMATION_FAST = 150;
    public static final int ANIMATION_NORMAL = 250;
    public static final int ANIMATION_SLOW = 350;
    public static final int ANIMATION_SLOWER = 500;
    
    // ==================== ICONS (Unicode) ====================
    
    public static final String ICON_ADD = "+";
    public static final String ICON_SEARCH = "🔍";
    public static final String ICON_STAR = "⭐";
    public static final String ICON_STAR_OUTLINE = "☆";
    public static final String ICON_HISTORY = "🕐";
    public static final String ICON_SETTINGS = "⚙";
    public static final String ICON_MOON = "🌙";
    public static final String ICON_SUN = "☀";
    public static final String ICON_BACK = "←";
    public static final String ICON_FORWARD = "→";
    public static final String ICON_MENU = "☰";
    public static final String ICON_CLOSE = "✕";
    public static final String ICON_CHECK = "✓";
    public static final String ICON_COPY = "📋";
    public static final String ICON_BOOK = "📖";
    public static final String ICON_BOOKS = "📚";
    public static final String ICON_IMPORT = "📥";
    public static final String ICON_EXPORT = "📤";
    public static final String ICON_NORMALIZE = "🔤";
    public static final String ICON_FAQ = "❓";
    public static final String ICON_INFO = "ℹ";
    public static final String ICON_GLOBE = "🌍";
    public static final String ICON_SPARKLE = "✨";
    
    // ==================== HELPER METHODS ====================
    
    public static Color getPrimary(boolean isDark) {
        return isDark ? DARK_PRIMARY : LIGHT_PRIMARY;
    }
    
    public static Color getPrimaryDark(boolean isDark) {
        return isDark ? DARK_PRIMARY_DARK : LIGHT_PRIMARY_DARK;
    }
    
    public static Color getPrimaryLight(boolean isDark) {
        return isDark ? DARK_PRIMARY : LIGHT_PRIMARY_LIGHT;
    }
    
    public static Color getSecondary(boolean isDark) {
        return isDark ? DARK_SECONDARY : LIGHT_SECONDARY;
    }
    
    public static Color getAccent(boolean isDark) {
        return isDark ? DARK_ACCENT : LIGHT_ACCENT;
    }
    
    public static Color getBackground(boolean isDark) {
        return isDark ? DARK_BACKGROUND : LIGHT_BACKGROUND;
    }
    
    public static Color getSurface(boolean isDark) {
        return isDark ? DARK_SURFACE : LIGHT_SURFACE;
    }
    
    public static Color getCard(boolean isDark) {
        return isDark ? DARK_CARD : LIGHT_CARD;
    }
    
    public static Color getTextPrimary(boolean isDark) {
        return isDark ? DARK_TEXT_PRIMARY : LIGHT_TEXT_PRIMARY;
    }
    
    public static Color getTextSecondary(boolean isDark) {
        return isDark ? DARK_TEXT_SECONDARY : LIGHT_TEXT_SECONDARY;
    }
    
    public static Color getBorder(boolean isDark) {
        return isDark ? DARK_BORDER : LIGHT_BORDER;
    }
    
    public static Color getDivider(boolean isDark) {
        return isDark ? DARK_DIVIDER : LIGHT_DIVIDER;
    }
    
    public static Color getHover(boolean isDark) {
        return isDark ? DARK_HOVER : LIGHT_HOVER;
    }
    
    public static Color getSidebar(boolean isDark) {
        return isDark ? SIDEBAR_DARK : SIDEBAR_LIGHT;
    }
    
    public static Color getSidebarItemHover(boolean isDark) {
        return isDark ? SIDEBAR_ITEM_HOVER_DARK : SIDEBAR_ITEM_HOVER_LIGHT;
    }
    
    public static Color getSidebarItemActive(boolean isDark) {
        return isDark ? SIDEBAR_ITEM_ACTIVE_DARK : SIDEBAR_ITEM_ACTIVE_LIGHT;
    }
    
    public static Color getGradientStart(boolean isDark) {
        return isDark ? GRADIENT_DARK_START : GRADIENT_BLUE_START;
    }
    
    public static Color getGradientEnd(boolean isDark) {
        return isDark ? GRADIENT_DARK_END : GRADIENT_BLUE_END;
    }
    
    public static Color getGlass(boolean isDark) {
        return isDark ? GLASS_DARK : GLASS_LIGHT;
    }
    
    public static Color getGlassBorder(boolean isDark) {
        return isDark ? GLASS_BORDER_DARK : GLASS_BORDER_LIGHT;
    }
    
    public static Color getShadow(boolean isDark) {
        return isDark ? SHADOW_COLOR_DARK : SHADOW_COLOR;
    }
}
