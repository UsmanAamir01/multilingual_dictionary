package pl.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Premium styled button with smooth animations and elevated design
 */
public class ModernButton extends JButton {
    
    public enum ButtonStyle {
        PRIMARY,
        SECONDARY,
        OUTLINE,
        TEXT,
        SUCCESS,
        DANGER,
        SIDEBAR,
        ICON_CIRCLE  // New circular icon button style
    }
    
    private ButtonStyle style;
    private boolean isDarkMode = false;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private int cornerRadius = UIConstants.RADIUS_MD;
    private Color customBackground;
    private Color customForeground;
    private String iconText = "";
    private float hoverProgress = 0f;
    private Timer hoverTimer;
    
    public ModernButton(String text) {
        this(text, ButtonStyle.PRIMARY);
    }
    
    public ModernButton(String text, ButtonStyle style) {
        super(text);
        this.style = style;
        setupButton();
    }
    
    public ModernButton(String iconText, String text, ButtonStyle style) {
        super(text);
        this.style = style;
        this.iconText = iconText;
        setupButton();
    }
    
    private void setupButton() {
        setFont(UIConstants.FONT_BUTTON);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        if (style == ButtonStyle.ICON_CIRCLE) {
            setPreferredSize(new Dimension(UIConstants.BUTTON_HEIGHT, UIConstants.BUTTON_HEIGHT));
            cornerRadius = UIConstants.RADIUS_ROUND;
        } else {
            setPreferredSize(new Dimension(140, UIConstants.BUTTON_HEIGHT));
        }
        
        // Smooth hover animation
        hoverTimer = new Timer(16, e -> {
            if (isHovered && hoverProgress < 1f) {
                hoverProgress = Math.min(1f, hoverProgress + 0.15f);
                repaint();
            } else if (!isHovered && hoverProgress > 0f) {
                hoverProgress = Math.max(0f, hoverProgress - 0.15f);
                repaint();
            }
            if ((!isHovered && hoverProgress == 0f) || (isHovered && hoverProgress == 1f)) {
                hoverTimer.stop();
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                if (!hoverTimer.isRunning()) {
                    hoverTimer.start();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                if (!hoverTimer.isRunning()) {
                    hoverTimer.start();
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
        
        updateColors();
    }
    
    public void setDarkMode(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
        updateColors();
        repaint();
    }
    
    public void setStyle(ButtonStyle style) {
        this.style = style;
        if (style == ButtonStyle.ICON_CIRCLE) {
            setPreferredSize(new Dimension(UIConstants.BUTTON_HEIGHT, UIConstants.BUTTON_HEIGHT));
            cornerRadius = UIConstants.RADIUS_ROUND;
        }
        updateColors();
        repaint();
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    public void setCustomColors(Color background, Color foreground) {
        this.customBackground = background;
        this.customForeground = foreground;
        repaint();
    }
    
    public void setIconText(String icon) {
        this.iconText = icon;
        repaint();
    }
    
    private void updateColors() {
        switch (style) {
            case PRIMARY:
            case SECONDARY:
            case SUCCESS:
            case DANGER:
                setForeground(Color.WHITE);
                break;
            case OUTLINE:
            case TEXT:
            case ICON_CIRCLE:
                setForeground(UIConstants.getPrimary(isDarkMode));
                break;
            case SIDEBAR:
                setForeground(UIConstants.getTextPrimary(isDarkMode));
                break;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // Apply subtle scale on press
        float scale = isPressed ? 0.97f : 1f;
        if (scale != 1f) {
            int dx = (int) ((width * (1 - scale)) / 2);
            int dy = (int) ((height * (1 - scale)) / 2);
            g2.translate(dx, dy);
            g2.scale(scale, scale);
        }
        
        // Get colors based on style and state
        Color bgColor = getBackgroundColor();
        Color fgColor = customForeground != null ? customForeground : getForeground();
        
        // Draw shadow for elevated buttons
        if (style == ButtonStyle.PRIMARY || style == ButtonStyle.SUCCESS || 
            style == ButtonStyle.DANGER || style == ButtonStyle.SECONDARY) {
            // Multi-layer shadow for depth
            int shadowLayers = 3;
            for (int i = shadowLayers; i >= 0; i--) {
                int alpha = isPressed ? 5 : (int) (10 + hoverProgress * 8) - (i * 3);
                Color shadowColor = getShadowColor();
                g2.setColor(new Color(shadowColor.getRed(), shadowColor.getGreen(), 
                                     shadowColor.getBlue(), Math.max(alpha, 0)));
                int offset = 2 + i;
                g2.fill(new RoundRectangle2D.Float(i, offset + i, width - (i * 2), 
                        height - (i * 2), cornerRadius + i, cornerRadius + i));
            }
        }
        
        // Draw background with gradient for primary buttons
        if ((style == ButtonStyle.PRIMARY || style == ButtonStyle.SECONDARY || 
             style == ButtonStyle.SUCCESS || style == ButtonStyle.DANGER) && !isPressed) {
            GradientPaint gradient = new GradientPaint(0, 0, brighten(bgColor, 0.08f),
                                                        0, height, bgColor);
            g2.setPaint(gradient);
        } else {
            g2.setColor(bgColor);
        }
        g2.fill(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, cornerRadius, cornerRadius));
        
        // Draw subtle top highlight for depth
        if (style == ButtonStyle.PRIMARY || style == ButtonStyle.SECONDARY || 
            style == ButtonStyle.SUCCESS || style == ButtonStyle.DANGER) {
            g2.setColor(new Color(255, 255, 255, 25));
            g2.fill(new RoundRectangle2D.Float(1, 1, width - 3, height / 3, cornerRadius, cornerRadius));
        }
        
        // Draw border for outline style
        if (style == ButtonStyle.OUTLINE) {
            Color borderColor = UIConstants.getPrimary(isDarkMode);
            g2.setColor(interpolateColor(UIConstants.getBorder(isDarkMode), borderColor, hoverProgress));
            g2.setStroke(new BasicStroke(UIConstants.BORDER_WIDTH_FOCUS));
            g2.draw(new RoundRectangle2D.Float(1, 1, width - 3, height - 3, cornerRadius, cornerRadius));
        }
        
        // Draw icon circle hover background
        if (style == ButtonStyle.ICON_CIRCLE && hoverProgress > 0) {
            g2.setColor(new Color(UIConstants.getPrimary(isDarkMode).getRed(),
                                 UIConstants.getPrimary(isDarkMode).getGreen(),
                                 UIConstants.getPrimary(isDarkMode).getBlue(),
                                 (int) (20 * hoverProgress)));
            g2.fill(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, cornerRadius, cornerRadius));
        }
        
        // Draw text
        g2.setColor(fgColor);
        g2.setFont(getFont());
        
        FontMetrics fm = g2.getFontMetrics();
        String fullText = iconText.isEmpty() ? getText() : iconText + "  " + getText();
        if (style == ButtonStyle.ICON_CIRCLE && !iconText.isEmpty()) {
            fullText = iconText; // Only show icon for circle buttons
        }
        int textWidth = fm.stringWidth(fullText);
        int textX = (width - textWidth) / 2;
        int textY = (height + fm.getAscent() - fm.getDescent()) / 2;
        
        g2.drawString(fullText, textX, textY);
        
        g2.dispose();
    }
    
    private Color getShadowColor() {
        switch (style) {
            case PRIMARY:
                return UIConstants.SHADOW_PRIMARY;
            case SUCCESS:
                return UIConstants.SHADOW_SUCCESS;
            case DANGER:
                return UIConstants.SHADOW_ERROR;
            default:
                return UIConstants.SHADOW_COLOR;
        }
    }
    
    private Color getBackgroundColor() {
        if (customBackground != null) {
            if (isPressed) return darker(customBackground, 0.12f);
            return interpolateColor(customBackground, darker(customBackground, 0.06f), hoverProgress);
        }
        
        switch (style) {
            case PRIMARY:
                Color primary = UIConstants.getPrimary(isDarkMode);
                if (isPressed) return darker(primary, 0.12f);
                return interpolateColor(primary, darker(primary, 0.06f), hoverProgress);
                
            case SECONDARY:
                Color secondary = UIConstants.getSecondary(isDarkMode);
                if (isPressed) return darker(secondary, 0.12f);
                return interpolateColor(secondary, darker(secondary, 0.06f), hoverProgress);
                
            case OUTLINE:
            case TEXT:
            case ICON_CIRCLE:
                if (isPressed) return new Color(UIConstants.getPrimary(isDarkMode).getRed(), 
                                                UIConstants.getPrimary(isDarkMode).getGreen(), 
                                                UIConstants.getPrimary(isDarkMode).getBlue(), 30);
                return new Color(0, 0, 0, 0);
                
            case SUCCESS:
                if (isPressed) return darker(UIConstants.SUCCESS, 0.12f);
                return interpolateColor(UIConstants.SUCCESS, darker(UIConstants.SUCCESS, 0.06f), hoverProgress);
                
            case DANGER:
                if (isPressed) return darker(UIConstants.ERROR, 0.12f);
                return interpolateColor(UIConstants.ERROR, darker(UIConstants.ERROR, 0.06f), hoverProgress);
                
            case SIDEBAR:
                if (isPressed) return UIConstants.getSidebarItemHover(isDarkMode);
                return interpolateColor(UIConstants.getSidebar(isDarkMode), 
                                       UIConstants.getSidebarItemHover(isDarkMode), hoverProgress);
                
            default:
                return UIConstants.getPrimary(isDarkMode);
        }
    }
    
    private Color interpolateColor(Color c1, Color c2, float progress) {
        int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * progress);
        int g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * progress);
        int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * progress);
        int a = (int) (c1.getAlpha() + (c2.getAlpha() - c1.getAlpha()) * progress);
        return new Color(r, g, b, a);
    }
    
    private Color darker(Color color, float factor) {
        return new Color(
            Math.max(0, (int)(color.getRed() * (1 - factor))),
            Math.max(0, (int)(color.getGreen() * (1 - factor))),
            Math.max(0, (int)(color.getBlue() * (1 - factor))),
            color.getAlpha()
        );
    }
    
    private Color brighten(Color color, float factor) {
        return new Color(
            Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor)),
            Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor)),
            Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor)),
            color.getAlpha()
        );
    }
    
    @Override
    public Dimension getPreferredSize() {
        if (style == ButtonStyle.ICON_CIRCLE) {
            return new Dimension(UIConstants.BUTTON_HEIGHT, UIConstants.BUTTON_HEIGHT);
        }
        Dimension size = super.getPreferredSize();
        size.height = UIConstants.BUTTON_HEIGHT;
        size.width = Math.max(size.width + 40, 110);
        return size;
    }
}
