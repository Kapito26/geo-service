import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.stream.Stream;

public class LocalizationServiceImplTests {
    LocalizationServiceImpl localizationService;

    @BeforeAll
    public static void before() {
        System.out.println("Тесты LocalizationServiceImpl стартовали");
    }

    @AfterAll
    public static void after() {
        System.out.println("Тесты LocalizationServiceImpl завершены");
    }

    @BeforeEach
    public void beforEachTest() {
        localizationService = new LocalizationServiceImpl();
        System.out.println("localizationService создан");
    }

    @AfterEach
    public void afterEachTest() {
        localizationService = null;
        System.out.println("localizationService уничтожен");
    }
    @ParameterizedTest
    @MethodSource("localeParameters")
    public void testLocale(Country country, String expected){
        System.out.println("Тест метода locale для " + country);

        //act      when
        String result = localizationService.locale(country);

        //assert   then
        Assertions.assertEquals(expected, result);
    }
    public static Stream<Arguments> localeParameters(){
        return Stream.of(
                Arguments.of(Country.RUSSIA,"Добро пожаловать"),
                Arguments.of(Country.GERMANY,"Welcome"),
                Arguments.of(Country.USA,"Welcome"),
                Arguments.of(Country.BRAZIL,"Welcome")
        );
    }
}
