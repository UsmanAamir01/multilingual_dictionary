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

	List<String> getTaggedAndStemmedWords();

	LinkedList<?> getPOSTaggedWord(String arabicWord);

	LinkedList<?> getStemmedWord(String arabicWord);

	String performPOSTagging(String arabicText) throws Exception;

	String[] saveWordAndUrduMeaning(String filePath);

	void saveFarsiMeaning(String word, String filePath);

	String getFarsiMeaning(String word);

	List<Word> getFavoriteWords();

	void markWordAsFavorite(String word, boolean isFavorite);

	boolean isWordFavorite(String arabicWord);

	void addSearchToHistory(Word word);

	List<Word> getRecentSearchHistory(int limit);

	List<String> getAllLemmaztizedWords();

	boolean insertLemmatizedWord(String originalWord, String lemmatizedWord);

	String getLemmatizedWord(String originalWord);

	List<String> segmentWords(String input);

	