package lib.ui.ios;

import io.appium.java_client.AppiumDriver;
import lib.ui.MyListsPageObject;

public class iOSMyListsPageObject extends MyListsPageObject
{
    static {
        ARTICLE_BY_TITLE_TPL = "xpath://XCUIElementTypeStaticText[contains(@name,'{ARTICLE_TITLE}')]";
        SYNC_YOUR_SAVED_ARTICLES_POPUP = "id:Sync your saved articles?";
        CLOSE_SYNC_POPUP_BUTTON = "id:Close";
        ARTICLE_LIST = "xpath://XCUIElementTypeCollectionView";
        ARTICLE_LIST_ITEM ="xpath://XCUIElementTypeCell";
    }

    public iOSMyListsPageObject(AppiumDriver driver){
        super(driver);
    }
}