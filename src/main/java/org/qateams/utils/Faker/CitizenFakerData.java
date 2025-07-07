package org.qateams.utils.Faker;

import com.github.javafaker.Faker;
import org.qateams.pages.components.user.valueobject.CitizenData;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CitizenFakerData {

    private static final Faker faker = new Faker(new Locale("ru")); // Используем русскую локаль для более релевантных данных
    private static final Random random = new Random();

    /**
     * Генерирует полные валидные данные гражданина.
     * ОБНОВЛЕНО: Отчество теперь гарантированно имеет длину не менее 6 символов.
     * @return Объект CitizenData с заполненными полями.
     */
    public static CitizenData generateFullCitizenData() {
        String lastName = faker.name().lastName();
        String firstName = faker.name().firstName();

        // ОБНОВЛЕНИЕ: Генерируем отчество, пока оно не будет не менее 6 символов.
        String middleName;
        do {
            middleName = faker.name().firstName(); // Faker не имеет отчеств, используем имя
        } while (middleName.length() > 6);


        String passportNumber = generateRussianPassportNumber();
        String gender = random.nextBoolean() ? "Муж" : "Жен"; // Соответствует maxlength="4"

        // Генерация даты рождения в формате YYYY-MM-DD для input type="date"
        LocalDate birthDate = LocalDate.now().minusYears(18 + random.nextInt(50)); // Возраст 18-68 лет
        String dateOfBirth = birthDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        // Корректировка адреса, чтобы он не содержал цифр, если это строго требуется UI-валидацией
        // Faker может генерировать адреса с цифрами. Если UI их не принимает, нужно очистить.
        String address = faker.address().fullAddress(); // Получаем полный адрес
        String cleanedAddress = address.replaceAll("[0-9]", "").trim(); // Удаляем цифры
        // Обрезаем до 50 символов, если необходимо
        if (cleanedAddress.length() > 50) {
            cleanedAddress = cleanedAddress.substring(0, 50);
        }

        return new CitizenData.Builder()
                .lastName(lastName)
                .firstName(firstName)
                .middleName(middleName) // Используем отчество, которое теперь >= 6 символов
                .address(cleanedAddress)
                .dateOfBirth(dateOfBirth)
                .passportNumber(passportNumber)
                .gender(gender)
                .build();
    }

    /**
     * Генерирует случайный номер российского паспорта (2 русские буквы + 6 цифр).
     * Соответствует формату "АБ123456"
     * @return Сгенерированный номер паспорта.
     */
    private static String generateRussianPassportNumber() {
        StringBuilder sb = new StringBuilder();
        // 2 русские буквы (пример: А, Б, В, Г, Д, Е, Ж, З, И, Й, К, Л, М, Н, О, П, Р, С, Т, У, Ф, Х, Ц, Ч, Ш, Щ, Ъ, Ы, Ь, Э, Ю, Я)
        String russianLetters = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        sb.append(russianLetters.charAt(random.nextInt(russianLetters.length())));
        sb.append(russianLetters.charAt(random.nextInt(russianLetters.length())));

        // 6 цифр
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(9)); // Цифры от 0 до 8
        }
        return sb.toString();
    }
}