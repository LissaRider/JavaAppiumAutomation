package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ArticlePageObject extends MainPageObject {

  private static final String
          TITLE = "org.wikipedia:id/view_page_title_text",
          FOOTER_ELEMENT = "//*[@text='View page in browser']",
          OPTIONS_BUTTON = "//android.widget.ImageView[@content-desc='More options']",
          OPTIONS_MENU = "//android.widget.ListView",
          OPTION_ADD_TO_MY_LIST_BUTTON = "//*[@text='Add to reading list']",
          ADD_TO_MY_LIST_OVERLAY = "org.wikipedia:id/onboarding_button",
          NEW_LIST_NAME_INPUT = "org.wikipedia:id/text_input",
          NEW_LIST_CREATION_OK_BUTTON = "//*[@text='OK']",
          CLOSE_ARTICLE_BUTTON = "//android.widget.ImageButton[@content-desc='Navigate up']";

  public ArticlePageObject(AppiumDriver<?> driver) {
    super(driver);
  }

  public WebElement waitForTitleElement() {
    return this.waitForElementPresent(By.id(TITLE), "Внимание! Заголовок статьи не найден.", 15);
  }

  public String getArticleTitle() {
    WebElement titleElement = waitForTitleElement();
    return titleElement.getAttribute("text");
  }

  public void swipeToFooter() {
    this.swipeUpToFindElement(By.xpath(FOOTER_ELEMENT), "  Конец статьи не найден.", 20);
  }

  public void addArticleToMyList(String folderName) {
    this.waitForElementAndClick(By.xpath(OPTIONS_BUTTON), "Внимание! Кнопка открытия панели действий со статьёй не найдена.", 5);
    this.waitForElementVisible(By.xpath(OPTIONS_MENU), "Внимание! Контекстное меню не найдено.", 15);
    this.waitForElementAndClick(By.xpath(OPTION_ADD_TO_MY_LIST_BUTTON), "Внимание! Элемент добавления статьи в список не найден.", 5);
    this.waitForElementAndClick(By.id(ADD_TO_MY_LIST_OVERLAY), "Внимание! Кнопка 'GOT IT' не найдена.", 5);
    this.waitForElementAndClear(By.id(NEW_LIST_NAME_INPUT), "Внимание! Поле ввода имени папки для добавления статьи не найдено.", 5);
    this.waitForElementAndSendKeys(By.id(NEW_LIST_NAME_INPUT), folderName, "Внимание! Невозможно ввести текст в поле ввода имени папки для добавления статьи.", 5);
    this.waitForElementAndClick(By.xpath(NEW_LIST_CREATION_OK_BUTTON), "Внимание! Невозможно нажать на кнопку 'OK'.", 5);
  }

  public void closeArticle() {
    this.waitForElementAndClick(By.xpath(CLOSE_ARTICLE_BUTTON), "Внимание! Кнопка закрытия статьи не найдена.", 5);
  }
}