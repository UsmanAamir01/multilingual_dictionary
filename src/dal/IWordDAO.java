package dal;

import java.util.List;

import dto.Word;

public interface IWordDAO {
	String[] scrapeWordAndUrduMeaning(String filePath);
    
    String scrapeFarsiMeaning(String filePath);
    
    
	

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

	String getPOSTaggedWord(String arabicWord);

	String getStemmedWord(String arabicWord);

	Word getWordFromDB(String arabicWord);

	void saveWordAndUrduMeaning(String word, String urduMeaning);

	void updateFarsiMeaning(String word, String farsiMeaning);

	String getFarsiMeaning(String word);

	String scrapeUrduMeaning(String url, String wordId);

	String[] scrapeWordAndUrduMeaning1(String filePath);
}