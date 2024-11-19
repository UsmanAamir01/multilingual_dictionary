package bl;

import dal.IWordDAOFacade;
import dal.WordDAOFacade;
import dto.Word;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

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
        List<Word> results = wordDAOFacade.searchWord(searchTerm);
        if (!results.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Word word : results) {
                sb.append("Word: ").append(word.getArabicWord())
                  .append(", Urdu Meaning: ").append(word.getUrduMeaning())
                  .append(", Persian Meaning: ").append(word.getPersianMeaning()).append("\n");
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
    public List<String> getTaggedAndStemmedWords() {
        return wordDAOFacade.getTaggedAndStemmedWords();
    }

    @Override
    public LinkedList<?> getPOSTaggedWord(String arabicWord) {
        return wordDAOFacade.getPOSTaggedWord(arabicWord);
    }

    @Override
    public LinkedList<?> getStemmedWord(String arabicWord) {
        return wordDAOFacade.getStemmedWord(arabicWord);
    }

    @Override
    public String performPOSTagging(String arabicText) throws Exception {
        StringBuilder resultText = new StringBuilder();
        try (URLClassLoader classLoader = new URLClassLoader(
                new URL[]{new File("/mnt/data/AlKhalil-2.1.21.jar").toURI().toURL()})) {
            Class<?> posTaggerClass = classLoader.loadClass("AlKhalil2.AnalyzedWords");
            Object posTaggerInstance = posTaggerClass.getDeclaredConstructor().newInstance();
            Method tagMethod = posTaggerClass.getMethod("analyzedWords", String.class);
            Object result = tagMethod.invoke(posTaggerInstance, arabicText);
            
            if (result instanceof LinkedList) {
                LinkedList<?> wordList = (LinkedList<?>) result;
                if (!wordList.isEmpty()) {
                    for (Object word : wordList) {
                        Method getVoweledWordMethod = word.getClass().getMethod("getVoweledWord");
                        Method getStemMethod = word.getClass().getMethod("getStem");
                        Method getWordTypeMethod = word.getClass().getMethod("getWordType");
                        Method getWordRootMethod = word.getClass().getMethod("getWordRoot");
                        Method getPosMethod = word.getClass().getMethod("getPos");
                        
                        String voweledWord = (String) getVoweledWordMethod.invoke(word);
                        String stem = (String) getStemMethod.invoke(word);
                        String wordType = (String) getWordTypeMethod.invoke(word);
                        String wordRoot = (String) getWordRootMethod.invoke(word);
                        String pos = (String) getPosMethod.invoke(word);
                        
                        resultText.append("Voweled Word: ").append(voweledWord).append("\n")
                                  .append("Stem of the Word: ").append(stem).append("\n")
                                  .append("Type of the Word: ").append(wordType).append("\n")
                                  .append("Root of the Word: ").append(wordRoot).append("\n")
                                  .append("Part of Speech (POS): ").append(pos).append("\n\n");
                    }
                } else {
                    resultText.append("Tags Not Found!");
                }
            } else {
                resultText.append("Unexpected result type: ").append(result.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error during POS tagging", e);
        }
        return resultText.toString();
    }
    



    public String[] saveWordAndUrduMeaning(String filePath) {
        String[] wordAndUrduMeaning = wordDAOFacade.scrapeWordAndUrduMeaning(filePath);

        if (wordAndUrduMeaning != null) {
            String word = wordAndUrduMeaning[0];
            String urduMeaning = wordAndUrduMeaning[1];

            wordDAOFacade.saveWordAndUrduMeaning(word, urduMeaning);
            System.out.println("Word: " + word + ", Urdu Meaning: " + urduMeaning);
            return wordAndUrduMeaning;
        } else {
            System.out.println("Failed to retrieve word and Urdu meaning.");
            return null;
        }
    }

    // Save Farsi Meaning
    public void saveFarsiMeaning(String word, String filePath) {
        String farsiMeaning = wordDAOFacade.scrapeFarsiMeaning(filePath);

        if (farsiMeaning != null) {
        	wordDAOFacade.updateFarsiMeaning(word, farsiMeaning);
            System.out.println("Farsi Meaning stored: " + farsiMeaning);
        } else {
            System.out.println("Failed to retrieve Farsi meaning.");
        }
    }

    // Get Farsi Meaning
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

}