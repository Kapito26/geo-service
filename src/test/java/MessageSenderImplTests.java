import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class MessageSenderImplTests {
    @BeforeAll
    public static void before() {
        System.out.println("Тесты MessageSenderImpl стартовали");
    }

    @AfterAll
    public static void after() {
        System.out.println("Тесты MessageSenderImpl завершены");
    }

    @ParameterizedTest
    @MethodSource("sendParameters")
    public void testSend(Map<String, String> headers, String expected) {
        GeoService geoService = Mockito.mock(GeoService.class);

        Mockito.when(geoService.byIp("172.0.32.11"))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
        Mockito.when(geoService.byIp("96.44.183.149"))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));
        Mockito.when(geoService.byIp("172."))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
        Mockito.when(geoService.byIp("96."))
                .thenReturn(new Location("New York", Country.USA, null, 0));


        LocalizationService localizationService = Mockito.mock(LocalizationService.class);

        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");


        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        //act      when
        String result = messageSender.send(headers);

        //assert   then
        Assertions.assertEquals(expected, result);
    }

    public static Stream<Arguments> sendParameters() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("x-real-ip", "172.0.32.11");
        Map<String, String> map2 = new HashMap<>();
        map2.put("x-real-ip", "96.44.183.149");
        Map<String, String> map3 = new HashMap<>();
        map3.put("x-real-ip", "172.");
        Map<String, String> map4 = new HashMap<>();
        map4.put("x-real-ip", "96.");
        Map<String, String> mapEmpty = new HashMap<>();
        mapEmpty.put("x-real-ip", "");
        return Stream.of(
                Arguments.of(map1, "Добро пожаловать"),
                Arguments.of(map2, "Welcome"),
                Arguments.of(map3, "Добро пожаловать"),
                Arguments.of(map4, "Welcome"),
                Arguments.of(mapEmpty, "Welcome")
        );
    }
}
