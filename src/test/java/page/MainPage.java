package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private SelenideElement heading = $$( ".heading" ).find( exactText( "Путешествие дня" ) );
    private SelenideElement buyButton = $$( ".button__text" ).find( exactText( "Купить" ) );
    private SelenideElement creditButton = $$( ".button__text" ).find( exactText( "Купить в кредит" ) );

    public MainPage() {
        heading.shouldBe( visible );
    }

    public PaymentPage goToPaymentPage() {
        buyButton.click();
        return new PaymentPage();
    }

    public CreditPage goToCreditPage() {
        creditButton.click();
        return new CreditPage();
    }
}