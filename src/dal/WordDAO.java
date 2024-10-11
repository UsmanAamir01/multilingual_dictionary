package dal;

import dto.Word;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WordDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/words";
    private static final String USER = "ranko"; // Database username
    private static final String PASS = "huzaifa123"; // Database password
    private static final Logger LOGGER = Logger.getLogger(WordDAO.class.getName());

    // Insert a word into the database
    public boolean addWordToDB(Word word) {
        String query = "INSERT INTO words (word, meaning1, meaning2) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, word.getWord());
            pstmt.setString(2, word.getMeaning1());
            pstmt.setString(3, word.getMeaning2());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding word to database: {0}", e.getMessage());
            return false;
        }
    }

    // Get a word from the database
    public Word getWordFromDB(String word) {
        String query = "SELECT * FROM words WHERE word = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, word);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Word(resultSet.getString("word"), resultSet.getString("meaning1"), resultSet.getString("meaning2"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving word: {0}", e.getMessage());
        }
        return null;
    }

    // Update a word in the database
    public boolean updateWordToDB(Word w) {
        String query = "UPDATE words SET meaning1 = ?, meaning2 = ? WHERE word = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, w.getMeaning1());
            statement.setString(2, w.getMeaning2());
            statement.setString(3, w.getWord());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating word: {0}", e.getMessage());
        }
        return false;
    }

    // Remove a word from the database
    public boolean removeWordFromDB(String word) {
        String query = "DELETE FROM words WHERE word = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, word);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing word: {0}", e.getMessage());
        }
        return false;
    }

    // Get all words from the database
    public List<Word> getAllWords() {
        String query = "SELECT word, meaning1, meaning2 FROM words";
        List<Word> wordList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                wordList.add(new Word(rs.getString("word"), rs.getString("meaning1"), rs.getString("meaning2")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all words: {0}", e.getMessage());
        }

        return wordList;
    }
}
