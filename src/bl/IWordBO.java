package bl;

import dto.Word;

import java.util.LinkedList;
import java.util.List;

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
}