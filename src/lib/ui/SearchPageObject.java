package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPageObject extends MainPageObject {

  private static final String
          MAIN_PAGE_SEARCH_INIT_ELEMENT = "//android.widget.ImageView[@content-desc='Search Wikipedia']",
          MENU_PAGE_SEARCH_INIT_ELEMENT = "org.wikipedia:id/menu_page_search",
          SEARCH_INPUT = "org.wikipedia:id/search_src_text",
          SEARCH_CANCEL_BUTTON = "org.wikipedia:id/search_close_btn",
          SEARCH_RESULT_BY_SUBSTRING_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']",
          SEARCH_RESULT_TITLE = "org.wikipedia:id/page_list_item_title",
          SEARCH_RESULT_BY_TITLE_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='{TITLE}']",
          SEARCH_RESULT_LIST_ITEM = "org.wikipedia:id/page_list_item_container",
          SEARCH_RESULT_LIST = "org.wikipedia:id/search_results_list",
          SEARCH_EMPTY_RESULT_ELEMENT = "//*[@text='No results found']";

  public SearchPageObject(AppiumDriver<?> driver) {
    super(driver);
  }

  //region TEMPLATES METHODS
  private static String getResultSearchElementWithSubstring(String substring) {
    return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", substring);
  }

  private static String getResultSearchElementWithTitle(String articleTitle) {
    return SEARCH_RESULT_BY_TITLE_TPL.replace("{TITLE}", articleTitle);
  }
  //endregion

  public void initSearchInput() {
    String searchWikiMessageError = "элемент 'Search Wikipedia' не найден или недоступен для действий";
    if (this.isElementPresent(By.xpath(MAIN_PAGE_SEARCH_INIT_ELEMENT)))
      this.waitForElementClickableAndClick(By.xpath(MAIN_PAGE_SEARCH_INIT_ELEMENT), String.format("На главной странице %s.", searchWikiMessageError), 5);
    else
      this.waitForElementClickableAndClick(By.id(MENU_PAGE_SEARCH_INIT_ELEMENT), String.format("На странице статьи %s.", searchWikiMessageError), 5);
    this.waitForElementPresent(By.id(SEARCH_INPUT), "Поле ввода текста для поиска не найдено.");
  }

  public void waitForCancelButtonToAppear() {
    this.waitForElementPresent(By.id(SEARCH_CANCEL_BUTTON), "Кнопка отмены поиска не найдена.");
  }

  public void waitForCancelButtonToDisappear() {
    this.waitForElementNotPresent(By.id(SEARCH_CANCEL_BUTTON), "Кнопка отмены поиска все ещё отображается.", 5);
  }

  public void clearSearchInput() {
    this.waitForElementAndClear(By.id(SEARCH_INPUT), "Поле ввода текста для поиска не найдено.", 5);
    this.waitForElementNotPresent(By.id(SEARCH_RESULT_LIST), "Список результатов все ещё отображается.", 15);
  }

  public List<WebElement> getSearchResultsList() {
    return this.waitForPresenceOfAllElements(By.id(SEARCH_RESULT_TITLE), "По заданному запросу ничего не найдено.", 15);
  }

  public void clickCancelButton() {
    this.waitForElementClickableAndClick(By.id(SEARCH_CANCEL_BUTTON), "Кнопка отмены поиска не найдена или недоступна для действий.", 5);
  }

  public void typeSearchLine(String substring) {
    this.waitForElementAndSendKeys(By.id(SEARCH_INPUT), substring, "Поле ввода текста для поиска не найдено.", 5);
  }

  public void waitForSearchResult(String substring) {
    String searchResultXpath = getResultSearchElementWithSubstring(substring);
    this.waitForElementPresent(By.xpath(searchResultXpath), String.format("Текст '%s' не найден.", substring), 15);
  }

  public void waitForNumberOfResultsMoreThan(int resultsCount) {
    this.waitForNumberOfElementsToBeMoreThan(By.id(SEARCH_RESULT_LIST_ITEM), resultsCount, String.format("Количество найденных результатов меньше ожидаемого: %d.", resultsCount), 15);
  }

  public void waitForNotEmptySearchResults() {
    this.waitForNumberOfResultsMoreThan(0);
  }

  public void clickByArticleWithSubstring(String substring) {
    String searchResultXpath = getResultSearchElementWithSubstring(substring);
    this.waitForElementClickableAndClick(By.xpath(searchResultXpath), String.format("Текст '%s' не найден или элемент недоступен для действий.", substring), 10);
  }

  public void clickByArticleWithTitle(String articleTitle) {
    String articleTitleXpath = getResultSearchElementWithTitle(articleTitle);
    this.waitForElementClickableAndClick(By.xpath(articleTitleXpath), String.format("Статья с заголовком '%s' не найдена или недоступна для действий.", articleTitle), 5);
  }

  public int getAmountOfFoundArticles() {
    this.waitForElementPresent(By.id(SEARCH_RESULT_LIST), "Ничего не найдено по заданному запросу.", 15);
    return getAmountOfElements(By.id(SEARCH_RESULT_LIST_ITEM));
  }

  public void waitForEmptyResultsLabel() {
    this.waitForElementPresent(By.xpath(SEARCH_EMPTY_RESULT_ELEMENT), "Найдены результаты по запросу поиска.", 15);
  }

  public void assertThereIsNoResultOfSearch() {
    this.assertElementNotPresent(By.id(SEARCH_RESULT_LIST_ITEM), "Найдены результаты по запросу поиска.");
  }

  public void assertSearchPlaceholderHasText(String placeholder) {
    this.assertElementHasText(By.id(SEARCH_INPUT), placeholder, "Поле ввода для поиска статьи содержит некорректный текст.");
  }
}