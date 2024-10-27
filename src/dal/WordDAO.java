package dal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                String word2 = resultSet.getString("word");
                String meaning = resultSet.getString("meaning");
                return new Word(word2, meaning);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateWordToDB(Word w) {
        String query = "UPDATE dictionary SET meaning = ? WHERE word = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, w.getMeaning());
            statement.setString(2, w.getWord());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addWordToDB(Word w) {
        String query = "INSERT INTO dictionary (word, meaning) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, w.getWord());
            statement.setString(2, w.getMeaning());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeWordFromDB(String word) {
        String query = "DELETE FROM dictionary WHERE word = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, word);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Word> getAllWords() {
        String query = "SELECT word, meaning FROM dictionary";
        List<Word> wordList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String word = rs.getString("word");
                String meaning = rs.getString("meaning");
                wordList.add(new Word(word, meaning));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wordList;
    }

    public List<Word> importDataFromFile(String filePath) {
        List<Word> importedWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split(",");
                if (parts.length >= 2) { 
                    String wordText = parts[0].trim();
                    String meaningText = parts[1].trim();
                    Word newWord = new Word(wordText, meaningText);
                    importedWords.add(newWord);
                } else {
                    LOGGER.log(Level.WARNING, "Invalid line format: {0}", line);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file: {0}", e.getMessage());
        }
        return importedWords;
    }

    public boolean insertImportedData(List<Word> words) {
        boolean allInserted = true;
        for (Word word : words) {
            if (!addWordToDB(word)) {
                allInserted = false;
                LOGGER.log(Level.SEVERE, "Failed to add word: {0}", word.getWord());
            }
        }
        return allInserted;
    }
}
