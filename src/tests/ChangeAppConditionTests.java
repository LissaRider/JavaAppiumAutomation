package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.SearchPageObject;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class ChangeAppConditionTests extends CoreTestCase {

  @Test
  public void testChangeScreenOrientationOnSearchResults() {
    SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");
    ArticlePageObject articlePageObject = new ArticlePageObject(driver);
    String titleBeforeRotation = articlePageObject.getArticleTitle();
    this.rotateScreenLandscape();
    String titleAfterRotation = articlePageObject.getArticleTitle();

    assertEquals(
            "\n  Ошибка! Название статьи изменилось после изменения ориентации экрана.\n",
            titleBeforeRotation,
            titleAfterRotation
    );

    this.rotateScreenPortrait();
    String titleAfterSecondRotation = articlePageObject.getArticleTitle();

    assertEquals(
            "\n  Ошибка! Название статьи изменилось после изменения ориентации экрана.\n",
            titleBeforeRotation,
            titleAfterSecondRotation
    );
  }

  @Test
  public void testCheckSearchArticleInBackground() {
    SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.waitForSearchResult("Object-oriented programming language");
    this.backgroundApp(2);
    searchPageObject.waitForSearchResult("Object-oriented programming language");
  }
}