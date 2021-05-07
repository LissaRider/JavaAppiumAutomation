package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import lib.Platform;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

public class MainPageObject {

    protected AppiumDriver driver;

    public MainPageObject(AppiumDriver driver) {
        this.driver = driver;
    }

    public boolean assertElementPresent(String locator, String errorMessage) {
        try {
            return getAmountOfElements(locator) > 0;
        } catch (Exception e) {
            String defaultMessage = String.format("\n  Ошибка! Элемент c локатором '%s' должен присутствовать.\n  ", locator);
            throw new AssertionError(defaultMessage + errorMessage);
        }
    }

    public void assertElementHasText(String locator, String expected, String errorMessage) {
        By by = this.getLocatorByString(locator);
        WebElement element = driver.findElement(by);
        String actual = Platform.getInstance().isAndroid()
                ? element.getAttribute("text")
                : element.getAttribute("value");
        Assert.assertEquals(String.format("\n  Ошибка! %s\n", errorMessage), expected, actual);
    }

    public WebElement waitForElementPresent(String locator, String errorMessage, long timeoutInSeconds) {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(String.format("\n  Внимание! %s\n", errorMessage));
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public WebElement waitForElementVisible(String locator, String errorMessage, long timeoutInSeconds) {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(String.format("\n  Внимание! %s\n", errorMessage));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public WebElement waitForElementClickable(String locator, String errorMessage, long timeoutInSeconds) {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(String.format("\n  Внимание! %s\n", errorMessage));
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public WebElement waitForElementPresent(String locator, String errorMessage) {
        return waitForElementPresent(locator, errorMessage, 5);
    }

    public WebElement waitForElementAndClick(String locator, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(locator, errorMessage, timeoutInSeconds);
        element.click();
        return element;
    }

    public WebElement waitForElementClickableAndClick(String locator, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementClickable(locator, errorMessage, timeoutInSeconds);
        element.click();
        return element;
    }

    public WebElement waitForElementAndSendKeys(String locator, String value, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(locator, errorMessage, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    public boolean waitForElementNotPresent(String locator, String errorMessage, long timeoutInSeconds) {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(String.format("\n  Внимание! %s\n", errorMessage));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public WebElement waitForElementAndClear(String locator, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(locator, errorMessage, timeoutInSeconds);
        element.clear();
        return element;
    }

    public List<WebElement> waitForNumberOfElementsToBeMoreThan(String locator, int number, String errorMessage, long timeoutInSeconds) {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(String.format("\n  Внимание! %s\n", errorMessage));
        return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(by, number));
    }

    public List<WebElement> waitForPresenceOfAllElements(String locator, String errorMessage, long timeoutInSeconds) {
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(String.format("\n  Внимание! %s\n", errorMessage));
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

    public void swipeUpToFindElement(String locator, String errorMessage, int maxSwipes) {
        By by = this.getLocatorByString(locator);
        int alreadySwiped = 0;
        while (driver.findElements(by).size() == 0) {
            if (alreadySwiped > maxSwipes) {
                waitForElementPresent(locator, String.format("При прокрутке вниз элемент с локатором '%s' не найден.\n  %s", by, errorMessage), 0);
                return;
            }
            swipeUpQuick();
            ++alreadySwiped;
        }
    }

    public void swipeUpTillElementAppear(String locator, String error_message, int max_swipes) {
        int already_swiped = 0;
        while (!this.isElementLocatedOnTheScreen(locator)) {
            if (already_swiped > max_swipes) {
                Assert.assertTrue(error_message, this.isElementLocatedOnTheScreen(locator));
            }
            swipeUpQuick();
            ++already_swiped;
        }
    }

    public boolean isElementLocatedOnTheScreen(String locator) {
        int elementLocationByY = this.waitForElementPresent(
                locator, String.format("Элемент с локатором '%s' не найден.\n", locator), 1)
                .getLocation()
                .getY();
        int screenSizeByY = driver.manage().window().getSize().getHeight();
        return elementLocationByY < screenSizeByY;
    }

    public void clickElementToTheRightUpperCorner(String locator, String errorMessage) {
        WebElement element = this.waitForElementPresent(locator + "/..", errorMessage);
        int rightX = element.getLocation().getX();
        int upperY = element.getLocation().getY();
        int lowerY = upperY + element.getSize().getHeight();
        int middleY = (upperY + lowerY) / 2;
        int width = element.getSize().getWidth();

        int pointToClickX = (rightX + width) - 3;
        int pointToClickY = middleY;

        TouchAction action = new TouchAction(driver);
        action.tap(PointOption.point(pointToClickX, pointToClickY)).perform();
    }

    public void swipeElementToLeft(String locator, String errorMessage) {

        WebElement element = waitForElementPresent(locator, errorMessage, 10);

        int leftX = element.getLocation().getX();
        int rightX = leftX + element.getSize().getWidth();
        int upperY = element.getLocation().getY();
        int lowerY = upperY + element.getSize().getHeight();
        int middleY = (upperY + lowerY) / 2;

        TouchAction action = new TouchAction(driver);
        action
                .press(PointOption.point(rightX, middleY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)));
        if (Platform.getInstance().isAndroid()) {
            action.moveTo(PointOption.point(leftX, middleY));
        } else {
            int offsetX = (-1 * element.getSize().getWidth());
            action.moveTo(PointOption.point(offsetX, 0));
        }
        action.release().perform();
    }

    public int getAmountOfElements(String locator) {
        By by = this.getLocatorByString(locator);
        List elements = driver.findElements(by);
        return elements.size();
    }

    public void assertElementNotPresent(String locator, String errorMessage) {
        int amountOfSearchResults = getAmountOfElements(locator);
        if (amountOfSearchResults > 0) {
            String defaultMessage = String.format("\n  Ошибка! Элемент c локатором '%s' должен отсутствовать.\n  ", locator);
            throw new AssertionError(defaultMessage + errorMessage);
        }
    }

    public String waitForElementAndGetAttribute(String locator, String attribute, String errorMessage, long timeoutInSec) {
        WebElement element = waitForElementPresent(locator, errorMessage, timeoutInSec);
        return element.getAttribute(attribute);
    }

    public boolean isElementPresent(String locator) {
        By by = this.getLocatorByString(locator);
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Метод для определения типа локатора
     *
     * @param locatorWithType (String) локатор с заданным типом в формате <b>type:locator</b>
     * @return By
     */
    private By getLocatorByString(String locatorWithType) {
        String[] explodedLocator = locatorWithType.split(Pattern.quote(":"), 2);
        String byType = explodedLocator[0];
        String locator = explodedLocator[1];
        switch (byType) {
            case "xpath":
                return By.xpath(locator);
            case "id":
                return By.id(locator);
            default:
                throw new IllegalArgumentException(String.format("Невозможно получить тип локатора: %s", locatorWithType));
        }
    }
}