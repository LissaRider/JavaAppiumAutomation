package lib;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import junit.framework.TestCase;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.time.Duration;

public class CoreTestCase extends TestCase {

    public static final String PLATFORM_ANDROID = "android";
    public static final String PLATFORM_IOS = "ios";

    protected AppiumDriver driver;
    private static final String appiumURL = "http://127.0.0.1:4723/wd/hub";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DesiredCapabilities capabilities = this.getCapabilitiesByPlatformEnv();
        this.getDriver(appiumURL, capabilities);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        driver.quit();
    }

    protected void rotateScreenPortrait() {
        driver.rotate(ScreenOrientation.PORTRAIT);
    }

    protected void rotateScreenLandscape() {
        driver.rotate(ScreenOrientation.LANDSCAPE);
    }

    protected void backgroundApp(int seconds) {
        driver.runAppInBackground(Duration.ofSeconds(seconds));
    }

    private DesiredCapabilities getCapabilitiesByPlatformEnv() throws Exception {
        String platform = System.getenv("PLATFORM");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String wikiPath = "";
        if (platform.equals(PLATFORM_ANDROID)) {
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("deviceName", "AndroidTestDevice");
            capabilities.setCapability("platformVersion", "8.0");
            capabilities.setCapability("automationName", "Appium");
            capabilities.setCapability("appPackage", "org.wikipedia");
            capabilities.setCapability("appActivity", ".main.MainActivity");
            capabilities.setCapability("orientation", "PORTRAIT");
            wikiPath = "./apks/org.wikipedia.apk";
        } else if (platform.equals(PLATFORM_IOS)) {
            capabilities.setCapability("platformName", "iOS");
            capabilities.setCapability("deviceName", "iPhone SE");
            capabilities.setCapability("platformVersion", "14.5");
            capabilities.setCapability("udid", "B8379464-F378-4117-94ED-3633C95672BF");
            wikiPath = "./apks/Wikipedia.app";
        } else {
            throw new Exception(String.format("\n  Внимание! Невозможно запустить платформу из переменной окружения: %s.", platform));
        }
        capabilities.setCapability("orientation", "PORTRAIT");
        capabilities.setCapability("app", new File(wikiPath).getCanonicalPath());
        return capabilities;
    }

    private AppiumDriver getDriver(String appiumURL, DesiredCapabilities capabilities) throws Exception {
        String platform = System.getenv("PLATFORM");
        if (platform.equals(PLATFORM_ANDROID)) {
            driver = new AndroidDriver(new URL(appiumURL), capabilities);
        } else if (platform.equals(PLATFORM_IOS)) {
            driver = new IOSDriver(new URL(appiumURL), capabilities);
        } else {
            throw new Exception(String.format("\n  Внимание! Невозможно запустить платформу из переменной окружения: %s.", platform));
        }
        return driver;
    }
}