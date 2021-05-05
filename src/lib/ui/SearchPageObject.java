package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

abstract public class SearchPageObject extends MainPageObject {

    protected static String
            MAIN_PAGE_SEARCH_INIT_ELEMENT,
            MENU_PAGE_SEARCH_INIT_ELEMENT,
            SEARCH_INPUT,
            SEARCH_CANCEL_BUTTON,
            SEARCH_RESULT_BY_SUBSTRING_TPL,
            SEARCH_RESULT_TITLE,
            SEARCH_RESULT_BY_TITLE_TPL,
            SEARCH_RESULT_LIST_ITEM,
            SEARCH_RESULT_LIST,
            SEARCH_EMPTY_RESULT_ELEMENT,
            SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION_TPL;

    public SearchPageObject(AppiumDriver driver) {
        super(driver);
    }

    //region TEMPLATES METHODS
    private static String getResultSearchElementWithSubstring(String substring) {
        return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", substring);
    }

    private static String getResultSearchElementWithTitle(String articleTitle) {
        return SEARCH_RESULT_BY_TITLE_TPL.replace("{TITLE}", articleTitle);
    }

    private static String getArticleWithTitleAndDescription(String articleTitle, String articleDescription) {
        return SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION_TPL
                .replace("{ARTICLE_TITLE}", articleTitle)
                .replace("{ARTICLE_DESCRIPTION}", articleDescription);
    }
    //endregion

    public void waitForElementByTitleAndDescription(String title, String description) {
        String articleWithTitleAndDescriptionXpath = getArticleWithTitleAndDescription(title, description);
        this.waitForElementPresent(
                articleWithTitleAndDescriptionXpath,
                String.format("Не найдена статья с заголовком '%s' и описанием '%s'.", title, description),
                15
        );
    }

    public void initSearchInput() {
        String searchWikiMessageError = "элемент 'Search Wikipedia' не найден или недоступен для действий";
        if (this.isElementPresent(MAIN_PAGE_SEARCH_INIT_ELEMENT))
            this.waitForElementClickableAndClick(MAIN_PAGE_SEARCH_INIT_ELEMENT, String.format("На главной странице %s.", searchWikiMessageError), 5);
        else
            this.waitForElementClickableAndClick(MENU_PAGE_SEARCH_INIT_ELEMENT, String.format("На странице статьи %s.", searchWikiMessageError), 5);
        this.waitForElementPresent(SEARCH_INPUT, "Поле ввода текста для поиска не найдено.");
    }

    public void waitForCancelButtonToAppear() {
        this.waitForElementPresent(SEARCH_CANCEL_BUTTON, "Кнопка отмены поиска не найдена.");
    }

    public void waitForCancelButtonToDisappear() {
        this.waitForElementNotPresent(SEARCH_CANCEL_BUTTON, "Кнопка отмены поиска все ещё отображается.", 5);
    }

    public void clearSearchInput() {
        this.waitForElementAndClear(SEARCH_INPUT, "Поле ввода текста для поиска не найдено.", 5);
        this.waitForElementNotPresent(SEARCH_RESULT_LIST, "Список результатов все ещё отображается.", 15);
    }

    public List<WebElement> getSearchResultsList() {
        return this.waitForPresenceOfAllElements(SEARCH_RESULT_TITLE, "По заданному запросу ничего не найдено.", 15);
    }

    public void clickCancelButton() {
        this.waitForElementClickableAndClick(SEARCH_CANCEL_BUTTON, "Кнопка отмены поиска не найдена или недоступна для действий.", 5);
    }

    public void typeSearchLine(String substring) {
        this.waitForElementAndSendKeys(SEARCH_INPUT, substring, "Поле ввода текста для поиска не найдено.", 5);
    }

    public void waitForSearchResult(String substring) {
        String searchResultXpath = getResultSearchElementWithSubstring(substring);
        this.waitForElementPresent(searchResultXpath, String.format("Текст '%s' не найден.", substring), 15);
    }

    public void waitForNumberOfResultsMoreThan(int resultsCount) {
        this.waitForNumberOfElementsToBeMoreThan(SEARCH_RESULT_LIST_ITEM, resultsCount, String.format("Количество найденных результатов меньше ожидаемого: %d.", resultsCount), 15);
    }

    public void waitForNotEmptySearchResults() {
        this.waitForNumberOfResultsMoreThan(0);
    }

    public void clickByArticleWithSubstring(String substring) {
        String searchResultXpath = getResultSearchElementWithSubstring(substring);
        this.waitForElementClickableAndClick(searchResultXpath, String.format("Текст '%s' не найден или элемент недоступен для действий.", substring), 10);
    }

    public void clickByArticleWithTitle(String articleTitle) {
        String articleTitleXpath = getResultSearchElementWithTitle(articleTitle);
        this.waitForElementClickableAndClick(articleTitleXpath, String.format("Статья с заголовком '%s' не найдена или недоступна для действий.", articleTitle), 5);
    }

    public int getAmountOfFoundArticles() {
        this.waitForElementPresent(SEARCH_RESULT_LIST, "Ничего не найдено по заданному запросу.", 15);
        return getAmountOfElements(SEARCH_RESULT_LIST_ITEM);
    }

    public void waitForEmptyResultsLabel() {
        this.waitForElementPresent(SEARCH_EMPTY_RESULT_ELEMENT, "Найдены результаты по запросу поиска.", 15);
    }

    public void assertThereIsNoResultOfSearch() {
        this.assertElementNotPresent(SEARCH_RESULT_LIST_ITEM, "Найдены результаты по запросу поиска.");
    }

    public void assertSearchPlaceholderHasText(String placeholder) {
        this.assertElementHasText(SEARCH_INPUT, placeholder, "Поле ввода для поиска статьи содержит некорректный текст.");
    }
}