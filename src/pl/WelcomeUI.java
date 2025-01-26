package pl;

import java.awt.*;

import javax.swing.*;

import bl.BLFacade;
import bl.IBLFacade;
import bl.UserBO;
import bl.WordBO;

public class WelcomeUI extends JFrame {

    public WelcomeUI(IBLFacade facade, UserBO userBo) {
        setTitle("Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Home", createHomePanel(facade, userBo));
        tabbedPane.addTab("About", createAboutPanel());
        tabbedPane.addTab("Contact", createContactPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createHomePanel(IBLFacade facade, UserBO userBo) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon("images/dictionary_logo.jpeg").getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(imageLabel, gbc);

        JLabel titleLabel = new JLabel("Welcome to Multilingual Dictionary", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JLabel taglineLabel = new JLabel("Your one-stop solution for language learning!", SwingConstants.CENTER);
        taglineLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
        taglineLabel.setForeground(new Color(47, 79, 79));
        gbc.gridy = 2;
        mainPanel.add(taglineLabel, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton loginButton = createButton("Login", new Color(0, 51, 153), Color.WHITE);
        loginButton.addActionListener(event -> navigateToLogin(facade, userBo));
        mainPanel.add(loginButton, gbc);

        JLabel footerLabel = new JLabel("© 2025 Multilingual Dictionary, All Rights Reserved", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.GRAY);
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(footerLabel, gbc);

        return mainPanel;
    }

    private JPanel createAboutPanel() {
        JPanel aboutPanel = new JPanel(new BorderLayout());
        aboutPanel.setBackground(new Color(240, 248, 255));

        JLabel headerLabel = new JLabel("About Us", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(25, 25, 112));
        aboutPanel.add(headerLabel, BorderLayout.NORTH);

        JTextArea aboutText = new JTextArea(
                "Multilingual Dictionary was established in 2002 to bridge the language gap and promote understanding across cultures.\n\n" +
                "Supported Languages:\n" +
                "- Urdu\n" +
                "- Arabic\n" +
                "- English\n\n" +
                "Features:\n" +
                "- Translate words in multiple languages.\n" +
                "- User-friendly interface.\n" +
                "- Powerful search and learning tools.\n\n" +
                "Built with passion for language enthusiasts worldwide, we strive to provide a comprehensive and reliable language resource."
        );
        aboutText.setFont(new Font("Arial", Font.PLAIN, 16));
        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setBackground(new Color(240, 248, 255));

        JScrollPane scrollPane = new JScrollPane(aboutText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        aboutPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel footerLabel = new JLabel("Thank you for choosing Multilingual Dictionary!", SwingConstants.CENTER);
        footerLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        footerLabel.setForeground(new Color(47, 79, 79));
        aboutPanel.add(footerLabel, BorderLayout.SOUTH);

        return aboutPanel;
    }

    private JPanel createContactPanel() {
        JPanel contactPanel = new JPanel(new BorderLayout());
        contactPanel.setBackground(new Color(240, 248, 255));
        contactPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Contact Us", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(25, 25, 112));
        contactPanel.add(headerLabel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 248, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea contactInfo = new JTextArea(
                "Get in Touch:\n" +
                "Email: support@multilingualdictionary.com\n" +
                "Phone: +1 (800) 123-4567\n" +
                "Website: www.multilingualdictionary.com\n"
        );
        contactInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        contactInfo.setEditable(false);
        contactInfo.setBackground(new Color(240, 248, 255));
        contactInfo.setLineWrap(true);
        contactInfo.setWrapStyleWord(true);
        contactInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        contentPanel.add(contactInfo);

        JLabel followUsLabel = new JLabel("Follow Us:");
        followUsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        followUsLabel.setForeground(new Color(25, 25, 112));
        followUsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        contentPanel.add(followUsLabel);

        JPanel socialMediaPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        socialMediaPanel.setBackground(new Color(240, 248, 255));

        JLabel facebookLabel = new JLabel("Facebook: fb.com/multilingualdictionary");
        facebookLabel.setIcon(new ImageIcon(new ImageIcon("images/facebook_logo.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        facebookLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        facebookLabel.setForeground(new Color(47, 79, 79));
        facebookLabel.setIconTextGap(10);
        socialMediaPanel.add(facebookLabel);

        JLabel twitterLabel = new JLabel("Twitter: @multilangdict");
        twitterLabel.setIcon(new ImageIcon(new ImageIcon("images/twitter_logo.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        twitterLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        twitterLabel.setForeground(new Color(47, 79, 79));
        twitterLabel.setIconTextGap(10);
        socialMediaPanel.add(twitterLabel);

        JLabel instagramLabel = new JLabel("Instagram: @multilangdict");
        instagramLabel.setIcon(new ImageIcon(new ImageIcon("images/instagram_logo.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        instagramLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instagramLabel.setForeground(new Color(47, 79, 79));
        instagramLabel.setIconTextGap(10);
        socialMediaPanel.add(instagramLabel);

        contentPanel.add(socialMediaPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contactPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel subscriptionPanel = new JPanel(new GridBagLayout());
        subscriptionPanel.setBackground(new Color(240, 248, 255));
        subscriptionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel subscriptionLabel = new JLabel("Subscribe to our monthly newsletter:");
        subscriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        subscriptionLabel.setForeground(new Color(25, 25, 112));

        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton subscribeButton = new JButton("Subscribe");
        subscribeButton.setFont(new Font("Arial", Font.BOLD, 14));
        subscribeButton.setBackground(new Color(0, 51, 153));
        subscribeButton.setForeground(Color.WHITE);
        subscribeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        subscriptionPanel.add(subscriptionLabel, gbc);

        gbc.gridx = 1;
        subscriptionPanel.add(emailField, gbc);

        gbc.gridx = 2;
        subscriptionPanel.add(subscribeButton, gbc);

        contactPanel.add(subscriptionPanel, BorderLayout.SOUTH);

        return contactPanel;
    }


    private JButton createButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(backgroundColor.darker(), 2));
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        return button;
    }

    private void navigateToLogin(IBLFacade facade, UserBO userBo) {
        getContentPane().removeAll();
        add(new LoginUI(facade, userBo));
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        UserBO userBO = new UserBO();
        WordBO wordBO = new WordBO();
        IBLFacade facade = new BLFacade(wordBO, userBO);

        new WelcomeUI(facade, userBO);
    }
}
