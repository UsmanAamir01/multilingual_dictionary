package dal;

import java.util.LinkedList;
import java.util.List;

import dto.Word;

public interface IWordDAO {
	Word getWordFromDB(String arabicWord);

	boolean updateWordToDB(Word w);

	boolean addWordToDB(Word w);

	boolean removeWordFromDB(String arabicWord);

	List<Word> getAllWords();

	Word viewOnceWord(String arabicWord);

	List<Word> importDataFromFile(String filePath);

	boolean insertImportedData(List<Word> words);

	List<Word> searchWord(String searchTerm);

	String getMeanings(String searchText, String language);

	public String fetchArabicWord() throws Exception;

	public void saveResults(String word, String stem, String root, String pos) throws Exception;

	String[] scrapeWordAndUrduMeaning(String url);

	void saveWordAndUrduMeaning(String word, String urduMeaning);

	String scrapeFarsiMeaning(String filePath);

	void updateFarsiMeaning(String word, String farsiMeaning);

	String getFarsiMeaning(String word);

	void markAsFavorite(String arabicWord, boolean isFavorite);

	List<Word> getFavoriteWords();
	
	String[] getMeaningsFromDB(String word);

	boolean isWordFavorite(String arabicWord);

	void addSearchToHistory(Word word);

	List<Word> getRecentSearchHistory(int limit);

	List<String> segmentWordWithDiacritics(String word);

	List<String> getRecentSearchSuggestions();
	
}