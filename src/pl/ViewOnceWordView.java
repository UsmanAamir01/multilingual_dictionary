package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dto.Word;
import bl.IBLFacade;

public class ViewOnceWordView extends JFrame {
    private IBLFacade facade;
    private JTextField arabicWordField;
    private JButton viewButton, backButton;
    private JLabel headingLabel;
    private JLabel arabicWordLabel, urduMeaningLabel, persianMeaningLabel;
    private JFrame parentFrame;

    public ViewOnceWordView(IBLFacade facade, JFrame parentFrame) {
        this.facade = facade;
        this.parentFrame = parentFrame;

        setTitle("View Word Once");
        setSize(600,600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        headingLabel = createHeadingLabel("View a Word Once");
        mainPanel.add(headingLabel, BorderLayout.NORTH);

        JPanel inputPanel = createInputPanel();
        JPanel footerPanel = createFooterPanel();

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel promptLabel = new JLabel("Enter Arabic Word to View:");
        promptLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(promptLabel, gbc);

        arabicWordField = new JTextField(20);
        arabicWordField.setFont(new Font("Arial", Font.PLAIN, 14));
        arabicWordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridy = 1;
        inputPanel.add(arabicWordField, gbc);

        arabicWordLabel = createMeaningLabel("");
        urduMeaningLabel = createMeaningLabel("");
        persianMeaningLabel = createMeaningLabel("");

        gbc.gridy = 2;
        inputPanel.add(arabicWordLabel, gbc);

        gbc.gridy = 3;
        inputPanel.add(urduMeaningLabel, gbc);

        gbc.gridy = 4;
        inputPanel.add(persianMeaningLabel, gbc);

        return inputPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        footerPanel.setBackground(new Color(245, 245, 245));

        viewButton = createButton("View Once");
        backButton = createButton("Back");

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayWord();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                parentFrame.setVisible(true);
            }
        });

        footerPanel.add(viewButton);
        footerPanel.add(backButton);

        return footerPanel;
    }

    private void displayWord() {
        String arabicWord = arabicWordField.getText().trim();
        if (arabicWord.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Arabic word.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Word word = facade.viewOnceWord(arabicWord);
        if (word != null) {
            arabicWordLabel.setText("Arabic Word: " + word.getArabicWord());
            urduMeaningLabel.setText("Urdu Meaning: " + word.getUrduMeaning());
            persianMeaningLabel.setText("Persian Meaning: " + word.getPersianMeaning());
            arabicWordLabel.setBackground(new Color(224, 247, 218));
            urduMeaningLabel.setBackground(new Color(224, 247, 218));
            persianMeaningLabel.setBackground(new Color(224, 247, 218));
        } else {
            arabicWordLabel.setText("Word not found.");
            urduMeaningLabel.setText("");
            persianMeaningLabel.setText("");
            arabicWordLabel.setBackground(new Color(255, 228, 225));
            urduMeaningLabel.setBackground(new Color(255, 228, 225));
            persianMeaningLabel.setBackground(new Color(255, 228, 225));
        }
    }

    private JLabel createHeadingLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setForeground(new Color(25, 25, 112));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        return label;
    }

    private JLabel createMeaningLabel(String text) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(new Color(240, 248, 255));
        label.setForeground(Color.DARK_GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
