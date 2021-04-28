package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MainPageObject {
  protected AppiumDriver<?> driver;

  public MainPageObject(AppiumDriver<?> driver) {
    this.driver = driver;
  }

  public boolean assertElementPresent(By by, String errorMessage) {
    try {
      return getAmountOfElements(by) > 0;
    } catch (Exception e) {
      String defaultMessage = String.format("\n  Ошибка! Элемент c локатором '%s' должен присутствовать.\n  ", by);
      throw new AssertionError(defaultMessage + errorMessage);
    }
  }

  public void assertElementHasText(By by, String expected, String errorMessage) {
    String actual = driver.findElement(by).getAttribute("text");
    Assert.assertEquals(errorMessage, expected, actual);
  }

  public WebElement waitForElementPresent(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.presenceOfElementLocated(by));
  }

  public WebElement waitForElementVisible(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
  }

  public WebElement waitForElementClickable(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.elementToBeClickable(by));
  }

  public WebElement waitForElementPresent(By by, String errorMessage) {
    return waitForElementPresent(by, errorMessage, 5);
  }

  public WebElement waitForElementAndClick(By by, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
    element.click();
    return element;
  }

  public WebElement waitForElementClickableAndClick(By by, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementClickable(by, errorMessage, timeoutInSeconds);
    element.click();
    return element;
  }

  public WebElement waitForElementAndSendKeys(By by, String value, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
    element.sendKeys(value);
    return element;
  }

  public boolean waitForElementNotPresent(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
  }

  public WebElement waitForElementAndClear(By by, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
    element.clear();
    return element;
  }

  public List<WebElement> waitForNumberOfElementsToBeMoreThan(By by, int number, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(by, number));
  }

  public List<WebElement> waitForPresenceOfAllElements(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
  }

  public void swipeUp(int timeOfSwipe) {
    TouchAction action = new TouchAction(driver);
    Dimension size = driver.manage().window().getSize();
    int x = size.width / 2;
    int startY = (int) (size.height * 0.8);
    int endY = (int) (size.height * 0.2);
    action
            .press(x, startY)
            .waitAction(timeOfSwipe)
            .moveTo(x, endY)
            .release()
            .perform();
  }

  public void swipeUpQuick() {
    swipeUp(200);
  }

  public void swipeUpToFindElement(By by, String errorMessage, int maxSwipes) {
    int alreadySwiped = 0;
    while (driver.findElements(by).size() == 0) {
      if (alreadySwiped > maxSwipes) {
        waitForElementPresent(by, "Внимание! При прокрутке вниз элемент с локатором '" + by + "' не найден.\n" +
                errorMessage, 0);
        return;
      }
      swipeUpQuick();
      ++alreadySwiped;
    }
  }

  public void swipeElementToLeft(By by, String errorMessage) {

    WebElement element = waitForElementPresent(by, errorMessage, 10);

    int leftX = element.getLocation().getX();
    int rightX = leftX + element.getSize().getWidth();
    int upperY = element.getLocation().getY();
    int lowerY = upperY + element.getSize().getHeight();
    int middleY = (upperY + lowerY) / 2;

    TouchAction action = new TouchAction(driver);
    action
            .press(rightX, middleY)
            .waitAction(300)
            .moveTo(leftX, middleY)
            .release()
            .perform();
  }

  public int getAmountOfElements(By by) {
    List<?> elements = driver.findElements(by);
    return elements.size();
  }

  public void assertElementNotPresent(By by, String errorMessage) {
    int amountOfSearchResults = getAmountOfElements(by);
    if (amountOfSearchResults > 0) {
      String defaultMessage = String.format("\n  Ошибка! Элемент c локатором '%s' должен отсутствовать.\n  ", by);
      throw new AssertionError(defaultMessage + errorMessage);
    }
  }

  public String waitForElementAndGetAttribute(By by, String attribute, String errorMessage, long timeoutInSec) {
    WebElement element = waitForElementPresent(by, errorMessage, timeoutInSec);
    return element.getAttribute(attribute);
  }

  public boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  public void searchFor(String searchLine) {

    By searchWikipediaLocator = By.xpath("//android.widget.ImageView[@content-desc='Search Wikipedia']");
    By menuPageSearchLocator = By.id("org.wikipedia:id/menu_page_search");
    By searchTextLocator = By.id("org.wikipedia:id/search_src_text");

    if (isElementPresent(searchWikipediaLocator)) waitForElementClickableAndClick(
            searchWikipediaLocator,
            "Внимание! На главной странице элемент 'Search Wikipedia' не найден или недоступен для действий.",
            5
    );
    else waitForElementClickableAndClick(
            menuPageSearchLocator,
            "Внимание! На странице статьи элемент 'Search Wikipedia' не найден или недоступен для действий.",
            5
    );

    waitForElementAndSendKeys(
            searchTextLocator,
            searchLine,
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );
  }

  public void openArticle(String articleTitle) {

    By resultItemLocator = By.id("org.wikipedia:id/page_list_item_container");
    By resultTitleLocator = By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + articleTitle + "']");
    By pageTitleLocator = By.id("org.wikipedia:id/view_page_title_text");

    waitForNumberOfElementsToBeMoreThan(
            resultItemLocator,
            0,
            "Внимание! В результатах поиска ни одной статьи не найдено.",
            15
    );

    waitForElementClickableAndClick(
            resultTitleLocator,
            String.format("Внимание! Статья с заголовком '%s' не найдена или недоступна для действий.", articleTitle),
            5
    );

    waitForElementPresent(
            pageTitleLocator,
            String.format("Внимание! Заголовок статьи '%s' не найден.", articleTitle),
            15
    );
  }

  public void selectOption(String option) {

    By pageToolbarLocator = By.id("org.wikipedia:id/page_toolbar");
    By moreOptionsButtonLocator = By.xpath("//android.widget.ImageView[@content-desc='More options']");
    By optionListLocator = By.xpath("//android.widget.ListView");
    By optionLocator = By.xpath("//*[@resource-id='org.wikipedia:id/title'][@text='" + option + "']");

    waitForElementVisible(
            pageToolbarLocator,
            "Внимание! На странице верхняя панель инструментов не найдена.",
            15
    );

    waitForElementClickableAndClick(
            moreOptionsButtonLocator,
            "Внимание! Кнопка открытия меню действий со статьёй не найдена или недоступна для действий.",
            5
    );

    waitForElementVisible(
            optionListLocator,
            "Внимание! Меню действий со статьёй не найдено.",
            15
    );

    waitForElementClickableAndClick(
            optionLocator,
            String.format("Внимание! В меню действий со статьёй пция '%s' не найдена или недоступна для действий.", option),
            5
    );
  }

  public void createAndAddArticleTo(String folderName) {

    By bottomSheetLocator = By.id("org.wikipedia:id/design_bottom_sheet");
    By onboardingButtonLocator = By.id("org.wikipedia:id/onboarding_button");
    By createButtonLocator = By.id("org.wikipedia:id/create_button");
    By parentPanelLocator = By.id("org.wikipedia:id/parentPanel");
    By textInputLocator = By.id("org.wikipedia:id/text_input");
    By acceptButtonLocator = By.xpath("//*[@resource-id='org.wikipedia:id/buttonPanel']//*[@text='OK']");

    waitForElementVisible(
            bottomSheetLocator,
            "Внимание! Элемент 'Bottom Sheet' не найден.",
            15
    );

    if (isElementPresent(onboardingButtonLocator)) waitForElementClickableAndClick(
            onboardingButtonLocator,
            "Внимание! Кнопка 'GOT IT' не найдена или недоступна для действий.",
            5
    );
    else waitForElementClickableAndClick(
            createButtonLocator,
            "Внимание! Кнопка 'Create' не найдена или недоступна для действий.",
            5
    );

    waitForElementVisible(
            parentPanelLocator,
            "Внимание! Форма создания новой папки для добавления сстатьи не найдена.",
            15
    );

    waitForElementAndClear(
            textInputLocator,
            "Внимание! Поле ввода имени новой папки для добавления статьи не найдено.",
            5
    );

    waitForElementAndSendKeys(
            textInputLocator,
            folderName,
            "Внимание! Невозможно ввести текст в поле ввода имени папки для добавления статьи.",
            5
    );

    waitForElementClickableAndClick(
            acceptButtonLocator,
            "Внимание! Кнопка 'OK' не найдена или недоступна для действий.",
            5
    );
  }

  public void addArticleTo(String folderName) {

    By bottomSheetLocator = By.id("org.wikipedia:id/design_bottom_sheet");
    By folderByNameLocator = By.xpath("//*[@resource-id='org.wikipedia:id/item_title'][@text='" + folderName + "']");

    waitForElementVisible(
            bottomSheetLocator,
            "Внимание! Элемент 'Bottom Sheet' не найден.",
            15
    );

    waitForElementClickableAndClick(
            folderByNameLocator,
            String.format("Внимание! Папка с именем '%s' не найдена или недоступна для действий.", folderName),
            5
    );
  }

  public void closeArticle() {

    By pageToolbarLocator = By.id("org.wikipedia:id/page_toolbar");
    By navigateUpButtonLocator = By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']");

    waitForElementVisible(
            pageToolbarLocator,
            "Внимание! На странице панель инструментов не найдена.",
            15
    );

    waitForElementClickableAndClick(
            navigateUpButtonLocator,
            "Внимание! Кнопка закрытия статьи не найдена или недоступна для действий.",
            5
    );
  }

  public void openMyLists() {

    By mainNavTabLocator = By.id("org.wikipedia:id/fragment_main_nav_tab_layout");
    By openMyListsButtonLocator = By.xpath("//android.widget.FrameLayout[@content-desc='My lists']");
    By myListsPageTitleLocator = By.xpath("//*[@resource-id='org.wikipedia:id/single_fragment_toolbar']/*[@text='My lists']");

    waitForElementVisible(
            mainNavTabLocator,
            "Внимание! Главная пнель навигации не найдена.",
            15
    );

    waitForElementClickableAndClick(
            openMyListsButtonLocator,
            "Внимание! Кнопка перехода к спискам не найдена или недоступна для действий.",
            5
    );

    waitForElementPresent(
            myListsPageTitleLocator,
            "Внимание! Переход к списку личных папок со статьями не был выполнен.",
            15
    );
  }

  public void openFolder(String folderName) {

    By itemContainerLocator = By.id("org.wikipedia:id/item_container");
    By folderNameLocator = By.xpath("//*[@resource-id='org.wikipedia:id/item_title'][@text='" + folderName + "']");

    waitForNumberOfElementsToBeMoreThan(
            itemContainerLocator,
            0,
            "Внимание! В списке 'My lists' ни одной папки не найдено.",
            15
    );


    waitForElementClickableAndClick(
            folderNameLocator,
            String.format("Внимание! Папка '%s' не найдена.", folderName),
            5
    );

    waitForElementVisible(
            folderNameLocator,
            String.format("Внимание! Заголовок папки '%s' не найден.", folderName),
            15
    );
  }
}
