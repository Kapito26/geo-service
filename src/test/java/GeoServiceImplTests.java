import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;

import java.util.stream.Stream;

public class GeoServiceImplTests {
    GeoServiceImpl geoService;

    @BeforeAll
    public static void before() {
        System.out.println("Тесты GeoServiceImpl стартовали");
    }

    @AfterAll
    public static void after() {
        System.out.println("Тесты GeoServiceImpl завершены");
    }

    @BeforeEach
    public void beforEachTest() {
        geoService = new GeoServiceImpl();
        System.out.println("geoService создан");
    }

    @AfterEach
    public void afterEachTest() {
        geoService = null;
        System.out.println("geoService уничтожен");
    }

    @ParameterizedTest
    @MethodSource("byIPParameters") //arrange  given
    public void testByIP(String ip, Location expected) {
        System.out.println("Тест метода byIP " + ip);
        //act      when
        Location result = geoService.byIp(ip);
        //assert   then
        if (expected == null) {
            Assertions.assertEquals(expected, result);
        } else {
            Assertions.assertEquals(expected.getCountry(), result.getCountry());
            Assertions.assertEquals(expected.getCity(), result.getCity());
            Assertions.assertEquals(expected.getStreet(), result.getStreet());
            Assertions.assertEquals(expected.getBuiling(), result.getBuiling());
        }
    }
    public static Stream<Arguments> byIPParameters() {
        return Stream.of(
                Arguments.of("127.0.0.1", new Location(null, null, null, 0)),
                Arguments.of("172.0.32.11", new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of("96.44.183.149", new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of("172.", new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of("96.", new Location("New York", Country.USA, null, 0)),
                Arguments.of("1.1.1.1", null)
        );
    }

    @Test
    public void testByCoordinates(){
        System.out.println("Тест метода byCoordinates");
        //arrange  given
        double latitude = 42.35, longitude = 33.55;
        Class<RuntimeException> expected = RuntimeException.class;

        //act      when
        Executable executable = () -> geoService.byCoordinates(latitude,longitude);

        //assert   then
        Assertions.assertThrowsExactly(expected, executable);
    }
}
