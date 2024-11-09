package pl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import bl.IBLFacade;

public class ArabicTaggerUI extends JFrame {
    private final IBLFacade facade;
    private JTextField arabicWordTextField;
    private JTextArea outputTextArea;
    private JButton tagButton;
    private JButton backButton;
    private JFrame mainDashboard;

    public ArabicTaggerUI(IBLFacade facade, JFrame mainDashboard) {
        this.facade = facade;
        this.mainDashboard = mainDashboard;
        setTitle("Arabic POS Tagger");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);

        backButton = createStyledButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(backButton, gbc);

        JLabel label = new JLabel("Enter Arabic Word:");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(50, 50, 50));
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(label, gbc);

        arabicWordTextField = new JTextField(30);
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        inputPanel.add(arabicWordTextField, gbc);

        tagButton = new JButton("Tag");
        tagButton.setBackground(new Color(30, 144, 255));
        tagButton.setForeground(Color.WHITE);
        tagButton.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(tagButton, gbc);

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        outputTextArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("POS Tagged Result"));

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        tagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String arabicText = arabicWordTextField.getText().trim();
                if (!arabicText.isEmpty()) {
                    try {
                        String posTaggedResult = facade.performPOSTagging(arabicText);
                        outputTextArea.setText(formatPosTaggedResult(posTaggedResult));
                    } catch (Exception ex) {
                        outputTextArea.setText("Error during POS tagging: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(ArabicTaggerUI.this, "Please enter Arabic Word.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArabicTaggerUI.this.setVisible(false);
                mainDashboard.setVisible(true);
            }
        });
    }

    private String formatPosTaggedResult(String result) {
        return result.replaceAll(",", "\n");
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }
}
