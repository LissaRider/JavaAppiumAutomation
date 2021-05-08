package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;
import org.openqa.selenium.WebElement;

import java.util.List;

abstract public class SearchPageObject extends MainPageObject {

    protected static String
            MAIN_PAGE_SEARCH_INIT_ELEMENT, /* INIT ELEMENT: элемент на главной странице для перехода на форму поиска */
            SEARCH_INPUT_FIELD, /* INPUT FIELD: поле ввода значения для поиска */
            SEARCH_CANCEL_BUTTON, /* BUTTON: кнопка для очистки поля ввода или закрытия формы поиска */
            SEARCH_EMPTY_RESULT_ELEMENT,/* SEARCH RESULT: форма, отображающаяся, если по заданному запросу ничего не найдено */
            SEARCH_RESULT_LIST, /* SEARCH RESULT: список всех найденных результатов */
            SEARCH_RESULT_LIST_ITEM, /* SEARCH RESULT: элемент списка всех найденных результатов (результат поиска) */
            SEARCH_RESULT_BY_LIST_ITEM_SUBSTRING_TPL, /* SEARCH RESULT: элемент списка всех найденных результатов с указанием текста в нем */
            SEARCH_RESULT_LIST_ITEM_TITLE,  /* SEARCH RESULT: заголовок статьи в элементе списка */
            SEARCH_RESULT_BY_LIST_ITEM_TITLE_TPL, /* SEARCH RESULT: заголовок статьи в элементе списка всех найденных результатов с указанием текста в нем */
            SEARCH_RESULT_BY_LIST_ITEM_TITLE_AND_DESCRIPTION_TPL, /* SEARCH RESULT: элемент списка всех найденных результатов с указанием заголовка и описания в нем */
            RECENT_SEARCHES_YET_ELEMENT; /* SEARCH ELEMENT: элемент с текстои, который отображается при очистке поля ввода для поиска (только iOS) */

    public SearchPageObject(AppiumDriver driver) {
        super(driver);
    }

    //region TEMPLATES METHODS
    private static String getResultSearchElementWithSubstring(String substring) {
        return SEARCH_RESULT_BY_LIST_ITEM_SUBSTRING_TPL.replace("{SUBSTRING}", substring);
    }

    private static String getResultSearchElementWithTitle(String articleTitle) {
        return SEARCH_RESULT_BY_LIST_ITEM_TITLE_TPL.replace("{TITLE}", articleTitle);
    }

    private static String getArticleWithTitleAndDescription(String articleTitle, String articleDescription) {
        return SEARCH_RESULT_BY_LIST_ITEM_TITLE_AND_DESCRIPTION_TPL
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
            this.waitForElementClickableAndClick(ArticlePageObject.ARTICLE_SEARCH_INIT_ELEMENT, String.format("На странице статьи %s.", searchWikiMessageError), 5);
        this.waitForElementPresent(SEARCH_INPUT_FIELD, "Поле ввода текста для поиска не найдено.");
    }

    public void waitForCancelButtonToAppear() {
        this.waitForElementPresent(SEARCH_CANCEL_BUTTON, "Кнопка отмены поиска не найдена.");
    }

    public void waitForCancelButtonToDisappear() {
        this.waitForElementNotPresent(SEARCH_CANCEL_BUTTON, "Кнопка отмены поиска все ещё отображается.", 5);
    }

    public void clearSearchInput() {
        this.waitForElementAndClear(SEARCH_INPUT_FIELD, "Поле ввода текста для поиска не найдено.", 5);
        if (Platform.getInstance().isAndroid())
            this.waitForElementNotPresent(SEARCH_RESULT_LIST, "Список результатов все ещё отображается.", 15);
        else
            this.waitForElementVisible(RECENT_SEARCHES_YET_ELEMENT, "Список результатов все ещё отображается.", 15);
    }

    public List<WebElement> getSearchResultsList() {
        return this.waitForPresenceOfAllElements(SEARCH_RESULT_LIST_ITEM_TITLE, "По заданному запросу ничего не найдено.", 15);
    }

    public void clickCancelButton() {
        this.waitForElementClickableAndClick(SEARCH_CANCEL_BUTTON, "Кнопка отмены поиска не найдена или недоступна для действий.", 5);
    }

    public void typeSearchLine(String substring) {
        this.waitForElementAndSendKeys(SEARCH_INPUT_FIELD, substring, "Поле ввода текста для поиска не найдено.", 5);
    }

    public WebElement waitForSearchResult(String substring) {
        String searchResultXpath = getResultSearchElementWithSubstring(substring);
        return this.waitForElementPresent(searchResultXpath, String.format("Текст '%s' не найден.", substring), 15);
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
        this.waitForElementVisible(SEARCH_RESULT_LIST_ITEM, "Ничего не найдено по заданному запросу.", 15);
        return getAmountOfElements(SEARCH_RESULT_LIST_ITEM);
    }

    public void waitForEmptyResultsLabel() {
        this.waitForElementPresent(SEARCH_EMPTY_RESULT_ELEMENT, "Найдены результаты по запросу поиска.", 15);
    }

    public void assertThereIsNoResultOfSearch() {
        this.assertElementNotPresent(SEARCH_RESULT_LIST_ITEM, "Найдены результаты по запросу поиска.");
    }

    public void assertSearchPlaceholderHasText(String placeholder) {
        this.assertElementHasText(SEARCH_INPUT_FIELD, placeholder, "Поле ввода для поиска статьи содержит некорректный текст.");
    }

    public void searchByValue(String searchLine) {
        initSearchInput();
        typeSearchLine(searchLine);
    }
}