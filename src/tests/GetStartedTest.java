package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.WelcomePageObject;
import org.junit.Test;

public class GetStartedTest extends CoreTestCase {

    /**
     * Для успешного прохождения теста необходимо в классе {@link lib.CoreTestCase}
     * отключить метод {@link CoreTestCase#setUp()#skipWelcomePageForIOSApp()}
     * @see CoreTestCase
     */
    @Test
    public void testPassThroughWelcome() {

        if (Platform.getInstance().isAndroid()) {
            return;
        }

        WelcomePageObject welcomePage = new WelcomePageObject(driver);

        welcomePage.waitForLearnMoreLink();
        welcomePage.clickNextButton();

        welcomePage.waitForNewWayToExploreText();
        welcomePage.clickNextButton();

        welcomePage.waitForAddOrEditPreferredLangText();
        welcomePage.clickNextButton();

        welcomePage.waitForLearnMoreAboutDataCollectedText();
        welcomePage.clickGetStartedButton();
    }
}