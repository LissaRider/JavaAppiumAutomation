package lib.ui.android;

import io.appium.java_client.AppiumDriver;
import lib.ui.NavigationUI;

public class AndroidNavigationUI extends NavigationUI {
    static {
        MAIN_NAV_TAB_ELEMENT = "id:org.wikipedia:id/fragment_main_nav_tab_layout";
        READING_LISTS_LINK = "xpath://android.widget.FrameLayout[@content-desc='My lists']";
        MY_LISTS_PAGE_TITLE = "xpath://*[@resource-id='org.wikipedia:id/single_fragment_toolbar']/*[@text='My lists']";
    }

    public AndroidNavigationUI(AppiumDriver driver) {
        super(driver);
    }
}