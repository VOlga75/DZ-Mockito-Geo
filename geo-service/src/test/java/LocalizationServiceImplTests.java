import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;

public class LocalizationServiceImplTests {
    LocalizationServiceImpl sut;

    @BeforeEach
    public void init() {
        sut = new LocalizationServiceImpl();
    }

    @Test
    public void testLocale() {
        Country countryForTest = Country.BRAZIL;
        String expected = sut.locale(countryForTest);
        Assertions.assertEquals(expected, "Welcome");
    }
}
