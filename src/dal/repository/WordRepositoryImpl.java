package dal.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import dal.pool.ConnectionPoolManager;
import dto.Word;

/**
 * JDBC implementation of IWordRepository using connection pooling.
 * 
 * This class replaces direct JDBC usage in WordDAO with pooled connections.
 * All database operations properly release connections back to the pool.
 */
public class WordRepositoryImpl implements IWordRepository {
    private static final Logger LOGGER = Logger.getLogger(WordRepositoryImpl.class.getName());
    
    // ==================== CRUD Operations ====================
    
    @Override
    public Word findByArabicWord(String arabicWord) {
        if (arabicWord == null) {
            throw new IllegalArgumentException("Arabic word cannot be null");
        }
        
        String query = "SELECT aw.arabic_word, aw.isFavorite, um.urdu_meaning, pm.persian_meaning " +
                       "FROM arabic_word aw " +
                       "LEFT JOIN urdu_meaning um ON aw.id = um.id " +
                       "LEFT JOIN persian_meaning pm ON aw.id = pm.id " +
                       "WHERE aw.arabic_word = ?";
        
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, arabicWord);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToWord(rs);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding word: " + arabicWord, e);
        } finally {
            ConnectionPoolManager.releaseConnection(conn);
        }
        return null;
    }
    
    @Override
    public String[] getMeanings(String word) {
        String[] meanings = new String[2];
        String query = "SELECT um.urdu_meaning, pm.persian_meaning " +
                       "FROM arabic_word aw " +
                       "LEFT JOIN urdu_meaning um ON aw.id = um.id " +
                       "LEFT JOIN persian_meaning pm ON aw.id = pm.id " +
                       "WHERE aw.arabic_word = ?";
        
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, word);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        meanings[0] = rs.getString("urdu_meaning");
                        meanings[1] = rs.getString("persian_meaning");
                    }
                }
            }
            LOGGER.log(Level.FINE, "Retrieved meanings for word: {0}", word);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting meanings for: " + word, e);
        } finally {
            ConnectionPoolManager.releaseConnection(conn);
        }
        return meanings;
    }
    
    @Override
    public List<Word> findAll() {
        List<Word> words = new ArrayList<>();
        String query = "SELECT aw.arabic_word, um.urdu_meaning, pm.persian_meaning, aw.isFavorite " +
                       "FROM arabic_word aw " +
                       "LEFT JOIN urdu_meaning um ON aw.id = um.id " +
                       "LEFT JOIN persian_meaning pm ON aw.id = pm.id";
        
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    words.add(mapResultSetToWord(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all words", e);
        } finally {
            ConnectionPoolManager.releaseConnection(conn);
        }
        return words;
    }
    
    @Override
    public List<Word> search(String searchTerm) {
        List<Word> results = new ArrayList<>();
        String query = "SELECT aw.arabic_word, aw.isFavorite, um.urdu_meaning, pm.persian_meaning " +
                       "FROM arabic_word aw " +
                       "LEFT JOIN urdu_meaning um ON aw.id = um.id " +
                       "LEFT JOIN persian_meaning pm ON aw.id = pm.id " +
                       "WHERE aw.arabic_word LIKE ? OR um.urdu_meaning LIKE ? OR pm.persian_meaning LIKE ?";
        
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                String pattern = "%" + searchTerm + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
                stmt.setString(3, pattern);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        results.add(mapResultSetToWord(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching for: " + searchTerm, e);
        } finally {
            ConnectionPoolManager.releaseConnection(conn);
        }
        return results;
    }
    
    @Override
    public boolean save(Word word) {
        String insertWord = "INSERT INTO arabic_word (arabic_word) VALUES (?)";
        String insertUrdu = "INSERT INTO urdu_meaning (id, urdu_meaning) VALUES ((SELECT id FROM arabic_word WHERE arabic_word = ?), ?)";
        String insertPersian = "INSERT INTO persian_meaning (id, persian_meaning) VALUES ((SELECT id FROM arabic_word WHERE arabic_word = ?), ?)";
        
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmtWord = conn.prepareStatement(insertWord);
                 PreparedStatement stmtUrdu = conn.prepareStatement(insertUrdu);
                 PreparedStatement stmtPersian = conn.prepareStatement(insertPersian)) {
                
                // Insert Arabic word
                stmtWord.setString(1, word.getArabicWord());
                stmtWord.executeUpdate();
                
                // Insert Urdu meaning
                stmtUrdu.setString(1, word.getArabicWord());
                stmtUrdu.setString(2, word.getUrduMeaning());
                stmtUrdu.executeUpdate();
                
                // Insert Persian meaning
                stmtPersian.setString(1, word.getArabicWord());
                stmtPersian.setString(2, word.getPersianMeaning());
                stmtPersian.executeUpdate();
                
                conn.commit();
                LOGGER.log(Level.INFO, "Saved word: {0}", word.getArabicWord());
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving word: " + word.getArabicWord(), e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed", ex);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Failed to reset autocommit", e);
                }
            }
            ConnectionPoolManager.releaseConnection(conn);
        }
        return false;
    }
    
    @Override
    public boolean update(Word word) {
        String updateUrdu = "UPDATE urdu_meaning SET urdu_meaning = ? WHERE id = (SELECT id FROM arabic_word WHERE arabic_word = ?)";
        String updatePersian = "UPDATE persian_meaning SET persian_meaning = ? WHERE id = (SELECT id FROM arabic_word WHERE arabic_word = ?)";
        
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmtUrdu = conn.prepareStatement(updateUrdu);
                 PreparedStatement stmtPersian = conn.prepareStatement(updatePersian)) {
                
                stmtUrdu.setString(1, word.getUrduMeaning());
                stmtUrdu.setString(2, word.getArabicWord());
                
                stmtPersian.setString(1, word.getPersianMeaning());
                stmtPersian.setString(2, word.getArabicWord());
                
                int urduResult = stmtUrdu.executeUpdate();
                int persianResult = stmtPersian.executeUpdate();
                
                conn.commit();
                return urduResult > 0 && persianResult > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating word: " + word.getArabicWord(), e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed", ex);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Failed to reset autocommit", e);
                }
            }
            ConnectionPoolManager.releaseConnection(conn);
        }
        return false;
    }
    
    @Override
    public boolean delete(String arabicWord) {
        String deleteUrdu = "DELETE FROM urdu_meaning WHERE id = (SELECT id FROM arabic_word WHERE arabic_word = ?)";
        String deletePersian = "DELETE FROM persian_meaning WHERE id = (SELECT id FROM arabic_word WHERE arabic_word = ?)";
        String deleteWord = "DELETE FROM arabic_word WHERE arabic_word = ?";
        
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmtUrdu = conn.prepareStatement(deleteUrdu);
                 PreparedStatement stmtPersian = conn.prepareStatement(deletePersian);
                 PreparedStatement stmtWord = conn.prepareStatement(deleteWord)) {
                
                stmtUrdu.setString(1, arabicWord);
                stmtUrdu.executeUpdate();
                
                stmtPersian.setString(1, arabicWord);
                stmtPersian.executeUpdate();
                
                stmtWord.setString(1, arabicWord);
                int result = stmtWord.executeUpdate();
                
                conn.commit();
                return result > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting word: " + arabicWord, e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed", ex);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Failed to reset autocommit", e);
                }
            }
            ConnectionPoolManager.releaseConnection(conn);
        }
        return false;
    }
    
    // ==================== Favorites ====================
    
    @Override
    public List<Word> findFavorites() {
        List<Word> favorites = new ArrayList<>();
        String query = "SELECT aw.arabic_word, aw.isFavorite, um.urdu_meaning, pm.persian_meaning " +
                       "FROM arabic_word aw " +
                       "LEFT JOIN urdu_meaning um ON aw.id = um.id " +
                       "LEFT JOIN persian_meaning pm ON aw.id = pm.id " +
                       "WHERE aw.isFavorite = TRUE";
        
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    favorites.add(mapResultSetToWord(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching favorites", e);
        } finally {
            ConnectionPoolManager.releaseConnection(conn);
        }
        return favorites;
    }
    
    @Override
    public void setFavorite(String arabicWord, boolean isFavorite) {
        if (arabicWord == null) {
            throw new NullPointerException("Arabic word cannot be null");
        }
        
        String query = "UPDATE arabic_word SET isFavorite = ? WHERE arabic_word = ?";
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setBoolean(1, isFavorite);
                stmt.setString(2, arabicWord);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating favorite: " + arabicWord, e);
        } finally {
            ConnectionPoolManager.releaseConnection(conn);
        }
    }
    
    @Override
    public boolean isFavorite(String arabicWord) {
        String query = "SELECT isFavorite FROM arabic_word WHERE arabic_word = ?";
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, arabicWord);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBoolean("isFavorite");
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking favorite: " + arabicWord, e);
        } finally {
            ConnectionPoolManager.releaseConnection(conn);
        }
        return false;
    }
    
    // ==================== Search History ====================
    
    @Override
    public void addToHistory(Word word) {
        String query = "INSERT INTO searchhistory (arabic_word, persian_meaning, urdu_meaning) VALUES (?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, word.getArabicWord());
                stmt.setString(2, word.getPersianMeaning());
                stmt.setString(3, word.getUrduMeaning());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding to history: " + word.getArabicWord(), e);
        } finally {
            ConnectionPoolManager.releaseConnection(conn);
        }
    }
    
    @Override
    public List<Word> getRecentHistory(int limit) {
        List<Word> history = new ArrayList<>();
        String query = "SELECT arabic_word, persian_meaning, urdu_meaning FROM searchhistory ORDER BY id DESC LIMIT ?";
        
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, limit);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        history.add(new Word(
                            rs.getString("arabic_word"),
                            rs.getString("persian_meaning"),
                            rs.getString("urdu_meaning")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting history", e);
        } finally {
            ConnectionPoolManager.releaseConnection(conn);
        }
        return history;
    }
    
    @Override
    public List<String> getRecentSuggestions() {
        List<String> suggestions = new ArrayList<>();
        String query = "SELECT arabic_word FROM searchhistory ORDER BY search_time DESC LIMIT 5";
        
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    suggestions.add(rs.getString("arabic_word"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting suggestions", e);
        } finally {
            ConnectionPoolManager.releaseConnection(conn);
        }
        return suggestions;
    }
    
    // ==================== Bulk Operations ====================
    
    @Override
    public List<Word> importFromFile(String filePath) {
        List<Word> imported = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    imported.add(new Word(parts[0].trim(), parts[1].trim(), parts[2].trim(), false));
                } else {
                    LOGGER.log(Level.WARNING, "Invalid line format: {0}", line);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file: " + filePath, e);
        }
        return imported;
    }
    
    @Override
    public boolean insertAll(List<Word> words) {
        for (Word word : words) {
            if (!save(word)) {
                LOGGER.log(Level.SEVERE, "Failed to insert word: {0}", word.getArabicWord());
                return false;
            }
        }
        return true;
    }
    
    // ==================== NLP/Processing ====================
    
    @Override
    public void saveProcessedWord(String word, String stem, String root, String pos) {
        String query = "INSERT INTO processed_words (original_word, stem, root, pos) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, word);
                stmt.setString(2, stem);
                stmt.setString(3, root);
                stmt.setString(4, pos);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving processed word: " + word, e);
        } finally {
            ConnectionPoolManager.releaseConnection(conn);
        }
    }
    
    @Override
    public String getMeaningsByLanguage(String word, String language) {
        StringBuilder meanings = new StringBuilder();
        String query;
        
        if ("Urdu".equalsIgnoreCase(language)) {
            query = "SELECT aw.arabic_word, pm.persian_meaning FROM arabic_word aw " +
                    "LEFT JOIN persian_meaning pm ON aw.id = pm.id " +
                    "LEFT JOIN urdu_meaning um ON aw.id = um.id WHERE um.urdu_meaning = ?";
        } else if ("Persian".equalsIgnoreCase(language)) {
            query = "SELECT aw.arabic_word, um.urdu_meaning FROM arabic_word aw " +
                    "LEFT JOIN urdu_meaning um ON aw.id = um.id " +
                    "LEFT JOIN persian_meaning pm ON aw.id = pm.id WHERE pm.persian_meaning = ?";
        } else if ("Arabic".equalsIgnoreCase(language)) {
            query = "SELECT um.urdu_meaning, pm.persian_meaning FROM arabic_word aw " +
                    "LEFT JOIN urdu_meaning um ON aw.id = um.id " +
                    "LEFT JOIN persian_meaning pm ON aw.id = pm.id WHERE aw.arabic_word = ?";
        } else {
            return "Invalid language selection.";
        }
        
        Connection conn = null;
        try {
            conn = ConnectionPoolManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, word);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        if ("Urdu".equalsIgnoreCase(language)) {
                            meanings.append("Arabic: ").append(rs.getString("arabic_word")).append("\n");
                            meanings.append("Persian: ").append(rs.getString("persian_meaning")).append("\n");
                        } else if ("Persian".equalsIgnoreCase(language)) {
                            meanings.append("Arabic: ").append(rs.getString("arabic_word")).append("\n");
                            meanings.append("Urdu: ").append(rs.getString("urdu_meaning")).append("\n");
                        } else if ("Arabic".equalsIgnoreCase(language)) {
                            meanings.append("Urdu: ").append(rs.getString("urdu_meaning")).append("\n");
                            meanings.append("Persian: ").append(rs.getString("persian_meaning")).append("\n");
                        }
                    } else {
                        meanings.append("Word not found.");
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting meanings by language", e);
            meanings.append("Error retrieving meanings.");
        } finally {
            ConnectionPoolManager.releaseConnection(conn);
        }
        return meanings.toString();
    }
    
    // ==================== Helper Methods ====================
    
    private Word mapResultSetToWord(ResultSet rs) throws SQLException {
        return new Word(
            rs.getString("arabic_word"),
            rs.getString("urdu_meaning"),
            rs.getString("persian_meaning"),
            rs.getBoolean("isFavorite")
        );
    }
}
