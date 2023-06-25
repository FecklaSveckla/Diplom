package page;

import com.codeborne.selenide.SelenideElement;
import data.Card;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage {
    //заголовок страницы
    private SelenideElement heading = $$( ".heading" ).find( exactText( "Кредит по данным карты" ) );

    // поля
    private SelenideElement cardNumberField = $( "[placeholder='0000 0000 0000 0000']" );
    private SelenideElement monthField = $( "[placeholder='08']" );
    private SelenideElement yearField = $( "[placeholder='22']" );
    private SelenideElement holderField = $( byText( "Владелец" ) ).parent().$( ".input__control" );
    private SelenideElement cvcField = $( "[placeholder='999']" );

    // кнопка Продолжить
    private SelenideElement continueButton = $$( ".button__text" ).find( exactText( "Продолжить" ) );

    //"Успешно! операция одобрена банком"
    private SelenideElement notificationSuccess = $( ".notification_status_ok" );

    //Ошибка! Банк отказал в проведении операции
    private SelenideElement notificationError = $( ".notification_status_error" );

    //Индикация полей
    private SelenideElement validatorFieldMessage = $( byText( "Поле обязательно для заполнения" ) );

    private SelenideElement wrongFormatMessage = $( byText( "Неверный формат" ) );
    private SelenideElement cardExpireMassage = $( byText( "Истёк срок действия карты" ) );

    private SelenideElement wrongExpirationMassage = $( byText( "Неверно указан срок действия карты" ) );

    public CreditPage() {
        heading.shouldBe( visible );
    }

    //заполнили поля
    public void fillData(Card card) {
        cardNumberField.setValue( card.getNumber() );
        monthField.setValue( card.getMonth() );
        yearField.setValue( card.getYear() );
        holderField.setValue( card.getHolder() );
        cvcField.setValue( card.getCvc() );
        continueButton.click();
    }


    //очистили поля
    public void cleanField() {
        cardNumberField.doubleClick().sendKeys( Keys.BACK_SPACE );
        monthField.doubleClick().sendKeys( Keys.BACK_SPACE );
        yearField.doubleClick().sendKeys( Keys.BACK_SPACE );
        holderField.doubleClick().sendKeys( Keys.BACK_SPACE );
        cvcField.doubleClick().sendKeys( Keys.BACK_SPACE );
    }


    //видимость сообщений: успешно-неусрешно
    public void notificationSuccessIsVisible() {
        notificationSuccess.shouldBe( visible, Duration.ofSeconds( 15 ) );
    }

    public void notificationErrorIsVisible() {
        notificationError.shouldBe( visible, Duration.ofSeconds( 15 ) );
    }

    // видимость индикации под полями
    //Поле обязательно для заполнения
    public void waitForValidationMassage() {
        validatorFieldMessage.shouldBe( visible );
    }

    //Неверный формат
    public void waitForWrongFormatMassage() {
        wrongFormatMessage.shouldBe( visible );
    }

    //Истёк срок действия карты
    public void waitForCardExpiredMassage() {
        cardExpireMassage.shouldBe( visible );
    }

    // Неверно указан срок действия карты
    public void waitForWrongCardExpirationMassage() {
        wrongExpirationMassage.shouldBe( visible );
    }

}

