package dal;

import java.util.List;
import java.util.stream.Collectors;

import dto.Word;

public  class WordDAOFacade implements IWordDAOFacade {
	private final IWordDAO wordDAO;

	public WordDAOFacade(IWordDAO wordDAO) {
		this.wordDAO = wordDAO;
	}

	public WordDAOFacade() {
		this.wordDAO = new WordDAO();
	}

	@Override
	public Word getWordFromDB(String arabicWord) {
		return wordDAO.getWordFromDB(arabicWord);
	}

	@Override
	public boolean updateWordToDB(Word w) {
		return wordDAO.updateWordToDB(w);
	}

	@Override
	public boolean addWordToDB(Word w) {
		return wordDAO.addWordToDB(w);
	}

	@Override
	public boolean removeWordFromDB(String arabicWord) {
		return wordDAO.removeWordFromDB(arabicWord);
	}

	@Override
	public List<Word> getAllWords() {
		return wordDAO.getAllWords();
	}

	@Override
	public Word viewOnceWord(String arabicWord) {
		return wordDAO.viewOnceWord(arabicWord);
	}

	@Override
	public List<Word> importDataFromFile(String filePath) {
		return wordDAO.importDataFromFile(filePath);
	}

	@Override
	public boolean insertImportedData(List<Word> words) {
		return wordDAO.insertImportedData(words);
	}

	@Override
	public List<Word> searchWord(String searchTerm) {
		return wordDAO.getAllWords().stream()
				.filter(word -> word.getArabicWord().equalsIgnoreCase(searchTerm)
						|| word.getUrduMeaning().equalsIgnoreCase(searchTerm)
						|| word.getPersianMeaning().equalsIgnoreCase(searchTerm))
				.collect(Collectors.toList());
	}

	@Override
	public String getMeanings(String searchText, String language) {
		return wordDAO.getMeanings(searchText, language);
	}

	@Override
	public List<String> getTaggedAndStemmedWords() {
		return wordDAO.getTaggedAndStemmedWords();
	}

	@Override
	public String getPOSTaggedWord(String arabicWord) {
		return wordDAO.getPOSTaggedWord(arabicWord);
	}

	@Override
	public String getStemmedWord(String arabicWord) {
		return wordDAO.getStemmedWord(arabicWord);
	}
	public String[] scrapeWordAndUrduMeaning(String filePath) {
        return ((FileWordDAO) wordDAO).scrapeWordAndUrduMeaning(filePath);
    }

    public void saveWordAndUrduMeaning(String word, String urduMeaning) {
        ((FileWordDAO) wordDAO).saveWordAndUrduMeaning(word, urduMeaning);
    }

    public String scrapeFarsiMeaning(String filePath) {
        return ((FileWordDAO) wordDAO).scrapeFarsiMeaning(filePath);
    }

    public void updateFarsiMeaning(String word, String farsiMeaning) {
        ((FileWordDAO) wordDAO).updateFarsiMeaning(word, farsiMeaning);
    }

    public String getFarsiMeaning(String word) {
        return ((FileWordDAO) wordDAO).getFarsiMeaning(word);
    }

	@Override
	public String scrapeUrduMeaning(String url, String wordId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] scrapeWordAndUrduMeaning1(String filePath) {
		// TODO Auto-generated method stub
		return null;
	}

}