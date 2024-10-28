package pl;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import bl.UserBO;
import bl.WordBO;

public class WordUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private WordBO wordBo;
    private JButton addWordButton;
    private JButton removeWordButton;
    private JButton updateWordButton;
    private JButton viewAllButton;
    private JButton importFileButton;
    private JButton closeButton;

    public WordUI(WordBO wordBo, UserBO userBo) {
        this.wordBo = wordBo;
        setTitle("Multilingual Dictionary");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(new Color(255, 255, 255));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Multilingual Dictionary", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(51, 51, 51));
        usernameField = new JTextField(15);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(51, 51, 51));
        passwordField = new JPasswordField(15);
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        loginButton = createButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        add(loginPanel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (userBo.validateUser(username, password)) {
                    JOptionPane.showMessageDialog(WordUI.this, "Login successful!");
                    showFunctionalityButtons();
                } else {
                    JOptionPane.showMessageDialog(WordUI.this, "Invalid credentials, please try again.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                button.setBackground(new Color(0, 102, 204));
                Timer timer = new Timer(200, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        button.setBackground(new Color(0, 123, 255));
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        return button;
    }

    private void showFunctionalityButtons() {
        getContentPane().removeAll();
        this.setTitle("Dashboard");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addWordButton = createButton("Add Word");
        removeWordButton = createButton("Remove Word");
        updateWordButton = createButton("Update Word");
        viewAllButton = createButton("View All");
        importFileButton = createButton("Import File");
        closeButton = createButton("Close");

        buttonPanel.add(addWordButton);
        buttonPanel.add(removeWordButton);
        buttonPanel.add(updateWordButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(importFileButton);
        buttonPanel.add(closeButton);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        addWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new AddWordView(wordBo, WordUI.this);
                WordUI.this.setVisible(false);
            }
        });

        removeWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new RemoveWordView(WordUI.this, wordBo);
                WordUI.this.setVisible(false);
            }
        });

        updateWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new UpdateWordView(WordUI.this, wordBo);
                WordUI.this.setVisible(false);
            }
        });

        viewAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new AllWordView(WordUI.this);
                WordUI.this.setVisible(false);
            }
        });

        importFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new DictionaryUI(wordBo, WordUI.this);
                WordUI.this.setVisible(false);
            }
        });

        add(buttonPanel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        WordBO wordBo = new WordBO();
        UserBO userBo = new UserBO();
        WordUI ui = new WordUI(wordBo, userBo);
        ui.setVisible(true);
    }
}