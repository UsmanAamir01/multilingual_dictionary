package bl;

import com.qcri.farasa.segmenter.Farasa;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dal.IWordDAOFacade;
import dal.WordDAOFacade;
import dto.Word;
import dal.WordDAO;
public class WordBO implements IWordBO {
	private IWordDAOFacade wordDAOFacade;

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
		if (searchTerm == null || searchTerm.trim().isEmpty()) {
			return "Please enter a valid word to search.";
		}

		List<Word> results = wordDAOFacade.searchWord(searchTerm);
		if (!results.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Word word : results) {
				sb.append("Word: ").append(word.getArabicWord()).append(", Urdu Meaning: ")
						.append(word.getUrduMeaning()).append(", Persian Meaning: ").append(word.getPersianMeaning())
						.append("\n");
			}
			return sb.toString();
		}
		return "Word not found.";
	}

	@Override
	public String getMeanings(String searchText, String language) {
		return wordDAOFacade.getMeanings(searchText, language);
	}

	@Override
	public String processWord(String arabicText) throws Exception {
		StringBuilder resultText = new StringBuilder();
		try (URLClassLoader classLoader = new URLClassLoader(
				new URL[] { new File("/mnt/data/AlKhalil-2.1.21.jar").toURI().toURL() })) {

			Class<?> lemmatizerClass = classLoader.loadClass("AlKhalil2.AnalyzedWords");
			Object lemmatizerInstance = lemmatizerClass.getDeclaredConstructor().newInstance();

			Method tagMethod = lemmatizerClass.getMethod("analyzedWords", String.class);
			Object result = tagMethod.invoke(lemmatizerInstance, arabicText);

			if (result instanceof LinkedList) {
				LinkedList<?> wordList = (LinkedList<?>) result;
				if (!wordList.isEmpty()) {
					for (Object word : wordList) {
						Method getStemMethod = word.getClass().getMethod("getStem");
						Method getWordRootMethod = word.getClass().getMethod("getWordRoot");
						Method getPosMethod = word.getClass().getMethod("getPos");

						String stem = (String) getStemMethod.invoke(word);
						String wordRoot = (String) getWordRootMethod.invoke(word);
						String pos = (String) getPosMethod.invoke(word);

						resultText.append("Original Word: ").append(arabicText).append("\n").append("Stem: ")
								.append(stem).append("\n").append("Root: ").append(wordRoot).append("\n")
								.append("POS: ").append(pos).append("\n");

						return stem + "," + wordRoot + "," + pos;
					}
				} else {
					resultText.append("Tags Not Found!");
				}
			} else {
				resultText.append("Unexpected result type: ").append(result.getClass().getName());
			}
		} catch (Exception e) {
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
            String[] wordAndUrduMeaning = wordDAOFacade.scrapeWordAndUrduMeaning(URL);

            if (wordAndUrduMeaning != null) {
                String word = wordAndUrduMeaning[0];
                String urduMeaning = wordAndUrduMeaning[1];

                wordDAOFacade.saveWordAndUrduMeaning(word, urduMeaning);
                System.out.println("Thread: Word: " + word + ", Urdu Meaning: " + urduMeaning);
            } else {
                System.out.println("Thread: Failed to retrieve word and Urdu meaning.");
            }
        };
        new Thread(task).start();
    }

	@Override
	 public void saveFarsiMeaning(String word, String filePath) {
        Runnable task = () -> {
            String farsiMeaning = wordDAOFacade.scrapeFarsiMeaning(filePath);

            if (farsiMeaning != null) {
            	wordDAOFacade.updateFarsiMeaning(word, farsiMeaning);
                System.out.println("Thread: Farsi Meaning stored: " + farsiMeaning);
            } else {
                System.out.println("Thread: Failed to retrieve Farsi meaning.");
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


	public List<String> getSegmentedWordsWithDiacritics(String word) {
		try {
			return wordDAOFacade.segmentWordWithDiacritics(word);
		} catch (Exception e) {
			System.out.println("Error during word segmentation: segmentWordWithDiacritics" + e.getMessage());
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