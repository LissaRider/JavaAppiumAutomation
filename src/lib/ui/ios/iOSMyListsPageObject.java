package lib.ui.ios;

import io.appium.java_client.AppiumDriver;
import lib.ui.MyListsPageObject;

public class iOSMyListsPageObject extends MyListsPageObject
{
    static {
        ARTICLE_BY_TITLE_TPL = "xpath://XCUIElementTypeStaticText[contains(@name,'{TITLE}')]";
        SYNC_YOUR_SAVED_ARTICLES_POPUP = "id:Sync your saved articles?";
        CLOSE_SYNC_POPUP_BUTTON = "id:Close";
    }

    public iOSMyListsPageObject(AppiumDriver driver){
        super(driver);
    }
}