package pl.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Premium styled text field with smooth animations and refined aesthetics
 */
public class ModernTextField extends JTextField {
    
    private String placeholder = "";
    private boolean isDarkMode = false;
    private boolean isFocused = false;
    private int cornerRadius = UIConstants.RADIUS_MD;
    private String prefixIcon = "";
    private Color focusColor;
    private float focusProgress = 0f;
    private Timer focusTimer;
    
    public ModernTextField() {
        this(20);
    }
    
    public ModernTextField(int columns) {
        super(columns);
        setupTextField();
    }
    
    public ModernTextField(String placeholder) {
        this(20);
        this.placeholder = placeholder;
    }
    
    public ModernTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        setupTextField();
    }
    
    private void setupTextField() {
        setFont(UIConstants.FONT_BODY);
        setOpaque(false);
        setBorder(new EmptyBorder(UIConstants.SPACING_SM + 2, UIConstants.SPACING_MD, 
                                  UIConstants.SPACING_SM + 2, UIConstants.SPACING_MD));
        setPreferredSize(new Dimension(200, UIConstants.INPUT_HEIGHT));
        focusColor = UIConstants.LIGHT_PRIMARY;
        
        // Smooth focus animation
        focusTimer = new Timer(16, e -> {
            if (isFocused && focusProgress < 1f) {
                focusProgress = Math.min(1f, focusProgress + 0.12f);
                repaint();
            } else if (!isFocused && focusProgress > 0f) {
                focusProgress = Math.max(0f, focusProgress - 0.12f);
                repaint();
            }
            if ((!isFocused && focusProgress == 0f) || (isFocused && focusProgress == 1f)) {
                focusTimer.stop();
            }
        });
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                if (!focusTimer.isRunning()) {
                    focusTimer.start();
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                if (!focusTimer.isRunning()) {
                    focusTimer.start();
                }
            }
        });
    }
    
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }
    
    public void setDarkMode(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
        setForeground(UIConstants.getTextPrimary(isDarkMode));
        setCaretColor(UIConstants.getTextPrimary(isDarkMode));
        focusColor = UIConstants.getPrimary(isDarkMode);
        repaint();
    }
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    public void setPrefixIcon(String icon) {
        this.prefixIcon = icon;
        int leftPadding = icon.isEmpty() ? UIConstants.SPACING_MD : UIConstants.SPACING_XL + 4;
        setBorder(new EmptyBorder(UIConstants.SPACING_SM + 2, leftPadding, 
                                  UIConstants.SPACING_SM + 2, UIConstants.SPACING_MD));
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw subtle outer shadow for depth
        g2.setColor(new Color(0, 0, 0, isDarkMode ? 15 : 8));
        g2.fill(new RoundRectangle2D.Float(1, 2, width - 2, height - 2, cornerRadius + 1, cornerRadius + 1));
        
        // Draw background
        g2.setColor(UIConstants.getSurface(isDarkMode));
        g2.fill(new RoundRectangle2D.Float(0, 0, width - 1, height - 2, cornerRadius, cornerRadius));
        
        // Draw subtle inner shadow for depth
        GradientPaint innerShadow = new GradientPaint(0, 1, 
            new Color(0, 0, 0, isDarkMode ? 20 : 10), 
            0, 6, 
            new Color(0, 0, 0, 0));
        g2.setPaint(innerShadow);
        g2.fill(new RoundRectangle2D.Float(1, 1, width - 3, 8, cornerRadius - 1, cornerRadius - 1));
        
        // Draw animated border
        Color borderColor = interpolateColor(UIConstants.getBorder(isDarkMode), focusColor, focusProgress);
        int borderWidth = focusProgress > 0.5f ? UIConstants.BORDER_WIDTH_FOCUS : UIConstants.BORDER_WIDTH;
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(borderWidth));
        g2.draw(new RoundRectangle2D.Float(0, 0, width - 1, height - 2, cornerRadius, cornerRadius));
        
        // Draw focus glow
        if (focusProgress > 0) {
            g2.setColor(new Color(focusColor.getRed(), focusColor.getGreen(), 
                                 focusColor.getBlue(), (int) (30 * focusProgress)));
            g2.setStroke(new BasicStroke(3));
            g2.draw(new RoundRectangle2D.Float(-1, -1, width + 1, height, cornerRadius + 2, cornerRadius + 2));
        }
        
        // Draw prefix icon
        if (!prefixIcon.isEmpty()) {
            Color iconColor = interpolateColor(UIConstants.getTextSecondary(isDarkMode), 
                                               focusColor, focusProgress * 0.5f);
            g2.setColor(iconColor);
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            g2.drawString(prefixIcon, UIConstants.SPACING_SM + 2, height / 2 + 4);
        }
        
        g2.dispose();
        
        super.paintComponent(g);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // Draw placeholder after everything else
        if (getText().isEmpty() && !placeholder.isEmpty() && !isFocused) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            Color placeholderColor = UIConstants.getTextSecondary(isDarkMode);
            g2.setColor(new Color(placeholderColor.getRed(), placeholderColor.getGreen(), 
                                 placeholderColor.getBlue(), 160));
            g2.setFont(getFont());
            Insets insets = getInsets();
            g2.drawString(placeholder, insets.left, getHeight() / 2 + 5);
            g2.dispose();
        }
    }
    
    private Color interpolateColor(Color c1, Color c2, float progress) {
        int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * progress);
        int g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * progress);
        int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * progress);
        int a = (int) (c1.getAlpha() + (c2.getAlpha() - c1.getAlpha()) * progress);
        return new Color(r, g, b, a);
    }
}
