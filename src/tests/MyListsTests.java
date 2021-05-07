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

    @Test
    public void testSaveFirstArticleToMyList() {

        SearchPageObject searchPage = SearchPageObjectFactory.get(driver);
        ArticlePageObject articlePage = ArticlePageObjectFactory.get(driver);
        NavigationUI navigation = NavigationUIFactory.get(driver);
        MyListsPageObject myListsPage = MyListsPageObjectFactory.get(driver);

        final String searchLine = "Java";
        searchPage.searchByValue(searchLine);

        searchPage.waitForNotEmptySearchResults();

        final String substring = "Object-oriented programming language";
        searchPage.clickByArticleWithSubstring(substring);

        articlePage.waitForTitleElement();
        String articleTitle = articlePage.getArticleTitle();

        final  String folderName = "Learning programming";
        if (Platform.getInstance().isAndroid()) {
            articlePage.addArticleToNewList(folderName);
            articlePage.closeArticle();
        } else {
            articlePage.addArticleToSavedList();
            articlePage.closeArticleAndReturnToMainPage();
        }

        navigation.clickMyLists();

        if (Platform.getInstance().isAndroid()) myListsPage.openFolderByName(folderName);
        else myListsPage.closeSyncSavedArticlesPopUp();

        myListsPage.swipeByArticleToDelete(articleTitle);
    }

    @Test
    public void testActionsWithArticlesInMyList() {

        SearchPageObject searchPage = SearchPageObjectFactory.get(driver);
        ArticlePageObject articlePage = ArticlePageObjectFactory.get(driver);
        NavigationUI navigation = NavigationUIFactory.get(driver);
        MyListsPageObject myListsPage = MyListsPageObjectFactory.get(driver);

        String searchLine;
        searchLine = "World of Tanks";
        searchPage.searchByValue(searchLine);

        searchPage.waitForNotEmptySearchResults();

        searchPage.clickByArticleWithSubstring(searchLine);

        articlePage.waitForTitleElement();
        String articleAboutWotTitle = articlePage.getArticleTitle();

        final String folderName = "Games";
        if (Platform.getInstance().isAndroid()) articlePage.addArticleToNewList(folderName);
        else articlePage.addArticleToSavedList();

        searchLine = "World of Warcraft";
        searchPage.searchByValue(searchLine);

        searchPage.clickByArticleWithSubstring(searchLine);

        articlePage.waitForTitleElement();
        String articleAboutWowTitle = articlePage.getArticleTitle();

        if (Platform.getInstance().isAndroid()) {
            articlePage.addArticleToExistingList(folderName);
            articlePage.closeArticle();
        } else {
            articlePage.addArticleToSavedList();
            articlePage.closeArticleAndReturnToMainPage();
        }

        navigation.clickMyLists();

        if (Platform.getInstance().isAndroid()) myListsPage.openFolderByName(folderName);
        else myListsPage.closeSyncSavedArticlesPopUp();

        int amountOfArticlesBefore = myListsPage.getAmountOfAddedArticles();

        assertEquals(
                String.format("\n  Ошибка! В папке '%s' отображается некорректное количество статей.\n", folderName),
                amountOfArticlesBefore,
                2);

        myListsPage.swipeByArticleToDelete(articleAboutWowTitle);

        int amountOfArticlesAfter = myListsPage.getAmountOfAddedArticles();

        assertEquals(
                String.format("\n  Ошибка! В папке '%s' отображается некорректное количество статей.\n", folderName),
                amountOfArticlesBefore - 1,
                amountOfArticlesAfter
        );

        myListsPage.clickByArticleWithTitle(articleAboutWotTitle);

        articlePage.waitForTitleElement();
        String actualTitle = articlePage.getArticleTitle();

        assertEquals(
                "\n  Ошибка! Отображается некорректное название статьи.\n",
                articleAboutWotTitle,
                actualTitle);
    }
}