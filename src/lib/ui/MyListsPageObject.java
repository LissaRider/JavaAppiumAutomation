package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;

abstract public class MyListsPageObject extends MainPageObject {

    protected static String
            FOLDER_BY_NAME_TPL,
            ARTICLE_LIST,
            ARTICLE_LIST_ITEM,
            ARTICLE_BY_TITLE_TPL,
            SYNC_YOUR_SAVED_ARTICLES_POPUP,
            CLOSE_SYNC_POPUP_BUTTON;

    public MyListsPageObject(AppiumDriver driver) {
        super(driver);
    }

    //region TEMPLATES METHODS
    private static String getFolderXpathByName(String folderName) {
        return FOLDER_BY_NAME_TPL.replace("{FOLDER_NAME}", folderName);
    }

    private static String getArticleXpathByTitle(String articleTitle) {
        return ARTICLE_BY_TITLE_TPL.replace("{ARTICLE_TITLE}", articleTitle);
    }
    //endregion

    public void openFolderByName(String folderName) {
        this.waitForNumberOfElementsToBeMoreThan(ARTICLE_LIST, 0, "В списке 'My lists' ни одной папки не найдено.", 15);
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
        this.waitForArticleToAppearByTitle(articleTitle);
        String articleTitleXpath = getArticleXpathByTitle(articleTitle);
        this.swipeElementToLeft(articleTitleXpath, String.format("В списке статья '%s' не найдена.", articleTitle));
        if (Platform.getInstance().isIOS())
            this.clickElementToTheRightUpperCorner(articleTitleXpath, "В списке статья не найдена.");
        this.waitForArticleToDisappearByTitle(articleTitle);
    }

    public int getAmountOfAddedArticles() {
        this.waitForElementPresent(ARTICLE_LIST_ITEM, "Ничего не найдено по заданному запросу.", 15);
        return getAmountOfElements(ARTICLE_LIST_ITEM);
    }

    public void clickByArticleWithTitle(String articleTitle) {
        String articleTitleXpath = getArticleXpathByTitle(articleTitle);
        this.waitForElementClickableAndClick(articleTitleXpath, String.format("Статья с заголовком '%s' не найдена или недоступна для действий.", articleTitle), 5);
    }

    /**
     * Метод для закрытия всплывающего окна при входе в список сохраненных статей с главной страницы на iOS
     */
    public void closeSyncSavedArticlesPopUp() {
        if (isElementPresent(SYNC_YOUR_SAVED_ARTICLES_POPUP))
            this.waitForElementClickableAndClick(CLOSE_SYNC_POPUP_BUTTON, "Кнопка 'Close' для закрытия окна 'Sync your saved articles popup?' не найдена или недоступна для действий.", 10);
    }
}