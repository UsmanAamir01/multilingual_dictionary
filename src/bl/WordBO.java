package bl;

import java.util.List;
import dal.IWordDAOFacade;
import dal.WordDAOFacade;
import dto.Word;

public class WordBO {
	private IWordDAOFacade wordDAOFacade;

	public WordBO() {
		wordDAOFacade = new WordDAOFacade();
	}

	public boolean updateWord(Word w) {
		return wordDAOFacade.updateWordToDB(w);
	}

	public boolean addWord(Word w) {
		return wordDAOFacade.addWordToDB(w);
	}

	public boolean removeWord(String arabicWord) {
		return wordDAOFacade.removeWordFromDB(arabicWord);
	}

	public String[][] getWordsWithMeanings() {
		List<Word> wordList = wordDAOFacade.getAllWords();
		String[][] wordData = new String[wordList.size()][3];

		for (int i = 0; i < wordList.size(); i++) {
			Word word = wordList.get(i);
			wordData[i][0] = word.getArabicWord();
			wordData[i][1] = word.getUrduMeaning();
			wordData[i][2] = word.getPersianMeaning();
		}

		return wordData;
	}

	public Word viewOnceWord(String arabicWord) {
		return wordDAOFacade.viewOnceWord(arabicWord);
	}

	public List<Word> importDataFromFile(String filePath) {
		return wordDAOFacade.importDataFromFile(filePath);
	}

	public boolean insertImportedData(List<Word> words) {
		return wordDAOFacade.insertImportedData(words);
	}

	 public String searchWord(String searchTerm) {
	        List<Word> results = wordDAOFacade.searchWord(searchTerm);
	        if (!results.isEmpty()) {
	            StringBuilder sb = new StringBuilder();
	            for (Word word : results) {
	                sb.append("Word: ").append(word.getArabicWord()).append(", Urdu Meaning: ")
	                    .append(word.getUrduMeaning()).append(", Persian Meaning: ")
	                    .append(word.getPersianMeaning()).append("\n");
	            }
	            return sb.toString();
	        }
	        return "Word not found.";
	    }
	 
	 public String getMeanings(String searchText, String language){
		String result = wordDAOFacade.getMeanings(searchText, language);
		return result;
		 
	 }
}