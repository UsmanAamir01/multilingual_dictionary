package test;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;

public class ArabicLemmatizer {

    public static void main(String[] args) {
        String arabicText = "العربي";  // Example Arabic text for lemmatization

        try {
            // Perform lemmatization and extract lemmas
            String result = performLemmatization(arabicText);
            System.out.println(result);  // Print out the lemmatized results
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String performLemmatization(String arabicText) throws Exception {
        StringBuilder resultText = new StringBuilder();
        try (URLClassLoader classLoader = new URLClassLoader(
                new URL[] { new File("/mnt/data/AlKhalil-2.1.21.jar").toURI().toURL() })) {

            // Load the AlKhalil class dynamically
            Class<?> lemmatizerClass = classLoader.loadClass("AlKhalil2.AnalyzedWords");
            Object lemmatizerInstance = lemmatizerClass.getDeclaredConstructor().newInstance();

            // Invoke the analyzedWords method for lemmatization
            Method analyzeWordsMethod = lemmatizerClass.getMethod("analyzedWords", String.class);
            Object result = analyzeWordsMethod.invoke(lemmatizerInstance, arabicText);

            // Process the result to extract lemmas
            if (result instanceof LinkedList) {
                LinkedList<?> wordList = (LinkedList<?>) result;
                if (!wordList.isEmpty()) {
                    for (Object word : wordList) {
                        // Reflectively call methods to extract lemma-related information
                        Method getLemmaMethod = word.getClass().getMethod("getStem");  // Assuming "getStem" provides the lemma
                        Method getRootMethod = word.getClass().getMethod("getWordRoot");
                        Method getPosMethod = word.getClass().getMethod("getPos");

                        String lemma = (String) getLemmaMethod.invoke(word);  // Extracting the lemma (base form)
                        String root = (String) getRootMethod.invoke(word);  // Root of the word
                        String pos = (String) getPosMethod.invoke(word);  // Part of speech (POS)

                        // Append the lemma and other details to the result text
                        resultText.append("Lemma: ").append(lemma).append("\n")
                                .append("Root: ").append(root).append("\n")
                                .append("Part of Speech (POS): ").append(pos).append("\n\n");
                    }
                } else {
                    resultText.append("No lemmas found for the provided text.");
                }
            } else {
                resultText.append("Unexpected result type: ").append(result.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error during lemmatization", e);
        }
        return resultText.toString();
    }
}
