package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.MyListsPageObject;
import lib.ui.NavigationUI;
import lib.ui.SearchPageObject;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class MyListsTests extends CoreTestCase {

  @Test
  public void testSaveFirstArticleToMyList() {
    SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");
    ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
    articlePageObject.waitForTitleElement();
    String articleTitle = articlePageObject.getArticleTitle();
    String folderName = "Learning programming";
    articlePageObject.addArticleToNewList(folderName);
    articlePageObject.closeArticle();
    NavigationUI navigationUI = new NavigationUI(driver);
    navigationUI.clickMyLists();
    MyListsPageObject myListsPageObject = new MyListsPageObject(driver);
    myListsPageObject.openFolderByName(folderName);
    myListsPageObject.swipeByArticleToDelete(articleTitle);
  }

  @Test
  public void testActionsWithArticlesInMyList() {
    String searchLine;
    SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
    searchPageObject.initSearchInput();
    searchLine = "World of Tanks";
    searchPageObject.typeSearchLine(searchLine);
    searchPageObject.waitForNotEmptySearchResults();
    searchPageObject.clickByArticleWithTitle(searchLine);
    ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
    articlePageObject.waitForTitleElement();
    String articleAboutWotTitle = articlePageObject.getArticleTitle();
    String folderName = "Games";
    articlePageObject.addArticleToNewList(folderName);
    searchPageObject.initSearchInput();
    searchLine = "World of Warcraft";
    searchPageObject.typeSearchLine(searchLine);
    searchPageObject.waitForNotEmptySearchResults();
    searchPageObject.clickByArticleWithTitle(searchLine);
    articlePageObject.waitForTitleElement();
    String articleAboutWowTitle = articlePageObject.getArticleTitle();
    articlePageObject.addArticleToExistingList(folderName);
    articlePageObject.closeArticle();
    NavigationUI navigationUI = new NavigationUI(driver);
    navigationUI.clickMyLists();
    MyListsPageObject myListsPageObject = new MyListsPageObject(driver);
    myListsPageObject.openFolderByName(folderName);
    int amountOfArticlesBefore = myListsPageObject.getAmountOfAddedArticles();

    assertEquals(
            String.format("\n  Ошибка! В папке '%s' отображается некорректное количество статей.\n", folderName),
            amountOfArticlesBefore,
            2);

    myListsPageObject.swipeByArticleToDelete(articleAboutWowTitle);
    int amountOfArticlesAfter = myListsPageObject.getAmountOfAddedArticles();

    assertEquals(
            String.format("\n  Ошибка! В папке '%s' отображается некорректное количество статей.\n", folderName),
            amountOfArticlesBefore - 1,
            amountOfArticlesAfter
    );

    myListsPageObject.clickByArticleWithTitle(articleAboutWotTitle);
    articlePageObject.waitForTitleElement();
    String actualTitle = articlePageObject.getArticleTitle();

    assertEquals(
            "\n  Ошибка! Отображается некорректное название статьи.\n",
            articleAboutWotTitle,
            actualTitle);
  }
}