package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;
import org.openqa.selenium.WebElement;

abstract public class ArticlePageObject extends MainPageObject {

    protected static String
            ARTICLE_TITLE,
            ARTICLE_FOOTER_ELEMENT,
            ARTICLE_TOP_TOOLBAR,
            ARTICLE_SEARCH_INIT_ELEMENT, /* INIT ELEMENT: элемент на странице статьи для перехода на форму поиска */
            OPTIONS_BUTTON,
            OPTIONS_MENU,
            OPTION_BY_TITLE_BUTTON_TPL,
            ADD_TO_LIST_OVERLAY,
            NEW_LIST_NAME_INPUT,
            NEW_LIST_CREATION_OK_BUTTON,
            CLOSE_ARTICLE_BUTTON,
            NEW_LIST_CREATION_FORM,
            CREATE_NEW_LIST_BUTTON,
            ADD_TO_LIST_INIT_FORM,
            READING_LIST_ELEMENT_TPL,
            SAVE_FOR_LATER_BUTTON,
            TAP_TO_GO_BACK_BUTTON;

    public ArticlePageObject(AppiumDriver driver) {
        super(driver);
    }

    //region TEMPLATES METHODS
    private static String getOptionByTitle(String optionTitle) {
        return OPTION_BY_TITLE_BUTTON_TPL.replace("{MENU_OPTION}", optionTitle);
    }

    private static String getFolderByName(String folderName) {
        return READING_LIST_ELEMENT_TPL.replace("{FOLDER_NAME}", folderName);
    }
    //endregion

    public WebElement waitForTitleElement() {
        return this.waitForElementPresent(ARTICLE_TITLE, "Заголовок статьи не найден.", 15);
    }

    public String getArticleTitle() {
        WebElement titleElement = waitForTitleElement();
        return Platform.getInstance().isAndroid() ? titleElement.getAttribute("text") : titleElement.getAttribute("name");
    }

    public void swipeToFooter() {
        if (Platform.getInstance().isAndroid())
            this.swipeUpToFindElement(ARTICLE_FOOTER_ELEMENT, "Конец статьи не найден.", 40);
        else
            this.swipeUpTillElementAppear(ARTICLE_FOOTER_ELEMENT, "Конец статьи не найден.", 150);
    }

    /**
     * Метод для инициализации добавления статьи в список на Android
     */
    public void selectAddToReadingListOption() {
        this.waitForElementVisible(ARTICLE_TOP_TOOLBAR, "На странице верхняя панель инструментов не найдена.", 15);
        this.waitForElementClickableAndClick(OPTIONS_BUTTON, "Кнопка открытия меню действий со статьей не найдена или недоступна для действий.", 5);
        this.waitForElementVisible(OPTIONS_MENU, "Меню действий со статьей не найдена.", 15);
        String addToReadingListXpath = getOptionByTitle("Add to reading list");
        this.waitForElementClickableAndClick(addToReadingListXpath, "Опция добавления статьи в список не найдена или недоступна для действий.", 5);
    }

    /**
     * Метод для создани списка и добавления статьи в него на Android
     *
     * @param folderName -существующий список статей
     */
    public void addArticleToNewList(String folderName) {
        selectAddToReadingListOption();
        this.waitForElementVisible(ADD_TO_LIST_INIT_FORM, "Форма добавления статьи в папку не найдена.", 15);
        if (this.isElementPresent(ADD_TO_LIST_OVERLAY))
            waitForElementClickableAndClick(ADD_TO_LIST_OVERLAY, "Кнопка 'GOT IT' не найдена или недоступна для действий.", 5);
        else
            this.waitForElementClickableAndClick(CREATE_NEW_LIST_BUTTON, "Кнопка 'Create' не найдена или недоступна для действий.", 5);
        this.waitForElementVisible(NEW_LIST_CREATION_FORM, "Форма создания новой папки для добавления статьи не найдена.", 15);
        this.waitForElementAndClear(NEW_LIST_NAME_INPUT, "Поле ввода имени новой папки для добавления статьи не найдено.", 5);
        this.waitForElementAndSendKeys(NEW_LIST_NAME_INPUT, folderName, "Невозможно ввести текст в поле ввода имени папки для добавления статьи.", 5);
        this.waitForElementClickableAndClick(NEW_LIST_CREATION_OK_BUTTON, "Кнопка 'OK' не найдена или недоступна для действий.", 5);
    }

    /**
     * Метод для добавления статьи в уже существующий список на Android
     *
     * @param folderName -существующий список статей
     */
    public void addArticleToExistingList(String folderName) {
        selectAddToReadingListOption();
        this.waitForElementVisible(ADD_TO_LIST_INIT_FORM, "Форма добавления статьи в папку не найдена.", 15);
        String folderToAddArticleXpath = getFolderByName(folderName);
        this.waitForElementClickableAndClick(folderToAddArticleXpath, String.format("Папка с именем '%s' не найдена или недоступна для действий.", folderName), 5);
    }

    /**
     * Метод для добавления статьи в список сохраненных на iOS
     */
    public void addArticleToSavedList() {
        this.waitForElementClickableAndClick(SAVE_FOR_LATER_BUTTON, "Кнопка 'Save for later' не найдена или недоступна для действий.", 5);
    }

    /**
     * Метод для закрытия страницы и автоматического перехода на главную страицу на Android
     */
    public void closeArticle() {
        this.waitForElementVisible(ARTICLE_TOP_TOOLBAR, "На странице верхняя панель инструментов не найдена.", 15);
        this.waitForElementAndClick(CLOSE_ARTICLE_BUTTON, "Кнопка закрытия статьи не найдена или недоступна для действий.", 10);
    }

    public void assertIsArticleTitlePresent() {
        this.assertElementPresent(ARTICLE_TITLE, "Заголовок статьи не найден.");
    }

    /**
     * Метод для закрытия страницы и возврата на главную страницу на iOS
     */
    public void closeArticleAndReturnToMainPage() {
        this.waitForElementClickableAndClick(TAP_TO_GO_BACK_BUTTON, "Кнопка возврата на главную страницу не найдена или недоступна для действий.", 5);
    }
}