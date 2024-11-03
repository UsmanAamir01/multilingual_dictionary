package bl;

import dto.Word;
import java.util.List;

public interface IWordBO {
    boolean updateWord(Word word);
    boolean addWord(Word word);
    String[] scrapeWordAndUrduMeaning(String filePath);
    boolean removeWord(String arabicWord);
    String[][] getWordsWithMeanings();
    Word viewOnceWord(String arabicWord);
    List<Word> importDataFromFile(String filePath);
    boolean insertImportedData(List<Word> words);
    String searchWord(String searchTerm);
    String getMeanings(String searchText, String language);
    List<String> getTaggedAndStemmedWords();
    String getPOSTaggedWord(String arabicWord);
    String getStemmedWord(String arabicWord);
    String performPOSTagging(String arabicText) throws Exception;
}
