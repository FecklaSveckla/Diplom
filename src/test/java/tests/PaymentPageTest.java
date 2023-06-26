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

    @DisplayName("Заявка Оплата по карте, заполненная валидными данными карты со статусом Approved успешно одобрена банком")
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


    @DisplayName("Заявка Оплата по карте, заполненная данными карты со статусом Declined отклонена банком")
    @Test
    void shouldBuyAllFieldValidDeclinedCard() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getDeclinedCard() );
        payment.notificationErrorIsVisible();

        var statusExpected = "DECLINED";
        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( statusExpected, statusActual );

    }

    @DisplayName("Отправка формы заявки, в которой поле Владелец содержит значение введенное в верхнем регистре. Остальные поля заполнены валидными данными.")
    @Test
    void shouldBuyApprovedCardWithHolderNameInUpperCase() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getHolderInUpperCase() );
        payment.notificationSuccessIsVisible();

        var statusExpected = "APPROVED";
        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( statusExpected, statusActual );

    }


    @DisplayName("Отправка формы заявки, в которой поле Владелец содержит значение через дефис. Остальные поля формы содержат валидные данные.")
    @Test
    void shouldBuyApprovedCardWithHolderHyphenated() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getHolderHyphenated() );
        payment.notificationSuccessIsVisible();

        var statusExpected = "APPROVED";
        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( statusExpected, statusActual );

    }

    //НЕГАТИВНЫЕ ПРОВЕРКИ

    @DisplayName("Сценарий 1. Заявка Оплата по карте, заполненная данными карты, отсутствующими в БД банка, отклонена банком. ")
    @Test
    void shouldBuyWithNonExistDebitCard() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getNonExistCard() );
        payment.notificationErrorIsVisible();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );

    }

    @DisplayName("Сценарий 2. Отправка формы заявки с пустым полем Номер карты. Остальные поля формы заполнены валидными значениями")
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

    @DisplayName("Сценарий 3. Отправка формы заявки, в которой поле Номер карты содержит одну цифру. Остальные поля формы заполнены валидными данными")
    @Test
    void shouldBuyWithOneNumberInFieldCard() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getOneNumberInFieldCard() );
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 4. Отправка формы заявки, в которой поле Номер карты содержит 15 цифр. Остальные поля формы заполнены валидными данными ")
    @Test
    void shouldBuyWithInvalidDebitCard() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getInvalidCardNumber() );
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 5. Отправка формы заявки, в которой поле Месяц пустое. Остальные поля формы заполнены валидными зданными.")
    @Test
    void shouldBuyWithFieldMonthIsEmpty() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getEmptyMonth() );
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 6. Отправка формы заявки, в которой поле Месяц содержит значение больше 12. Остальные поля формы заполнены валидными данными.")
    @Test
    void shouldBuyWithFieldMonthOver12() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getMonthOver12() );
        payment.waitForWrongCardExpirationMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 7. Отправка формы заявки, в которой поле Месяц меньше значения 01. Остальные поля формы заполнены валидными данными.")
    @Test
    void shouldBuyWithFieldMonthZeroAndNowYear() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getZeroMonth() );
        payment.waitForWrongCardExpirationMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 8. Отправка формы заявки, в которой поле Месяц содержит значение предыдущее от текущего. Остальные поля формы заполнены валидными данными.")
    @Test
    void shouldBuyWithExpiredCardMonth() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getInvalidPastMonth() );
        payment.waitForWrongCardExpirationMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 9. Отправка формы заявки, в которой поле Год пустое. Остальные поля формы заполнены валидными данными.")
    @Test
    void shouldBuyWithFieldYearIsEmpty() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getEmptyYear() );
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 10. Отправка формы заявки, в которой поле Год содержит значение соответствующее любому предыдущему году. Остальные поля формы заполнены валидными данными.")
    @Test
    void shouldBuyWithFieldYearIsLastYear() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getLastYear() );
        payment.waitForCardExpiredMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 11. Отправка формы заявки, в которой поле Год содержит значение +10 лет к текущему значению года. Остальные поля формы заполнены валидными значениями.")
    @Test
    void shouldBuyWithFieldInvalidYear() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getNotComingYear() );
        payment.waitForWrongCardExpirationMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 12. Отправка формы заявки, в которой поле CVC/CVV пусто. Остальные поля формы заполнены валидными значениями.")
    @Test
    void shouldBuyWithEmptyCvcField() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getEmptyCVC() );
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 13. Отправка формы заявки, в которой поле CVC/CVV содержит 1 цифру. Остальные поля формы заполнены валидными значениями.")
    @Test
    void shouldBuyWithCVCFieldOneNumber() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getOneNumberCVC() );
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 14. Отправка формы заявки, в которой поле CVC/CVV содержит 2-е цифры. Остальные поля формы заполнены валидными значениями.")
    @Test
    void shouldBuyWithCVCFieldTwoNumber() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getTwoNumberCVC() );
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 15. Отправка формы заявки, в которой поле Владелец пустое. Остальные поля формы содержат валидные данные.")
    @Test
    void shouldBuyWithEmptyFieldHolder() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getEmptyHolderCard() );
        payment.waitForValidationMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 16. Отправка формы заявки, в которой поле Владелец содержит фамилию на латинице, а имя отсуствует. Остальные поля формы заполнены валидными данными.")
    @Test
    void shouldBuyWithFieldHolderOnlyName() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getInvalidHolderOneNameCard() );
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 17. Отправка формы заявки, в которой поле Владелец содержит фамилию и имя на кирилице. Остальные поля формы заполнены валидными данными.")
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

    @DisplayName("Сценарий 18.  Отправка формы заявки, в которой поле Владелец заполнено цифрами. Остальные поля формы заполнены валидными данными")
    @Test
    void shouldBuyWithFieldHolderOnlyNumbers() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getInvalidHolderNumbersCard() );
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }

    @DisplayName("Сценарий 19. Оправка формы заявки, в которой поле Владелец заполнено спец.символами. Остальные поля формы заполнены валидными данными.")
    @Test
    void shouldBuyWithFieldHolderOnlySymbols() {
        var startPage = new MainPage();
        var payment = startPage.goToPaymentPage();
        payment.cleanField();
        payment.fillData( DataHelper.getInvalidHolderSymbolsCard() );
        payment.waitForWrongFormatMassage();

        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals( null, statusActual );
    }


}
