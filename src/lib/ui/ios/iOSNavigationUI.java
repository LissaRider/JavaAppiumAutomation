package lib.ui.ios;

import io.appium.java_client.AppiumDriver;
import lib.ui.NavigationUI;

public class iOSNavigationUI extends NavigationUI
{
    static{
        READING_LISTS_LINK = "id:Saved";
        MAIN_NAV_TAB_ELEMENT = "id:Tab Bar";
        MY_LISTS_PAGE_TITLE ="xpath://XCUIElementTypeToolbar[@name='Toolbar']//XCUIElementTypeStaticText[@name='Saved']";
    }

    public iOSNavigationUI(AppiumDriver driver){
        super(driver);
    }
}