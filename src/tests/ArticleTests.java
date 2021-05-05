package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.SearchPageObject;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class ArticleTests extends CoreTestCase {

  @Test
  public void testCompareArticleTitle() {
    SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

    ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
    String articleTitle = articlePageObject.getArticleTitle();

    assertEquals(
            "\n  Ошибка! Отображается некорректный заголовок статьи.\n",
            "Java (programming language)",
            articleTitle
    );
  }

  @Test
  public void testSwipeArticle() {
    SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Appium");
    searchPageObject.clickByArticleWithSubstring("Appium");
    ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
    articlePageObject.waitForTitleElement();
    articlePageObject.swipeToFooter();
  }

  @Test
  public void testArticleTitlePresence() {
    SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
    searchPageObject.initSearchInput();
    String searchLine = "Lords Mobile";
    searchPageObject.typeSearchLine(searchLine);
    searchPageObject.waitForNotEmptySearchResults();
    searchPageObject.clickByArticleWithTitle(searchLine);
    ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
    articlePageObject.assertIsArticleTitlePresent();
  }
}