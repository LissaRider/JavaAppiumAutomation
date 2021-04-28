import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.MainPageObject;
import lib.ui.SearchPageObject;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FirstTest extends CoreTestCase {

  private MainPageObject mainPageObject;

  protected void setUp() throws Exception {
    super.setUp();
    mainPageObject = new MainPageObject(driver);
  }

  @Test
  public void testSearch() {

    SearchPageObject searchPageObject = new SearchPageObject(driver);

    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.waitForSearchResult("Object-oriented programming language");
  }

  @Test
  public void testSearchPlaceholder() {

    mainPageObject.waitForElementAndClick(
            By.id("org.wikipedia:id/search_container"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    mainPageObject.waitForElementPresent(
            By.id("org.wikipedia:id/search_src_text"),
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    mainPageObject.assertElementHasText(
            By.id("org.wikipedia:id/search_src_text"),
            "Search…",
            "\n Внимание! Поле ввода для поиска статьи содержит некорректный текст."
    );
  }

  @Test
  public void testCancelSearch() {

    SearchPageObject searchPageObject = new SearchPageObject(driver);

    searchPageObject.initSearchInput();
    searchPageObject.waitForCancelButtonToAppear();
    searchPageObject.clickCancelButton();
    searchPageObject.waitForCancelButtonToDisappear();
  }

  @Test
  public void testSearchAndClear() {

    // инициируем поиск
    mainPageObject.waitForElementAndClick(
            By.id("org.wikipedia:id/search_container"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    // вводим соответствующее значение для поиска
    mainPageObject.waitForElementAndSendKeys(
            By.id("org.wikipedia:id/search_src_text"),
            "Java",
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    // ждем отображения списка с количеством статей больше 1 (несколько статей)
    mainPageObject.waitForNumberOfElementsToBeMoreThan(
            By.id("org.wikipedia:id/page_list_item_container"),
            1,
            "Внимание! Не найдено ни одной статьи или найдено статей меньше ожидаемого количества.",
            15
    );

    // чистим поле ввода (переиспользуем уже существующий метод, хотя ожидание здесь не требуется)
    mainPageObject.waitForElementAndClear(
            By.id("org.wikipedia:id/search_src_text"),
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    // ждем чтобы ни одна статья не отображалась (тут может быть много вариаций)
    mainPageObject.waitForElementNotPresent(
            By.id("org.wikipedia:id/page_list_item_container"),
            "Внимание! Список статей все ещё отображается.",
            15
    );
  }

  @Test
  public void testCompareArticleTitle() {

    SearchPageObject searchPageObject = new SearchPageObject(driver);

    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

    ArticlePageObject articlePageObject = new ArticlePageObject(driver);

    String articleTitle = articlePageObject.getArticleTitle();


    Assert.assertEquals(
            "\n Внимание! Отображается некорректный заголовок статьи.",
            "Java (programming language)",
            articleTitle
    );
  }

  @Test
  public void testSwipeArticle() {

    SearchPageObject searchPageObject = new SearchPageObject(driver);

    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Appium");
    searchPageObject.clickByArticleWithSubstring("Appium");

    ArticlePageObject articlePageObject = new ArticlePageObject(driver);

    articlePageObject.waitForTitleElement();
    articlePageObject.swipeToFooter();
  }

  @Test
  public void testSearchResults() {

    final String searchValue = "JAVA";

    mainPageObject.waitForElementAndClick(
            By.id("org.wikipedia:id/search_container"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );


    mainPageObject.waitForElementAndSendKeys(
            By.id("org.wikipedia:id/search_src_text"),
            searchValue,
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    List<WebElement> articleTitles = mainPageObject.waitForPresenceOfAllElements(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@resource-id='org.wikipedia:id/page_list_item_title']"),
            "Внимание! Не найдено ни одной статьи.",
            15
    );

    for (int i = 0; i < articleTitles.size(); i++) {
      String articleTitle = articleTitles.get(i).getAttribute("text").toLowerCase();
      Assert.assertTrue(
              "\n Внимание! В заголовке найденной статьи с индексом [" + i + "]" +
                      " отсутствует заданное для поиска значение '" + searchValue + "'.\n",
              articleTitle.contains(searchValue.toLowerCase()));
    }
  }

  @Test
  public void testSaveFirstArticleToMyList() {

    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@text='Object-oriented programming language']"),
            "Внимание! Текст 'Object-oriented programming language' не найден.",
            5
    );

    mainPageObject.waitForElementPresent(
            By.id("org.wikipedia:id/view_page_title_text"),
            "Внимание! Заголовок статьи не найден.",
            15
    );

    mainPageObject.waitForElementAndClick(
            By.xpath("//android.widget.ImageView[@content-desc='More options']"),
            "Внимание! Кнопка открытия панели действий со статьёй не найдена.",
            5
    );

    mainPageObject.waitForElementVisible(
            By.xpath("//android.widget.ListView"),
            "Внимание! Контекстное меню не найдено.",
            15
    );

    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@text='Add to reading list']"),
            "Внимание! Элемент добавления статьи в список не найден.",
            5
    );

    mainPageObject.waitForElementAndClick(
            By.id("org.wikipedia:id/onboarding_button"),
            "Внимание! Кнопка 'GOT IT' не найдена.",
            5
    );

    mainPageObject.waitForElementAndClear(
            By.id("org.wikipedia:id/text_input"),
            "Поле ввода имени папки для добавления статьи не найдено.",
            5
    );

    String folderName = "Learning programming";

    mainPageObject.waitForElementAndSendKeys(
            By.id("org.wikipedia:id/text_input"),
            folderName,
            "Внимание! Невозможно ввести текст в поле ввода имени папки для добавления статьи.",
            5
    );

    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@text='OK']"),
            "Внимание! Невозможно нажать на кнопку 'OK'.",
            5
    );

    mainPageObject.waitForElementAndClick(
            By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
            "Внимание! Кнопка закрытия статьи не найдена.",
            5
    );

    mainPageObject.waitForElementAndClick(
            By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
            "Внимание! Кнопка перехода к спискам не найдена.",
            5
    );

    mainPageObject.waitForElementVisible(
            By.xpath("//*[@resource-id='org.wikipedia:id/item_container']"),
            "Внимание! Ни одной папки не найдено.",
            15
    );

    mainPageObject.waitForElementAndClick(
            By.xpath(String.format("//*[@text='%s']", folderName)),
            "Внимание! Папка 'Learning programming' не найдена.",
            5
    );

    mainPageObject.waitForElementVisible(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']"),
            "Внимание! Ни одной статьи не найдено.",
            15
    );

    mainPageObject.swipeElementToLeft(
            By.xpath("//*[@text='Java (programming language)']"),
            "Внимание! В списке статья 'Java (programming language)' не найдена."
    );

    mainPageObject.waitForElementNotPresent(
            By.xpath("//*[@text='Java (programming language)']"),
            "Внимание! Статья 'Java (programming language)' не удалилась из списка.",
            15
    );
  }

  @Test
  public void testAmountOfNotEmptySearch() {

    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    String searchLine = "Linkin Park Discography";
    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            searchLine,
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    String searchResultLocator = "//*[@resource-id='org.wikipedia:id/search_results_list']" +
            "/*[@resource-id='org.wikipedia:id/page_list_item_container']";

    mainPageObject.waitForElementPresent(
            By.xpath(searchResultLocator),
            String.format("Внимание! По запросу поиска '%s' ничего не найдено.", searchLine),
            15
    );

    int amountOfSearchResults = mainPageObject.getAmountOfElements(By.xpath(searchResultLocator));

    Assert.assertTrue("Найдено меньше результатов, чем ожидалось.",
            amountOfSearchResults > 1);
  }

  @Test
  public void testAmountOfEmptySearch() {

    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    String searchLine = "TIJIIOLLIKA";

    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            searchLine,
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    String searchResultLocator = "//*[@resource-id='org.wikipedia:id/search_results_list']" +
            "/*[@resource-id='org.wikipedia:id/page_list_item_container']";

    String emptyResultLabelLocator = "//*[@text='No results found']";

    mainPageObject.waitForElementPresent(
            By.xpath(emptyResultLabelLocator),
            String.format("Внимание! По запросу поиска '%s' лэйбл 'No results found' не отобразился.", searchLine),
            15
    );

    mainPageObject.assertElementNotPresent(By.xpath(searchResultLocator),
            String.format("Найдены результаты по запросу поиска '%s'.", searchLine));
  }

  @Test
  public void testChangeScreenOrientationOnSearchResults() {

    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    String searchLine = "Java";

    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            searchLine,
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    mainPageObject.waitForElementAndClick(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@text='Object-oriented programming language']"),
            String.format("Внимание! По запросу поиска '%s' текст 'Object-oriented programming language' не найден.", searchLine),
            15
    );

    String titleBeforeRotation = mainPageObject.waitForElementAndGetAttribute(
            By.id("org.wikipedia:id/view_page_title_text"),
            "text",
            "Внимание! Название статьи не найдено.",
            15
    );

    driver.rotate(ScreenOrientation.LANDSCAPE);

    String titleAfterRotation = mainPageObject.waitForElementAndGetAttribute(
            By.id("org.wikipedia:id/view_page_title_text"),
            "text",
            "Внимание! Название статьи не найдено.",
            15
    );

    Assert.assertEquals(
            "\n  Внимание! Название статьи изменилось после изменения ориентации экрана.\n",
            titleBeforeRotation,
            titleAfterRotation
    );

    driver.rotate(ScreenOrientation.PORTRAIT);

    String titleAfterSecondRotation = mainPageObject.waitForElementAndGetAttribute(
            By.id("org.wikipedia:id/view_page_title_text"),
            "text",
            "Внимание! Название статьи не найдено.",
            15
    );

    Assert.assertEquals(
            "\n  Внимание! Название статьи изменилось после изменения ориентации экрана.\n",
            titleBeforeRotation,
            titleAfterSecondRotation
    );
  }

  @Test
  public void testCheckSearchArticleInBackground() {

    mainPageObject.waitForElementAndClick(
            By.xpath("//*[contains(@text,'Search Wikipedia')]"),
            "Внимание! Элемент 'Search Wikipedia' не найден.",
            5
    );

    mainPageObject.waitForElementAndSendKeys(
            By.xpath("//*[contains(@text,'Search…')]"),
            "Java",
            "Внимание! Поле ввода текста для поиска не найдено.",
            5
    );

    mainPageObject.waitForElementPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@text='Object-oriented programming language']"),
            "Внимание! Текст 'Object-oriented programming language' не найден.",
            5
    );

    driver.runAppInBackground(2);

    mainPageObject.waitForElementPresent(
            By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']" +
                    "//*[@text='Object-oriented programming language']"),
            "Внимание! После сворачивания/разворачивания приложения текст 'Object-oriented programming language' не найден.",
            5
    );
  }

  @Test
  public void testActionsWithArticlesInFolder() {

    String searchWotLine = "World of Tanks";

    mainPageObject.searchFor(searchWotLine);

    mainPageObject.openArticle(searchWotLine);

    mainPageObject.selectOption("Add to reading list");

    String folderName = "Games";

    mainPageObject.createAndAddArticleTo(folderName);

    String searchWowLine = "World of Warcraft";

    mainPageObject.searchFor(searchWowLine);

    mainPageObject.openArticle(searchWowLine);

    mainPageObject.selectOption("Add to reading list");

    mainPageObject.addArticleTo(folderName);

    mainPageObject.closeArticle();

    mainPageObject.openMyLists();

    mainPageObject.openFolder(folderName);

    By searchResultLocator = By.id("org.wikipedia:id/page_list_item_container");
    By articleAboutWowLocator = By.xpath("//*[@text='" + searchWowLine + "']");
    By articleAboutWotLocator = By.xpath("//*[@text='" + searchWotLine + "']");
    By pageTitleLocator = By.id("org.wikipedia:id/view_page_title_text");

    mainPageObject.waitForNumberOfElementsToBeMoreThan(
            searchResultLocator,
            0,
            String.format("Внимание! В папке '%s' не найдено ни одной статьи.", folderName),
            15
    );

    int amountOfArticlesBefore = mainPageObject.getAmountOfElements(searchResultLocator);

    Assert.assertEquals(
            String.format("\nВнимание! В папке '%s' отображается некорректное количество статей.", folderName),
            amountOfArticlesBefore,
            2);

    mainPageObject.swipeElementToLeft(
            articleAboutWowLocator,
            String.format("Внимание! В списке статья с заголовком '%s' не найдена.", searchWowLine)
    );

    mainPageObject.waitForElementNotPresent(
            articleAboutWowLocator,
            String.format("Внимание! Статья '%s' не удалилась из списка.", articleAboutWowLocator),
            15
    );

    int amountOfArticlesAfter = mainPageObject.getAmountOfElements(searchResultLocator);

    Assert.assertEquals(
            String.format("\nВнимание! В папке '%s' отображается некорректное количество статей.", folderName),
            amountOfArticlesBefore - 1,
            amountOfArticlesAfter
    );

    mainPageObject.waitForElementClickableAndClick(
            articleAboutWotLocator,
            String.format("Внимание! В папке '%s' статья с заголовком '%s' не найдена или недоступна для действий.",
                    folderName, searchWotLine),
            15
    );

    String actualTitle = mainPageObject.waitForElementAndGetAttribute(
            pageTitleLocator,
            "text",
            "Внимание! Название статьи не найдено.",
            15
    );

    Assert.assertEquals(
            "\nВнимание! Отображается некорректное название статьи.",
            searchWotLine,
            actualTitle
    );
  }

  @Test
  public void testArticleTitlePresence() {

    String searchLine = "Lords Mobile";

    By resultItemLocator = By.id("org.wikipedia:id/page_list_item_container");
    By resultTitleLocator = By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='" + searchLine + "']");
    By pageTitleLocator = By.id("org.wikipedia:id/view_page_title_text");

    mainPageObject.searchFor(searchLine);

    mainPageObject.waitForNumberOfElementsToBeMoreThan(
            resultItemLocator,
            0,
            "Внимание! В результатах поиска ни одной статьи не найдено.",
            15
    );

    mainPageObject.waitForElementClickableAndClick(
            resultTitleLocator,
            String.format("Внимание! Статья с заголовком '%s' не найдена или недоступна для действий.", searchLine),
            5
    );

    mainPageObject.assertElementPresent(pageTitleLocator, "Заголовок статьи не найден.");
  }
}