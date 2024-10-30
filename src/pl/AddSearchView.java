package pl;

import bl.WordBO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddSearchView extends JPanel {
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JComboBox<String> searchTypeComboBox;
    private WordBO wordBo;

    public AddSearchView(WordBO wordBo) {
        this.wordBo = wordBo;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        String[] searchTypes = { "Key", "Value" };
        searchTypeComboBox = new JComboBox<>(searchTypes);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(searchTypeComboBox, gbc);

        JLabel searchLabel = new JLabel("Search Word:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        add(searchLabel, gbc);

        searchField = new JTextField(15);
        gbc.gridx = 2;
        add(searchField, gbc);

        searchButton = new JButton("Search");
        gbc.gridx = 3;
        add(searchButton, gbc);

        addButton = new JButton("Add Word");
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(addButton, gbc);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = searchField.getText();
                String searchType = (String) searchTypeComboBox.getSelectedItem();
                JOptionPane.showMessageDialog(AddSearchView.this, "Searching for: " + word + " by " + searchType);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = searchField.getText();
                JOptionPane.showMessageDialog(AddSearchView.this, "Adding word: " + word);
            }
        });
    }
}
