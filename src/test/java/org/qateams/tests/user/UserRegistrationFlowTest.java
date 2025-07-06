package org.qateams.tests.user;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.qateams.base.BaseTest;
import org.qateams.pages.BasePage;
import org.qateams.pages.components.user.UserRegistrationPage; // Предполагается, что UserRegistrationPage находится в этом пакете
import org.qateams.pages.components.user.valueobject.ApplicantData;
import org.qateams.pages.components.user.ServiceSelectionPage; // Предполагаем, что вам нужна эта страница для начальной навигации
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class UserRegistrationFlowTest extends BaseTest {

    private BasePage basePage;
    private UserRegistrationPage userRegistrationPage;

    // Вспомогательный метод для открытия модального окна и инициализации Page Objects
    private void setupModalWindow() {
        basePage = new BasePage();
        userRegistrationPage = new UserRegistrationPage();
        basePage.clickEnterAsUser();
        // Дождемся загрузки модального окна, чтобы избежать NoSuchElementException
        userRegistrationPage.getPageTitleHint();
    }

    /**
     * Тест: Проверка отображения модального окна и корректности заголовка
     * после нажатия "Войти как пользователь".
     */
    @Test(description = "Проверка отображения модального окна 'Данные заявителя' и заголовка")
    public void testUserRegistrationPageTitleAndServiceText() {
        setupModalWindow();

        String expectedTitle = "Вы вошли как Пользоватиль";
        String actualTitle = userRegistrationPage.getPageTitleHint();
        softAssert.assertEquals(actualTitle, expectedTitle, "Заголовок модального окна не соответствует ожидаемому.");

    }

    /**
     * Тест: Проверка, что кнопка "Далее" изначально неактивна,
     * если обязательные поля не заполнены.
     */
    @Test(description = "Проверка начального состояния кнопки 'Далее' (неактивна)")
    public void testNextButtonIsInitiallyDisabled() {
        setupModalWindow();

        softAssert.assertFalse(userRegistrationPage.isNextButtonEnabled(),
                "Кнопка 'Далее' должна быть неактивна до заполнения обязательного поля.");
    }

    /**
     * Тест: Проверка валидации поля "Номер паспорта" (обязательное поле).
     * Поле должно подсветиться красным при попытке продвинуться без его заполнения.
     */
    @Test(description = "Проверка валидации обязательного поля 'Номер паспорта'")
    public void testPassportNumberFieldValidation() {
        setupModalWindow();

        // Валидный паспортный номер для создания ApplicantData, чтобы избежать ошибки VO
        String validPassportForVO = "АБ123456";

        // Заполняем все поля, включая обязательное, чтобы ApplicantData создался без ошибки
        ApplicantData fullData = new ApplicantData.Builder()
                .lastName("Тестов")
                .firstName("Тест")
                .middleName("Тестович")
                .phone("12345678901")
                .address("Улица Пушкина, дом Колотушкина")
                .passportNumber(validPassportForVO) // Передаем валидный номер для создания VO
                .build();

        userRegistrationPage.fillApplicantData(fullData); // Заполняем форму на UI

        // Очищаем поле "Номер паспорта" на UI, чтобы имитировать незаполненное состояние
        userRegistrationPage.clearPassportNumberField();

        // Кликаем по свободной области, чтобы вызвать валидацию (если она срабатывает по blur)
        userRegistrationPage.clickOnFreeArea();

        // Проверяем, что кнопка "Далее" по-прежнему неактивна
        softAssert.assertFalse(userRegistrationPage.isNextButtonEnabled(),
                "Кнопка 'Далее' должна быть неактивна при незаполненном обязательном поле.");

        // Проверяем, что поле "Номер паспорта" подсвечено красным
        softAssert.assertTrue(userRegistrationPage.isFieldHighlightedRed("номер паспорта"),
                "Поле 'Номер паспорта' должно быть подсвечено красным при ошибке валидации.");

    }

    /**
     * Тест: Успешное заполнение всех обязательных и необязательных полей
     * и переход на следующий шаг.
     */
    @Test(description = "Успешное заполнение формы и переход на следующий шаг")
    public void testSuccessfulFormSubmission() {
        setupModalWindow();

        // Создаем данные с валидным номером паспорта и другими полями
        ApplicantData validData = new ApplicantData.Builder()
                .lastName("Фамилия")
                .firstName("Имя")
                .middleName("Отчество")
                .address("Адрес 123")
                .phone("01234567890")
                .passportNumber("АА123456") // Валидный номер паспорта
                .build();

        userRegistrationPage.fillApplicantData(validData);

        // Проверяем, что кнопка "Далее" стала активной
        softAssert.assertTrue(userRegistrationPage.isNextButtonEnabled(),
                "Кнопка 'Далее' должна стать активной после заполнения всех обязательных полей.");

        // Кликаем "Далее"
        userRegistrationPage.clickNextButton();

        // Дополнительные ассерты для проверки перехода на следующий шаг должны быть здесь.
        // Например, проверка URL или видимости элементов следующей страницы.
    }

    /**
     * Тест: Проверка закрытия модального окна по кнопке "Закрыть".
     */
    @Test(description = "Проверка закрытия модального окна кнопкой 'Закрыть'")
    public void testCloseButtonFunctionality() {
        setupModalWindow();

        // Проверяем, что модальное окно видимо до закрытия
        softAssert.assertTrue(userRegistrationPage.getPageTitleHint().contains("Пользоватиль"),
                "Модальное окно должно быть видимо перед попыткой закрытия.");

        userRegistrationPage.clickCloseButton();

        // Проверяем, что модальное окно исчезло, ожидая невидимости заголовка
        // Это более надежный способ, чем просто проверять isNextButtonEnabled, так как оно может быть невидимо
        // но все еще существовать в DOM.
        boolean isModalInvisible = userRegistrationPage.wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//h2[contains(@class, 'MuiDialogTitle-root')]//b[text()='Пользоватиль']")));
        softAssert.assertTrue(isModalInvisible, "Модальное окно не закрылось или не стало невидимым.");

    }


    @Test(description = "Проверка ограничений полей на длину и тип символов")
    public void testFieldInputRestrictions() {
        setupModalWindow();

        // Для того чтобы кнопка "Далее" была активной, обязательное поле должно быть заполнено.
        // Заполним его один раз в начале, так как фокус теста на других полях.
        userRegistrationPage.passportNumberField.sendKeys("АБ123456");

        // --- 1. Фамилия: не более 100 знаков, в т.ч. спецсимволы, латиница, цифры. ---
        userRegistrationPage.clearLastNameField();
        String longInputLastName ="@#$f23" + "A".repeat(100); // Общая длина > 100
        userRegistrationPage.lastNameField.sendKeys(longInputLastName);
        softAssert.assertEquals(userRegistrationPage.lastNameField.getAttribute("value").length(), 100,
                "Фамилия: Поле должно обрезать ввод до 100 символов.");
        softAssert.assertTrue(userRegistrationPage.lastNameField.getAttribute("value").contains("@#$f23"),
                "Фамилия: Поле должно принимать спецсимволы, латиницу и цифры.");

        // --- 2. Имя: не более 100 знаков, в т.ч. латиница, цифры. ---
        userRegistrationPage.clearFirstNameField();
        String longInputFirstName = "xyz789" +  "B".repeat(100); // Общая длина > 100
        userRegistrationPage.firstNameField.sendKeys(longInputFirstName);
        softAssert.assertEquals(userRegistrationPage.firstNameField.getAttribute("value").length(), 100,
                "Имя: Поле должно обрезать ввод до 100 символов.");
        softAssert.assertTrue(userRegistrationPage.firstNameField.getAttribute("value").contains("xyz789"),
                "Имя: Поле должно принимать латиницу и цифры.");

        // --- 3. Отчество: не более 100 знаков, только латиницей без спецсимволов и русских букв. ---
        userRegistrationPage.clearMiddleNameField();
        String longInputMiddleName = "ОченьДлинноеОтчество" + "C".repeat(80); // Общая длина > 100
        String invalidCharsMiddleName = "Отчество!@#$РусскиеБуквы";
        String validCharsMiddleName = "ValidMiddleNameLat";

        // Проверка обрезки длины
        userRegistrationPage.middleNameField.sendKeys(longInputMiddleName);
        softAssert.assertEquals(userRegistrationPage.middleNameField.getAttribute("value").length(), 100,
                "Отчество: Поле должно обрезать ввод до 100 символов.");

        // Проверка на недопустимые символы
        userRegistrationPage.clearMiddleNameField();
        userRegistrationPage.middleNameField.sendKeys(invalidCharsMiddleName);
        String actualMiddleNameAfterInvalid = userRegistrationPage.middleNameField.getAttribute("value");
        softAssert.assertFalse(actualMiddleNameAfterInvalid.contains("!@#$%^&*"), "Отчество: Не должно содержать спецсимволы");
        softAssert.assertFalse(actualMiddleNameAfterInvalid.matches(".*[а-яА-Я].*"), "Отчество: Не должно содержать русские буквы.");

        // Проверка на допустимые символы
        userRegistrationPage.clearMiddleNameField();
        userRegistrationPage.middleNameField.sendKeys(validCharsMiddleName);
        softAssert.assertEquals(userRegistrationPage.middleNameField.getAttribute("value"), validCharsMiddleName,
                "Отчество: Должно принимать только латиницу.");


        // --- 4. Телефон: не более 11 символов, доступны латиница и цифры, спецсимволы не допускаются. ---
        userRegistrationPage.clearPhoneField();
        String longInputPhone = "1234567890123"; // > 11 символов
        String validCharsPhone = "123abc45678"; // Латиница и цифры
        String invalidCharsPhone = "123-4э6(789)"; // Спецсимволы

        // Проверка обрезки длины
        userRegistrationPage.phoneField.sendKeys(longInputPhone);
        softAssert.assertEquals(userRegistrationPage.phoneField.getAttribute("value").length(), 11,
                "Телефон: Поле должно обрезать ввод до 11 символов.");

        // Проверка на допустимые символы (латиница и цифры)
        userRegistrationPage.clearPhoneField();
        userRegistrationPage.phoneField.sendKeys(validCharsPhone);
        softAssert.assertEquals(userRegistrationPage.phoneField.getAttribute("value"), validCharsPhone,
                "Телефон: Должен принимать латиницу и цифры.");

        // Проверка на недопустимые символы (спецсимволы)
        userRegistrationPage.clearPhoneField();
        userRegistrationPage.phoneField.sendKeys(invalidCharsPhone);
        String actualPhoneAfterInvalid = userRegistrationPage.phoneField.getAttribute("value");
        softAssert.assertFalse(actualPhoneAfterInvalid.contains("-") || actualPhoneAfterInvalid.contains("(") || actualPhoneAfterInvalid.contains(")"),
                "Телефон: Не должен принимать спецсимволы.");


        // --- 5. Номер паспорта: не более 8 символов, только русские буквы и цифры. ---
        // (Обязательное поле, но мы его очищаем перед каждой проверкой и затем восстанавливаем)
        userRegistrationPage.clearPassportNumberField();
        String longInputPassport = "АБ123456789"; // > 8 символов
        String invalidCharsPassport = "AB12345!"; // Латиница и спецсимволы
        String validCharsPassport = "ВГ987654"; // Русские буквы и цифры

        // Проверка обрезки длины
        userRegistrationPage.passportNumberField.sendKeys(longInputPassport);
        softAssert.assertEquals(userRegistrationPage.passportNumberField.getAttribute("value").length(), 8,
                "Номер паспорта: Поле должно обрезать ввод до 8 символов.");

        // Проверка на недопустимые символы
        userRegistrationPage.clearPassportNumberField();
        userRegistrationPage.passportNumberField.sendKeys(invalidCharsPassport);
        String actualPassportAfterInvalid = userRegistrationPage.passportNumberField.getAttribute("value");
        softAssert.assertFalse(actualPassportAfterInvalid.contains("A") || actualPassportAfterInvalid.contains("B"), "Номер паспорта: Не должен принимать латиницу.");
        softAssert.assertFalse(actualPassportAfterInvalid.contains("!"), "Номер паспорта: Не должен принимать спецсимволы.");

        // Проверка на допустимые символы
        userRegistrationPage.clearPassportNumberField();
        userRegistrationPage.passportNumberField.sendKeys(validCharsPassport);
        softAssert.assertEquals(userRegistrationPage.passportNumberField.getAttribute("value"), validCharsPassport,
                "Номер паспорта: Должен принимать русские буквы и цифры.");


        // --- 6. Адрес прописки: не более 50 символов, только буквы и спецсимволы. Цифры не должны приниматься. ---
        // TODO: Реализовать после уточнения требований
    }
}
