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
import org.qateams.pages.components.user.MarriageServiceDataPage; // Импортируем MarriageServiceDataPage
import org.qateams.pages.components.user.ServiceSelectionPage;
import org.qateams.pages.components.user.UserRegistrationPage;
import org.qateams.pages.components.user.valueobject.ApplicantData;
import org.qateams.pages.components.user.valueobject.CitizenData;
import org.qateams.pages.components.user.valueobject.MarriageServiceData; // Импортируем MarriageServiceData
import org.qateams.utils.Faker.ApplicantFakerData;
import org.qateams.utils.Faker.CitizenFakerData;
import org.qateams.utils.Faker.MarriageServiceFakerData; // Импортируем MarriageServiceFakerData
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MarriageServiceTest extends BaseTest {

    private UserRegistrationPage userRegistrationPage;
    private ServiceSelectionPage serviceSelectionPage;
    private CitizenDataPage citizenDataPage;
    private MarriageServiceDataPage marriageServiceDataPage; // Новая страница
    private StepIndicatorComponent stepIndicatorComponent;

    // Вспомогательный метод для ожидания загрузки модального окна
    private void waitForModalLoaded() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("MuiDialog-paper")));
    }

    @BeforeMethod
    public void setupMarriageServiceTest() {
        initSoftAssert(); // Инициализация SoftAssert из BaseTest

        BasePage hp = new BasePage();
        userRegistrationPage = new UserRegistrationPage();
        serviceSelectionPage = new ServiceSelectionPage(DriverManager.getDriver());
        citizenDataPage = new CitizenDataPage();
        marriageServiceDataPage = new MarriageServiceDataPage(); // Инициализация MarriageServiceDataPage
        stepIndicatorComponent = new StepIndicatorComponent();

        // Шаг 1: Переход на страницу UserRegistrationPage и заполнение данных заявителя
        hp.clickEnterAsUser();
        waitForModalLoaded();
        ApplicantData fullApplicantData = ApplicantFakerData.generateFullApplicantData();
        userRegistrationPage.fillApplicantData(fullApplicantData);
        userRegistrationPage.clickNextButton();

        // Шаг 2: Переход на страницу ServiceSelectionPage и выбор услуги "Регистрация брака"
        waitForModalLoaded();
        serviceSelectionPage.selectMarriageRegistration(); // Выбираем именно эту услугу

        // Шаг 3: Переход на CitizenDataPage и заполнение данных гражданина
        waitForModalLoaded();
        CitizenData validCitizenData = CitizenFakerData.generateFullCitizenData();
        citizenDataPage.fillCitizenData(validCitizenData);
        citizenDataPage.clickOnFreeArea(); // Активируем валидацию
        softAssert.assertTrue(citizenDataPage.isNextButtonEnabled(),
                "Предварительная проверка: Кнопка 'Далее' на CitizenDataPage должна быть активна.");
        citizenDataPage.clickNextButton();

        // Шаг 4: Теперь мы должны быть на MarriageServiceDataPage
        waitForModalLoaded();
        // Дополнительная проверка, что мы на MarriageServiceDataPage, например, по заголовку
        // softAssert.assertTrue(marriageServiceDataPage.isPageLoaded(), "Не удалось перейти на страницу 'Данные услуги брака'.");
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

    // --- Тесты для индикаторов шагов ---
    @Test(description = "Проверка цветового индикатора этапов на странице 'Данные услуги брака'")
    public void testStepIndicatorOnMarriageServiceDataPage() {
        // Проверка индикаторов шагов: Шаг 1, 2, 3 завершены (зеленые/синие), Шаг 4 активен (зеленый/синий), Шаг 5 серый
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(1),
                "Индикатор этапов: Шаг 1 'Данные заявителя' должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(2),
                "Индикатор этапов: Шаг 2 'Выбор услуги' должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(3),
                "Индикатор этапов: Шаг 3 'Данные гражданина' должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(4),
                "Индикатор этапов: Шаг 4 'Даные услуги' должен быть активным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGray(5),
                "Индикатор этапов: Шаг 5 'Статус заявки' должен быть серым.");
        softAssert.assertAll();
    }

    // --- Тесты для начального состояния и валидации ---
    @Test(description = "Проверка начального состояния: кнопка 'Завершить' неактивна")
    public void testCompleteButtonIsInitiallyDisabled() {
        softAssert.assertFalse(marriageServiceDataPage.isCompleteButtonEnabled(),
                "Кнопка 'Завершить' должна быть неактивна до заполнения обязательных полей.");
        softAssert.assertAll();
    }

    @Test(description = "Проверка валидации обязательных полей при пустом заполнении")
    public void testRequiredFieldsValidation() {
        MarriageServiceData emptyData = new MarriageServiceData.Builder().build();
        marriageServiceDataPage.fillMarriageServiceData(emptyData);
        marriageServiceDataPage.clickOnFreeArea(); // Активируем валидацию

        softAssert.assertFalse(marriageServiceDataPage.isCompleteButtonEnabled(),
                "Кнопка 'Завершить' должна быть неактивна при незаполненных обязательных полях.");

        // Проверяем, что все ОБЯЗАТЕЛЬНЫЕ поля подсвечены красным
        // Согласно HTML, обязательные поля: Дата регистрации, Фамилия супруга/и, Имя супруга/и, Отчество супруга/и, Номер паспорта супруга/и
        softAssert.assertTrue(marriageServiceDataPage.isFieldHighlightedRed("дата регистрации"), "Поле 'Дата регистрации' должно быть красным.");
        softAssert.assertTrue(marriageServiceDataPage.isFieldHighlightedRed("фамилия супруга"), "Поле 'Фамилия супруга/и' должно быть красным.");
        softAssert.assertTrue(marriageServiceDataPage.isFieldHighlightedRed("имя супруга"), "Поле 'Имя супруга/и' должно быть красным.");
        softAssert.assertTrue(marriageServiceDataPage.isFieldHighlightedRed("отчество супруга"), "Поле 'Отчество супруга/и' должно быть красным.");
        softAssert.assertTrue(marriageServiceDataPage.isFieldHighlightedRed("номер паспорта супруга"), "Поле 'Номер паспорта супруга/и' должно быть красным.");

        // Необязательные поля не должны подсвечиваться красным
        softAssert.assertFalse(marriageServiceDataPage.isFieldHighlightedRed("новая фамилия"), "Поле 'Новая фамилия' не должно быть красным (необязательное).");
        softAssert.assertFalse(marriageServiceDataPage.isFieldHighlightedRed("дата рождения супруга"), "Поле 'Дата рождения супруга/и' не должно быть красным (необязательное).");

        softAssert.assertAll();
    }

    // --- Тесты для ограничений ввода ---

    @Test(description = "Проверка ограничений поля 'Дата регистрации'")
    public void testRegistrationDateFieldRestrictions() {
        marriageServiceDataPage.clearRegistrationDateField();
        String validDate = LocalDate.now().plusDays(10).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        marriageServiceDataPage.registrationDateField().sendKeys(validDate);
        softAssert.assertEquals(marriageServiceDataPage.registrationDateField().getAttribute("value"), validDate,
                "Дата регистрации: Поле должно отображать дату в формате DD-MM-YYYY после ввода.");

        marriageServiceDataPage.clearRegistrationDateField();
        String invalidDateInput = "не дата";
        marriageServiceDataPage.registrationDateField().sendKeys(invalidDateInput);
        softAssert.assertTrue(marriageServiceDataPage.registrationDateField().getAttribute("value").isEmpty(),
                "Дата регистрации: Поле не должно принимать невалидный формат.");

        softAssert.assertEquals(marriageServiceDataPage.registrationDateField().getAttribute("placeholder"), "дд/мм/гггг",
                "Дата регистрации: Подсказка должна быть 'дд/мм/гггг'.");
        softAssert.assertAll();
    }

    @Test(description = "Проверка ограничений поля 'Новая фамилия' (необязательное)")
    public void testNewLastNameFieldRestrictions() {
        marriageServiceDataPage.clearNewLastNameField();
        // Длина
        String longInput = "НоваяФамилияОченьДлиннаяИПревышающаяМаксимальнуюДлинуВ50Символов"; // > 50 символов
        verifyFieldLengthRestriction(marriageServiceDataPage.newLastNameField(), "Новая фамилия", 50, longInput);

        marriageServiceDataPage.clearNewLastNameField();
        // Допустимые символы (предполагаем, как для обычной фамилии: текст, числа, спецсимволы, латиница)
        String validChars = "Новая_Фамилия-123!@#$";
        verifyFieldAcceptsValidChars(marriageServiceDataPage.newLastNameField(), "Новая фамилия", validChars);
        softAssert.assertAll();
    }

    @Test(description = "Проверка ограничений поля 'Фамилия супруга/и'")
    public void testSpouseLastNameFieldRestrictions() {
        marriageServiceDataPage.clearSpouseLastNameField();
        // Длина
        String longInput = "ФамилияСупругаОченьДлиннаяИПревышающаяМаксимальнуюДлинуВ50Символов"; // > 50 символов
        verifyFieldLengthRestriction(marriageServiceDataPage.spouseLastNameField(), "Фамилия супруга", 50, longInput);

        marriageServiceDataPage.clearSpouseLastNameField();
        // Допустимые символы (предполагаем, как для обычной фамилии: текст, числа, спецсимволы, латиница)
        String validChars = "Супруг_Фамилия-123!@#$";
        verifyFieldAcceptsValidChars(marriageServiceDataPage.spouseLastNameField(), "Фамилия супруга", validChars);
        softAssert.assertAll();
    }

    @Test(description = "Проверка ограничений поля 'Имя супруга/и'")
    public void testSpouseFirstNameFieldRestrictions() {
        marriageServiceDataPage.clearSpouseFirstNameField();
        // Длина
        String longInput = "ИмяСупругаОченьДлинноеИПревышающееМаксимальнуюДлинуВ20Символов"; // > 20 символов
        verifyFieldLengthRestriction(marriageServiceDataPage.spouseFirstNameField(), "Имя супруга", 20, longInput);

        marriageServiceDataPage.clearSpouseFirstNameField();
        // Допустимые символы (предполагаем: текст, числа, латиница)
        String validChars = "ИмяСупруга123";
        verifyFieldAcceptsValidChars(marriageServiceDataPage.spouseFirstNameField(), "Имя супруга", validChars);
        softAssert.assertAll();
    }

    @Test(description = "Проверка ограничений поля 'Отчество супруга/и'")
    public void testSpouseMiddleNameFieldRestrictions() {
        marriageServiceDataPage.clearSpouseMiddleNameField();
        // Длина
        String longInput = "ОтчествоСупругаОченьДлинноеИПревышающееМаксимальнуюДлинуВ20Символов"; // > 20 символов
        verifyFieldLengthRestriction(marriageServiceDataPage.spouseMiddleNameField(), "Отчество супруга", 20, longInput);

        marriageServiceDataPage.clearSpouseMiddleNameField();
        // Недопустимые символы: спецсимволы и русские буквы (как в ТЗ для отчества заявителя)
        String invalidChars = "Отчество!@#$РусскиеБуквы";
        verifyFieldRejectsInvalidChars(marriageServiceDataPage.spouseMiddleNameField(), "Отчество супруга", invalidChars, ".*[!@#$%^&*А-Яа-я].*", "Не должно содержать спецсимволы или русские буквы.");

        marriageServiceDataPage.clearSpouseMiddleNameField();
        // Допустимые символы: только латиница
        String validChars = "ValidSpouseMiddleName";
        verifyFieldAcceptsValidChars(marriageServiceDataPage.spouseMiddleNameField(), "Отчество супруга", validChars);
        softAssert.assertAll();
    }

    // НОВЫЙ ТЕСТ: Баг с минимальной длиной поля "Отчество супруга/и" (если такой же баг есть и здесь)
    // Если этот баг проявляется и здесь, то этот тест будет падать, указывая на него.
    @Test(description = "Баг: Кнопка 'Завершить' неактивна при 'Отчестве супруга/и' менее 5 символов (если баг присутствует)")
    public void testSpouseMiddleNameMinLengthBug() {
        MarriageServiceData baseData = MarriageServiceFakerData.generateFullMarriageServiceData();
        MarriageServiceData dataWithShortSpouseMiddleName = new MarriageServiceData.Builder()
                .registrationDate(baseData.getRegistrationDate())
                .newLastName(baseData.getNewLastName())
                .spouseLastName(baseData.getSpouseLastName())
                .spouseFirstName(baseData.getSpouseFirstName())
                .spouseMiddleName("Олег") // 4 символа, меньше 5
                .spouseDateOfBirth(baseData.getSpouseDateOfBirth())
                .spousePassportNumber(baseData.getSpousePassportNumber())
                .build();

        marriageServiceDataPage.fillMarriageServiceData(dataWithShortSpouseMiddleName);
        marriageServiceDataPage.clickOnFreeArea(); // Активируем валидацию

        softAssert.assertFalse(marriageServiceDataPage.isCompleteButtonEnabled(),
                "Баг: Кнопка 'Завершить' должна быть неактивна, если 'Отчество супруга/и' содержит менее 5 символов.");
        softAssert.assertTrue(marriageServiceDataPage.isFieldHighlightedRed("отчество супруга"),
                "Баг: Поле 'Отчество супруга/и' должно быть подсвечено красным, если содержит менее 5 символов.");
        softAssert.assertAll();
    }

    @Test(description = "Проверка ограничений поля 'Дата рождения супруга/и' (необязательное)")
    public void testSpouseDateOfBirthFieldRestrictions() {

        marriageServiceDataPage.clearSpouseDateOfBirthField();
        String invalidDateInput = "не дата";
        marriageServiceDataPage.spouseDateOfBirthField().sendKeys(invalidDateInput);
        softAssert.assertTrue(marriageServiceDataPage.spouseDateOfBirthField().getAttribute("value").isEmpty(),
                "Дата рождения супруга/и: Поле не должно принимать невалидный формат.");

        softAssert.assertEquals(marriageServiceDataPage.spouseDateOfBirthField().getAttribute("placeholder"), "дд/мм/гггг",
                "Дата рождения супруга/и: Подсказка должна быть 'дд/мм/гггг'.");
        softAssert.assertAll();
    }

    @Test(description = "Проверка ограничений поля 'Номер паспорта супруга/и'")
    public void testSpousePassportNumberFieldRestrictions() {
        marriageServiceDataPage.clearSpousePassportNumberField();
        // Длина
        String longInput = "АБ123456789"; // 9 символов
        verifyFieldLengthRestriction(marriageServiceDataPage.spousePassportNumberField(), "Номер паспорта супруга", 8, longInput);

        marriageServiceDataPage.clearSpousePassportNumberField();
        // Недопустимые символы: латиница и спецсимволы (как в ТЗ для паспорта заявителя)
        String invalidChars = "AB12345!"; // Латиница и спецсимволы
        verifyFieldRejectsInvalidChars(marriageServiceDataPage.spousePassportNumberField(), "Номер паспорта супруга", invalidChars, ".*[A-Za-z!@#$%^&*].*", "Не должен принимать латиницу или спецсимволы.");

        marriageServiceDataPage.clearSpousePassportNumberField();
        // Допустимые символы: русские буквы и цифры
        String validChars = "ВГ987654";
        verifyFieldAcceptsValidChars(marriageServiceDataPage.spousePassportNumberField(), "Номер паспорта супруга", validChars);
        softAssert.assertAll();
    }

    // --- Тесты для навигации и успешной отправки ---

    @Test(description = "Проверка активации кнопки 'Завершить' при валидном заполнении полей")
    public void testCompleteButtonActivationOnValidInput() {
        // Заполняем все ОБЯЗАТЕЛЬНЫЕ поля валидными данными
        MarriageServiceData validData = MarriageServiceFakerData.generateFullMarriageServiceData();
        // Убедимся, что необязательные поля могут быть null или пустыми, но кнопка все равно активна
        MarriageServiceData dataForActivation = new MarriageServiceData.Builder()
                .registrationDate(validData.getRegistrationDate())
                .spouseLastName(validData.getSpouseLastName())
                .spouseFirstName(validData.getSpouseFirstName())
                .spouseMiddleName(validData.getSpouseMiddleName())
                .spousePassportNumber(validData.getSpousePassportNumber())
                // newLastName и spouseDateOfBirth могут быть null, если они необязательны
                .newLastName(validData.getSpouseLastName())
                .spouseDateOfBirth(validData.getSpouseDateOfBirth())
                .build();

        marriageServiceDataPage.fillMarriageServiceData(dataForActivation);
        marriageServiceDataPage.clickOnFreeArea(); // Активируем валидацию

        softAssert.assertTrue(marriageServiceDataPage.isCompleteButtonEnabled(),
                "Кнопка 'Завершить' должна быть активна после заполнения всех обязательных полей.");
        softAssert.assertAll();
    }

    @Test(description = "Успешная отправка формы и проверка перехода на этап 'Статус заявки'")
    public void testSuccessfulFormSubmissionAndStepNavigation() {
        // Заполняем все поля валидными данными
        MarriageServiceData validData = MarriageServiceFakerData.generateFullMarriageServiceData();
        marriageServiceDataPage.fillMarriageServiceData(validData);
        marriageServiceDataPage.clickOnFreeArea(); // Активируем валидацию

        // Убеждаемся, что кнопка "Завершить" активна
        softAssert.assertTrue(marriageServiceDataPage.isCompleteButtonEnabled(),
                "Предварительная проверка: Кнопка 'Завершить' должна быть активна перед кликом.");

        // Кликаем "Завершить"
        marriageServiceDataPage.clickCompleteButton();

        // TODO: Добавить проверку перехода на страницу "Статус заявки"
        // Например, по уникальному элементу на странице статуса заявки.

        // Проверяем индикаторы шагов после перехода на Шаг 5 ("Статус заявки")
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(1), "Шаг 1 должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(2), "Шаг 2 должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(3), "Шаг 3 должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(4), "Шаг 4 должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(5), "Шаг 5 'Статус заявки' должен быть активным.");

        softAssert.assertAll();
    }

    @Test(description = "Проверка кнопки 'Назад' и возврата на предыдущую страницу")
    public void testBackButtonNavigation() {
        // Для этого теста нужно сначала заполнить поля, чтобы кнопка "Назад" была кликабельна
        MarriageServiceData validData = MarriageServiceFakerData.generateFullMarriageServiceData();
        marriageServiceDataPage.fillMarriageServiceData(validData);
        marriageServiceDataPage.clickOnFreeArea(); // Активируем валидацию

        marriageServiceDataPage.clickBackButton();

        // TODO: Добавить проверку, что мы вернулись на CitizenDataPage
        // CitizenDataPage prevCitizenDataPage = new CitizenDataPage(); // Не передаем driver, т.к. он в DriverManager
        // softAssert.assertTrue(prevCitizenDataPage.isPageLoaded(), "Не удалось вернуться на страницу 'Данные гражданина'.");
        // (Вам нужно будет добавить метод isPageLoaded() в CitizenDataPage, если его еще нет)

        // Проверяем индикаторы шагов после возврата на Шаг 3 ("Данные гражданина")
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(1), "Шаг 1 должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(2), "Шаг 2 должен быть завершенным.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(3), "Шаг 3 должен быть активным."); // Снова активен
        softAssert.assertTrue(stepIndicatorComponent.isStepGray(4), "Шаг 4 должен быть серым."); // Снова серый
        softAssert.assertTrue(stepIndicatorComponent.isStepGray(5), "Шаг 5 должен быть серым.");

        softAssert.assertAll();
    }

    @Test(description = "Проверка кнопки 'Закрыть' и закрытия модального окна")
    public void testCloseButtonFunctionality() {
        marriageServiceDataPage.clickCloseButton();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
        softAssert.assertTrue(wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.className("MuiDialog-paper"))), "Модальное окно не закрылось.");

        softAssert.assertTrue(wait.until(ExpectedConditions.invisibilityOfElementLocated(
                        By.xpath("//div[contains(@class, 'MuiStepper-root')]"))),
                "Индикаторы шагов должны быть невидимы после закрытия модального окна.");

        softAssert.assertAll();
    }
}