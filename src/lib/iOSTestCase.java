package lib;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import junit.framework.TestCase;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.time.Duration;

public class iOSTestCase extends TestCase {

  protected AppiumDriver driver;
  private static final String appiumURL = "http://127.0.0.1:4723/wd/hub";

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("platformName", "iOS");
    capabilities.setCapability("deviceName", "iPhone SE");
    capabilities.setCapability("platformVersion", "14.5");
    capabilities.setCapability("udid", "B8379464-F378-4117-94ED-3633C95672BF");

    capabilities.setCapability("orientation", "PORTRAIT");

    String oldWikiPath = "./apks/Wikipedia.app";
    capabilities.setCapability("app", new File(oldWikiPath).getCanonicalPath());

    driver = new IOSDriver(new URL(appiumURL), capabilities);
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
