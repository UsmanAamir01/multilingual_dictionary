package pl.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Premium styled combo box with refined aesthetics
 */
public class ModernComboBox<E> extends JComboBox<E> {
    
    private boolean isDarkMode = false;
    private int cornerRadius = UIConstants.RADIUS_MD;
    
    public ModernComboBox() {
        super();
        setupComboBox();
    }
    
    public ModernComboBox(E[] items) {
        super(items);
        setupComboBox();
    }
    
    private void setupComboBox() {
        setFont(UIConstants.FONT_BODY);
        setPreferredSize(new Dimension(150, UIConstants.INPUT_HEIGHT));
        setBorder(BorderFactory.createEmptyBorder());
        setUI(new ModernComboBoxUI());
        
        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(
                    UIConstants.SPACING_SM + 2, UIConstants.SPACING_MD, 
                    UIConstants.SPACING_SM + 2, UIConstants.SPACING_MD));
                
                if (isSelected) {
                    setBackground(UIConstants.getPrimary(isDarkMode));
                    setForeground(Color.WHITE);
                } else {
                    setBackground(UIConstants.getSurface(isDarkMode));
                    setForeground(UIConstants.getTextPrimary(isDarkMode));
                }
                return this;
            }
        });
    }
    
    public void setDarkMode(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
        setBackground(UIConstants.getSurface(isDarkMode));
        setForeground(UIConstants.getTextPrimary(isDarkMode));
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw subtle shadow
        g2.setColor(new Color(0, 0, 0, isDarkMode ? 15 : 8));
        g2.fill(new RoundRectangle2D.Float(1, 2, width - 2, height - 2, cornerRadius + 1, cornerRadius + 1));
        
        // Draw background
        g2.setColor(UIConstants.getSurface(isDarkMode));
        g2.fill(new RoundRectangle2D.Float(0, 0, width - 1, height - 2, cornerRadius, cornerRadius));
        
        // Draw border
        g2.setColor(UIConstants.getBorder(isDarkMode));
        g2.setStroke(new BasicStroke(UIConstants.BORDER_WIDTH));
        g2.draw(new RoundRectangle2D.Float(0, 0, width - 1, height - 2, cornerRadius, cornerRadius));
        
        g2.dispose();
        
        super.paintComponent(g);
    }
    
    private class ModernComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    int w = getWidth();
                    int h = getHeight();
                    
                    // Draw chevron arrow
                    g2.setColor(UIConstants.getTextSecondary(isDarkMode));
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int[] xPoints = {w/2 - 4, w/2, w/2 + 4};
                    int[] yPoints = {h/2 - 2, h/2 + 2, h/2 - 2};
                    g2.drawPolyline(xPoints, yPoints, 3);
                    
                    g2.dispose();
                }
            };
            button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, UIConstants.SPACING_SM));
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(24, 24));
            return button;
        }
        
        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                @Override
                protected void configurePopup() {
                    super.configurePopup();
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UIConstants.getBorder(isDarkMode), 1),
                        BorderFactory.createEmptyBorder(UIConstants.SPACING_XS, 0, UIConstants.SPACING_XS, 0)
                    ));
                    setBackground(UIConstants.getSurface(isDarkMode));
                }
            };
            popup.getAccessibleContext().setAccessibleParent(comboBox);
            return popup;
        }
        
        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            // Don't paint default background
        }
    }
}
