package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.SearchPageObject;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class ChangeAppConditionTests extends CoreTestCase {

    @Test
    public void testChangeScreenOrientationOnSearchResults() {

        SearchPageObject searchPage = SearchPageObjectFactory.get(driver);
        ArticlePageObject articlePage = ArticlePageObjectFactory.get(driver);

        final String searchLine = "Java";
        searchPage.searchByValue(searchLine);

        final String substring = "Object-oriented programming language";
        searchPage.clickByArticleWithSubstring(substring);

        articlePage.waitForTitleElement();
        String titleBeforeRotation = articlePage.getArticleTitle();

        this.rotateScreenLandscape();

        articlePage.waitForTitleElement();
        String titleAfterRotation = articlePage.getArticleTitle();

        assertEquals(
                "\n  Ошибка! Название статьи изменилось после изменения ориентации экрана.\n",
                titleBeforeRotation,
                titleAfterRotation
        );

        this.rotateScreenPortrait();

        articlePage.waitForTitleElement();
        String titleAfterSecondRotation = articlePage.getArticleTitle();

        assertEquals(
                "\n  Ошибка! Название статьи изменилось после изменения ориентации экрана.\n",
                titleBeforeRotation,
                titleAfterSecondRotation
        );
    }

    @Test
    public void testCheckSearchArticleInBackground() {

        SearchPageObject searchPage = SearchPageObjectFactory.get(driver);

        final String searchLine = "Java";
        searchPage.searchByValue(searchLine);

        final String substring = "Object-oriented programming language";
        searchPage.waitForSearchResult(substring);

        this.backgroundApp(2);

        searchPage.waitForSearchResult(substring);
    }
}