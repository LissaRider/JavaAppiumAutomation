package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class MyListsPageObject extends MainPageObject {

  private static final String
          LIST_ELEMENT = "//*[@resource-id='org.wikipedia:id/item_container']",
          FOLDER_BY_NAME_TFL = "//*[@text='{FOLDER_NAME}']",
//          ARTICLE_LIST_ELEMENT = "//*[@resource-id='org.wikipedia:id/page_list_item_container']",
          ARTICLE_BY_TITLE_TFL = "//*[@text='{TITLE}']";

  public MyListsPageObject(AppiumDriver<?> driver) {
    super(driver);
  }

  //region TEMPLATES METHODS
  private static String getFolderXpathByName(String folderName) {
    return FOLDER_BY_NAME_TFL.replace("{FOLDER_NAME}", folderName);
  }

  private static String getSavedArticleXpathByTitle(String articleTitle) {
    return ARTICLE_BY_TITLE_TFL.replace("{TITLE}", articleTitle);
  }
  //endregion

  public void openFolderByName(String folderName) {
    String folderNameXpath = getFolderXpathByName(folderName);
    this.waitForElementVisible(By.xpath(LIST_ELEMENT), "Внимание! Ни одной папки не найдено.", 15);
    this.waitForElementAndClick(By.xpath(folderNameXpath), String.format("Внимание! Папка '%s' не найдена.", folderName), 5);
  }

  public void waitForArticleToAppearByTitle(String articleTitle) {
    String articleTitleXpath = getSavedArticleXpathByTitle(articleTitle);
    this.waitForElementPresent(By.xpath(articleTitleXpath), String.format("Внимание! В списке статья '%s' не найдена.", articleTitle), 15);
  }

  public void waitForArticleToDisappearByTitle(String articleTitle) {
    String articleTitleXpath = getSavedArticleXpathByTitle(articleTitle);
    this.waitForElementNotPresent(By.xpath(articleTitleXpath), String.format("Внимание! Статья '%s' не удалилась из списка.", articleTitle), 15);
  }

  public void swipeByArticleToDelete(String articleTitle) {
//    this.waitForElementVisible(By.xpath(ARTICLE_LIST_ELEMENT), "Внимание! Ни одной статьи не найдено.", 15);
    this.waitForArticleToAppearByTitle(articleTitle);
    String articleTitleXpath = getSavedArticleXpathByTitle(articleTitle);
    this.swipeElementToLeft(By.xpath(articleTitleXpath), String.format("Внимание! В списке статья '%s' не найдена.", articleTitle));
    this.waitForArticleToDisappearByTitle(articleTitle);
  }
}
