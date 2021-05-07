package lib.ui.ios;

import io.appium.java_client.AppiumDriver;
import lib.ui.ArticlePageObject;

public class iOSArticlePageObject extends ArticlePageObject {
    static {
        ARTICLE_TITLE="xpath:(//XCUIElementTypeOther[@name='banner']//XCUIElementTypeStaticText[@name])[1]";
        ARTICLE_SEARCH_INIT_ELEMENT = "xpath://XCUIElementTypeNavigationBar//XCUIElementTypeButton[@name='Search Wikipedia']";
        ARTICLE_FOOTER_ELEMENT = "id:View article in browser";
        SAVE_FOR_LATER_BUTTON = "id:Save for later";
        TAP_TO_GO_BACK_BUTTON = "xpath://XCUIElementTypeButton[@name='W']";
    }

    public iOSArticlePageObject(AppiumDriver driver) {
        super(driver);
    }
}