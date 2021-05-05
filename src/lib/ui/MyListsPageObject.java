package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class MyListsPageObject extends MainPageObject {

  private static final String
          LIST_ELEMENT = "org.wikipedia:id/item_container",
          FOLDER_BY_NAME_TFL = "//*[@resource-id='org.wikipedia:id/item_title'][@text='{FOLDER_NAME}']",
          ARTICLE_LIST_ELEMENT = "org.wikipedia:id/page_list_item_container",
          ARTICLE_BY_TITLE_TFL = "//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='{TITLE}']";

  public MyListsPageObject(AppiumDriver driver) {
    super(driver);
  }

  //region TEMPLATES METHODS
  private static String getFolderXpathByName(String folderName) {
    return FOLDER_BY_NAME_TFL.replace("{FOLDER_NAME}", folderName);
  }

  private static String getArticleXpathByTitle(String articleTitle) {
    return ARTICLE_BY_TITLE_TFL.replace("{TITLE}", articleTitle);
  }
  //endregion

  public void openFolderByName(String folderName) {
    this.waitForNumberOfElementsToBeMoreThan(By.id(LIST_ELEMENT), 0, "В списке 'My lists' ни одной папки не найдено.", 15);
    String folderNameXpath = getFolderXpathByName(folderName);
    this.waitForElementAndClick(By.xpath(folderNameXpath), String.format("Папка '%s' не найдена или недоступна для действий.", folderName), 5);
    waitForElementVisible(By.xpath(folderNameXpath), String.format("Заголовок папки '%s' не найден.", folderName), 15);
  }

  public void waitForArticleToAppearByTitle(String articleTitle) {
    String articleTitleXpath = getArticleXpathByTitle(articleTitle);
    this.waitForElementPresent(By.xpath(articleTitleXpath), String.format("В списке статья '%s' не найдена.", articleTitle), 15);
  }

  public void waitForArticleToDisappearByTitle(String articleTitle) {
    String articleTitleXpath = getArticleXpathByTitle(articleTitle);
    this.waitForElementNotPresent(By.xpath(articleTitleXpath), String.format("Статья '%s' не удалилась из списка.", articleTitle), 15);
  }

  public void swipeByArticleToDelete(String articleTitle) {
//    this.waitForElementVisible(By.xpath(ARTICLE_LIST_ELEMENT), "Ни одной статьи не найдено.", 15);
    this.waitForArticleToAppearByTitle(articleTitle);
    String articleTitleXpath = getArticleXpathByTitle(articleTitle);
    this.swipeElementToLeft(By.xpath(articleTitleXpath), String.format("В списке статья '%s' не найдена.", articleTitle));
    this.waitForArticleToDisappearByTitle(articleTitle);
  }

  public int getAmountOfAddedArticles() {
    this.waitForElementPresent(By.id(ARTICLE_LIST_ELEMENT), "Ничего не найдено по заданному запросу.", 15);
    return getAmountOfElements(By.id(ARTICLE_LIST_ELEMENT));
  }

  public void clickByArticleWithTitle(String articleTitle) {
    String articleTitleXpath = getArticleXpathByTitle(articleTitle);
    this.waitForElementClickableAndClick(By.xpath(articleTitleXpath), String.format("Статья с заголовком '%s' не найдена или недоступна для действий.", articleTitle), 5);
  }
}