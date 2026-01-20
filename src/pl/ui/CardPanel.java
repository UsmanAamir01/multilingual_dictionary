package pl.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Premium card panel with smooth elevation, glassmorphism, and refined shadows
 */
public class CardPanel extends JPanel {
    
    private int cornerRadius = UIConstants.RADIUS_MD;
    private int elevation = 2;
    private boolean isDarkMode = false;
    private Color customBackground;
    private boolean hasBorder = false;
    private boolean hasGlassEffect = false;
    private boolean isHoverable = false;
    private boolean isHovered = false;
    private int baseElevation;
    
    public CardPanel() {
        this(new BorderLayout());
    }
    
    public CardPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SPACING_MD, 
            UIConstants.SPACING_MD, 
            UIConstants.SPACING_MD, 
            UIConstants.SPACING_MD
        ));
        this.baseElevation = elevation;
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    public void setElevation(int elevation) {
        this.elevation = elevation;
        this.baseElevation = elevation;
        repaint();
    }
    
    public void setDarkMode(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
        repaint();
    }
    
    public void setCustomBackground(Color color) {
        this.customBackground = color;
        repaint();
    }
    
    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
        repaint();
    }
    
    public void setHasGlassEffect(boolean hasGlassEffect) {
        this.hasGlassEffect = hasGlassEffect;
        repaint();
    }
    
    public void setHoverable(boolean hoverable) {
        this.isHoverable = hoverable;
        if (hoverable) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    elevation = baseElevation + 2;
                    repaint();
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    elevation = baseElevation;
                    repaint();
                }
            });
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        int shadowPadding = Math.max(elevation * 2, 4);
        int shadowOffset = elevation;
        
        // Draw multi-layer shadow for natural depth
        if (elevation > 0) {
            for (int i = elevation; i >= 0; i--) {
                int alpha = isDarkMode ? 
                    Math.max(5, 25 - (i * 5)) : 
                    Math.max(3, 15 - (i * 3));
                g2.setColor(new Color(0, 0, 0, alpha));
                int offset = shadowOffset - (elevation - i);
                g2.fill(new RoundRectangle2D.Float(
                    shadowPadding / 2 - i + 1, 
                    offset + i + 1, 
                    width - shadowPadding + (i * 2) - 2, 
                    height - shadowPadding + i - 2, 
                    cornerRadius + i, 
                    cornerRadius + i
                ));
            }
        }
        
        // Draw card background
        Color bgColor = customBackground != null ? customBackground : UIConstants.getCard(isDarkMode);
        
        if (hasGlassEffect) {
            // Glass morphism effect
            g2.setColor(UIConstants.getGlass(isDarkMode));
            g2.fill(new RoundRectangle2D.Float(
                shadowPadding / 2, 
                shadowPadding / 2, 
                width - shadowPadding, 
                height - shadowPadding - shadowOffset / 2, 
                cornerRadius, 
                cornerRadius
            ));
            
            // Glass border highlight
            g2.setColor(UIConstants.getGlassBorder(isDarkMode));
            g2.setStroke(new BasicStroke(1));
            g2.draw(new RoundRectangle2D.Float(
                shadowPadding / 2, 
                shadowPadding / 2, 
                width - shadowPadding - 1, 
                height - shadowPadding - shadowOffset / 2 - 1, 
                cornerRadius, 
                cornerRadius
            ));
        } else {
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(
                shadowPadding / 2, 
                shadowPadding / 2, 
                width - shadowPadding, 
                height - shadowPadding - shadowOffset / 2, 
                cornerRadius, 
                cornerRadius
            ));
        }
        
        // Draw subtle top highlight for depth
        if (!hasGlassEffect && !isDarkMode) {
            GradientPaint highlight = new GradientPaint(
                0, shadowPadding / 2,
                new Color(255, 255, 255, 40),
                0, shadowPadding / 2 + 20,
                new Color(255, 255, 255, 0)
            );
            g2.setPaint(highlight);
            g2.fill(new RoundRectangle2D.Float(
                shadowPadding / 2 + 1, 
                shadowPadding / 2 + 1, 
                width - shadowPadding - 2, 
                20, 
                cornerRadius - 1, 
                cornerRadius - 1
            ));
        }
        
        // Draw border if enabled
        if (hasBorder) {
            Color borderColor = isHovered ? 
                UIConstants.getPrimary(isDarkMode) : 
                UIConstants.getBorder(isDarkMode);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1));
            g2.draw(new RoundRectangle2D.Float(
                shadowPadding / 2, 
                shadowPadding / 2, 
                width - shadowPadding - 1, 
                height - shadowPadding - shadowOffset / 2 - 1, 
                cornerRadius, 
                cornerRadius
            ));
        }
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    @Override
    public Insets getInsets() {
        Insets original = super.getInsets();
        int extra = Math.max(elevation * 2, 4);
        return new Insets(
            original.top + extra / 2, 
            original.left + extra / 2, 
            original.bottom + extra, 
            original.right + extra / 2
        );
    }
}
