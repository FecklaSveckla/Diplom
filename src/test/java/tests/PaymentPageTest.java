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

    @AfterEach
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
        payment.clear();
        payment.fillData( DataHelper.getApprovedCard() );
        payment.notificationSuccessIsVisible();

        var statusExpected = "APPROVED";
        var statusActual = SQLHelper.getPaymentStatus();
        assertEquals(statusExpected, statusActual);
    }



}
