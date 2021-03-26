import org.junit.Assert;
import org.junit.Test;

public class MainClassTest {

    MainClass main = new MainClass();

    @Test
    public void testGetLocalNumber() {
        int actual = main.getLocalNumber();
        int expected = 14;
        String message = "\nОшибка! Метод \"getLocalNumber\" не вернул ожидаемое значение.";
        Assert.assertEquals(message, expected, actual);
    }


    @Test
    public void getClassNumber() {
        int actual = main.getClassNumber();
        int boundary = 45;
        String message =
                "\nОшибка! Метод \"getClassNumber\" не вернул значение больше значения \"" + boundary + "\"." +
                "\nФактическое значение: \"" + actual + "\"\n";
        Assert.assertTrue(message, actual > boundary);
    }
}