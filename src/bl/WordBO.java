package bl;

import com.qcri.farasa.segmenter.Farasa;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import dal.IWordDAOFacade;
import dal.WordDAOFacade;
import dto.Word;
import dal.WordDAO;
public class WordBO implements IWordBO {
	private IWordDAOFacade wordDAOFacade;
	private static final Logger LOGGER = Logger.getLogger(WordDAO.class.getName());
	
//	static {
//	    try {
//	        FileHandler fileHandler = new FileHandler("src/logs/dictionary-app-log.txt", true); 
//	        fileHandler.setFormatter(new SimpleFormatter());
//	        LOGGER.addHandler(fileHandler);
//	        LOGGER.setLevel(Level.ALL);
//	    } catch (IOException e) {
//	        LOGGER.log(Level.SEVERE, "Error setting up file handler", e);
//	    }
//	}

	public WordBO() {
		wordDAOFacade = new WordDAOFacade();
	}

	@Override
	public boolean updateWord(Word w) {
		return wordDAOFacade.updateWordToDB(w);
	}

	@Override
	public boolean addWord(Word w) {
		return wordDAOFacade.addWordToDB(w);
	}

	@Override
	public boolean removeWord(String arabicWord) {
		return wordDAOFacade.removeWordFromDB(arabicWord);
	}

	@Override
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

	@Override
	public Word viewOnceWord(String arabicWord) {
		return wordDAOFacade.viewOnceWord(arabicWord);
	}

	@Override
	public List<Word> importDataFromFile(String filePath) {
		return wordDAOFacade.importDataFromFile(filePath);
	}

	@Override
	public boolean insertImportedData(List<Word> words) {
		return wordDAOFacade.insertImportedData(words);
	}

	@Override
	public String searchWord(String searchTerm) {
	    LOGGER.log(Level.INFO, "Starting search for term: {0}", searchTerm);

	    if (searchTerm == null || searchTerm.trim().isEmpty()) {
	        LOGGER.log(Level.WARNING, "Invalid search term provided: {0}", searchTerm);
	        return "Please enter a valid word to search.";
	    }

	    try {
	        List<Word> results = wordDAOFacade.searchWord(searchTerm);

	        if (!results.isEmpty()) {
	            StringBuilder sb = new StringBuilder();
	            for (Word word : results) {
	                sb.append("Word: ").append(word.getArabicWord())
	                  .append(", Urdu Meaning: ").append(word.getUrduMeaning())
	                  .append(", Persian Meaning: ").append(word.getPersianMeaning())
	                  .append("\n");
	            }
	            LOGGER.log(Level.INFO, "Search results found for term: {0}", searchTerm);
	            return sb.toString();
	        }

	        LOGGER.log(Level.INFO, "No results found for term: {0}", searchTerm);
	        return "Word not found.";
	    } catch (Exception e) {
	        LOGGER.log(Level.SEVERE, "Error occurred while searching for term: {0}. Error: {1}", 
	                   new Object[]{searchTerm, e.getMessage()});
	        return "An error occurred while searching. Please try again.";
	    }
	}


	@Override
	public String getMeanings(String searchText, String language) {
		return wordDAOFacade.getMeanings(searchText, language);
	}

	@Override
	public String processWord(String arabicText) throws Exception {
	    LOGGER.log(Level.INFO, "Processing word: {0}", arabicText);

	    StringBuilder resultText = new StringBuilder();
	    try (URLClassLoader classLoader = new URLClassLoader(
	            new URL[] { new File("/mnt/data/AlKhalil-2.1.21.jar").toURI().toURL() })) {

	        LOGGER.log(Level.INFO, "ClassLoader created and AlKhalil-2.1.21.jar loaded.");

	        Class<?> lemmatizerClass = classLoader.loadClass("AlKhalil2.AnalyzedWords");
	        Object lemmatizerInstance = lemmatizerClass.getDeclaredConstructor().newInstance();

	        Method tagMethod = lemmatizerClass.getMethod("analyzedWords", String.class);
	        LOGGER.log(Level.INFO, "Calling analyzedWords method with input: {0}", arabicText);

	        Object result = tagMethod.invoke(lemmatizerInstance, arabicText);

	        if (result instanceof LinkedList) {
	            LinkedList<?> wordList = (LinkedList<?>) result;
	            if (!wordList.isEmpty()) {
	                LOGGER.log(Level.INFO, "Processing {0} words from the result.", wordList.size());
	                for (Object word : wordList) {
	                    Method getStemMethod = word.getClass().getMethod("getStem");
	                    Method getWordRootMethod = word.getClass().getMethod("getWordRoot");
	                    Method getPosMethod = word.getClass().getMethod("getPos");

	                    String stem = (String) getStemMethod.invoke(word);
	                    String wordRoot = (String) getWordRootMethod.invoke(word);
	                    String pos = (String) getPosMethod.invoke(word);

	                    resultText.append("Original Word: ").append(arabicText).append("\n")
	                            .append("Stem: ").append(stem).append("\n")
	                            .append("Root: ").append(wordRoot).append("\n")
	                            .append("POS: ").append(pos).append("\n");

	                    LOGGER.log(Level.INFO, "Processed word: Stem: {0}, Root: {1}, POS: {2}",
	                            new Object[]{stem, wordRoot, pos});

	                    return stem + "," + wordRoot + "," + pos;
	                }
	            } else {
	                resultText.append("Tags Not Found!");
	                LOGGER.log(Level.WARNING, "No tags found for the word: {0}", arabicText);
	            }
	        } else {
	            resultText.append("Unexpected result type: ").append(result.getClass().getName());
	            LOGGER.log(Level.SEVERE, "Unexpected result type from analyzedWords method: {0}", result.getClass().getName());
	        }
	    } catch (Exception e) {
	        LOGGER.log(Level.SEVERE, "Error during lemmatization of word: {0}. Error: {1}", 
	                   new Object[]{arabicText, e.getMessage()});
	        throw new Exception("Error during lemmatization", e);
	    }
	    return resultText.toString();
	}


