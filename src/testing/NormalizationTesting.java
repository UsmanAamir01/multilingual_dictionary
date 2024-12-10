package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dal.WordDAO;

public class NormalizationTesting {
    private static final String URL = "jdbc:mysql://localhost:3306/Dictionarydb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private WordDAO dao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        dao = new WordDAO(connection);

        String deleteQuery = "DELETE FROM processed_words";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deleteQuery);
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        String deleteQuery = "DELETE FROM processed_words";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deleteQuery);
        }

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testSaveResults_EnglishWord() throws Exception {
        String word = "test";
        String stem = "test";
        String root = "test";
        String pos = "NOUN";

        dao.saveResults(word, stem, root, pos);

        assertWordExists(word, stem, root, pos);
    }

    @Test
    public void testSaveResults_ArabicWord() throws Exception {
        String word = "سلام";
        String stem = "سل";
        String root = "سلم";
        String pos = "VERB";

        dao.saveResults(word, stem, root, pos);

        assertWordExists(word, stem, root, pos);
    }

    @Test
    public void testSaveResults_UrduWord() throws Exception {
        String word = "کتاب";
        String stem = "کت";
        String root = "کتب";
        String pos = "NOUN";

        dao.saveResults(word, stem, root, pos);

        assertWordExists(word, stem, root, pos);
    }

    @Test
    public void testSaveResults_PersianWord() throws Exception {
        String word = "دوست";
        String stem = "دو";
        String root = "دوست";
        String pos = "NOUN";

        dao.saveResults(word, stem, root, pos);

        assertWordExists(word, stem, root, pos);
    }

    @Test
    public void testSaveResults_NullValues() throws Exception {
        String word = "nullTest";
        String stem = null;
        String root = null;
        String pos = null;

        dao.saveResults(word, stem, root, pos);

        String selectQuery = "SELECT * FROM processed_words WHERE original_word = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
            stmt.setString(1, word);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(word, rs.getString("original_word"));
                assertNull(rs.getString("stem"));
                assertNull(rs.getString("root"));
                assertNull(rs.getString("pos"));
            }
        }
    }

    private void assertWordExists(String word, String stem, String root, String pos) throws SQLException {
        String selectQuery = "SELECT * FROM processed_words WHERE original_word = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
            stmt.setString(1, word);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(word, rs.getString("original_word"));
                assertEquals(stem, rs.getString("stem"));
                assertEquals(root, rs.getString("root"));
                assertEquals(pos, rs.getString("pos"));
            }
        }
    }
}
