package org.qateams.tests.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.base.BaseTest;
import org.qateams.core.driver.DriverManager;
import org.qateams.pages.BasePage;
import org.qateams.pages.components.common.StepIndicatorComponent;
import org.qateams.pages.components.user.CitizenDataPage;
import org.qateams.pages.components.user.ServiceSelectionPage;
import org.qateams.pages.components.user.UserRegistrationPage;
import org.qateams.pages.components.user.valueobject.ApplicantData;
import org.qateams.pages.components.user.valueobject.CitizenData;
import org.qateams.utils.Faker.ApplicantFakerData;
import org.qateams.utils.Faker.CitizenFakerData;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CitizenDataTest extends BaseTest {

    private UserRegistrationPage userRegistrationPage;
    private ServiceSelectionPage serviceSelectionPage;
    private CitizenDataPage citizenDataPage;
    private StepIndicatorComponent stepIndicatorComponent;

    // Вспомогательный метод для ожидания загрузки модального окна
    private void waitForModalLoaded() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("MuiDialog-paper")));
    }

    @BeforeMethod
    public void setupCitizenDataTest() {
        initSoftAssert(); // Инициализация SoftAssert из BaseTest

        BasePage hp = new BasePage();
        userRegistrationPage = new UserRegistrationPage();
        serviceSelectionPage = new ServiceSelectionPage(DriverManager.getDriver());
        citizenDataPage = new CitizenDataPage(); // Инициализация CitizenDataPage
        stepIndicatorComponent = new StepIndicatorComponent();

        // Шаг 1: Переход на страницу UserRegistrationPage и заполнение данных заявителя
        hp.clickEnterAsUser();
        waitForModalLoaded(); // Ожидаем модальное окно UserRegistrationPage
        ApplicantData fullApplicantData = ApplicantFakerData.generateFullApplicantData();
        userRegistrationPage.fillApplicantData(fullApplicantData);
        userRegistrationPage.clickNextButton();

        // Шаг 2: Переход на страницу ServiceSelectionPage и выбор услуги (например, "Регистрация брака")
        waitForModalLoaded(); // Ожидаем модальное окно ServiceSelectionPage
        serviceSelectionPage.selectMarriageRegistration(); // Выбираем любую услугу

        // Шаг 3: Теперь мы должны быть на CitizenDataPage
        waitForModalLoaded(); // Ожидаем модальное окно CitizenDataPage
        // softAssert.assertTrue(citizenDataPage.isPageLoaded(), "Не удалось перейти на страницу 'Данные гражданина'.");
    }

    // --- Тесты для индикаторов шагов ---
    @Test(description = "Проверка цветового индикатора этапов на странице 'Данные гражданина'")
    public void testStepIndicatorOnCitizenDataPage() {
        // Проверка индикаторов шагов: Шаг 1 и 2 завершены (зеленые/синие), Шаг 3 активен (зеленый/синий), остальные серые
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(1),
                "Индикатор этапов: Шаг 1 'Данные заявителя' должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(2),
                "Индикатор этапов: Шаг 2 'Выбор услуги' должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(3),
                "Индикатор этапов: Шаг 3 'Данные гражданина' должен быть активным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGray(4),
                "Индикатор этапов: Шаг 4 'Даные услуги' должен быть серым.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGray(5),
                "Индикатор этапов: Шаг 5 'Статус заявки' должен быть серым.");
        
    }

    // --- Тесты для начального состояния и валидации ---
    @Test(description = "Проверка начального состояния: кнопка 'Далее' неактивна")
    public void testNextButtonIsInitiallyDisabled() {
        softAssert.assertFalse(citizenDataPage.isNextButtonEnabled(),
                "Кнопка 'Далее' должна быть неактивна до заполнения обязательных полей.");
        
    }

    @Test(description = "Проверка валидации обязательных полей при пустом заполнении")
    public void testRequiredFieldsValidation() {
        CitizenData emptyData = new CitizenData.Builder().build();
        citizenDataPage.fillCitizenData(emptyData);
        citizenDataPage.clickOnFreeArea(); // Кликаем на свободную область для активации валидации

        softAssert.assertFalse(citizenDataPage.isNextButtonEnabled(),
                "Кнопка 'Далее' должна быть неактивна при незаполненных обязательных полях.");

        softAssert.assertTrue(citizenDataPage.isFieldHighlightedRed("фамилия"), "Поле 'Фамилия' должно быть красным.");
        softAssert.assertTrue(citizenDataPage.isFieldHighlightedRed("имя"), "Поле 'Имя' должно быть красным.");
        softAssert.assertTrue(citizenDataPage.isFieldHighlightedRed("отчество"), "Поле 'Отчество' должно быть красным.");
        softAssert.assertTrue(citizenDataPage.isFieldHighlightedRed("дата рождения"), "Поле 'Дата рождения' должно быть красным.");
        softAssert.assertTrue(citizenDataPage.isFieldHighlightedRed("номер паспорта"), "Поле 'Номер паспорта' должно быть красным.");
        softAssert.assertTrue(citizenDataPage.isFieldHighlightedRed("пол"), "Поле 'Пол' должно быть красным.");

        
    }

    // --- Вспомогательные методы для проверки ограничений полей ---

    /**
     * Проверяет, что поле обрезает ввод до указанной максимальной длины.
     * @param fieldElement WebElement поля.
     * @param fieldName Название поля для сообщения об ошибке.
     * @param maxLength Максимально допустимая длина.
     * @param inputString Строка, превышающая максимальную длину.
     */
    private void verifyFieldLengthRestriction(WebElement fieldElement, String fieldName, int maxLength, String inputString) {
        fieldElement.sendKeys(inputString);
        softAssert.assertEquals(fieldElement.getAttribute("value").length(), maxLength,
                String.format("%s: Поле должно обрезать ввод до %d символов.", fieldName, maxLength));
    }

    /**
     * Проверяет, что поле принимает указанные допустимые символы.
     * @param fieldElement WebElement поля.
     * @param fieldName Название поля для сообщения об ошибке.
     * @param validChars Строка с допустимыми символами.
     */
    private void verifyFieldAcceptsValidChars(WebElement fieldElement, String fieldName, String validChars) {
        fieldElement.sendKeys(validChars);
        softAssert.assertEquals(fieldElement.getAttribute("value"), validChars,
                String.format("%s: Поле должно принимать '%s'.", fieldName, validChars));
    }

    /**
     * Проверяет, что поле не принимает указанные недопустимые символы.
     * @param fieldElement WebElement поля.
     * @param fieldName Название поля для сообщения об ошибке.
     * @param inputString Строка, содержащая недопустимые символы.
     * @param invalidCharsRegex Регулярное выражение для недопустимых символов.
     * @param errorMessage Сообщение об ошибке, если недопустимые символы обнаружены.
     */
    private void verifyFieldRejectsInvalidChars(WebElement fieldElement, String fieldName, String inputString, String invalidCharsRegex, String errorMessage) {
        fieldElement.sendKeys(inputString);
        String actualValue = fieldElement.getAttribute("value");
        softAssert.assertFalse(actualValue.matches(invalidCharsRegex),
                String.format("%s: %s", fieldName, errorMessage));
    }

    // --- Тесты для ограничений ввода (упрощенные и улучшенная читаемость данных) ---

    @Test(description = "Проверка ограничений поля 'Фамилия'")
    public void testLastNameFieldRestrictions() {
        citizenDataPage.clearLastNameField();
        // Длина и спецсимволы
        String longLastNameWithSpecialChars = "СпецСимволы123!@#" + "Д".repeat(84); // Общая длина 100+
        verifyFieldLengthRestriction(citizenDataPage.lastNameField(), "Фамилия", 100, longLastNameWithSpecialChars);
        softAssert.assertTrue(citizenDataPage.lastNameField().getAttribute("value").contains("!@#"),
                "Фамилия: Поле должно принимать спецсимволы.");
        
    }

    @Test(description = "Проверка ограничений поля 'Имя'")
    public void testFirstNameFieldRestrictions() {
        citizenDataPage.clearFirstNameField();
        // Длина и цифры
        String longFirstNameWithNumbers = "ИмяСЦифрами1234" + "Е".repeat(86); // Общая длина 100+
        verifyFieldLengthRestriction(citizenDataPage.firstNameField(), "Имя", 100, longFirstNameWithNumbers);
        softAssert.assertTrue(citizenDataPage.firstNameField().getAttribute("value").contains("1234"),
                "Имя: Поле должно принимать цифры.");
        
    }

    @Test(description = "Проверка ограничений поля 'Отчество'")
    public void testMiddleNameFieldRestrictions() {
        citizenDataPage.clearMiddleNameField();
        // Длина
        String longMiddleName = "A".repeat(100); // 100 символов
        String extraChar = "X"; // Дополнительный символ, чтобы проверить обрезку
        verifyFieldLengthRestriction(citizenDataPage.middleNameField(), "Отчество", 100, longMiddleName + extraChar);

        citizenDataPage.clearMiddleNameField();
        // Недопустимые символы: спецсимволы и русские буквы
        String middleNameWithInvalidChars = "Отчество!@#$РусскиеБуквы";
        verifyFieldRejectsInvalidChars(citizenDataPage.middleNameField(), "Отчество", middleNameWithInvalidChars, ".*[!@#$%^&*А-Яа-я].*", "Не должно содержать спецсимволы или русские буквы.");

        citizenDataPage.clearMiddleNameField();
        // Допустимые символы: только латиница
        String validMiddleNameLat = "ValidMiddleNameLat";
        verifyFieldAcceptsValidChars(citizenDataPage.middleNameField(), "Отчество", validMiddleNameLat);
        
    }

    // НОВЫЙ ТЕСТ: Баг с минимальной длиной поля "Отчество"
    @Test(description = "Баг: Кнопка 'Далее' неактивна при 'Отчестве' менее 5 символов")
    public void testMiddleNameMinLengthBug() {
        CitizenData baseData = CitizenFakerData.generateFullCitizenData();
        // Создаем новый объект CitizenData, изменяя только Отчество
        CitizenData dataWithShortMiddleName = new CitizenData.Builder()
                .lastName(baseData.getLastName())
                .firstName(baseData.getFirstName())
                .middleName("Иван") // 4 символа, меньше 5
                .address(baseData.getAddress())
                .dateOfBirth(baseData.getDateOfBirth())
                .passportNumber(baseData.getPassportNumber())
                .gender(baseData.getGender())
                .build();

        citizenDataPage.fillCitizenData(dataWithShortMiddleName);
        citizenDataPage.clickOnFreeArea(); // Активируем валидацию

        softAssert.assertFalse(citizenDataPage.isNextButtonEnabled(),
                "Баг: Кнопка 'Далее' неактивна, если 'Отчество' содержит менее 5 символов.");
    }

    @Test(description = "Проверка ограничений поля 'Дата рождения'")
    public void testDateOfBirthFieldRestrictions() {
        citizenDataPage.clearDateOfBirthField();
        String validDate = "31.12.2000";
        citizenDataPage.dateOfBirthField().sendKeys(validDate);
        softAssert.assertEquals(citizenDataPage.dateOfBirthField().getAttribute("value"), "2000-12-31",
                "Дата рождения: Поле должно отображать дату в формате YYYY-MM-DD после ввода.");

        citizenDataPage.clearDateOfBirthField();
        String invalidDateInput = "не дата";
        citizenDataPage.dateOfBirthField().sendKeys(invalidDateInput);
        softAssert.assertTrue(citizenDataPage.dateOfBirthField().getAttribute("value").isEmpty(),
                "Дата рождения: Поле не должно принимать невалидный формат.");

        softAssert.assertEquals(citizenDataPage.dateOfBirthField().getAttribute("placeholder"), "дд/мм/гггг",
                "Дата рождения: Подсказка должна быть 'дд/мм/гггг'.");
        
    }

    @Test(description = "Проверка ограничений поля 'Номер паспорта'")
    public void testPassportNumberFieldRestrictions() {
        citizenDataPage.clearPassportNumberField();
        // Длина
        String longPassportNumber = "АБ123456789"; // 9 символов
        verifyFieldLengthRestriction(citizenDataPage.passportNumberField(), "Номер паспорта", 8, longPassportNumber);

        citizenDataPage.clearPassportNumberField();
        // Недопустимые символы: латиница и спецсимволы
        String passportNumberWithInvalidChars = "AB12345!"; // Латиница и спецсимволы
        verifyFieldRejectsInvalidChars(citizenDataPage.passportNumberField(), "Номер паспорта", passportNumberWithInvalidChars, ".*[A-Za-z!@#$%^&*].*", "Не должен принимать латиницу или спецсимволы.");

        citizenDataPage.clearPassportNumberField();
        // Допустимые символы: русские буквы и цифры
        String validPassportNumber = "ВГ987654";
        verifyFieldAcceptsValidChars(citizenDataPage.passportNumberField(), "Номер паспорта", validPassportNumber);
        
    }

    @Test(description = "Проверка ограничений поля 'Пол'")
    public void testGenderFieldRestrictions() {
        citizenDataPage.clearGenderField();
        // Длина
        String longGenderInput = "Мужской"; // > 4 символов
        verifyFieldLengthRestriction(citizenDataPage.genderField(), "Пол", 4, longGenderInput);

        citizenDataPage.clearGenderField();
        // Допустимые значения (сокращения)
        String validMaleGender = "Муж";
        verifyFieldAcceptsValidChars(citizenDataPage.genderField(), "Пол", validMaleGender);

        citizenDataPage.clearGenderField();
        String validFemaleGender = "Жен";
        verifyFieldAcceptsValidChars(citizenDataPage.genderField(), "Пол", validFemaleGender);

        // Проверка на недопустимые значения (если UI их не принимает)
        citizenDataPage.clearGenderField();
        String invalidGenderInput = "Другой";
        citizenDataPage.genderField().sendKeys(invalidGenderInput);
        // Здесь нужно будет уточнить, как UI реагирует на невалидный ввод.
        // Если поле очищается или подсвечивается красным, то проверяем это.
        // softAssert.assertTrue(citizenDataPage.isFieldHighlightedRed("пол"), "Поле 'Пол' должно быть красным для невалидного ввода.");
        
    }

    @Test(description = "Проверка ограничений поля 'Адрес прописки'")
    public void testAddressFieldRestrictions() {
        citizenDataPage.clearAddressField();
        // Длина
        String veryLongAddress = "ОченьДлинныйАдресНаУлицеЛенинаДомОдинКвартираСтоПятьдесятВосемьГородМосква"; // > 50 символов
        verifyFieldLengthRestriction(citizenDataPage.addressField(), "Адрес прописки", 50, veryLongAddress);

        citizenDataPage.clearAddressField();
        // Допустимые символы: буквы и спецсимволы
        String validAddressWithSpecialChars = "Улица Пушкина, дом А!@#$";
        verifyFieldAcceptsValidChars(citizenDataPage.addressField(), "Адрес прописки", validAddressWithSpecialChars);

        citizenDataPage.clearAddressField();
        // Недопустимые символы: цифры
        String addressWithNumbers = "Улица Мира 123";
        verifyFieldRejectsInvalidChars(citizenDataPage.addressField(), "Адрес прописки", addressWithNumbers, ".*\\d.*", "Не должен принимать цифры.");
        
    }

    // --- Тесты для навигации и успешной отправки ---

    @Test(description = "Проверка активации кнопки 'Далее' при валидном заполнении полей")
    public void testNextButtonActivationOnValidInput() {
        // Проверка полей с минимальной длиной в 1 символ (если не указано иное в ТЗ)
        // Для фамилии, имени и адреса прописки, убедимся, что 1-2 символа не блокируют кнопку "Далее"
        // (Отчество теперь будет валидным по длине благодаря CitizenFakerData)
        CitizenData dataWithMinLengths = new CitizenData.Builder()
                .lastName("Ф") // 1 символ
                .firstName("И") // 1 символ
                .middleName(CitizenFakerData.generateFullCitizenData().getMiddleName()) // Используем валидное отчество
                .dateOfBirth("01.01.2000")
                .passportNumber("АА123456")
                .gender("Муж")
                .address("А") // 1 символ
                .build();
        citizenDataPage.fillCitizenData(dataWithMinLengths);
        citizenDataPage.clickOnFreeArea(); // Активируем валидацию
        softAssert.assertTrue(citizenDataPage.isNextButtonEnabled(),
                "Кнопка 'Далее' должна быть активна при минимальном заполнении полей 'Фамилия', 'Имя', 'Адрес прописки' (если они не имеют скрытых ограничений).");

        
    }

    @Test(description = "Успешная отправка формы и проверка перехода по этапам")
    public void testSuccessfulFormSubmissionAndStepNavigation() {
        // Заполняем все поля валидными данными (гарантированно валидными, т.к. Faker настроен)
        CitizenData validCitizenData = CitizenFakerData.generateFullCitizenData();
        citizenDataPage.fillCitizenData(validCitizenData);
        citizenDataPage.clickOnFreeArea(); // Активируем валидацию

        // Убеждаемся, что кнопка "Далее" активна (можно опустить, если предыдущий тест это покрывает)
        softAssert.assertTrue(citizenDataPage.isNextButtonEnabled(),
                "Предварительная проверка: Кнопка 'Далее' должна быть активна перед кликом.");

        // Кликаем "Далее"
        citizenDataPage.clickNextButton();

        // TODO: Добавить проверку перехода на следующую страницу (MarriageServiceDataPage, BirthServiceDataPage, DeathServiceDataPage)
        // В зависимости от выбранной услуги на предыдущем шаге, здесь будет инициализироваться соответствующая страница.
        // Например, если в setupServiceSelectionTest() выбрана "Регистрация брака":
        // MarriageServiceDataPage marriageServiceDataPage = new MarriageServiceDataPage(DriverManager.getDriver());
        // softAssert.assertTrue(marriageServiceDataPage.isPageLoaded(), "Не удалось перейти на страницу данных услуги брака.");

        // Проверяем индикаторы шагов после перехода на Шаг 4 ("Данные услуги")
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(1), "Шаг 1 должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(2), "Шаг 2 должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(3), "Шаг 3 должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(4), "Шаг 4 'Данные услуги' должен быть активным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGray(5), "Шаг 5 'Статус заявки' должен быть серым.");

        
    }

    @Test(description = "Проверка кнопки 'Назад' и возврата на предыдущую страницу")
    public void testBackButtonNavigation() {
        // Для этого теста нужно сначала заполнить поля, чтобы кнопка "Назад" была кликабельна
        CitizenData validCitizenData = CitizenFakerData.generateFullCitizenData();
        citizenDataPage.fillCitizenData(validCitizenData);
        citizenDataPage.clickOnFreeArea(); // Активируем валидацию, чтобы кнопка "Назад" стала кликабельной (если она зависит от валидации)

        citizenDataPage.clickBackButton();

        // TODO: Добавить проверку, что мы вернулись на ServiceSelectionPage
        // ServiceSelectionPage prevServiceSelectionPage = new ServiceSelectionPage(DriverManager.GgetDriver());
        // softAssert.assertTrue(prevServiceSelectionPage.isMarriageRegistrationButtonDisplayed(), "Не удалось вернуться на страницу выбора услуги.");

        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(1), "Шаг 1 должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(2), "Шаг 2 должен быть активным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGray(3), "Шаг 3 должен быть серым.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGray(4), "Шаг 4 должен быть серым.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGray(5), "Шаг 5 должен быть серым.");

        
    }

    @Test(description = "Проверка кнопки 'Закрыть' и закрытия модального окна")
    public void testCloseButtonFunctionality() {
        citizenDataPage.clickCloseButton();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
        softAssert.assertTrue(wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.className("MuiDialog-paper"))), "Модальное окно не закрылось.");

        softAssert.assertTrue(wait.until(ExpectedConditions.invisibilityOfElementLocated(
                        By.xpath("//div[contains(@class, 'MuiStepper-root')]"))),
                "Индикаторы шагов должны быть невидимы после закрытия модального окна.");

        
    }
}