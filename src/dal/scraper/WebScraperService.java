package dal.scraper;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import config.ConfigurationManager;

/**
 * Web scraper service for fetching word meanings from external websites.
 * 
 * Features:
 * - Configurable timeout and retry logic
 * - Proper error handling and logging
 * - User agent spoofing for compatibility
 */
public class WebScraperService implements IWebScraperService {
    private static final Logger LOGGER = Logger.getLogger(WebScraperService.class.getName());
    
    // Configuration
    private final int timeout;
    private final int retryCount;
    private final int retryDelayMs;
    private final String userAgent;
    
    public WebScraperService() {
        this.timeout = ConfigurationManager.getDbPropertyInt("scraper.timeout", 30000);
        this.retryCount = ConfigurationManager.getDbPropertyInt("scraper.retryCount", 3);
        this.retryDelayMs = ConfigurationManager.getDbPropertyInt("scraper.retryDelayMs", 1000);
        this.userAgent = ConfigurationManager.getDbProperty("scraper.userAgent", 
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0 Safari/537.36");
    }
    
    /**
     * Execute a scraping operation with retry logic.
     * @param operation The operation to execute
     * @param url The URL being scraped (for logging)
     * @param <T> Return type
     * @return The result, or null if all retries failed
     */
    private <T> T executeWithRetry(ScraperOperation<T> operation, String url) {
        for (int attempt = 1; attempt <= retryCount; attempt++) {
            try {
                Document doc = Jsoup.connect(url)
                    .userAgent(userAgent)
                    .timeout(timeout)
                    .get();
                
                T result = operation.execute(doc);
                LOGGER.log(Level.INFO, "Successfully scraped URL: {0} (attempt {1})", 
                           new Object[]{url, attempt});
                return result;
                
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Scraping attempt {0} failed for URL {1}: {2}", 
                           new Object[]{attempt, url, e.getMessage()});
                
                if (attempt < retryCount) {
                    try {
                        Thread.sleep(retryDelayMs * attempt); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return null;
                    }
                }
            }
        }
        
        LOGGER.log(Level.SEVERE, "All {0} scraping attempts failed for URL: {1}", 
                   new Object[]{retryCount, url});
        return null;
    }
    
    @Override
    public String[] scrapeWordAndUrduMeaning(String url) {
        return executeWithRetry(doc -> {
            Element wordElement = doc.select("td[id^=w]").first();
            Element meaningElement = doc.select("td[id^=m]").first();
            
            if (wordElement != null && meaningElement != null) {
                String word = wordElement.text().replaceAll("\\s*\\[.*?\\]", "");
                String urduMeaning = meaningElement.text();
                return new String[] { word, urduMeaning };
            }
            return null;
        }, url);
    }
    
    @Override
    public String scrapeFarsiMeaning(String url) {
        return executeWithRetry(doc -> {
            Elements farsiMeaningElements = doc.select("td[id^=m]");
            
            if (farsiMeaningElements.size() > 1) {
                Element farsiMeaningElement = farsiMeaningElements.get(1);
                if (farsiMeaningElement != null) {
                    return farsiMeaningElement.text();
                }
            }
            return null;
        }, url);
    }
    
    /**
     * Functional interface for scraper operations.
     */
    @FunctionalInterface
    private interface ScraperOperation<T> {
        T execute(Document doc) throws Exception;
    }
}
