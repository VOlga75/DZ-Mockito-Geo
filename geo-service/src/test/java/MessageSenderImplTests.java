import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static ru.netology.entity.Country.RUSSIA;
import static ru.netology.entity.Country.USA;


//@RunWith(MockitoJUnitRunner.class)
public class MessageSenderImplTests {
    private static Map<String, String> headers = new HashMap<String, String>();

    @Test
    public void testSendRU() { // пример работы теста без параметра
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");
       // GeoService geoServiceRu = Mockito.mock(GeoServiceImpl.class); // мокировать класс бессмысленно 
        GeoService geoServiceRu = Mockito.mock(GeoService.class);
        Mockito.when(geoServiceRu.byIp(Mockito.startsWith("172.")))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
      //  LocalizationService localizationServiceRu = Mockito.mock(LocalizationServiceImpl.class);
        LocalizationService localizationServiceRu = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationServiceRu.locale(RUSSIA))
                .thenReturn("Добро пожаловать");
        MessageSenderImpl messageSenderRU = new MessageSenderImpl(geoServiceRu, localizationServiceRu);
        String expected = messageSenderRU.send(headers);
        Assertions.assertEquals(expected, "Добро пожаловать");
    }

  /*  @Test
    public void testSend() { // странно пытаюсь объединить

       headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.123.12.19");
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(geoService.byIp(Mockito.startsWith("96.")))
                    .thenReturn(new Location("NY", Country.USA, null, 0));
        Mockito.when(localizationService.locale(USA))
                .thenReturn("Welcome");
        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);
        String expected = messageSender.send(headers);
        Assertions.assertEquals(expected, "Welcome");

        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");
        Mockito.when(geoService.byIp(Mockito.startsWith("172.")))
                    .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
            Mockito.when(localizationService.locale(RUSSIA))
                    .thenReturn("Добро пожаловать");
            messageSender = new MessageSenderImpl(geoService, localizationService);
            expected = messageSender.send(headers);
            Assertions.assertEquals(expected, "Добро пожаловать");
    }*/

}
