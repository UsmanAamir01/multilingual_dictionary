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

	List<String> getTaggedAndStemmedWords();

	LinkedList<?> getStemmedWord(String arabicWord);

	LinkedList<?> getPOSTaggedWord(String arabicWord);
//
//	String getFarsiMeaning(String word);
//
//	void updateFarsiMeaning(String word, String farsiMeaning);
//
//	String scrapeFarsiMeaning(String filePath);
//
//	void saveWordAndUrduMeaning(String word, String urduMeaning);
//
//	String[] scrapeWordAndUrduMeaning(String filePath);
	  String[] scrapeWordAndUrduMeaning(String filePath);
    void saveWordAndUrduMeaning(String word, String urduMeaning);
    String scrapeFarsiMeaning(String filePath);
    void updateFarsiMeaning(String word, String farsiMeaning);
    String getFarsiMeaning(String word);
}