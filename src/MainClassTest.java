import org.junit.Assert;
import org.junit.Test;

public class MainClassTest {

    MainClass main = new MainClass();

    @Test
    public void testGetLocalNumber() {
        int actual = main.getLocalNumber();
        int expected = 14;
        String message = "\nОшибка! Метод \"getLocalNumber\" не возвращает ожидаемое значение.";
        Assert.assertEquals(message, expected, actual);
    }
}