package lib.ui;

import io.appium.java_client.AppiumDriver;

public class NavigationUI extends MainPageObject {

    private static final String
            MAIN_NAV_TAB_ELEMENT = "id:org.wikipedia:id/fragment_main_nav_tab_layout",
            READING_LISTS_LINK = "xpath://android.widget.FrameLayout[@content-desc='My lists']",
            MY_LISTS_PAGE_TITLE = "xpath://*[@resource-id='org.wikipedia:id/single_fragment_toolbar']/*[@text='My lists']";

    public NavigationUI(AppiumDriver driver) {
        super(driver);
    }

    public void clickMyLists() {
        this.waitForElementVisible(MAIN_NAV_TAB_ELEMENT, "Главная панель навигации не найдена.", 15);
        this.waitForElementClickableAndClick(READING_LISTS_LINK, "Кнопка перехода к спискам не найдена или недоступна для действий.", 5);
        this.waitForElementPresent(MY_LISTS_PAGE_TITLE, "Переход к списку личных папок со статьями не был выполнен.", 15);
    }
}