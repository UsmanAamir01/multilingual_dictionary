package dal.repository;

import java.util.List;

import dto.Word;

/**
 * Repository interface for Word entity persistence.
 * 
 * This follows the Repository pattern to abstract data access from business logic.
 * Implementations can use JDBC, JPA, or any other persistence mechanism.
 */
public interface IWordRepository {
    
    // ==================== CRUD Operations ====================
    
    /**
     * Find a word by its Arabic text.
     * @param arabicWord The Arabic word to find
     * @return The Word entity, or null if not found
     */
    Word findByArabicWord(String arabicWord);
    
    /**
     * Get meanings for a word from the database.
     * @param word The word to look up
     * @return Array with [urduMeaning, persianMeaning], values may be null
     */
    String[] getMeanings(String word);
    
    /**
     * Get all words from the database.
     * @return List of all words
     */
    List<Word> findAll();
    
    /**
     * Search for words matching a term.
     * @param searchTerm The term to search for
     * @return List of matching words
     */
    List<Word> search(String searchTerm);
    
    /**
     * Save a new word to the database.
     * @param word The word to save
     * @return true if successful
     */
    boolean save(Word word);
    
    /**
     * Update an existing word.
     * @param word The word with updated values
     * @return true if successful
     */
    boolean update(Word word);
    
    /**
     * Delete a word by its Arabic text.
     * @param arabicWord The Arabic word to delete
     * @return true if successful
     */
    boolean delete(String arabicWord);
    
    // ==================== Favorites ====================
    
    /**
     * Get all favorite words.
     * @return List of favorite words
     */
    List<Word> findFavorites();
    
    /**
     * Mark a word as favorite or not.
     * @param arabicWord The word to update
     * @param isFavorite The new favorite status
     */
    void setFavorite(String arabicWord, boolean isFavorite);
    
    /**
     * Check if a word is marked as favorite.
     * @param arabicWord The word to check
     * @return true if favorite
     */
    boolean isFavorite(String arabicWord);
    
    // ==================== Search History ====================
    
    /**
     * Add a word to search history.
     * @param word The word that was searched
     */
    void addToHistory(Word word);
    
    /**
     * Get recent search history.
     * @param limit Maximum number of results
     * @return List of recently searched words
     */
    List<Word> getRecentHistory(int limit);
    
    /**
     * Get recent search suggestions (words only).
     * @return List of recent search terms
     */
    List<String> getRecentSuggestions();
    
    // ==================== Bulk Operations ====================
    
    /**
     * Import words from a file.
     * @param filePath Path to the import file
     * @return List of imported words
     */
    List<Word> importFromFile(String filePath);
    
    /**
     * Insert multiple words (bulk insert).
     * @param words Words to insert
     * @return true if all successful
     */
    boolean insertAll(List<Word> words);
    
    // ==================== NLP/Processing ====================
    
    /**
     * Save processed word results (stem, root, POS).
     * @param word Original word
     * @param stem Stem
     * @param root Root
     * @param pos Part of speech
     */
    void saveProcessedWord(String word, String stem, String root, String pos);
    
    /**
     * Get meanings by language (cross-language lookup).
     * @param word The word to look up
     * @param language The source language (Arabic, Urdu, Persian)
     * @return Formatted meaning string
     */
    String getMeaningsByLanguage(String word, String language);
}
