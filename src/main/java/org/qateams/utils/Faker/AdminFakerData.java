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


    public static AdminLoginComponent generateAndFillAdminData(int... fieldsToGenerate) {
//        BasePage hp = new BasePage();
//        hp.clickEnterAsAdmin();

        AdminLoginComponent ad = new AdminLoginComponent();

        // Массив всех возможных значений
        String[] generatedFields = new String[6];
        generatedFields[0] = generateLastName();
        generatedFields[1] = generateFirstName();
        generatedFields[2] = generateMiddleName();
        generatedFields[3] = generatePhoneNumber();
        generatedFields[4] = generatePassportNumber();
        generatedFields[5] = generateBirthDate();

        // Если не переданы поля, генерируем все
        if (fieldsToGenerate.length == 0) {
            ad.fillForm(generatedFields[0], generatedFields[1], generatedFields[2],
                    generatedFields[3], generatedFields[4], generatedFields[5]);
            logGeneratedData(generatedFields);
            return ad;
        }

        // Подготовка массива для передачи в fillForm
        String[] filledFields = new String[6];
        for (int index : fieldsToGenerate) {
            // Проверка на корректность индекса
            if (index < 0 || index >= 6) {
                throw new IllegalArgumentException("Некорректный индекс поля: " + index);
            }
            filledFields[index] = generatedFields[index];
        }

        // Вызов fillForm с выбранными полями
        ad.fillForm(
                filledFields[0] != null ? filledFields[0] : "",
                filledFields[1] != null ? filledFields[1] : "",
                filledFields[2] != null ? filledFields[2] : "",
                filledFields[3] != null ? filledFields[3] : "",
                filledFields[4] != null ? filledFields[4] : "",
                filledFields[5] != null ? filledFields[5] : ""
        );

        logGeneratedData(filledFields);
        return ad;
    }

    // Вспомогательный метод для логирования
    private static void logGeneratedData(String[] fields) {
        String[] fieldNames = {
                "Фамилия", "Имя", "Отчество",
                "Телефон", "Номер паспорта", "Дата рождения"
        };

        StringBuilder logMessage = new StringBuilder("Сгенерированы данные для админа:\n");
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] != null) {
                logMessage.append(fieldNames[i]).append(": ").append(fields[i]).append("\n");
            }
        }

        logger.info(logMessage.toString());
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
