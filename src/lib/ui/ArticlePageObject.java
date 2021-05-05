package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

public class ArticlePageObject extends MainPageObject {

    private static final String
            TITLE = "id:org.wikipedia:id/view_page_title_text",
            FOOTER_ELEMENT = "xpath://*[@text='View page in browser']",
            PAGE_TOOLBAR = "id:org.wikipedia:id/page_toolbar",
            OPTIONS_BUTTON = "xpath://android.widget.ImageView[@content-desc='More options']",
            OPTIONS_MENU = "xpath://android.widget.ListView",
            OPTION_BY_TITLE_BUTTON_TPL = "xpath://*[@resource-id='org.wikipedia:id/title'][@text='{OPTION}']",
            ADD_TO_LIST_OVERLAY = "id:org.wikipedia:id/onboarding_button",
            NEW_LIST_NAME_INPUT = "id:org.wikipedia:id/text_input",
            NEW_LIST_CREATION_OK_BUTTON = "xpath://*[@resource-id='org.wikipedia:id/buttonPanel']//*[@text='OK']",
            CLOSE_ARTICLE_BUTTON = "xpath://android.widget.ImageButton[@content-desc='Navigate up']",
            NEW_LIST_CREATION_FORM = "id:org.wikipedia:id/parentPanel",
            CREATE_NEW_LIST_BUTTON = "id:org.wikipedia:id/create_button",
            ADD_TO_LIST_INIT_FORM = "id:org.wikipedia:id/design_bottom_sheet",
            READING_LIST_ELEMENT_TPL = "xpath://*[@resource-id='org.wikipedia:id/item_title'][@text='{FOLDER}']";

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
        return titleElement.getAttribute("text");
    }

    public void swipeToFooter() {
        this.swipeUpToFindElement(FOOTER_ELEMENT, "Конец статьи не найден.", 20);
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