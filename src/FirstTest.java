import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.util.List;

public class FirstTest {

  private AndroidDriver<?> driver;

  @Before
  public void SetUp() throws Exception {
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("platformName", "Android");
    capabilities.setCapability("deviceName", "AndroidTestDevice");
    capabilities.setCapability("platformVersion", "8.0");
    capabilities.setCapability("automationName", "Appium");
    capabilities.setCapability("appPackage", "org.wikipedia");
    capabilities.setCapability("appActivity", ".main.MainActivity");

    String oldWikiPath = "./apks/org.wikipedia.old.apk";
    capabilities.setCapability("app", new File(oldWikiPath).getCanonicalPath());
/*        String stableWikiPath = "./apks/org.wikipedia.stable.apk";
        capabilities.setCapability("app", new File(stableWikiPath).getCanonicalPath());*/
/*        String betaWikiPath = "./apks/org.wikipedia.beta.apk";
        capabilities.setCapability("app", new File(betaWikiPath).getCanonicalPath());*/

    driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
  }

  @After
  public void TearDown() {
    driver.quit();
  }

  @Test
  public void testWikiSearch() {

    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    waitForElementPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@text='Object-oriented programming language']"),
            "Внимание! Текст 'Object-oriented programming language' не найден.",
            15
    );
  }

  @Test
  public void testSearchPlaceholder() {

    waitForElementAndClick(
            By.id("org.wikipedia:id/search_container"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    waitForElementPresent(
            By.id("org.wikipedia:id/search_src_text"),
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    assertElementHasText(
            By.id("org.wikipedia:id/search_src_text"),
            "Search…",
            "\n Внимание! Поле ввода для поиска статьи содержит некорректный текст."
    );
  }

  @Test
  public void testCancelSearch() {

    waitForElementAndClick(
            By.id("org.wikipedia:id/search_container"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    waitForElementAndClear(
            By.id("org.wikipedia:id/search_src_text"),
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    waitForElementAndClick(
            By.id("org.wikipedia:id/search_close_btn"),
            "Внимание! Кнопка отмены поиска не найдена.",
            5
    );

    waitForElementNotPresent(
            By.id("org.wikipedia:id/search_close_btn"),
            "Внимание! Кнопка отмены поиска все ещё отображается.",
            5
    );
  }

  @Test
  public void testSearchAndClear() {

    // инициируем поиск
    waitForElementAndClick(
            By.id("org.wikipedia:id/search_container"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    // вводим соответствующее значение для поиска
    waitForElementAndSendKeys(
            By.id("org.wikipedia:id/search_src_text"),
            "Java",
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    // ждем отображения списка с количеством статей больше 1 (несколько статей)
    waitForNumberOfElementsToBeMoreThan(
            By.id("org.wikipedia:id/page_list_item_container"),
            1,
            "Внимание! Не найдено ни одной статьи или найдено статей меньше ожидаемого количества.",
            15
    );

    // чистим поле ввода (переиспользуем уже существующий метод, хотя ожидание здесь не требуется)
    waitForElementAndClear(
            By.id("org.wikipedia:id/search_src_text"),
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    // ждем чтобы ни одна статья не отображалась (тут может быть много вариаций)
    waitForElementNotPresent(
            By.id("org.wikipedia:id/page_list_item_container"),
            "Внимание! Список статей все ещё отображается.",
            15
    );
  }

  @Test
  public void testCompareArticleTitle() {

    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@text='Object-oriented programming language']"),
            "Внимание! Текст 'Object-oriented programming language' не найден.",
            5
    );

    WebElement titleElement = waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Внимание! Заголовок статьи не найден.",
            15
    );

    String articleTitle = titleElement.getAttribute("text");

    Assert.assertEquals(
            "\n Внимание! Отображается некорректный заголовок статьи.",
            "Java (programming language)",
            articleTitle
    );
  }

  @Test
  public void testSwipeArticle() {

    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Appium",
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Appium']"),
            "Внимание! Статья с заголовком 'Appium' не найдена.",
            5
    );

    waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Внимание! Заголовок статьи не найден.",
            15
    );

    swipeUpToFindElement(
            By.xpath(".//*[@text='View page in browser']"),
            "  Конец статьи не найден.",
            20
    );
  }

  @Test
  public void testSearchResults() {

    final String searchValue = "JAVA";

    waitForElementAndClick(
            By.id("org.wikipedia:id/search_container"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );


    waitForElementAndSendKeys(
            By.id("org.wikipedia:id/search_src_text"),
            searchValue,
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    List<WebElement> articleTitles = waitForPresenceOfAllElements(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@resource-id='org.wikipedia:id/page_list_item_title']"),
            "Внимание! Не найдено ни одной статьи.",
            15
    );

    for (int i = 0; i < articleTitles.size(); i++) {
      String articleTitle = articleTitles.get(i).getAttribute("text").toLowerCase();
      Assert.assertTrue(
              "\n Внимание! В заголовке найденной статьи с индексом [" + i + "]" +
                      " отсутствует заданное для поиска значение '" + searchValue + "'.\n",
              articleTitle.contains(searchValue.toLowerCase()));
    }
  }

  @Test
  public void testSaveFirstArticleToMyList() {

    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@text='Object-oriented programming language']"),
            "Внимание! Текст 'Object-oriented programming language' не найден.",
            5
    );

    waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Внимание! Заголовок статьи не найден.",
            15
    );

    waitForElementAndClick(
            By.xpath("//android.widget.ImageView[@content-desc='More options']"),
            "Внимание! Кнопка открытия панели действий со статьёй не найдена.",
            5
    );

    waitForElementVisible(
            By.xpath("//android.widget.ListView"),
            "Внимание! Контекстное меню не найдено.",
            15
    );

    waitForElementAndClick(
            By.xpath("//*[@text='Add to reading list']"),
            "Внимание! Элемент добавления статьи в список не найден.",
            5
    );

    waitForElementAndClick(
            By.id("org.wikipedia:id/onboarding_button"),
            "Внимание! Кнопка 'GOT IT' не найдена.",
            5
    );

    waitForElementAndClear(
            By.id("org.wikipedia:id/text_input"),
            "Поле ввода имени папки для добавления статьи не найдено.",
            5
    );

    String folderName = "Learning programming";

    waitForElementAndSendKeys(
            By.id("org.wikipedia:id/text_input"),
            folderName,
            "Внимание! Невозможно ввести текст в поле ввода имени папки для добавления статьи.",
            5
    );

    waitForElementAndClick(
            By.xpath("//*[@text='OK']"),
            "Внимание! Невозможно нажать на кнопку 'OK'.",
            5
    );

    waitForElementAndClick(
            By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
            "Внимание! Кнопка закрытия статьи не найдена.",
            5
    );

    waitForElementAndClick(
            By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
            "Внимание! Кнопка перехода к спискам не найдена.",
            5
    );

    waitForElementVisible(
            By.xpath("//*[@resource-id='org.wikipedia:id/item_container']"),
            "Внимание! Ни одной папки не найдено.",
            15
    );

    waitForElementAndClick(
            By.xpath(String.format("//*[@text='%s']", folderName)),
            "Внимание! Папка 'Learning programming' не найдена.",
            5
    );

    waitForElementVisible(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']"),
            "Внимание! Ни одной статьи не найдено.",
            15
    );

    swipeElementToLeft(
            By.xpath("//*[@text='Java (programming language)']"),
            "Внимание! В списке статья 'Java (programming language)' не найдена."
    );

    waitForElementNotPresent(
            By.xpath("//*[@text='Java (programming language)']"),
            "Внимание! Статья 'Java (programming language)' не удалилась из списка.",
            15
    );
  }

  @Test
  public void testAmountOfNotEmptySearch() {

    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    String searchLine = "Linkin Park Discography";
    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            searchLine,
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    String searchResultLocator = "//*[@resource-id='org.wikipedia:id/search_results_list']" +
            "/*[@resource-id='org.wikipedia:id/page_list_item_container']";

    waitForElementPresent(
            By.xpath(searchResultLocator),
            String.format("Внимание! По запросу поиска '%s' ничего не найдено.", searchLine),
            15
    );

    int amountOfSearchResults = getAmountOfElements(By.xpath(searchResultLocator));

    Assert.assertTrue("Найдено меньше результатов, чем ожидалось.",
            amountOfSearchResults > 1);
  }

  @Test
  public void testAmountOfEmptySearch() {

    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    String searchLine = "TIJIIOLLIKA";

    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            searchLine,
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    String searchResultLocator = "//*[@resource-id='org.wikipedia:id/search_results_list']" +
            "/*[@resource-id='org.wikipedia:id/page_list_item_container']";

    String emptyResultLabelLocator = "//*[@text='No results found']";

    waitForElementPresent(
            By.xpath(emptyResultLabelLocator),
            String.format("Внимание! По запросу поиска '%s' лэйбл 'No results found' не отобразился.", searchLine),
            15
    );

    assertElementNotPresent(By.xpath(searchResultLocator),
            String.format("Найдены результаты по запросу поиска '%s'.", searchLine));
  }

  @Test
  public void testChangeScreenOrientationOnSearchResults() {

    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    String searchLine = "Java";

    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            searchLine,
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@text='Object-oriented programming language']"),
            String.format("Внимание! По запросу поиска '%s' текст 'Object-oriented programming language' не найден.", searchLine),
            15
    );

    String titleBeforeRotation = waitForElementAndGetAttribute(
            By.id("org.wikipedia:id/view_page_title_text"),
            "text",
            "Внимание! Название статьи не найдено.",
            15
    );

    driver.rotate(ScreenOrientation.LANDSCAPE);

    String titleAfterRotation = waitForElementAndGetAttribute(
            By.id("org.wikipedia:id/view_page_title_text"),
            "text",
            "Внимание! Название статьи не найдено.",
            15
    );

    Assert.assertEquals(
            "\n  Внимание! Название статьи изменилось после изменения ориентации экрана.\n",
            titleBeforeRotation,
            titleAfterRotation
    );

    driver.rotate(ScreenOrientation.PORTRAIT);

    String titleAfterSecondRotation = waitForElementAndGetAttribute(
            By.id("org.wikipedia:id/view_page_title_text"),
            "text",
            "Внимание! Название статьи не найдено.",
            15
    );

    Assert.assertEquals(
            "\n  Внимание! Название статьи изменилось после изменения ориентации экрана.\n",
            titleBeforeRotation,
            titleAfterSecondRotation
    );
  }

  @Test
  public void testCheckSearchArticleInBackground() {

    waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    waitForElementPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@text='Object-oriented programming language']"),
            "Внимание! Текст 'Object-oriented programming language' не найден.",
            5
    );

    driver.runAppInBackground(2);

    waitForElementPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@text='Object-oriented programming language']"),
            "Внимание! После сворачивания/разворачивания приложения текст 'Object-oriented programming language' не найден.",
            5
    );
  }

  @Test
  public void testActionsWithArticlesInFolder() {

    String searchWotLine = "World of Tanks";

    searchFor(searchWotLine);

    openArticle(searchWotLine);

    selectOption("Add to reading list");

    String folderName = "Games";

    createAndAddArticleTo(folderName);

    String searchWowLine = "World of Warcraft";

    searchFor(searchWowLine);

    openArticle(searchWowLine);

    selectOption("Add to reading list");

    addArticleTo(folderName);

    closeArticle();

    openMyLists();

    openFolder(folderName);

    By searchResultLocator = By.id("org.wikipedia:id/page_list_item_container");
    By articleAboutWowLocator = By.xpath("//*[@text='" + searchWowLine + "']");
    By articleAboutWotLocator = By.xpath("//*[@text='" + searchWotLine + "']");
    By pageTitleLocator = By.id("org.wikipedia:id/view_page_title_text");

    waitForNumberOfElementsToBeMoreThan(
            searchResultLocator,
            0,
            String.format("Внимание! В папке '%s' не найдено ни одной статьи.", folderName),
            15
    );

    int amountOfArticlesBefore = getAmountOfElements(searchResultLocator);

    Assert.assertEquals(
            String.format("\nВнимание! В папке '%s' отображается некорректное количество статей.", folderName),
            amountOfArticlesBefore,
            2);

    swipeElementToLeft(
            articleAboutWowLocator,
            String.format("Внимание! В списке статья с заголовком '%s' не найдена.", searchWowLine)
    );

    waitForElementNotPresent(
            articleAboutWowLocator,
            String.format("Внимание! Статья '%s' не удалилась из списка.", articleAboutWowLocator),
            15
    );

    int amountOfArticlesAfter = getAmountOfElements(searchResultLocator);

    Assert.assertEquals(
            String.format("\nВнимание! В папке '%s' отображается некорректное количество статей.", folderName),
            amountOfArticlesBefore - 1,
            amountOfArticlesAfter
    );

    waitForElementClickableAndClick(
            articleAboutWotLocator,
            String.format("Внимание! В папке '%s' статья с заголовком '%s' не найдена или недоступна для действий.",
                    folderName, searchWotLine),
            15
    );

    String actualTitle = waitForElementAndGetAttribute(
            pageTitleLocator,
            "text",
            "Внимание! Название статьи не найдено.",
            15
    );

    Assert.assertEquals(
            "\nВнимание! Отображается некорректное название статьи.",
            searchWotLine,
            actualTitle
    );
  }

  private void assertElementHasText(By by, String expected, String errorMessage) {
    String actual = driver.findElement(by).getAttribute("text");
    Assert.assertEquals(errorMessage, expected, actual);
  }

  private WebElement waitForElementPresent(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.presenceOfElementLocated(by));
  }

  private WebElement waitForElementVisible(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
  }

  private WebElement waitForElementClickable(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.elementToBeClickable(by));
  }

  private WebElement waitForElementPresent(By by, String errorMessage) {
    return waitForElementPresent(by, errorMessage, 5);
  }

  private WebElement waitForElementAndClick(By by, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
    element.click();
    return element;
  }

  private WebElement waitForElementClickableAndClick(By by, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementClickable(by, errorMessage, timeoutInSeconds);
    element.click();
    return element;
  }

  private WebElement waitForElementAndSendKeys(By by, String value, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
    element.sendKeys(value);
    return element;
  }

  private boolean waitForElementNotPresent(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
  }

  private WebElement waitForElementAndClear(By by, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
    element.clear();
    return element;
  }

  private List<WebElement> waitForNumberOfElementsToBeMoreThan(By by, int number, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(by, number));
  }

  private List<WebElement> waitForPresenceOfAllElements(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
  }

  protected void swipeUp(int timeOfSwipe) {
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

  protected void swipeUpQuick() {
    swipeUp(200);
  }

  protected void swipeUpToFindElement(By by, String errorMessage, int maxSwipes) {
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

  protected void swipeElementToLeft(By by, String errorMessage) {

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

  private int getAmountOfElements(By by) {
    List<?> elements = driver.findElements(by);
    return elements.size();
  }

  private void assertElementNotPresent(By by, String errorMessage) {
    int amountOfSearchResults = getAmountOfElements(by);
    if (amountOfSearchResults > 0) {
      String defaultMessage = String.format("\n  Внимание! Элемент c локатором '%s' должен отсутствовать.\n  ", by);
      throw new AssertionError(defaultMessage + errorMessage);
    }
  }

  private String waitForElementAndGetAttribute(By by, String attribute, String errorMessage, long timeoutInSec) {
    WebElement element = waitForElementPresent(by, errorMessage, timeoutInSec);
    return element.getAttribute(attribute);
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private void searchFor(String searchLine) {

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

  private void openArticle(String articleTitle) {

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

  private void selectOption(String option) {

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

  private void createAndAddArticleTo(String folderName) {

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

  private void addArticleTo(String folderName) {

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

  private void closeArticle() {

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

  private void openMyLists() {

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

  private void openFolder(String folderName) {

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