package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class SearchPageObject extends MainPageObject {

  private static final String
          SEARCH_INIT_ELEMENT = "//*[contains(@text,'Search Wikipedia')]",
          SEARCH_INPUT = "//*[contains(@text,'Search…')]",
          SEARCH_RESULT_BY_SUBSTRING_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']";

  public SearchPageObject(AppiumDriver<?> driver) {
    super(driver);
  }

  //region TEMPLATES METHODS
  private static String getResultSearchElement(String substring) {
    return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", substring);
  }
  //endregion

  public void initSearchInput() {
    this.waitForElementAndClick(By.xpath(SEARCH_INIT_ELEMENT), "Внимание! Элемент 'Search Wikipedia' не найден.", 5);
    this.waitForElementPresent(By.xpath(SEARCH_INPUT), "Внимание! Поле ввода текста для поиска не найдено.");
  }

  public void typeSearchLine(String substring) {
    this.waitForElementAndSendKeys(By.xpath(SEARCH_INPUT), substring, "Внимание! Поле ввода текста для поиска не найдено.", 5);
  }

  public void waitForSearchResult(String substring) {
    String searchResultXpath = getResultSearchElement(substring);
    this.waitForElementPresent(By.xpath(searchResultXpath), String.format("Внимание! Текст '%s' не найден.", substring), 15);
  }
}
