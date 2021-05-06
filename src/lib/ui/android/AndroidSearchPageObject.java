package lib.ui.android;

import io.appium.java_client.AppiumDriver;
import lib.ui.SearchPageObject;

public class AndroidSearchPageObject extends SearchPageObject {

    static {
        MAIN_PAGE_SEARCH_INIT_ELEMENT = "xpath://android.widget.ImageView[@content-desc='Search Wikipedia']";
        SEARCH_INPUT_FIELD = "id:org.wikipedia:id/search_src_text";
        SEARCH_CANCEL_BUTTON = "id:org.wikipedia:id/search_close_btn";
        SEARCH_RESULT_BY_LIST_ITEM_SUBSTRING_TPL = "xpath://*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']";
        SEARCH_RESULT_LIST_ITEM_TITLE = "id:org.wikipedia:id/page_list_item_title";
        SEARCH_RESULT_BY_LIST_ITEM_TITLE_TPL = "xpath://*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='{TITLE}']";
        SEARCH_RESULT_LIST_ITEM = "id:org.wikipedia:id/page_list_item_container";
        SEARCH_RESULT_LIST = "id:org.wikipedia:id/search_results_list";
        SEARCH_EMPTY_RESULT_ELEMENT = "xpath://*[@text='No results found']";
        SEARCH_RESULT_BY_LIST_ITEM_TITLE_AND_DESCRIPTION_TPL =
                "xpath://*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                        "[.//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='{ARTICLE_TITLE}']]" +
                        "[.//*[@resource-id='org.wikipedia:id/page_list_item_description'][@text='{ARTICLE_DESCRIPTION}']]";

    }

    public AndroidSearchPageObject(AppiumDriver driver) {
        super(driver);
    }
}