package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class NavigationUI extends MainPageObject {

  private static final String
          MAIN_NAV_TAB_ELEMENT = "org.wikipedia:id/fragment_main_nav_tab_layout",
          READING_LISTS_LINK = "//android.widget.FrameLayout[@content-desc='My lists']",
          MY_LISTS_PAGE_TITLE = "//*[@resource-id='org.wikipedia:id/single_fragment_toolbar']/*[@text='My lists']";

  public NavigationUI(AppiumDriver driver) {
    super(driver);
  }

  public void clickMyLists() {
    this.waitForElementVisible(By.id(MAIN_NAV_TAB_ELEMENT), "Главная панель навигации не найдена.", 15);
    this.waitForElementClickableAndClick(By.xpath(READING_LISTS_LINK), "Кнопка перехода к спискам не найдена или недоступна для действий.", 5);
    this.waitForElementPresent(By.xpath(MY_LISTS_PAGE_TITLE), "Переход к списку личных папок со статьями не был выполнен.", 15);
  }
}