package dal.scraper;

/**
 * Interface for web scraping operations.
 * Separates web scraping concerns from database access.
 */
public interface IWebScraperService {
    
    /**
     * Scrape a word and its Urdu meaning from a URL.
     * @param url The URL to scrape
     * @return Array with [arabicWord, urduMeaning], or null if failed
     */
    String[] scrapeWordAndUrduMeaning(String url);
    
    /**
     * Scrape Farsi meaning from a URL.
     * @param url The URL to scrape
     * @return The Farsi meaning, or null if failed
     */
    String scrapeFarsiMeaning(String url);
}
