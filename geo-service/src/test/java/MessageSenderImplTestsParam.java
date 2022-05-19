import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static ru.netology.entity.Country.*;

//@RunWith(MockitoJUnitRunner.class)
public class MessageSenderImplTestsParam {
    private final static Map<String, String> headers = new HashMap<String, String>();

    @ParameterizedTest
    //Параметры через Csv
    @CsvSource({
            "96.123.12.19, NY, USA, Welcome",
            "172.123.12.19, Moscow, RUSSIA, Добро пожаловать",
            "172.123.12.19, BA, BRAZIL, Welcome",
            "172.123.12.19, Moscow, RUSSIA, Welcome" //эта проверка должна быть желтой...
    })
    public void testParamSend(String ip, String city, Country country, String text) {
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(Mockito.startsWith(ip)))
                .thenReturn(new Location(city, country, null, 0));
        //// // а как правильнее?Эти  строчки с 42 по 45 или одна 46 вместо них? т.е. обязательно ли здесь нужна заглушка для LocalizationServiceImpl?
        //// или в строке 46 new LocalizationServiceImpl() тоже может быть? как-то странно, я text возвращаю в строке 44 принудительно и с ним же сравниваю expect
        //// но expected вроде как результат messageSender...тогда это не масло масляное, т.е. не text = text Суть вопроса - а тест ли я написала?)
       // LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        //Mockito.when(localizationService.locale(country))
         //       .thenReturn(text);
        //MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);
        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, new LocalizationServiceImpl());
        String expected = messageSender.send(headers);
        Assertions.assertEquals(expected, text);
    }

    // параметры через поток
    @ParameterizedTest
    @MethodSource("argsSourceStream")
    public void testParamMethodStreamSend(String ip, String city, Country country, String text) {
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(Mockito.startsWith(ip)))
                .thenReturn(new Location(city, country, null, 0));
        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(country))
                .thenReturn(text);
        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);
        // при варианте со строки 62-64 тесты выполняются всегда, а правильно выполняются если заменить 62-64 на строку 66 .
        // Это нормально, если  (new LocalizationServiceImpl()) ?
        //MessageSenderImpl messageSender = new MessageSenderImpl(geoService, new LocalizationServiceImpl());
        String expected = messageSender.send(headers);
        Assertions.assertEquals(expected, text);

    }

    private static Stream<Arguments> argsSourceStream() {
        return Stream.of(

                Arguments.of("96.123.12.19", "NY", USA, "Welcome"),
                Arguments.of("172.123.12.19", "Moscow", RUSSIA, "Добро пожаловать"),
                Arguments.of("172.123.12.19", "Moscow", RUSSIA, "Welcome")
        );
    }

    //параметры через класс
    @ParameterizedTest
    @MethodSource("argsSource")
    public void testParamMethodSend(Param p) {
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, p.ip);
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(Mockito.startsWith(p.ip)))
                .thenReturn(new Location(p.city, p.country, null, 0));
        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, new LocalizationServiceImpl());
        String expected = messageSender.send(headers);
        Assertions.assertEquals(expected, p.text);

    }

    static Stream<Param> argsSource() {
        Param[] params = {
                new Param("172.123.12.19", "Moscow", RUSSIA, "Welcome"),
                new Param("96.123.12.19", "NY", USA, "Welcome"),
                new Param("172.123.12.19", "Moscow", RUSSIA, "Добро пожаловать"),
                new Param("172.123.12.19", "BA", BRAZIL, "Welcome")
        };
        return Stream.of(params);
    }
}


class Param {
    String ip;
    String city;
    Country country;
    String text;

    public Param(String ip, String city, Country country, String text) {
        this.ip = ip;
        this.city = city;
        this.country = country;
        this.text = text;
    }
}



