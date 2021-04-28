import lib.CoreTestCase;
import lib.ui.*;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FirstTest extends CoreTestCase {

  private MainPageObject mainPageObject;

  protected void setUp() throws Exception {
    super.setUp();
    mainPageObject = new MainPageObject(driver);
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
            "\n  Ошибка! Поле ввода для поиска статьи содержит некорректный текст.\n"
    );
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
      assertTrue(
              "\n  Ошибка! В заголовке найденной статьи с индексом [" + i + "]" +
                      " отсутствует заданное для поиска значение '" + searchValue + "'.\n",
              articleTitle.contains(searchValue.toLowerCase()));
    }
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

    assertEquals(
            String.format("\n  Ошибка! В папке '%s' отображается некорректное количество статей.\n", folderName),
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

    assertEquals(
            String.format("\n  Ошибка! В папке '%s' отображается некорректное количество статей.\n", folderName),
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

    assertEquals(
            "\n  Ошибка! Отображается некорректное название статьи.\n",
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