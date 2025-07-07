package org.qateams.utils.Faker;

import com.github.javafaker.Faker;
import org.qateams.pages.components.user.valueobject.MarriageServiceData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class MarriageServiceFakerData {

    private static final Faker faker = new Faker(new Locale("ru"));

    /**
     * Генерирует полные валидные данные для услуги "Регистрация брака".
     * @return Объект MarriageServiceData с заполненными данными.
     */
    public static MarriageServiceData generateFullMarriageServiceData() {
        // Дата регистрации - сегодня или в ближайшем будущем
        LocalDate registrationDate = LocalDate.now().plusDays(ThreadLocalRandom.current().nextInt(1, 30));
        String formattedRegistrationDate = registrationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Для input type="date"

        String newLastName = faker.bool().bool() ? faker.name().lastName() : null; // Необязательное поле

        String spouseLastName = faker.name().lastName();
        String spouseFirstName = faker.name().firstName();
        String spouseMiddleName;
        // Отчество супруга/и: не менее 6 символов, как мы обнаружили для отчества заявителя
        do {
            spouseMiddleName = faker.name().firstName();
        } while (spouseMiddleName.length() < 6);

        // Дата рождения супруга/и - от 18 до 65 лет назад
        LocalDate today = LocalDate.now();
        LocalDate minSpouseDate = today.minusYears(65);
        LocalDate maxSpouseDate = today.minusYears(18);
        long minSpouseDay = minSpouseDate.toEpochDay();
        long maxSpouseDay = maxSpouseDate.toEpochDay();
        LocalDate randomSpouseDate = LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(minSpouseDay, maxSpouseDay));
        String formattedSpouseDateOfBirth = randomSpouseDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Для input type="date"

        // Номер паспорта супруга/и: 2 русские буквы + 6 цифр
        String passportSeries = faker.regexify("[А-Я]{2}");
        String passportNumber = faker.number().digits(6);
        String fullSpousePassportNumber = passportSeries + passportNumber;

        return new MarriageServiceData.Builder()
                .registrationDate(formattedRegistrationDate)
                .newLastName(newLastName)
                .spouseLastName(spouseLastName)
                .spouseFirstName(spouseFirstName)
                .spouseMiddleName(spouseMiddleName)
                .spouseDateOfBirth(faker.bool().bool() ? formattedSpouseDateOfBirth : null) // Необязательное поле
                .spousePassportNumber(fullSpousePassportNumber)
                .build();
    }

    /**
     * Генерирует номер российского паспорта (2 русские буквы + 6 цифр).
     * @return Сгенерированный номер паспорта.
     */
    private static String generateRussianPassportNumber() {
        StringBuilder sb = new StringBuilder();
        String russianLetters = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        sb.append(russianLetters.charAt(ThreadLocalRandom.current().nextInt(russianLetters.length())));
        sb.append(russianLetters.charAt(ThreadLocalRandom.current().nextInt(russianLetters.length())));

        for (int i = 0; i < 6; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(10)); // Цифры от 0 до 9
        }
        return sb.toString();
    }
}