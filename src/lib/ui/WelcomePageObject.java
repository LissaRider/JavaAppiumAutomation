package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class WelcomePageObject extends MainPageObject {

    public static final String LEARN_MORE_ABOUT_WIKIPEDIA_LINK = "Learn more about Wikipedia",
            NEW_WAYS_TO_EXPLORE_TEXT = "New ways to explore",
            ADD_OR_EDIT_PREFERRED_LANG_LINK = "Add or edit preferred languages",
            LEARN_MORE_ABOUT_DATA_COLLECTED_LINK = "Learn more about data collected",
            NEXT_LINK = "Next",
            GET_STARTED_BUTTON = "Get started";

    public WelcomePageObject(AppiumDriver driver) {
        super(driver);
    }

    public void waitForLearnMoreLink() {
        this.waitForElementPresent(By.id(LEARN_MORE_ABOUT_WIKIPEDIA_LINK), "Ссылка 'Learn more about Wikipedia' не найдена.", 10);
    }

    public void waitForNewWayToExploreText() {
        this.waitForElementPresent(By.id(NEW_WAYS_TO_EXPLORE_TEXT), "Заголовок страницы 'New ways to explore' не найден.", 10);
    }

    public void waitForAddOrEditPreferredLangText() {
        this.waitForElementPresent(By.id(ADD_OR_EDIT_PREFERRED_LANG_LINK), "Ссылка 'Add or edit preferred languages' не найдена.", 10);
    }

    public void waitForLearnMoreAboutDataCollectedText() {
        this.waitForElementPresent(By.id(LEARN_MORE_ABOUT_DATA_COLLECTED_LINK), "Ссылка 'Learn more about data collected' не найдена.", 10);
    }

    public void clickNextButton() {
        this.waitForElementClickableAndClick(By.id(NEXT_LINK), "Кнопка 'Next' не найдена или недоступна для действий.", 10);
    }

    public void clickGetStartedButton() {
        this.waitForElementClickableAndClick(By.id(GET_STARTED_BUTTON), "Кнопка 'Get started' не найдена или недоступна для действий.", 10);
    }
}
