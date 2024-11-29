package bl;

import java.util.LinkedList;
import java.util.List;

import dto.Word;

public interface IWordBO {
	boolean updateWord(Word word);

	boolean addWord(Word word);

	boolean removeWord(String arabicWord);

	String[][] getWordsWithMeanings();

	Word viewOnceWord(String arabicWord);

	List<Word> importDataFromFile(String filePath);

	boolean insertImportedData(List<Word> words);

	String searchWord(String searchTerm);

	String getMeanings(String searchText, String language);

	String processWord(String arabicText) throws Exception;

	void saveResults(String word, String stem, String root, String pos) throws Exception;

	String[] saveWordAndUrduMeaning(String filePath);

	void saveFarsiMeaning(String word, String filePath);

	String getFarsiMeaning(String word);

	List<Word> getFavoriteWords();

	void markWordAsFavorite(String word, boolean isFavorite);

	boolean isWordFavorite(String arabicWord);

	void addSearchToHistory(Word word);

	List<Word> getRecentSearchHistory(int limit);

	List<String> getSegmentedWordsWithDiacritics(String word);

	List<String> getRecentSearchSuggestions();
	
	String[] getMeaningsFromDB(String word);
	public String[] getMeaning1(String word) throws Exception;
}