import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;

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
  public void firstTest() {

    waitForElementByXpathAndClick(
            "//*[contains(@text,'Search Wikipedia')]",
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    waitForElementByXpathAndSendKeys(
            "//*[contains(@text,'Search…')]",
            "Java",
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    waitForElementPresentByXpath(
            "//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@text='Object-oriented programming language']",
            "Внимание! Текст 'Object-oriented programming language' не найден.",
            15
    );
  }

  @Test
  public void testCancelSearch() {

    waitForElementByIdAndClick(
            "org.wikipedia:id/search_container",
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    waitForElementByIdAndClick(
            "org.wikipedia:id/search_close_btn",
            "Внимание! Кнопка отмены поиска не найдена.",
            5
    );

    waitForElementNotPresentById(
            "org.wikipedia:id/search_close_btn",
            "Внимание! Кнопка отмены поиска все ещё отображается.",
            5
    );
  }

  private WebElement waitForElementPresentByXpath(String xpath, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    By by = By.xpath(xpath);
    return wait.until(ExpectedConditions.presenceOfElementLocated(by));
  }

  private WebElement waitForElementPresentByXpath(String xpath, String errorMessage) {
    return waitForElementPresentByXpath(xpath, errorMessage, 5);
  }

  private WebElement waitForElementByXpathAndClick(String xpath, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementPresentByXpath(xpath, errorMessage, timeoutInSeconds);
    element.click();
    return element;
  }

  private WebElement waitForElementByXpathAndSendKeys(String xpath, String value, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementPresentByXpath(xpath, errorMessage, timeoutInSeconds);
    element.sendKeys(value);
    return element;
  }

  private WebElement waitForElementPresentById(String id, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    By by = By.id(id);
    return wait.until(ExpectedConditions.presenceOfElementLocated(by));
  }

  private WebElement waitForElementByIdAndClick(String id, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementPresentById(id, errorMessage, timeoutInSeconds);
    element.click();
    return element;
  }

  private boolean waitForElementNotPresentById(String id, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  " + errorMessage + "\n");
    By by = By.id(id);
    return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
  }
}