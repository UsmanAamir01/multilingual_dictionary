package pl;

import javax.swing.*;
import java.awt.*;

public class InstructionsView extends JFrame {

    public InstructionsView(JFrame parentFrame) {
        setTitle("Instructions");
        setSize(600, 450);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 51, 153)); 
        JLabel headerLabel = new JLabel("Application Instructions", SwingConstants.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        headerPanel.add(headerLabel);

        JTextArea instructionsArea = new JTextArea(10, 40);
        instructionsArea.setText("Welcome to the Multilingual Dictionary!\n\n" +
                "Here are the instructions on how to use the application:\n\n" +
                "1. Use the sidebar to navigate through different sections such as Add Word, View All Words, and more.\n" +
                "2. To search for a word, select the language from the dropdown and enter the word in the search field.\n" +
                "3. You can toggle between light and dark themes by clicking the theme icon at the top right.\n" +
                "4. The application also allows you to import files, view favorites, and manage custom dictionaries.\n" +
                "5. For word normalization and segmentation, use the respective options from the sidebar.\n\n" +
                "If you encounter any issues, please refer to the FAQs or contact support.");
        instructionsArea.setEditable(false);
        instructionsArea.setFont(new Font("Georgia", Font.PLAIN, 14));
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(instructionsArea);
        scrollPane.setPreferredSize(new Dimension(550, 250));

        JButton backButton = new JButton("Dashboard");
        backButton.setFont(new Font("Georgia", Font.BOLD, 14));
        backButton.setBackground(new Color(0, 51, 153));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose(); 
            parentFrame.setVisible(true); 
        });

        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
}
