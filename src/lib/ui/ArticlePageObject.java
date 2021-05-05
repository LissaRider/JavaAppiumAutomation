package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;
import org.openqa.selenium.WebElement;

abstract public class ArticlePageObject extends MainPageObject {

    protected static String
            TITLE,
            FOOTER_ELEMENT,
            PAGE_TOOLBAR,
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
            OPTIONS_ADD_TO_MY_LIST_BUTTON;

    public ArticlePageObject(AppiumDriver driver) {
        super(driver);
    }

    //region TEMPLATES METHODS
    private static String getOptionByTitle(String optionTitle) {
        return OPTION_BY_TITLE_BUTTON_TPL.replace("{OPTION}", optionTitle);
    }

    private static String getFolderByName(String folderName) {
        return READING_LIST_ELEMENT_TPL.replace("{FOLDER}", folderName);
    }
    //endregion

    public WebElement waitForTitleElement() {
        return this.waitForElementPresent(TITLE, "Заголовок статьи не найден.", 15);
    }

    public String getArticleTitle() {
        WebElement titleElement = waitForTitleElement();
        if (Platform.getInstance().isAndroid()) {
            return titleElement.getAttribute("text");
        } else {
            return titleElement.getAttribute("name");
        }
    }

    public void swipeToFooter() {
        if (Platform.getInstance().isAndroid()) {
            this.swipeUpToFindElement(FOOTER_ELEMENT, "Конец статьи не найден.", 40);
        } else {
            this.swipeUpTillElementAppear(FOOTER_ELEMENT, "Конец статьи не найден.", 150);
        }
    }

    public void selectAddToReadingListOption() {
        this.waitForElementVisible(PAGE_TOOLBAR, "На странице верхняя панель инструментов не найдена.", 15);
        this.waitForElementClickableAndClick(OPTIONS_BUTTON, "Кнопка открытия панели действий со статьёй не найдена или недоступна для действий.", 5);
        this.waitForElementVisible(OPTIONS_MENU, "Панель действий со статьёй не найдена.", 15);
        String addToReadingListXpath = getOptionByTitle("Add to reading list");
        this.waitForElementClickableAndClick(addToReadingListXpath, "Опция добавления статьи в список не найдена или недоступна для действий.", 5);
    }

    public void addArticleToNewList(String folderName) {
        selectAddToReadingListOption();
        this.waitForElementVisible(ADD_TO_LIST_INIT_FORM, "Форма добавления статьи в папку не найдена.", 15);
        if (this.isElementPresent(ADD_TO_LIST_OVERLAY))
            waitForElementClickableAndClick(ADD_TO_LIST_OVERLAY, "Кнопка 'GOT IT' не найдена или недоступна для действий.", 5);
        else
            this.waitForElementClickableAndClick(CREATE_NEW_LIST_BUTTON, "Кнопка 'Create' не найдена или недоступна для действий.", 5);
        this.waitForElementVisible(NEW_LIST_CREATION_FORM, "Форма создания новой папки для добавления сстатьи не найдена.", 15);
        this.waitForElementAndClear(NEW_LIST_NAME_INPUT, "Поле ввода имени новой папки для добавления статьи не найдено.", 5);
        this.waitForElementAndSendKeys(NEW_LIST_NAME_INPUT, folderName, "Невозможно ввести текст в поле ввода имени папки для добавления статьи.", 5);
        this.waitForElementClickableAndClick(NEW_LIST_CREATION_OK_BUTTON, "Кнопка 'OK' не найдена или недоступна для действий.", 5);
    }

    public void addArticleToExistingList(String folderName) {
        selectAddToReadingListOption();
        this.waitForElementVisible(ADD_TO_LIST_INIT_FORM, "Форма добавления статьи в папку не найдена.", 15);
        String folderToAddArticleXpath = getFolderByName(folderName);
        this.waitForElementClickableAndClick(folderToAddArticleXpath, String.format("Папка с именем '%s' не найдена или недоступна для действий.", folderName), 5);
    }

    public void closeArticle() {
        this.waitForElementVisible(PAGE_TOOLBAR, "На странице панель инструментов не найдена.", 15);
        this.waitForElementClickableAndClick(CLOSE_ARTICLE_BUTTON, "Кнопка закрытия статьи не найдена или недоступна для действий.", 5);
    }

    public void assertIsArticleTitlePresent() {
        this.assertElementPresent(TITLE, "Заголовок статьи не найден.");
    }
}