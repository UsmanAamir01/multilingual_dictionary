package dal;

import java.util.List;

import dto.Word;

public interface IWordDAOFacade extends IWordDAO {
	 String[] scrapeWordAndUrduMeaning(String filePath);
	    
	    void saveWordAndUrduMeaning(String word, String urduMeaning);
	    
	    String scrapeFarsiMeaning(String filePath);
	    
	    void updateFarsiMeaning(String word, String farsiMeaning);
	    
	    String getFarsiMeaning(String word);
	@Override
	Word getWordFromDB(String arabicWord);

	@Override
	boolean updateWordToDB(Word w);

	@Override
	boolean addWordToDB(Word w);

	@Override
	boolean removeWordFromDB(String arabicWord);

	@Override
	List<Word> getAllWords();

	@Override
	Word viewOnceWord(String arabicWord);

	@Override
	List<Word> importDataFromFile(String filePath);

	@Override
	boolean insertImportedData(List<Word> words);

	@Override
	List<Word> searchWord(String searchTerm);

	@Override
	String getMeanings(String searchText, String language);

	@Override
	List<String> getTaggedAndStemmedWords();

	@Override
	String getPOSTaggedWord(String arabicWord);

	@Override
	String getStemmedWord(String arabicWord);

	
}