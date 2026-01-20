package pl.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Factory class for creating consistent modern UI components
 */
public class ComponentFactory {
    
    private static boolean isDarkMode = false;
    
    public static void setDarkMode(boolean dark) {
        isDarkMode = dark;
    }
    
    public static boolean isDarkMode() {
        return isDarkMode;
    }
    
    // ==================== BUTTONS ====================
    
    public static ModernButton createPrimaryButton(String text) {
        ModernButton button = new ModernButton(text, ModernButton.ButtonStyle.PRIMARY);
        button.setDarkMode(isDarkMode);
        return button;
    }
    
    public static ModernButton createSecondaryButton(String text) {
        ModernButton button = new ModernButton(text, ModernButton.ButtonStyle.SECONDARY);
        button.setDarkMode(isDarkMode);
        return button;
    }
    
    public static ModernButton createOutlineButton(String text) {
        ModernButton button = new ModernButton(text, ModernButton.ButtonStyle.OUTLINE);
        button.setDarkMode(isDarkMode);
        return button;
    }
    
    public static ModernButton createTextButton(String text) {
        ModernButton button = new ModernButton(text, ModernButton.ButtonStyle.TEXT);
        button.setDarkMode(isDarkMode);
        return button;
    }
    
    public static ModernButton createSuccessButton(String text) {
        ModernButton button = new ModernButton(text, ModernButton.ButtonStyle.SUCCESS);
        button.setDarkMode(isDarkMode);
        return button;
    }
    
    public static ModernButton createDangerButton(String text) {
        ModernButton button = new ModernButton(text, ModernButton.ButtonStyle.DANGER);
        button.setDarkMode(isDarkMode);
        return button;
    }
    
    public static ModernButton createIconButton(String icon, String text) {
        ModernButton button = new ModernButton(icon, text, ModernButton.ButtonStyle.PRIMARY);
        button.setDarkMode(isDarkMode);
        return button;
    }
    
    public static ModernButton createSidebarButton(String icon, String text) {
        ModernButton button = new ModernButton(icon, text, ModernButton.ButtonStyle.SIDEBAR);
        button.setDarkMode(isDarkMode);
        button.setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH - 32, 44));
        return button;
    }
    
    // ==================== TEXT FIELDS ====================
    
    public static ModernTextField createTextField() {
        ModernTextField field = new ModernTextField();
        field.setDarkMode(isDarkMode);
        return field;
    }
    
    public static ModernTextField createTextField(String placeholder) {
        ModernTextField field = new ModernTextField(placeholder);
        field.setDarkMode(isDarkMode);
        return field;
    }
    
    public static ModernTextField createTextField(String placeholder, int columns) {
        ModernTextField field = new ModernTextField(placeholder, columns);
        field.setDarkMode(isDarkMode);
        return field;
    }
    
    public static ModernTextField createSearchField() {
        ModernTextField field = new ModernTextField("Search...", 25);
        field.setPrefixIcon(UIConstants.ICON_SEARCH);
        field.setDarkMode(isDarkMode);
        return field;
    }
    
    // ==================== COMBO BOX ====================
    
    public static <E> ModernComboBox<E> createComboBox(E[] items) {
        ModernComboBox<E> comboBox = new ModernComboBox<>(items);
        comboBox.setDarkMode(isDarkMode);
        return comboBox;
    }
    
    // ==================== CARDS ====================
    
    public static CardPanel createCard() {
        CardPanel card = new CardPanel();
        card.setDarkMode(isDarkMode);
        return card;
    }
    
    public static CardPanel createCard(LayoutManager layout) {
        CardPanel card = new CardPanel(layout);
        card.setDarkMode(isDarkMode);
        return card;
    }
    
    public static CardPanel createElevatedCard() {
        CardPanel card = new CardPanel();
        card.setElevation(4);
        card.setDarkMode(isDarkMode);
        return card;
    }
    
    public static CardPanel createFlatCard() {
        CardPanel card = new CardPanel();
        card.setElevation(0);
        card.setHasBorder(true);
        card.setDarkMode(isDarkMode);
        return card;
    }
    
    public static CardPanel createGlassCard() {
        CardPanel card = new CardPanel();
        card.setElevation(2);
        card.setHasGlassEffect(true);
        card.setDarkMode(isDarkMode);
        return card;
    }
    
    public static CardPanel createHoverableCard() {
        CardPanel card = new CardPanel();
        card.setElevation(2);
        card.setHasBorder(true);
        card.setHoverable(true);
        card.setDarkMode(isDarkMode);
        return card;
    }
    
    public static CardPanel createPremiumCard() {
        CardPanel card = new CardPanel();
        card.setElevation(3);
        card.setCornerRadius(UIConstants.RADIUS_LG);
        card.setDarkMode(isDarkMode);
        return card;
    }
    
    // ==================== PANELS ====================
    
    public static JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(UIConstants.getBackground(isDarkMode));
        return panel;
    }
    
    public static JPanel createPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(UIConstants.getBackground(isDarkMode));
        return panel;
    }
    
    public static JPanel createTransparentPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }
    
    public static JPanel createTransparentPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        return panel;
    }
    
    // ==================== LABELS ====================
    
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_TITLE);
        label.setForeground(UIConstants.getPrimary(isDarkMode));
        return label;
    }
    
    public static JLabel createHeadingLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_HEADING);
        label.setForeground(UIConstants.getTextPrimary(isDarkMode));
        return label;
    }
    
    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_SUBTITLE);
        label.setForeground(UIConstants.getTextSecondary(isDarkMode));
        return label;
    }
    
    public static JLabel createBodyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_BODY);
        label.setForeground(UIConstants.getTextPrimary(isDarkMode));
        return label;
    }
    
    public static JLabel createCaptionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_CAPTION);
        label.setForeground(UIConstants.getTextSecondary(isDarkMode));
        return label;
    }
    
    public static JLabel createIconLabel(String icon) {
        JLabel label = new JLabel(icon);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        return label;
    }
    
    // ==================== SCROLL PANES ====================
    
    public static JScrollPane createScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(UIConstants.getBackground(isDarkMode));
        scrollPane.getViewport().setBackground(UIConstants.getBackground(isDarkMode));
        return scrollPane;
    }
    
    // ==================== SEPARATORS ====================
    
    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(UIConstants.getBorder(isDarkMode));
        separator.setBackground(UIConstants.getBackground(isDarkMode));
        return separator;
    }
    
    // ==================== TEXT AREAS ====================
    
    public static JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(UIConstants.FONT_BODY);
        textArea.setForeground(UIConstants.getTextPrimary(isDarkMode));
        textArea.setBackground(UIConstants.getSurface(isDarkMode));
        textArea.setBorder(new EmptyBorder(UIConstants.SPACING_MD, UIConstants.SPACING_MD, 
                                           UIConstants.SPACING_MD, UIConstants.SPACING_MD));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }
    
    // ==================== DIALOGS ====================
    
    public static void showInfoDialog(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showErrorDialog(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showSuccessDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static boolean showConfirmDialog(Component parent, String title, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, title, 
                                                   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
    
    // ==================== SPACING ====================
    
    public static Component createVerticalStrut(int height) {
        return Box.createVerticalStrut(height);
    }
    
    public static Component createHorizontalStrut(int width) {
        return Box.createHorizontalStrut(width);
    }
    
    public static Component createVerticalGlue() {
        return Box.createVerticalGlue();
    }
    
    public static Component createHorizontalGlue() {
        return Box.createHorizontalGlue();
    }
}
