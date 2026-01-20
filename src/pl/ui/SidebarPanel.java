package pl.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Premium sidebar navigation panel with smooth animations
 */
public class SidebarPanel extends JPanel {
    
    private boolean isDarkMode = false;
    private List<SidebarItem> items = new ArrayList<>();
    private SidebarItem activeItem = null;
    private JPanel itemsContainer;
    private JPanel bottomContainer;
    private SidebarItemClickListener clickListener;
    private JPanel headerPanel;
    
    public interface SidebarItemClickListener {
        void onItemClicked(String itemName);
    }
    
    public SidebarPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH, 0));
        setBackground(UIConstants.SIDEBAR_LIGHT);
        
        // Logo/Header area
        headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Items container with vertical layout
        itemsContainer = new JPanel();
        itemsContainer.setLayout(new BoxLayout(itemsContainer, BoxLayout.Y_AXIS));
        itemsContainer.setOpaque(false);
        itemsContainer.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SPACING_MD, UIConstants.SPACING_SM, UIConstants.SPACING_SM, UIConstants.SPACING_SM));
        
        JScrollPane scrollPane = new JScrollPane(itemsContainer);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom container for settings, theme toggle
        bottomContainer = new JPanel();
        bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.Y_AXIS));
        bottomContainer.setOpaque(false);
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SPACING_SM, UIConstants.SPACING_SM, UIConstants.SPACING_LG, UIConstants.SPACING_SM));
        add(bottomContainer, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw subtle accent at bottom
                GradientPaint gradient = new GradientPaint(
                    0, getHeight() - 2,
                    UIConstants.getPrimary(isDarkMode),
                    getWidth(), getHeight() - 2,
                    UIConstants.getSecondary(isDarkMode)
                );
                g2.setPaint(gradient);
                g2.fillRect(UIConstants.SPACING_MD, getHeight() - 2, getWidth() - UIConstants.SPACING_MD * 2, 2);
                
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SPACING_LG, UIConstants.SPACING_MD, 
            UIConstants.SPACING_LG, UIConstants.SPACING_MD));
        
        // Logo container
        JPanel logoContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, UIConstants.SPACING_SM, 0));
        logoContainer.setOpaque(false);
        
        JLabel logoIcon = new JLabel(UIConstants.ICON_BOOK);
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JLabel titleLabel = new JLabel("Dictionary");
        titleLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 18));
        titleLabel.setForeground(UIConstants.getPrimary(isDarkMode));
        
        logoContainer.add(logoIcon);
        logoContainer.add(titleLabel);
        
        header.add(logoContainer, BorderLayout.CENTER);
        
        return header;
    }
    
    public void addItem(String icon, String name) {
        SidebarItem item = new SidebarItem(icon, name);
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setActiveItem(item);
                if (clickListener != null) {
                    clickListener.onItemClicked(name);
                }
            }
        });
        items.add(item);
        itemsContainer.add(item);
        itemsContainer.add(Box.createVerticalStrut(UIConstants.SPACING_XXS));
    }
    
    public void addBottomItem(String icon, String name) {
        SidebarItem item = new SidebarItem(icon, name);
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickListener != null) {
                    clickListener.onItemClicked(name);
                }
            }
        });
        bottomContainer.add(item);
        bottomContainer.add(Box.createVerticalStrut(UIConstants.SPACING_XXS));
    }
    
    public void addDivider() {
        JPanel dividerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient divider
                GradientPaint gradient = new GradientPaint(
                    UIConstants.SPACING_MD, 0,
                    new Color(0, 0, 0, 0),
                    getWidth() / 2, 0,
                    UIConstants.getBorder(isDarkMode)
                );
                g2.setPaint(gradient);
                g2.fillRect(UIConstants.SPACING_MD, getHeight() / 2, getWidth() / 2 - UIConstants.SPACING_MD, 1);
                
                GradientPaint gradient2 = new GradientPaint(
                    getWidth() / 2, 0,
                    UIConstants.getBorder(isDarkMode),
                    getWidth() - UIConstants.SPACING_MD, 0,
                    new Color(0, 0, 0, 0)
                );
                g2.setPaint(gradient2);
                g2.fillRect(getWidth() / 2, getHeight() / 2, getWidth() / 2 - UIConstants.SPACING_MD, 1);
                
                g2.dispose();
            }
        };
        dividerPanel.setOpaque(false);
        dividerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, UIConstants.SPACING_LG));
        dividerPanel.setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH - 16, UIConstants.SPACING_LG));
        itemsContainer.add(dividerPanel);
    }
    
    public void setActiveItem(SidebarItem item) {
        if (activeItem != null) {
            activeItem.setActive(false);
        }
        activeItem = item;
        if (activeItem != null) {
            activeItem.setActive(true);
        }
    }
    
    public void setActiveItemByName(String name) {
        for (SidebarItem item : items) {
            if (item.getName().equals(name)) {
                setActiveItem(item);
                break;
            }
        }
    }
    
    public void setClickListener(SidebarItemClickListener listener) {
        this.clickListener = listener;
    }
    
    public void setDarkMode(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
        setBackground(UIConstants.getSidebar(isDarkMode));
        
        // Update header title color
        for (Component comp : ((JPanel) headerPanel.getComponent(0)).getComponents()) {
            if (comp instanceof JLabel && !((JLabel) comp).getText().isEmpty() && 
                !((JLabel) comp).getText().equals(UIConstants.ICON_BOOK)) {
                ((JLabel) comp).setForeground(UIConstants.getPrimary(isDarkMode));
            }
        }
        
        for (SidebarItem item : items) {
            item.setDarkMode(isDarkMode);
        }
        
        for (Component comp : bottomContainer.getComponents()) {
            if (comp instanceof SidebarItem) {
                ((SidebarItem) comp).setDarkMode(isDarkMode);
            }
        }
        
        repaint();
    }
    
    // Inner class for sidebar items
    public class SidebarItem extends JPanel {
        private String icon;
        private String name;
        private boolean isActive = false;
        private boolean isHovered = false;
        private boolean isDark = false;
        private JLabel iconLabel;
        private JLabel nameLabel;
        private float hoverProgress = 0f;
        private Timer hoverTimer;
        
        public SidebarItem(String icon, String name) {
            this.icon = icon;
            this.name = name;
            
            setLayout(new FlowLayout(FlowLayout.LEFT, UIConstants.SPACING_MD, UIConstants.SPACING_SM));
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH - 20, 44));
            setBorder(BorderFactory.createEmptyBorder(0, UIConstants.SPACING_XS, 0, UIConstants.SPACING_XS));
            
            iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            iconLabel.setForeground(UIConstants.getTextSecondary(isDark));
            
            nameLabel = new JLabel(name);
            nameLabel.setFont(UIConstants.FONT_BODY);
            nameLabel.setForeground(UIConstants.getTextPrimary(isDark));
            
            add(iconLabel);
            add(nameLabel);
            
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
            });
        }
        
        public String getName() {
            return name;
        }
        
        public void setActive(boolean active) {
            this.isActive = active;
            updateColors();
            repaint();
        }
        
        public void setDarkMode(boolean isDarkMode) {
            this.isDark = isDarkMode;
            updateColors();
            repaint();
        }
        
        private void updateColors() {
            if (isActive) {
                iconLabel.setForeground(UIConstants.getSidebarItemActive(isDark));
                nameLabel.setForeground(UIConstants.getSidebarItemActive(isDark));
                nameLabel.setFont(UIConstants.FONT_BODY_BOLD);
            } else {
                iconLabel.setForeground(UIConstants.getTextSecondary(isDark));
                nameLabel.setForeground(UIConstants.getTextPrimary(isDark));
                nameLabel.setFont(UIConstants.FONT_BODY);
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            if (isActive) {
                // Active background with gradient
                Color bgColor = UIConstants.getSidebarItemHover(isDark);
                g2.setColor(bgColor);
                g2.fill(new RoundRectangle2D.Float(4, 2, width - 8, height - 4, 
                                UIConstants.RADIUS_SM, UIConstants.RADIUS_SM));
                
                // Left accent bar with gradient
                GradientPaint accentGradient = new GradientPaint(
                    0, 6,
                    UIConstants.getSidebarItemActive(isDark),
                    0, height - 6,
                    UIConstants.getSecondary(isDark)
                );
                g2.setPaint(accentGradient);
                g2.fill(new RoundRectangle2D.Float(4, 8, 3, height - 16, 2, 2));
            } else if (hoverProgress > 0) {
                // Animated hover background
                Color hoverColor = UIConstants.getSidebarItemHover(isDark);
                g2.setColor(new Color(hoverColor.getRed(), hoverColor.getGreen(), 
                                     hoverColor.getBlue(), (int) (hoverColor.getAlpha() * hoverProgress)));
                g2.fill(new RoundRectangle2D.Float(4, 2, width - 8, height - 4, 
                                UIConstants.RADIUS_SM, UIConstants.RADIUS_SM));
            }
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
