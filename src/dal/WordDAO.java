package dal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import dto.Word;

public class WordDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/Dictionarydb";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final Logger LOGGER = Logger.getLogger(WordDAO.class.getName());

    public Word getWordFromDB(String word) {
        String query = "SELECT * FROM dictionary WHERE word = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, word);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String wordText = resultSet.getString("word");
                String meaning = resultSet.getString("meaning");
                return new Word(wordText, meaning);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
        }
        return null;
    }

    public boolean updateWordToDB(Word w) {
        String query = "UPDATE dictionary SET meaning = ? WHERE word = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, w.getMeaning());
            statement.setString(2, w.getWord());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
        }
        return false;
    }

    public boolean addWordToDB(Word w) {
        String query = "INSERT INTO dictionary (word, meaning) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, w.getWord());
            statement.setString(2, w.getMeaning());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
        }
        return false;
    }

    public boolean removeWordFromDB(String word) {
        String query = "DELETE FROM dictionary WHERE word = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, word);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
        }
        return false;
    }

    public List<Word> getAllWords() {
        List<Word> wordList = new ArrayList<>();
        String query = "SELECT word, meaning FROM dictionary";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                wordList.add(new Word(rs.getString("word"), rs.getString("meaning")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
        }
        return wordList;
    }

    public List<Word> importDataFromFile(String filePath) {
        List<Word> importedWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    importedWords.add(new Word(parts[0].trim(), parts[1].trim()));
                } else {
                    LOGGER.log(Level.WARNING, "Invalid line format: {0}", line);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file", e);
        }
        return importedWords;
    }

    public boolean insertImportedData(List<Word> words) {
        for (Word word : words) {
            if (!addWordToDB(word)) {
                LOGGER.log(Level.SEVERE, "Failed to add word: {0}", word.getWord());
                return false;
            }
        }
        return true;
    }
}
