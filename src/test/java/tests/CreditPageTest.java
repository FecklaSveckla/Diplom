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

    @DisplayName("Заявка Оплата по карте, заполненная валидными данными карты со статусом Approved успешно одобрена банком")
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


    @DisplayName("Заявка Оплата по карте, заполненная данными карты со статусом Declined отклонена банком")
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

    @DisplayName("Отправка формы заявки, в которой поле Владелец содержит значение введенное в верхнем регистре. Остальные поля заполнены валидными данными.")
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


    @DisplayName("Отправка формы заявки, в которой поле Владелец содержит значение через дефис. Остальные поля формы содержат валидные данные.")
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

    @DisplayName("Сценарий 1.Заявка Оплата по карте, заполненная данными карты, отсутствующими в БД банка, отклонена банком. ")
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

    @DisplayName("Сценарий 2. Отправка формы заявки с пустым полем Номер карты. Остальные поля формы заполнены валидными значениями")
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

    @DisplayName("Сценарий 3. Отправка формы заявки, в которой поле Номер карты содержит одну цифру. Остальные поля формы заполнены валидными данными")
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

    @DisplayName("Сценарий 4. Отправка формы заявки, в которой поле Номер карты содержит 15 цифр. Остальные поля формы заполнены валидными данными ")
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

    @DisplayName("Сценарий 5. Отправка формы заявки, в которой поле Месяц пустое. Остальные поля формы заполнены валидными зданными.")
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

    @DisplayName("Сценарий 6. Отправка формы заявки, в которой поле Месяц содержит значение больше 12. Остальные поля формы заполнены валидными данными.")
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

    @DisplayName("Сценарий 7. Отправка формы заявки, в которой поле Месяц меньше значения 01. Остальные поля формы заполнены валидными данными.")
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

    @DisplayName("Сценарий 8. Отправка формы заявки, в которой поле Месяц содержит значение предыдущее от текущего. Остальные поля формы заполнены валидными данными.")
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

    @DisplayName("Сценарий 9. Отправка формы заявки, в которой поле Год пустое. Остальные поля формы заполнены валидными данными.")
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

    @DisplayName("Сценарий 10. Отправка формы заявки, в которой поле Год содержит значение соответствующее любому предыдущему году. Остальные поля формы заполнены валидными данными.")
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

    @DisplayName("Сценарий 11. Отправка формы заявки, в которой поле Год содержит значение +10 лет к текущему значению года. Остальные поля формы заполнены валидными значениями.")
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

    @DisplayName("Сценарий 12. Отправка формы заявки, в которой поле CVC/CVV пусто. Остальные поля формы заполнены валидными значениями.")
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

    @DisplayName("Сценарий 13. Отправка формы заявки, в которой поле CVC/CVV содержит 1 цифру. Остальные поля формы заполнены валидными значениями.")
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

    @DisplayName("Сценарий 14. Отправка формы заявки, в которой поле CVC/CVV содержит 2-е цифры. Остальные поля формы заполнены валидными значениями.")
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

    @DisplayName("Сценарий 15. Отправка формы заявки, в которой поле Владелец пустое. Остальные поля формы содержат валидные данные.")
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

    @DisplayName("Сценарий 16. Отправка формы заявки, в которой поле Владелец содержит фамилию на латинице, а имя отсуствует. Остальные поля формы заполнены валидными данными.")
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

    @DisplayName("Сценарий 17. Отправка формы заявки, в которой поле Владелец содержит фамилию и имя на кирилице. Остальные поля формы заполнены валидными данными.")
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

    @DisplayName("Сценарий 18.  Отправка формы заявки, в которой поле Владелец заполнено цифрами. Остальные поля формы заполнены валидными данными")
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

    @DisplayName("Сценарий 19. Оправка формы заявки, в которой поле Владелец заполнено спец.символами. Остальные поля формы заполнены валидными данными.")
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
