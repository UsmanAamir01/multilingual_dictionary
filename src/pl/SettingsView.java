package pl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsView extends JFrame {

    private JComboBox<String> languageComboBox;
    private JButton themeToggleButton;
    private boolean isDarkMode = false;
    private JCheckBox notificationsCheckBox;
    private String defaultLanguage = "English";
    private boolean defaultNotifications = true;

    public SettingsView(JFrame parentFrame) {
        setTitle("Settings");
        setSize(400, 500);  
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon("images/settings-icon.png").getImage());  

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));  
        settingsPanel.setBackground(new Color(245, 245, 245));  

        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));  
        titleLabel.setForeground(new Color(0, 51, 153));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsPanel.add(titleLabel);

        JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        languagePanel.setBackground(new Color(245, 245, 245));
        JLabel languageLabel = new JLabel("Select Language: ");
        languageLabel.setFont(new Font("Arial", Font.PLAIN, 14));  
        languageLabel.setForeground(new Color(0, 51, 153));  
        languageComboBox = new JComboBox<>(new String[]{"English", "Arabic", "Persian", "Urdu"});
        languageComboBox.setBackground(Color.WHITE);
        languageComboBox.setFont(new Font("Arial", Font.PLAIN, 12));  
        languagePanel.add(languageLabel);
        languagePanel.add(languageComboBox);
        settingsPanel.add(languagePanel);

        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        themePanel.setBackground(new Color(245, 245, 245));
        themeToggleButton = new JButton("Switch to Dark Mode");
        themeToggleButton.setFocusPainted(false);
        themeToggleButton.setBackground(new Color(0, 51, 153));
        themeToggleButton.setForeground(Color.WHITE);
        themeToggleButton.setFont(new Font("Arial", Font.BOLD, 12));  
        themeToggleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        themeToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleTheme();
            }
        });
        themePanel.add(themeToggleButton);
        settingsPanel.add(themePanel);

        JPanel notificationsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        notificationsPanel.setBackground(new Color(245, 245, 245));
        notificationsCheckBox = new JCheckBox("Enable Notifications");
        notificationsCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));  
        notificationsCheckBox.setForeground(new Color(0, 51, 153));
        notificationsPanel.add(notificationsCheckBox);
        settingsPanel.add(notificationsPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton resetButton = new JButton("Reset to Default");
        resetButton.setFocusPainted(false);
        resetButton.setBackground(new Color(255, 69, 0)); 
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("Arial", Font.BOLD, 12));  
        resetButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetToDefault();
            }
        });

        JButton saveButton = new JButton("Save Settings");
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.setBackground(new Color(34, 139, 34));  
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 12));  
        saveButton.addActionListener(e -> saveSettings());

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setBackground(new Color(0, 51, 153));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 12)); 
        backButton.addActionListener(e -> {
            dispose();
            parentFrame.setVisible(true);
        });

        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 8)));  
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        buttonPanel.add(backButton);

        settingsPanel.add(buttonPanel);

        add(settingsPanel, BorderLayout.CENTER);

        languageComboBox.setSelectedItem(defaultLanguage);
        notificationsCheckBox.setSelected(defaultNotifications);
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        String themeText = isDarkMode ? "Switch to Light Mode" : "Switch to Dark Mode";
        themeToggleButton.setText(themeText);
        updateUITheme();
    }

    private void updateUITheme() {
        Color backgroundColor = isDarkMode ? new Color(30, 30, 30) : new Color(245, 245, 245);
        Color textColor = isDarkMode ? Color.WHITE : new Color(0, 51, 153);

        getContentPane().setBackground(backgroundColor);

        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                component.setBackground(backgroundColor);
            }
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(textColor);
            }
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setBackground(isDarkMode ? new Color(75, 75, 75) : new Color(0, 51, 153));
                button.setForeground(Color.WHITE);
            }
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.setForeground(textColor);
            }
        }
    }

    private void saveSettings() {
        String selectedLanguage = (String) languageComboBox.getSelectedItem();
        boolean notificationsEnabled = notificationsCheckBox.isSelected();

        JOptionPane.showMessageDialog(this,
                "Settings Saved:\n" + "Language: " + selectedLanguage + "\n" + "Notifications: " + (notificationsEnabled ? "Enabled" : "Disabled"),
                "Settings Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetToDefault() {
        languageComboBox.setSelectedItem(defaultLanguage);
        notificationsCheckBox.setSelected(defaultNotifications);
        themeToggleButton.setText("Switch to Dark Mode");
        isDarkMode = false;
        updateUITheme();

        JOptionPane.showMessageDialog(this, "Settings have been reset to default.",
                "Reset to Default", JOptionPane.INFORMATION_MESSAGE);
    }
}
