package testing;

import org.junit.jupiter.api.*;

import dal.WordDAO;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class WordDAOTest {

    private static Connection connection;
    private WordDAO wordDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize the DAO with the test database connection
        wordDAO = new WordDAO(connection);
        
        // Clean the tables before each test to ensure a clean state
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM arabic_word");
            stmt.executeUpdate("DELETE FROM persian_meaning");
        }
    }

    @Test
    void testUpdateFarsiMeaning() throws SQLException {
        // Insert a word with an initial Farsi meaning
        String word = "سلام";
        String initialMeaning = "Hello in Arabic";
        String updatedMeaning = "Peace in Arabic";
        
        // Insert the word into the arabic_word table
        String insertWordQuery = "INSERT INTO arabic_word (arabic_word) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertWordQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, word);
            stmt.executeUpdate();
            
            // Get the generated id of the arabic word
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int wordId = generatedKeys.getInt(1);

                // Insert initial Farsi meaning into the persian_meaning table
                String insertMeaningQuery = "INSERT INTO persian_meaning (id, persian_meaning) VALUES (?, ?)";
                try (PreparedStatement meaningStmt = connection.prepareStatement(insertMeaningQuery)) {
                    meaningStmt.setInt(1, wordId);
                    meaningStmt.setString(2, initialMeaning);
                    meaningStmt.executeUpdate();
                }
            }
        }

        // Update the Farsi meaning
        wordDAO.updateFarsiMeaning(word, updatedMeaning);

        // Verify the Farsi meaning was updated
        String verifyMeaningQuery = "SELECT persian_meaning FROM persian_meaning WHERE id = (SELECT id FROM arabic_word WHERE arabic_word = ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(verifyMeaningQuery)) {
            pstmt.setString(1, word);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                String updatedFetchedMeaning = resultSet.getString("persian_meaning");
                assertEquals(updatedMeaning, updatedFetchedMeaning, "The Farsi meaning should be updated.");
            }
        }
    }
}
