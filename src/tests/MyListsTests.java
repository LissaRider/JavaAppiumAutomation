package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.ArticlePageObject;
import lib.ui.MyListsPageObject;
import lib.ui.NavigationUI;
import lib.ui.SearchPageObject;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.MyListsPageObjectFactory;
import lib.ui.factories.NavigationUIFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class MyListsTests extends CoreTestCase {

    private static final String folderName = "Learning programming";

    @Test
    public void testSaveFirstArticleToMyList() {
        SearchPageObject search = SearchPageObjectFactory.get(driver);
        search.initSearchInput();
        search.typeSearchLine("Java");
        search.waitForNotEmptySearchResults();
        search.clickByArticleWithSubstring("Object-oriented programming language");
        ArticlePageObject article = ArticlePageObjectFactory.get(driver);
        article.waitForTitleElement();
        String articleTitle = article.getArticleTitle();
        if (Platform.getInstance().isAndroid()) {
            article.addArticleToNewList(folderName);
            article.closeArticle();
        } else {
            article.addArticleToSavedList();
            article.closeArticleAndReturnToMainPage();
        }
        NavigationUI navigation = NavigationUIFactory.get(driver);
        navigation.clickMyLists();
        MyListsPageObject myLists = MyListsPageObjectFactory.get(driver);
        if (Platform.getInstance().isAndroid()) {
            myLists.openFolderByName(folderName);
        } else {
            myLists.closeSyncSavedArticlesPopUp();
        }
        myLists.swipeByArticleToDelete(articleTitle);
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
        NavigationUI navigationUI = NavigationUIFactory.get(driver);
        navigationUI.clickMyLists();
        MyListsPageObject myListsPageObject = MyListsPageObjectFactory.get(driver);
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