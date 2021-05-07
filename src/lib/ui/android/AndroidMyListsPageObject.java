package lib.ui.android;

import io.appium.java_client.AppiumDriver;
import lib.ui.MyListsPageObject;

public class AndroidMyListsPageObject extends MyListsPageObject {
    static {
        ARTICLE_LIST = "id:org.wikipedia:id/item_container";
        FOLDER_BY_NAME_TPL = "xpath://*[@resource-id='org.wikipedia:id/item_title'][@text='{FOLDER_NAME}']";
        ARTICLE_LIST_ITEM = "id:org.wikipedia:id/page_list_item_container";
        ARTICLE_BY_TITLE_TPL = "xpath://*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='{ARTICLE_TITLE}']";
    }

    public AndroidMyListsPageObject(AppiumDriver driver) {
        super(driver);
    }
}