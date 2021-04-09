import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

  private void assertElementHasText(By by, String expected, String errorMessage) {
    String actual = driver.findElement(by).getAttribute("text");
    Assert.assertEquals(errorMessage, expected, actual);
  }

  private WebElement waitForElementPresent(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    return wait.until(ExpectedConditions.presenceOfElementLocated(by));
  }


  private WebElement waitForElementPresent(By by, String errorMessage) {
    return waitForElementPresent(by, errorMessage, 5);
  }

  private WebElement waitForElementAndClick(By by, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
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
}