	@Override
	public void saveResults(String word, String stem, String root, String pos) throws Exception {
		wordDAOFacade.saveResults(word, stem, root, pos);

	}

	@Override
	public void saveWordAndUrduMeaning(String URL) {
	    Runnable task = () -> {
	        try {
	            String[] wordAndUrduMeaning = wordDAOFacade.scrapeWordAndUrduMeaning(URL);

	            if (wordAndUrduMeaning != null) {
	                String word = wordAndUrduMeaning[0];
	                String urduMeaning = wordAndUrduMeaning[1];

	                wordDAOFacade.saveWordAndUrduMeaning(word, urduMeaning);
	                LOGGER.log(Level.INFO, "Thread: Word: {0}, Urdu Meaning: {1}", new Object[]{word, urduMeaning});
	            } else {
	                LOGGER.log(Level.WARNING, "Thread: Failed to retrieve word and Urdu meaning from URL: {0}", URL);
	            }
	        } catch (Exception e) {
	            LOGGER.log(Level.SEVERE, "Thread: Error saving word and Urdu meaning from URL: {0}", e.getMessage());
	        }
	    };
	    new Thread(task).start();
	}

	@Override
	public void saveFarsiMeaning(String word, String filePath) {
	    Runnable task = () -> {
	        try {
	            String farsiMeaning = wordDAOFacade.scrapeFarsiMeaning(filePath);

	            if (farsiMeaning != null) {
	                wordDAOFacade.updateFarsiMeaning(word, farsiMeaning);
	                LOGGER.log(Level.INFO, "Thread: Farsi Meaning stored for word: {0}, Meaning: {1}", new Object[]{word, farsiMeaning});
	            } else {
	                LOGGER.log(Level.WARNING, "Thread: Failed to retrieve Farsi meaning from file path: {0}", filePath);
	            }
	        } catch (Exception e) {
	            LOGGER.log(Level.SEVERE, "Thread: Error saving Farsi meaning for word: {0}. Error: {1}", new Object[]{word, e.getMessage()});
	        }
	    };
	    new Thread(task).start();
	}



	@Override
	public String getFarsiMeaning(String word) {
		return wordDAOFacade.getFarsiMeaning(word);
	}

	@Override
	public void markWordAsFavorite(String arabicWord, boolean isFavorite) {
		wordDAOFacade.markAsFavorite(arabicWord, isFavorite);
	}

	@Override
	public List<Word> getFavoriteWords() {
		return wordDAOFacade.getFavoriteWords();
	}

	@Override
	public boolean isWordFavorite(String arabicWord) {
		return wordDAOFacade.isWordFavorite(arabicWord);
	}

	@Override
	public void addSearchToHistory(Word word) {
		wordDAOFacade.addSearchToHistory(word);

	}

	@Override
	public List<Word> getRecentSearchHistory(int limit) {
		return wordDAOFacade.getRecentSearchHistory(limit);
	}


	@Override
	public List<String> getSegmentedWordsWithDiacritics(String word) {
	    LOGGER.log(Level.INFO, "Segmenting word with diacritics: {0}", word);

	    try {
	        List<String> segmentedWords = wordDAOFacade.segmentWordWithDiacritics(word);
	        
	        if (segmentedWords != null && !segmentedWords.isEmpty()) {
	            LOGGER.log(Level.INFO, "Successfully segmented word: {0}, Segmented words: {1}", 
	                       new Object[]{word, segmentedWords});
	        } else {
	            LOGGER.log(Level.WARNING, "No segmented words found for: {0}", word);
	        }
	        return segmentedWords;
	    } catch (Exception e) {
	        LOGGER.log(Level.SEVERE, "Error during word segmentation: {0}, Error: {1}", 
	                   new Object[]{word, e.getMessage()});
	        return null;
	    }
	}


	@Override
	public List<String> getRecentSearchSuggestions() {
		return wordDAOFacade.getRecentSearchSuggestions();
	}
	@Override
	public String[] getMeaning1(String word) throws Exception {
        String[] meanings =  wordDAOFacade.getMeaningsFromDB(word);
        if (meanings[0] == null && meanings[1] == null) {
            String rootWord = processWord(word);
            meanings = wordDAOFacade.getMeaningsFromDB(rootWord);
        }
        return meanings;
    }
	@Override
	public String[] getMeaningsFromDB(String word) {
		return wordDAOFacade.getMeaningsFromDB(word);
	}

	
}