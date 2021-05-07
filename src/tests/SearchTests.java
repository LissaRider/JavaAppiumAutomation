package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.SearchPageObject;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchTests extends CoreTestCase {

    @Test
    public void testSearch() {

        SearchPageObject searchPage = SearchPageObjectFactory.get(driver);

        final String searchLine = "Java";
        searchPage.searchByValue(searchLine);

        final String substring = "Object-oriented programming language";
        searchPage.waitForSearchResult(substring);
    }

    @Test
    public void testCancelSearch() {

        SearchPageObject searchPage = SearchPageObjectFactory.get(driver);

        searchPage.initSearchInput();

        searchPage.waitForCancelButtonToAppear();

        searchPage.clickCancelButton();

        searchPage.waitForCancelButtonToDisappear();
    }

    @Test
    public void testAmountOfNotEmptySearch() {

        SearchPageObject searchPage = SearchPageObjectFactory.get(driver);

        final String searchLine = "Linkin Park Discography";
        searchPage.searchByValue(searchLine);

        int amountOfSearchResults = searchPage.getAmountOfFoundArticles();

        assertTrue(
                String.format("\n  Ошибка! Найдено меньше результатов, чем ожидалось: %d.\n", amountOfSearchResults),
                amountOfSearchResults > 1);
    }

    @Test
    public void testAmountOfEmptySearch() {

        SearchPageObject searchPage = SearchPageObjectFactory.get(driver);

        final String searchLine = "TIJIIOLLIKA";
        searchPage.searchByValue(searchLine);

        searchPage.waitForEmptyResultsLabel();

        searchPage.assertThereIsNoResultOfSearch();
    }

    @Test
    public void testSearchPlaceholder() {

        SearchPageObject searchPage = SearchPageObjectFactory.get(driver);

        searchPage.initSearchInput();

        searchPage.assertSearchPlaceholderHasText(Platform.getInstance().isAndroid()
                ? "Search…"
                : "Search Wikipedia");
    }

    @Test
    public void testSearchAndClear() {

        SearchPageObject searchPage = SearchPageObjectFactory.get(driver);

        final String searchLine = "Java";
        searchPage.searchByValue(searchLine);

        searchPage.waitForNumberOfResultsMoreThan(1);

        searchPage.clearSearchInput();
    }

    @Test
    public void testSearchResults() {
        SearchPageObject searchPage = SearchPageObjectFactory.get(driver);

        final String searchValue = "JAVA";
        searchPage.searchByValue(searchValue);

        List<WebElement> articleTitles = searchPage.getSearchResultsList();

        int n = Platform.getInstance().isAndroid() ? 0 : 1;
        for (int i = n; i < articleTitles.size(); i++) {
            final WebElement titleElement = articleTitles.get(i);
            String articleTitle = Platform.getInstance().isAndroid()
                    ? titleElement.getAttribute("text").toLowerCase()
                    : titleElement.getAttribute("name").toLowerCase();
            System.out.println(titleElement.getAttribute("name").toLowerCase());
            assertTrue(
                    String.format("\n  Ошибка! В заголовке найденной статьи с индексом [%d] отсутствует заданное для поиска значение '%s'.\n", i, searchValue),
                    articleTitle.contains(searchValue.toLowerCase()));
        }
    }

    @Test
    public void testSearchArticleWithTitleAndDescription() {
        Map<String, String> searchResults = new HashMap<>();
        searchResults.put("Java", "Island of Indonesia");
        searchResults.put("JavaScript", "Programming language");
        searchResults.put("Java (programming language)", "Object-oriented programming language");

        SearchPageObject searchPage = SearchPageObjectFactory.get(driver);
        searchPage.initSearchInput();
        searchPage.typeSearchLine("Java");

        int amountOfSearchResults = searchPage.getAmountOfFoundArticles();

        assertTrue(
                String.format("\n  Ошибка! Найдено меньше статей, чем ожидалось: %d.\n", amountOfSearchResults),
                amountOfSearchResults >= 3);

        searchResults.forEach(searchPage::waitForElementByTitleAndDescription);
    }
}