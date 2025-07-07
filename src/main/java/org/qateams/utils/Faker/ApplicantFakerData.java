package org.qateams.utils.Faker; // Убедитесь, что пакет соответствует вашему проекту

import com.github.javafaker.Faker;
import org.qateams.pages.components.user.valueobject.ApplicantData; // Импортируйте ваш Value Object ApplicantData
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Класс для генерации случайных тестовых данных для объекта ApplicantData.
 * Использует библиотеку JavaFaker.
 */
public class ApplicantFakerData {

    private static final Logger logger = LoggerFactory.getLogger(ApplicantFakerData.class);
    private static final Faker faker = new Faker(new Locale("ru")); // Используем русскую локаль для более релевантных данных
    private static final Random random = new Random();

    /**
     * Генерирует и возвращает объект ApplicantData, заполненный случайными данными.
     * Все поля будут заполнены.
     *
     * @return Объект ApplicantData со сгенерированными данными.
     */
    public static ApplicantData generateFullApplicantData() {
        ApplicantData data = new ApplicantData.Builder()
                .lastName(generateLastName())
                .firstName(generateFirstName())
                .middleName(generateMiddleName())
                .address(generateAddress())
                .phone(generatePhoneNumber())
                .passportNumber(generatePassportNumber())
                .build();
        logGeneratedData(data);
        return data;
    }

    /**
     * Генерирует и возвращает объект ApplicantData, позволяя переопределить
     * значения для определенных полей. Поля, не указанные в `overrides`,
     * будут заполнены случайными данными.
     *
     * @param overrides Карта, где ключ - название поля (например, "lastName"),
     * а значение - желаемое значение для этого поля.
     * @return Объект ApplicantData со сгенерированными или переопределенными данными.
     */
    public static ApplicantData generateApplicantDataWithOverrides(Map<String, String> overrides) {
        // Генерируем все поля по умолчанию
        String lastName = generateLastName();
        String firstName = generateFirstName();
        String middleName = generateMiddleName();
        String address = generateAddress();
        String phone = generatePhoneNumber();
        String passportNumber = generatePassportNumber();

        // Применяем переопределения, если они есть
        if (overrides != null) {
            lastName = overrides.getOrDefault("lastName", lastName);
            firstName = overrides.getOrDefault("firstName", firstName);
            middleName = overrides.getOrDefault("middleName", middleName);
            address = overrides.getOrDefault("address", address);
            phone = overrides.getOrDefault("phone", phone);
            passportNumber = overrides.getOrDefault("passportNumber", passportNumber);
        }

        // Строим объект ApplicantData
        ApplicantData data = new ApplicantData.Builder()
                .lastName(lastName)
                .firstName(firstName)
                .middleName(middleName)
                .address(address)
                .phone(phone)
                .passportNumber(passportNumber)
                .build();

        logGeneratedData(data);
        return data;
    }

    /**
     * Генерирует случайную фамилию.
     * @return Фамилия.
     */
    public static String generateLastName() {
        return faker.name().lastName();
    }

    /**
     * Генерирует случайное имя.
     * @return Имя.
     */
    public static String generateFirstName() {
        return faker.name().firstName();
    }

    /**
     * Генерирует случайное отчество (латиницей, как указано в документации).
     * Использует английский Faker для генерации латинских символов.
     * @return Отчество.
     */
    public static String generateMiddleName() {
        // Согласно документации: "данные из паспорта только латиницей без спецсимволов."
        // Используем английский Faker для генерации латинского имени.
        Faker latinFaker = new Faker(Locale.ENGLISH);
        return latinFaker.name().firstName();
    }

    /**
     * Генерирует случайный адрес прописки.
     * Ограничение: не более 50 символов, только буквы и спецсимволы.
     * @return Адрес прописки.
     */
    public static String generateAddress() {
        String city = faker.address().cityName();
        String street = faker.address().streetName();
        String address = String.format("%s, ул. %s", city, street);

        // Удаляем цифры и оставляем только буквы, знаки препинания и пробелы
        address = address.replaceAll("[0-9]", ""); // Удаляем все цифры
        // Оставляем только буквы (включая кириллицу), знаки препинания и пробелы
        address = address.replaceAll("[^\\p{L}\\p{Punct}\\s]", "");

        if (address.length() > 50) {
            address = address.substring(0, 50); // Обрезаем до 50 символов
        }
        return address.trim(); // Удаляем лишние пробелы в начале/конце
    }

    /**
     * Генерирует случайный номер телефона (11 цифр, начинается с 7).
     * @return Номер телефона.
     */
    public static String generatePhoneNumber() {
        // Согласно документации: "не более 11 символов; с кодом страны и кодом оператора, доступны латиница и цифры."
        // Генерируем 11-значный номер, начинающийся с 7, чтобы соответствовать формату РФ.
        return String.valueOf(faker.number().numberBetween(70000000000L, 79999999999L));
    }

    /**
     * Генерирует случайный номер паспорта.
     * Ограничение: не более 8 символов; русские буквы и цифры (0-8).
     * @return Номер паспорта.
     */
    public static String generatePassportNumber() {
        // Русские буквы, используемые в сериях паспортов, которые имеют латинские аналоги
        // (чтобы избежать проблем с кодировкой и обеспечить совместимость)
        char[] cyrillicLetters = {'А', 'В', 'Е', 'К', 'М', 'Н', 'О', 'Р', 'С', 'Т', 'У', 'Х'};
        StringBuilder passportSeries = new StringBuilder();
        passportSeries.append(cyrillicLetters[random.nextInt(cyrillicLetters.length)]);
        passportSeries.append(cyrillicLetters[random.nextInt(cyrillicLetters.length)]);

        StringBuilder passportDigits = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            passportDigits.append(random.nextInt(9)); // Цифры от 0 до 8
        }
        return passportSeries.toString() + passportDigits.toString();
    }

    /**
     * Вспомогательный метод для логирования сгенерированных данных заявителя.
     * @param data Объект ApplicantData для логирования.
     */
    private static void logGeneratedData(ApplicantData data) {
        StringBuilder logMessage = new StringBuilder("Сгенерированы данные для заявителя:\n");
        logMessage.append("Фамилия: ").append(data.getLastName()).append("\n");
        logMessage.append("Имя: ").append(data.getFirstName()).append("\n");
        logMessage.append("Отчество: ").append(data.getMiddleName()).append("\n");
        logMessage.append("Адрес прописки: ").append(data.getAddress()).append("\n");
        logMessage.append("Телефон: ").append(data.getPhone()).append("\n");
        logMessage.append("Номер паспорта: ").append(data.getPassportNumber()).append("\n");
        logger.info(logMessage.toString());
    }
}
