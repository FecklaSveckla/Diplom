package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentPageTest {

    public static String url = System.getProperty("sut.url");

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {
        open(url);
    }

    @BeforeEach
    public void cleanBase() {
        SQLHelper.cleanTables();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");

    }

    @DisplayName("Для Сценария 1. Позитивный")
    @Test
    void shouldBuyAllFieldsValidApprovedCard() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getApprovedCard() );
        payment.notificationSuccessIsVisible();

        var statusExpected = "APPROVED";
        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( statusExpected, statusActual );

    }


    @DisplayName("Для Сценария 2. Позитивный")
    @Test
    void shouldBuyAllFieldValidDeclinedCard() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getDeclinedCard());
        payment.notificationErrorIsVisible();

        var statusExpected = "DECLINED";
        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( statusExpected, statusActual );

    }

    @DisplayName("Для Сценария 3. Позитивный")
    @Test
    void shouldBuyApprovedCardWithHolderNameInUpperCase() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getHolderInUpperCase());
        payment.notificationSuccessIsVisible();

        var statusExpected = "APPROVED";
        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( statusExpected, statusActual );

    }


    @DisplayName("Для Сценария 4. Позитивный")
    @Test
    void shouldBuyApprovedCardWithHolderHyphenated() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getHolderHyphenated());
        payment.notificationSuccessIsVisible();

        var statusExpected = "APPROVED";
        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( statusExpected, statusActual );

    }

    //НЕГАТИВНЫЕ ПРОВЕРКИ

    @DisplayName("Для Сценария 1. Негативный")
    @Test
    void shouldBuyWithNonExistDebitCard(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getNonExistCard());
        payment.notificationErrorIsVisible();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );

    }

    @DisplayName("Для Сценария 2. Негативный")
    @Test
    void shouldBuyWithEmptyFieldCardNumber() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getEmptyFieldCardNumber() );
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 3. Негативный")
    @Test
    void shouldBuyWithOneNumberInFieldCard() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getOneNumberInFieldCard());
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 4. Негативный")
    @Test
    void shouldBuyWithInvalidDebitCard(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getInvalidCardNumber());
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 5. Негативный")
    @Test
    void shouldBuyWithFieldMonthIsEmpty(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getEmptyMonth());
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 6. Негативный")
    @Test
    void shouldBuyWithFieldMonthOver12(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getMonthOver12());
        payment.waitForWrongCardExpirationMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 7. Негативный")
    @Test
    void shouldBuyWithFieldMonthZeroAndNowYear(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getZeroMonth());
        payment.waitForWrongCardExpirationMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 8. Негативный")
    @Test
    void shouldBuyWithExpiredCardMonth(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getInvalidPastMonth());
        payment.waitForWrongCardExpirationMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 9. Негативный")
    @Test
    void shouldBuyWithFieldYearIsEmpty(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getEmptyYear());
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 10. Негативный")
    @Test
    void shouldBuyWithFieldYearIsLastYear(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getLastYear());
        payment.waitForCardExpiredMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 11. Негативный")
    @Test
    void shouldBuyWithFieldInvalidYear(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getNotComingYear());
        payment.waitForWrongCardExpirationMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 12. Негативный")
    @Test
    void shouldBuyWithEmptyCvcField(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getEmptyCVC());
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 13. Негативный")
    @Test
    void shouldBuyWithCVCFieldOneNumber(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getOneNumberCVC());
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 14. Негативный")
    @Test
    void shouldBuyWithCVCFieldTwoNumber(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getTwoNumberCVC());
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 15. Негативный")
    @Test
    void shouldBuyWithEmptyFieldHolder(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getEmptyHolderCard());
        payment.waitForValidationMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 16. Негативный")
    @Test
    void shouldBuyWithFieldHolderOnlyName(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getInvalidHolderOneNameCard());
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 17. Негативный")
    @Test
    void shouldBuyWithFieldHolderRusLang() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getInvalidHolderRusCard() );
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );

    }

    @DisplayName("Для Сценария 18. Негативный")
    @Test
    void shouldBuyWithFieldHolderOnlyNumbers(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getInvalidHolderNumbersCard());
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Для Сценария 19. Негативный")
    @Test
    void shouldBuyWithFieldHolderOnlySymbols(){
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData(DataHelper.getInvalidHolderSymbolsCard());
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }





}
