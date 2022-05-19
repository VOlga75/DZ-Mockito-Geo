import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;


public class GeoServiceImplTests {
    GeoServiceImpl sut = new GeoServiceImpl();

    @Test
    public void testByIp() {
        Location expected = sut.byIp("172.0.32.11");
        Assertions.assertNotNull(expected);
        Assertions.assertEquals(expected.getCountry(), Country.RUSSIA);
        System.out.println("Локализация по ip проверена - ок!");
    }

    @Test
    public void testExceptionByCoordinates() {
        var expected = RuntimeException.class;
        Assertions.assertThrows(expected, () -> sut.byCoordinates(3.5, 3.3));
    }

    @Test
    public void testExceptionByCoordinatesMoc() {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byCoordinates(3.3, 5.5)) // почему нельзя c any() Mockito.when(geoService.byCoordinates(any()))
                .thenThrow(new IllegalArgumentException());
    }
}
