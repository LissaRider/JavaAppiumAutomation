package lib;

import io.appium.java_client.android.AndroidDriver;
import junit.framework.TestCase;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.time.Duration;

public class CoreTestCase extends TestCase {

  protected AndroidDriver<?> driver;
  private static final String appiumURL = "http://127.0.0.1:4723/wd/hub";

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("platformName", "Android");
    capabilities.setCapability("deviceName", "AndroidTestDevice");
    capabilities.setCapability("platformVersion", "8.0");
    capabilities.setCapability("automationName", "Appium");
    capabilities.setCapability("appPackage", "org.wikipedia");
    capabilities.setCapability("appActivity", ".main.MainActivity");
    capabilities.setCapability("orientation", "PORTRAIT");

    String oldWikiPath = "./apks/org.wikipedia.old.apk";
    capabilities.setCapability("app", new File(oldWikiPath).getCanonicalPath());
/*        String stableWikiPath = "./apks/org.wikipedia.stable.apk";
        capabilities.setCapability("app", new File(stableWikiPath).getCanonicalPath());*/
/*        String betaWikiPath = "./apks/org.wikipedia.beta.apk";
        capabilities.setCapability("app", new File(betaWikiPath).getCanonicalPath());*/

    driver = new AndroidDriver<>(new URL(appiumURL), capabilities);
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    driver.quit();
  }

  protected  void rotateScreenPortrait(){
    driver.rotate(ScreenOrientation.PORTRAIT);
  }

  protected  void rotateScreenLandscape(){
    driver.rotate(ScreenOrientation.LANDSCAPE);
  }

  protected void backgroundApp(int seconds) {
    driver.runAppInBackground(Duration.ofSeconds(seconds));
  }
}
