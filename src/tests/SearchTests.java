package tests;

import lib.CoreTestCase;
import lib.ui.SearchPageObject;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchTests extends CoreTestCase {

  @Test
  public void testSearch() {
    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.waitForSearchResult("Object-oriented programming language");
  }

  @Test
  public void testCancelSearch() {
    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    searchPageObject.waitForCancelButtonToAppear();
    searchPageObject.clickCancelButton();
    searchPageObject.waitForCancelButtonToDisappear();
  }

  @Test
  public void testAmountOfNotEmptySearch() {
    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    String searchLine = "Linkin Park Discography";
    searchPageObject.typeSearchLine(searchLine);
    int amountOfSearchResults = searchPageObject.getAmountOfFoundArticles();

    assertTrue(
            String.format("\n  Ошибка! Найдено меньше результатов, чем ожидалось: %d.\n",amountOfSearchResults),
            amountOfSearchResults >= 1);
  }

  @Test
  public void testAmountOfEmptySearch() {
    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    String searchLine = "TIJIIOLLIKA";
    searchPageObject.typeSearchLine(searchLine);
    searchPageObject.waitForEmptyResultsLabel();
    searchPageObject.assertThereIsNoResultOfSearch();
  }

  @Test
  public void testSearchPlaceholder() {
    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    searchPageObject.assertSearchPlaceholderHasText("Search…");
  }

  @Test
  public void testSearchAndClear() {
    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.waitForNumberOfResultsMoreThan(1);
    searchPageObject.clearSearchInput();
  }

  @Test
  public void testSearchResults() {
    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    final String searchValue = "JAVA";
    searchPageObject.typeSearchLine(searchValue);
    List<WebElement> articleTitles = searchPageObject.getSearchResultsList();

    for (int i = 0; i < articleTitles.size(); i++) {
      String articleTitle = articleTitles.get(i).getAttribute("text").toLowerCase();
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

    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");

    int amountOfSearchResults = searchPageObject.getAmountOfFoundArticles();

    assertTrue(
            String.format("\n  Ошибка! Найдено меньше статей, чем ожидалось: %d.\n", amountOfSearchResults),
            amountOfSearchResults >= 3);

    searchResults.forEach(searchPageObject::waitForElementByTitleAndDescription);
  }
}