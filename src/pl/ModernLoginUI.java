package pl;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

import bl.BLFacade;
import bl.IBLFacade;
import bl.UserBO;
import bl.WordBO;
import pl.ui.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Modern Login UI with glassmorphism-inspired design
 */
public class ModernLoginUI extends JFrame implements ThemeManager.ThemeListener {

    private static final Logger logger = LogManager.getLogger(ModernLoginUI.class);
    private ModernTextField usernameField;
    private JPasswordField passwordField;
    private ModernButton loginButton;
    private IBLFacade facade;
    private UserBO userBo;
    private boolean isDarkMode = false;
    private JPanel mainPanel;
    private JLabel themeToggle;

    public ModernLoginUI(IBLFacade facade, UserBO userBo) {
        this.facade = facade;
        this.userBo = userBo;
        ThemeManager.getInstance().addListener(this);
        
        initializeFrame();
        createUI();
    }

    private void initializeFrame() {
        setTitle("Multilingual Dictionary - Login");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Remove window decorations for custom title bar look
        // setUndecorated(true);
    }

    private void createUI() {
        mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient;
                if (isDarkMode) {
                    gradient = new GradientPaint(0, 0, new Color(30, 30, 50), 
                                                  getWidth(), getHeight(), new Color(20, 20, 35));
                } else {
                    gradient = new GradientPaint(0, 0, new Color(240, 248, 255), 
                                                  getWidth(), getHeight(), new Color(220, 235, 250));
                }
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Decorative circles
                g2.setColor(new Color(UIConstants.getPrimary(isDarkMode).getRGB() & 0x20FFFFFF, true));
                g2.fillOval(-100, -100, 300, 300);
                g2.fillOval(getWidth() - 150, getHeight() - 200, 250, 250);
                
                g2.dispose();
            }
        };
        
        // Theme toggle in top right
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(UIConstants.SPACING_MD, 0, 0, UIConstants.SPACING_MD));
        
        themeToggle = new JLabel(isDarkMode ? UIConstants.ICON_SUN : UIConstants.ICON_MOON);
        themeToggle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        themeToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        themeToggle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ThemeManager.toggleDarkMode();
            }
        });
        topPanel.add(themeToggle);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center panel with login card
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        // Login card
        CardPanel loginCard = new CardPanel();
        loginCard.setPreferredSize(new Dimension(380, 420));
        loginCard.setCornerRadius(UIConstants.RADIUS_LG);
        loginCard.setElevation(4);
        loginCard.setDarkMode(isDarkMode);
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.SPACING_XL, UIConstants.SPACING_XL, 
            UIConstants.SPACING_XL, UIConstants.SPACING_XL));
        
        // Logo/Icon
        JLabel iconLabel = new JLabel(UIConstants.ICON_BOOK);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(iconLabel);
        loginCard.add(Box.createVerticalStrut(UIConstants.SPACING_MD));
        
        // Title
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(UIConstants.FONT_TITLE);
        titleLabel.setForeground(UIConstants.getPrimary(isDarkMode));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(titleLabel);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to continue");
        subtitleLabel.setFont(UIConstants.FONT_SUBTITLE);
        subtitleLabel.setForeground(UIConstants.getTextSecondary(isDarkMode));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(subtitleLabel);
        
        loginCard.add(Box.createVerticalStrut(UIConstants.SPACING_XL));
        
        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(UIConstants.FONT_BODY_BOLD);
        usernameLabel.setForeground(UIConstants.getTextPrimary(isDarkMode));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginCard.add(usernameLabel);
        loginCard.add(Box.createVerticalStrut(UIConstants.SPACING_XS));
        
        usernameField = new ModernTextField("Enter your username", 20);
        usernameField.setDarkMode(isDarkMode);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, UIConstants.INPUT_HEIGHT));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginCard.add(usernameField);
        
        loginCard.add(Box.createVerticalStrut(UIConstants.SPACING_MD));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(UIConstants.FONT_BODY_BOLD);
        passwordLabel.setForeground(UIConstants.getTextPrimary(isDarkMode));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginCard.add(passwordLabel);
        loginCard.add(Box.createVerticalStrut(UIConstants.SPACING_XS));
        
        passwordField = new JPasswordField(20);
        passwordField.setFont(UIConstants.FONT_BODY);
        passwordField.setPreferredSize(new Dimension(200, UIConstants.INPUT_HEIGHT));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, UIConstants.INPUT_HEIGHT));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.getBorder(isDarkMode)),
            BorderFactory.createEmptyBorder(UIConstants.SPACING_SM, UIConstants.SPACING_MD, 
                                           UIConstants.SPACING_SM, UIConstants.SPACING_MD)
        ));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginCard.add(passwordField);
        
        loginCard.add(Box.createVerticalStrut(UIConstants.SPACING_LG));
        
        // Login button
        loginButton = new ModernButton("Sign In", ModernButton.ButtonStyle.PRIMARY);
        loginButton.setDarkMode(isDarkMode);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, UIConstants.BUTTON_HEIGHT));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());
        loginCard.add(loginButton);
        
        loginCard.add(Box.createVerticalStrut(UIConstants.SPACING_MD));
        
        // Demo credentials hint
        JLabel hintLabel = new JLabel("<html><center>Demo: usman / 3709<br>or any username/password</center></html>");
        hintLabel.setFont(UIConstants.FONT_CAPTION);
        hintLabel.setForeground(UIConstants.getTextSecondary(isDarkMode));
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(hintLabel);
        
        centerPanel.add(loginCard);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, UIConstants.SPACING_MD, 0));
        
        JLabel footerLabel = new JLabel("© 2026 Multilingual Dictionary");
        footerLabel.setFont(UIConstants.FONT_CAPTION);
        footerLabel.setForeground(UIConstants.getTextSecondary(isDarkMode));
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Add enter key listener
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
        
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
        
        setContentPane(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        if (userBo.validateUser(username, password)) {
            logger.info("Login successful for user: " + username);
            showSuccess("Welcome, " + username + "!");
            navigateToWordUI();
        } else {
            logger.warn("Failed login attempt for user: " + username);
            showError("Invalid credentials. Please try again.");
            passwordField.setText("");
        }
    }

    private void navigateToWordUI() {
        dispose();
        ThemeManager.getInstance().removeListener(this);
        SwingUtilities.invokeLater(() -> {
            ModernWordUI wordUI = new ModernWordUI(facade);
            wordUI.setVisible(true);
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void themeChanged(boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
        themeToggle.setText(isDarkMode ? UIConstants.ICON_SUN : UIConstants.ICON_MOON);
        usernameField.setDarkMode(isDarkMode);
        loginButton.setDarkMode(isDarkMode);
        
        // Recreate UI with new theme
        getContentPane().removeAll();
        createUI();
        revalidate();
        repaint();
    }

    @Override
    public void dispose() {
        ThemeManager.getInstance().removeListener(this);
        super.dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            UserBO userBO = new UserBO();
            WordBO wordBO = new WordBO();
            IBLFacade facade = new BLFacade(wordBO, userBO);

            ModernLoginUI loginUI = new ModernLoginUI(facade, userBO);
            loginUI.setVisible(true);
        });
    }
}
