package lib.ui.android;

import io.appium.java_client.AppiumDriver;
import lib.ui.ArticlePageObject;

public class AndroidArticlePageObject extends ArticlePageObject {
    static {
        ARTICLE_TITLE = "id:org.wikipedia:id/view_page_title_text";
        ARTICLE_FOOTER_ELEMENT = "xpath://*[@text='View page in browser']";
        ARTICLE_TOP_TOOLBAR = "id:org.wikipedia:id/page_toolbar";
        ARTICLE_SEARCH_INIT_ELEMENT = "id:org.wikipedia:id/menu_page_search";
        OPTIONS_BUTTON = "xpath://android.widget.ImageView[@content-desc='More options']";
        OPTIONS_MENU = "xpath://android.widget.ListView";
        OPTION_BY_TITLE_BUTTON_TPL = "xpath://*[@resource-id='org.wikipedia:id/title'][@text='{MENU_OPTION}']";
        ADD_TO_LIST_OVERLAY = "id:org.wikipedia:id/onboarding_button";
        NEW_LIST_NAME_INPUT = "id:org.wikipedia:id/text_input";
        NEW_LIST_CREATION_OK_BUTTON = "xpath://*[@resource-id='org.wikipedia:id/buttonPanel']//*[@text='OK']";
        CLOSE_ARTICLE_BUTTON = "xpath://android.widget.ImageButton[@content-desc='Navigate up']";
        NEW_LIST_CREATION_FORM = "id:org.wikipedia:id/parentPanel";
        CREATE_NEW_LIST_BUTTON = "id:org.wikipedia:id/create_button";
        ADD_TO_LIST_INIT_FORM = "id:org.wikipedia:id/design_bottom_sheet";
        READING_LIST_ELEMENT_TPL = "xpath://*[@resource-id='org.wikipedia:id/item_title'][@text='{FOLDER_NAME}']";
    }

    public AndroidArticlePageObject(AppiumDriver driver) {
        super(driver);
    }
}