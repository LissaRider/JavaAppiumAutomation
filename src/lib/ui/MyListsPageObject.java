package lib.ui;

import io.appium.java_client.AppiumDriver;

public class MyListsPageObject extends MainPageObject {

    private static final String
            LIST_ELEMENT = "id:org.wikipedia:id/item_container",
            FOLDER_BY_NAME_TFL = "xpath://*[@resource-id='org.wikipedia:id/item_title'][@text='{FOLDER_NAME}']",
            ARTICLE_LIST_ELEMENT = "id:org.wikipedia:id/page_list_item_container",
            ARTICLE_BY_TITLE_TFL = "xpath://*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='{TITLE}']";

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
        this.waitForNumberOfElementsToBeMoreThan(LIST_ELEMENT, 0, "В списке 'My lists' ни одной папки не найдено.", 15);
        String folderNameXpath = getFolderXpathByName(folderName);
        this.waitForElementAndClick(folderNameXpath, String.format("Папка '%s' не найдена или недоступна для действий.", folderName), 5);
        waitForElementVisible(folderNameXpath, String.format("Заголовок папки '%s' не найден.", folderName), 15);
    }

    public void waitForArticleToAppearByTitle(String articleTitle) {
        String articleTitleXpath = getArticleXpathByTitle(articleTitle);
        this.waitForElementPresent(articleTitleXpath, String.format("В списке статья '%s' не найдена.", articleTitle), 15);
    }

    public void waitForArticleToDisappearByTitle(String articleTitle) {
        String articleTitleXpath = getArticleXpathByTitle(articleTitle);
        this.waitForElementNotPresent(articleTitleXpath, String.format("Статья '%s' не удалилась из списка.", articleTitle), 15);
    }

    public void swipeByArticleToDelete(String articleTitle) {
//    this.waitForElementVisible(ARTICLE_LIST_ELEMENT, "Ни одной статьи не найдено.", 15);
        this.waitForArticleToAppearByTitle(articleTitle);
        String articleTitleXpath = getArticleXpathByTitle(articleTitle);
        this.swipeElementToLeft(articleTitleXpath, String.format("В списке статья '%s' не найдена.", articleTitle));
        this.waitForArticleToDisappearByTitle(articleTitle);
    }

    public int getAmountOfAddedArticles() {
        this.waitForElementPresent(ARTICLE_LIST_ELEMENT, "Ничего не найдено по заданному запросу.", 15);
        return getAmountOfElements(ARTICLE_LIST_ELEMENT);
    }

    public void clickByArticleWithTitle(String articleTitle) {
        String articleTitleXpath = getArticleXpathByTitle(articleTitle);
        this.waitForElementClickableAndClick(articleTitleXpath, String.format("Статья с заголовком '%s' не найдена или недоступна для действий.", articleTitle), 5);
    }
}