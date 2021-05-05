package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MainPageObject {

  protected AppiumDriver driver;

  public MainPageObject(AppiumDriver driver) {
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
    Assert.assertEquals("\n  Ошибка! " + errorMessage + "\n", expected, actual);
  }

  public WebElement waitForElementPresent(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  Внимание! " + errorMessage + "\n");
    return wait.until(ExpectedConditions.presenceOfElementLocated(by));
  }

  public WebElement waitForElementVisible(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  Внимание! " + errorMessage + "\n");
    return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
  }

  public WebElement waitForElementClickable(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  Внимание! " + errorMessage + "\n");
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
    wait.withMessage("\n  Внимание! " + errorMessage + "\n");
    return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
  }

  public WebElement waitForElementAndClear(By by, String errorMessage, long timeoutInSeconds) {
    WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
    element.clear();
    return element;
  }

  public List<WebElement> waitForNumberOfElementsToBeMoreThan(By by, int number, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  Внимание! " + errorMessage + "\n");
    return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(by, number));
  }

  public List<WebElement> waitForPresenceOfAllElements(By by, String errorMessage, long timeoutInSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
    wait.withMessage("\n  Внимание! " + errorMessage + "\n");
    return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
  }

  public void swipeUp(int timeOfSwipe) {
    TouchAction action = new TouchAction(driver);
    Dimension size = driver.manage().window().getSize();
    int x = size.width / 2;
    int startY = (int) (size.height * 0.8);
    int endY = (int) (size.height * 0.2);
    action
            .press(PointOption.point(x, startY))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(timeOfSwipe)))
            .moveTo(PointOption.point(x, endY))
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
        waitForElementPresent(by, String.format("При прокрутке вниз элемент с локатором '%s' не найден.\n  %s", by, errorMessage), 0);
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
            .press(PointOption.point(rightX, middleY))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)))
            .moveTo(PointOption.point(leftX, middleY))
            .release()
            .perform();
  }

  public int getAmountOfElements(By by) {
    List elements = driver.findElements(by);
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
}