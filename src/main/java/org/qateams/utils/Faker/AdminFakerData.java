package org.qateams.utils.Faker;

import com.github.javafaker.Faker;
import org.qateams.pages.BasePage;
import org.qateams.pages.components.admin.AdminLoginComponent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminFakerData {

    private static final Logger logger = LoggerFactory.getLogger(AdminFakerData.class);


    private static final Faker faker = new Faker(new Locale("ru"));

    public static AdminLoginComponent generateAndFillAdminData() {
        BasePage hp = new BasePage();
        hp.clickEnterAsAdmin();

        AdminLoginComponent ad = new AdminLoginComponent();
        String lastName = generateLastName();
        String firstName = generateFirstName();
        String middleName = generateMiddleName();
        String phoneNumber = generatePhoneNumber();
        String passportNumber = generatePassportNumber();
        String birthDate = generateBirthDate();

        ad.fillForm(
                lastName,
                firstName,
                middleName,
                phoneNumber,
                passportNumber,
                birthDate
        );

        logger.info("Сгенерированы данные для входа:\n" +
                        "Фамилия: {}\n" +
                        "Имя: {}\n" +
                        "Отчество: {}\n" +
                        "Телефон: {}\n" +
                        "Номер паспорта: {}\n" +
                        "Дата рождения: {}",
                lastName, firstName, middleName,
                phoneNumber, passportNumber, birthDate);

        return ad;
    }

    public static String generateLastName() {
        return faker.name().lastName();
    }

    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generateMiddleName() {
        String firstName = faker.name().firstName();
        return generatePatronymic(firstName);
    }

    public static String generatePhoneNumber() {
        return "+7" + faker.number().numberBetween(9000000000L, 9999999999L);
    }

    public static String generatePassportNumber() {
        // Генерация 10-значного номера паспорта
        return faker.number().numberBetween(1000000000L, 9999999999L) + "";
    }

    public static String generateBirthDate() {
        // Генерация даты рождения (совершеннолетний)
        LocalDate birthDate = LocalDate.now()
                .minusYears(faker.number().numberBetween(18, 65))
                .minusDays(faker.number().numberBetween(1, 365));

        return birthDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    // Метод для генерации отчества на основе имени
    private static String generatePatronymic(String firstName) {
        String[] patronymicSuffixes = {"ович", "евич", "ич", "овна", "евна"};
        String suffix = patronymicSuffixes[faker.number().numberBetween(0, patronymicSuffixes.length)];

        // Усечение или модификация основы имени
        String patronymicBase = firstName.length() > 3
                ? firstName.substring(0, firstName.length() - 1)
                : firstName;

        return patronymicBase + suffix;
    }


}
