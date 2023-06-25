package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditPageTest {
    public static String url = System.getProperty( "sut.url" );

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener( "allure", new AllureSelenide() );
    }

    @BeforeEach
    public void openPage() {
        open( url );
    }

    @BeforeEach
    public void cleanBase() {
        SQLHelper.cleanTables();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener( "allure" );

    }

    @DisplayName("Для Сценария 1. Позитивный")
    @Test
    void shouldCreditAllFieldsValidApprovedCard() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getApprovedCard() );
        credit.notificationSuccessIsVisible();

        var statusExpected = "APPROVED";
        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( statusExpected, statusActual );

    }


    @DisplayName("Для Сценария 2. Позитивный")
    @Test
    void shouldCreditAllFieldValidDeclinedCard() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getDeclinedCard() );
        credit.notificationErrorIsVisible();

        var statusExpected = "DECLINED";
        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( statusExpected, statusActual );

    }

    @DisplayName("Для Сценария 3. Позитивный")
    @Test
    void shouldCreditApprovedCardWithHolderNameInUpperCase() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getHolderInUpperCase() );
        credit.notificationSuccessIsVisible();

        var statusExpected = "APPROVED";
        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( statusExpected, statusActual );

    }


    @DisplayName("Для Сценария 4. Позитивный")
    @Test
    void shouldCreditApprovedCardWithHolderHyphenated() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getHolderHyphenated() );
        credit.notificationSuccessIsVisible();

        var statusExpected = "APPROVED";
        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( statusExpected, statusActual );

    }

    //НЕГАТИВНЫЕ ПРОВЕРКИ

    @DisplayName("Для Сценария 1. Негативный")
    @Test
    void shouldCreditWithNonExistDebitCard() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getNonExistCard() );
        credit.notificationErrorIsVisible();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );

    }

    @DisplayName("Для Сценария 2. Негативный")
    @Test
    void shouldCreditWithEmptyFieldCardNumber() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getEmptyFieldCardNumber() );
        credit.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 3. Негативный")
    @Test
    void shouldCreditWithOneNumberInFieldCard() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getOneNumberInFieldCard() );
        credit.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 4. Негативный")
    @Test
    void shouldCreditWithInvalidDebitCard() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getInvalidCardNumber() );
        credit.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 5. Негативный")
    @Test
    void shouldCreditWithFieldMonthIsEmpty() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getEmptyMonth() );
        credit.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 6. Негативный")
    @Test
    void shouldCreditWithFieldMonthOver12() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getMonthOver12() );
        credit.waitForWrongCardExpirationMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 7. Негативный")
    @Test
    void shouldCreditWithFieldMonthZeroAndNowYear() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getZeroMonth() );
        credit.waitForWrongCardExpirationMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 8. Негативный")
    @Test
    void shouldCreditWithExpiredCardMonth() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getInvalidPastMonth() );
        credit.waitForWrongCardExpirationMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 9. Негативный")
    @Test
    void shouldCreditWithFieldYearIsEmpty() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getEmptyYear() );
        credit.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 10. Негативный")
    @Test
    void shouldCreditWithFieldYearIsLastYear() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getLastYear() );
        credit.waitForCardExpiredMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 11. Негативный")
    @Test
    void shouldCreditWithFieldInvalidYear() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getNotComingYear() );
        credit.waitForWrongCardExpirationMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 12. Негативный")
    @Test
    void shouldCreditWithEmptyCvcField() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getEmptyCVC() );
        credit.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 13. Негативный")
    @Test
    void shouldCreditWithCVCFieldOneNumber() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getOneNumberCVC() );
        credit.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 14. Негативный")
    @Test
    void shouldCreditWithCVCFieldTwoNumber() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getTwoNumberCVC() );
        credit.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 15. Негативный")
    @Test
    void shouldCreditWithEmptyFieldHolder() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getEmptyHolderCard() );
        credit.waitForValidationMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 16. Негативный")
    @Test
    void shouldCreditWithFieldHolderOnlyName() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getInvalidHolderOneNameCard() );
        credit.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 17. Негативный")
    @Test
    void shouldCreditWithFieldHolderRusLang() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getInvalidHolderRusCard() );
        credit.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );

    }

    @DisplayName("Для Сценария 18. Негативный")
    @Test
    void shouldCreditWithFieldHolderOnlyNumbers() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getInvalidHolderNumbersCard() );
        credit.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 19. Негативный")
    @Test
    void shouldCreditWithFieldHolderOnlySymbols() {
        var startPage = new MainPage();
        var credit = startPage.goToCreditPage();
        credit.cleanField();
        credit.fillData( DataHelper.getInvalidHolderSymbolsCard() );
        credit.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getCreditStatus();
        assertEquals( null, statusActual );
    }


}
