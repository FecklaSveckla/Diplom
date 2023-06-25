package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static data.DataHelper.getApprovedCard;
import static data.DataHelper.getDeclinedCard;
import static data.RestApiHelper.paymentRequest;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener( "allure", new AllureSelenide() );
    }

    @BeforeEach
    void connectDB() {
        SQLHelper.getConn();
    }

    @AfterEach
    void cleanDB() {
        SQLHelper.cleanTables();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener( "allure" );
    }

    @DisplayName("Отправка POST запроса Купить с валидным body и карты со статусом APPROVED" +
            "Ожидаемый результат: ответ сервера - код 200, запись в БД со статус карты APPROVED.")
    @Test
    void shouldGiveValidApprovedDebitCard() {
        var validApprovedCardForApi = getApprovedCard();
        var response = paymentRequest( validApprovedCardForApi, "/api/v1/pay" );
        assertTrue( response.contains( SQLHelper.getPaymentStatus() ) );
    }

    @DisplayName("Отправка POST запроса Купить с валидным body и карты со статусом DECLINED" +
            "Ожидаемый результат: ответ сервера - код 200, запись в БД со статусом карты DECLINED.")
    @Test
    void shouldGiveValidDeclinedDebitCard() {
        var validDeclinedCardForApi = getDeclinedCard();
        var response = paymentRequest( validDeclinedCardForApi, "/api/v1/pay" );
        assertTrue( response.contains( SQLHelper.getPaymentStatus() ) );
    }

    @DisplayName("Отправка POST запроса Купить в кредит с валидным body и карты со статусом APPROVED" +
            "Ожидаемый результат: ответ сервера - код 200, запись в БД со статус карты APPROVED")
    @Test
    void shouldGiveValidApprovedCreditCard() {
        var validApprovedCardForApi = getApprovedCard();
        var response = paymentRequest( validApprovedCardForApi, "/api/v1/credit" );
        assertTrue( response.contains( SQLHelper.getCreditStatus() ) );
    }

    @DisplayName("Оправка POST запроса Купить в креди с валидным body и карты со статусом DECLINED" +
            "Ожидаемый результат: ответ сервера - код 200, запись в БД со статусом карты DECLINED.")
    @Test
    void shouldGiveValidDeclinedCreditCard() {
        var validDeclinedCardForApi = getDeclinedCard();
        var response = paymentRequest( validDeclinedCardForApi, "/api/v1/credit" );
        assertTrue( response.contains( SQLHelper.getCreditStatus() ) );
    }
}
