package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bl.WordBO;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;

public class NormalizeWordsUsingJar {

    private WordBO wordBO;

    @BeforeEach
    public void setUp() {
        wordBO = new WordBO();
    }

    @Test
    public void testProcessWord() throws Exception {
        String arabicWord = "سلام";

        File jarFile = new File("/mnt/data/AlKhalil-2.1.21.jar");
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()})) {

            Class<?> lemmatizerClass = classLoader.loadClass("AlKhalil2.AnalyzedWords");
            Object lemmatizerInstance = lemmatizerClass.getDeclaredConstructor().newInstance();
            Method tagMethod = lemmatizerClass.getMethod("analyzedWords", String.class);

            Object result = tagMethod.invoke(lemmatizerInstance, arabicWord);

            if (result instanceof LinkedList) {
                LinkedList<?> wordList = (LinkedList<?>) result;
                if (!wordList.isEmpty()) {
                    Object word = wordList.get(0);
                    Method getStemMethod = word.getClass().getMethod("getStem");
                    Method getWordRootMethod = word.getClass().getMethod("getWordRoot");
                    Method getPosMethod = word.getClass().getMethod("getPos");

                    String stem = (String) getStemMethod.invoke(word);
                    String wordRoot = (String) getWordRootMethod.invoke(word);
                    String pos = (String) getPosMethod.invoke(word);

                    String expectedResult = "سَلَام,سلم,مفرد مذكر مرفوع في حالة الإضافة";
                    String resultString = stem + "," + wordRoot + "," + pos;
                    assertEquals(expectedResult, resultString);
                } else {
                    fail("No words found in the analysis.");
                }
            } else {
                fail("Unexpected result type: " + result.getClass().getName());
            }
        }
    }

    @Test
    public void testProcessWord_EmptyResult() throws Exception {
        String arabicWord = "unknown";

        File jarFile = new File("/mnt/data/AlKhalil-2.1.21.jar");
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()})) {

            Class<?> lemmatizerClass = classLoader.loadClass("AlKhalil2.AnalyzedWords");
            Object lemmatizerInstance = lemmatizerClass.getDeclaredConstructor().newInstance();
            Method tagMethod = lemmatizerClass.getMethod("analyzedWords", String.class);

            Object result = tagMethod.invoke(lemmatizerInstance, arabicWord);

            if (result instanceof LinkedList) {
                LinkedList<?> wordList = (LinkedList<?>) result;
                if (wordList.isEmpty()) {
                    String resultText = "Tags Not Found!";
                    assertTrue(resultText.contains("Tags Not Found"));
                } else {
                    fail("Expected empty result, but found words.");
                }
            } else {
                fail("Unexpected result type: " + result.getClass().getName());
            }
        }
    }

    @Test
    public void testProcessWord_Exception() {
        String arabicWord = "testError";

        try {
            File jarFile = new File("/mnt/data/AlKhalil-2.1.21.jar");
            try (URLClassLoader classLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()})) {
                classLoader.loadClass("NonExistentClass");
            }
            fail("Expected ClassNotFoundException, but it wasn't thrown.");
        } catch (ClassNotFoundException e) {
            assertTrue(e.getMessage().contains("NonExistentClass"));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
