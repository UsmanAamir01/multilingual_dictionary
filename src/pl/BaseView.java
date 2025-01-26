package pl;

import javax.swing.*;
import java.awt.*;

public class BaseView extends JFrame implements ThemeManager.ThemeListener {
    protected Color primaryColor;
    protected Color secondaryColor;

    public BaseView() {
        ThemeManager.getInstance().addListener(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void themeChanged(boolean isDarkMode) {
        applyTheme(isDarkMode);
        SwingUtilities.updateComponentTreeUI(this);
        revalidate();
        repaint();
    }

    protected void applyTheme(boolean isDarkMode) {
        Color background = isDarkMode ? new Color(30, 30, 30) : Color.WHITE;
        Color foreground = isDarkMode ? Color.WHITE : Color.BLACK;
        primaryColor = isDarkMode ? new Color(70, 70, 70) : new Color(240, 240, 240);
        secondaryColor = isDarkMode ? new Color(100, 100, 100) : new Color(200, 200, 200);

        getContentPane().setBackground(background);
        applyComponentTheme(getContentPane(), background, foreground);
    }

    protected void applyComponentTheme(Container container, Color background, Color foreground) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof Container) {
                applyComponentTheme((Container) comp, background, foreground);
            }
            if (comp instanceof JComponent) {
                comp.setBackground(background);
                if (comp instanceof JLabel || comp instanceof JButton) {
                    comp.setForeground(foreground);
                }
                if (comp instanceof JTextField) {
                    ((JTextField) comp).setBorder(BorderFactory.createLineBorder(secondaryColor, 1));
                }
                if (comp instanceof JComboBox) {
                    ((JComboBox<?>) comp).setBackground(primaryColor);
                    ((JComboBox<?>) comp).setForeground(foreground);
                }
                if (comp instanceof JButton) {
                    JButton button = (JButton) comp;
                    button.setBackground(secondaryColor);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(primaryColor, 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                    ));
                }
            }
        }
    }

    @Override
    public void dispose() {
        ThemeManager.getInstance().removeListener(this);
        super.dispose();
    }
}
