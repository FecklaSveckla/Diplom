package data;

import data.Card;

import com.github.javafaker.Faker;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {

    private DataHelper() {

    }

    private static Faker fakerEn = new Faker( new Locale( "en" ) );
    private static Faker fakerRu = new Faker( new Locale( "ru" ) );

    //Для позитивных проверок.

    @DisplayName("Для Сценария 1. Заявка Оплата по карте, заполненная валидными данными карты со статусом Approved успешно одобрена банком")
    public static Card getApprovedCard() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), gerValidYear(), getValidHolder(), getValidCvc() );

    }

    @DisplayName("Для сценария 2. Заявка Оплата по карте, заполненная данными карты со статусом Declined отклонена банком")
    public static Card getDeclinedCard() {
        return new Card( "4444 4444 4444 4442", getValidMonth(), gerValidYear(), getValidHolder(), getValidCvc() );
    }

    @DisplayName("Сценарий 3. Отправка формы заявки, в которой поле Владелец содержит значение введенное в верхнем регистре. Остальные поля заполнены валидными данными")
    public static Card getHolderInUpperCase() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), gerValidYear(), "IVAN IVANOV", getValidCvc() );
    }

    @DisplayName("Сценарий 4. Отправка формы заявки, в которой поле Владелец содержит значение через дефис. Остальные поля формы содержат валидные данные.")
    public static Card getHolderHyphenated() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), gerValidYear(), " Ivan Petrov-Razumovskiy", getValidCvc() );
    }


    //Поле "Номер карты"
    @DisplayName("Сценарий 1. Негативный. Заявка Оплата по карте, заполненная данными карты, отсутствующими в БД банка, отклонена банком")
    public static Card getNonExistCard() {
        return new Card( getRandomCardNumber(), getValidMonth(), gerValidYear(), getValidHolder(), getValidCvc() );
    }

    @DisplayName("Сценарий 2. Негативный. Отправка формы заявки с  пустым полем Номер карты. Остальные поля формы заполнены валидными значениями")
    public static Card getEmptyFieldCardNumber() {
        return new Card( "", getValidMonth(), gerValidYear(), getValidHolder(), getValidCvc() );
    }

    @DisplayName("Сценарий 3. Негативный. Отправка формы заявки, в которой поле Номер карты содержит одну цифру. Остальные поля формы заполнены валидными данными")
    public static Card getOneNumberInFieldCard() {
        return new Card( "1", getValidMonth(), gerValidYear(), getValidHolder(), getValidCvc() );
    }

    @DisplayName("Сценарий 4. Негативный. Отправка формы заявки, в которой поле Номер карты содержит 15 цифр. Остальные поля формы заполнены валидными данным")
    public static Card getInvalidCardNumber() {
        return new Card( "4444 4444 4444 444", getValidMonth(), gerValidYear(), getValidHolder(), getValidCvc() );
    }

    // Поле месяц
    @DisplayName("Сценарий 5. Негативный. Отправка формы заявки, в которой поле Месяц пустое. Остальные поля формы заполнены валидными зданными.")
    public static Card getEmptyMonth() {
        return new Card( "4444 4444 4444 4441", "", gerValidYear(), getValidHolder(), getValidCvc() );
    }

    @DisplayName("Сценарий 6. Негативный. Отправка формы заявки, в которой поле Месяц содержит  значение больше 12. Остальные поля формы заполнены валидными данными.")
    public static Card getMonthOver12() {
        return new Card( "4444 4444 4444 4441", "13", gerValidYear(), getValidHolder(), getValidCvc() );
    }

    @DisplayName("Сценарий 7. Негативный. Отправка формы заявки, в которой поле Месяц меньше значения 01. Остальные поля формы заполнены валидными данными.")
    public static Card getZeroMonth() {
        return new Card( "4444 4444 4444 4441", "00", gerValidYear(), getValidHolder(), getValidCvc() );
    }

    @DisplayName("Сценарий 8. Негативный. Отправка формы заявки, в которой поле Месяц содержит значение предыдущее от текущего. Остальные поля формы заполнены валидными данным")
    public static Card getInvalidPastMonth() {
        return new Card( "4444 4444 4444 4441", getPastMonth(), gerValidYear(), getValidHolder(), getValidCvc() );
    }

    // Поле "Год"
    @DisplayName("Сценарий 9. Негативный. Отправка формы заявки, в которой поле Год пустое. Остальные поля формы заполнены валидными данными.")
    public static Card getEmptyYear() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), "", getValidHolder(), getValidCvc() );
    }

    @DisplayName("Сценарий 10. Негативный. Отправка формы заявки, в которой поле Год содержит значение соответствующее любому предыдущему году. Остальные поля формы заполнены валидными данными.")
    public static Card getLastYear() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), getPastYear(), getValidHolder(), getValidCvc() );
    }

    @DisplayName("Сценарий 11. Негативный. Отправка формы заявки, в которой поле  Год содержит значение +10 лет к текущему значению года. Остальные поля формы заполнены валидными значениями.")
    public static Card getNotComingYear() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), getFutureYear(), getValidHolder(), getValidCvc() );
    }

    //Поле CVC
    @DisplayName("Сцуеарий 12. Негативный. Отправка формы заявки, в которой поле CVC/CVV пусто. Остальные поля формы заполнены валидными значениями.")
    public static Card getEmptyCVC() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), gerValidYear(), getValidHolder(), "" );
    }

    @DisplayName("Сценарий 13. Негативный. Отправка формы заявки, в которой поле CVC/CVV содержит 1 цифру. Остальные поля формы заполнены валидными значениями.")
    public static Card getOneNumberCVC() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), gerValidYear(), getValidHolder(), "2" );
    }

    @DisplayName("Сценарий 14. Негативный. Отправка формы заявки, в которой поле CVC/CVV содержит 2-е цифры. Остальные поля формы заполнены валидными значениями.")
    public static Card getTwoNumberCVC() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), gerValidYear(), getValidHolder(), "33" );
    }

    //Поле "Владелец"
    @DisplayName("Сценарий 15. Негативный. Отправка формы заявки, в которой поле Владелец пустое. Остальные поля формы содержат валидные данные.")
    public static Card getEmptyHolderCard() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), gerValidYear(), "", getValidCvc() );
    }

    @DisplayName("Сценарий 16. Негативный. Отправка формы заявки, в которой поле Владелец содержит фамилию на латинице, а имя отсуствует. Остальные поля формы заполнены валидными данными.")
    public static Card getInvalidHolderOneNameCard() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), gerValidYear(), "Ivanov", getValidCvc() );
    }

    @DisplayName("Сценарий 17. Негативный. Отправка формы заявки, в которой поле Владелец содержит фамилию и имя на кирилице. Остальные поля формы заполнены валидными данными.")
    public static Card getInvalidHolderRusCard() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), gerValidYear(), getRusHolder(), getValidCvc() );
    }

    @DisplayName("Сценарий 18. Негативный. Отправка формы заявки, в которой поле Владелец заполнено цифрами. Остальные поля формы заполнены валидными данными")
    public static Card getInvalidHolderNumbersCard() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), gerValidYear(), "12345 12345", getValidCvc() );
    }

    @DisplayName("Сценарий 19. Негативный. Оправка формы заявки, в которой поле Владелец заполнено спец.символами. Остальные поля формы заполнены валидными данными.")
    public static Card getInvalidHolderSymbolsCard() {
        return new Card( "4444 4444 4444 4441", getValidMonth(), gerValidYear(), "@#$%^&*()~-+/*?><|", getValidCvc() );
    }

    //ГЕНЕРАЦИЯ ДАННЫХ

    public static String getRandomCardNumber() {
        return fakerEn.business().creditCardNumber();
    }

    public static String getValidMonth() {
        String validMonth = LocalDate.now().format( DateTimeFormatter.ofPattern( "MM" ) );
        return validMonth;
    }

    //получение предыдущего месяца
    private static String getPastMonth() {
        return LocalDate.now().minusMonths( 1 ).format( DateTimeFormatter.ofPattern( "MM" ) );
    }

    public static String gerValidYear() {
        String validYear = LocalDate.now().format( DateTimeFormatter.ofPattern( "yy" ) );
        return validYear;
    }

    // получение предыдущего года
    public static String getPastYear() {
        String PastYear = LocalDate.now().minusYears( 1 ).format( DateTimeFormatter.ofPattern( "yy" ) );
        return PastYear;
    }

    //получение будущего года +10 лет
    public static String getFutureYear() {
        String futureYear = LocalDate.now().plusYears( 10 ).format( DateTimeFormatter.ofPattern( "yy" ) );
        return futureYear;
    }

    public static String getValidHolder() {
        return fakerEn.name().firstName() + " " + fakerEn.name().lastName();
    }

    public static String getRusHolder() {
        return fakerRu.name().fullName();
    }

    public static String getValidCvc() {
        return fakerEn.number().digits( 3 );
    }


}
