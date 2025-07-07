package org.qateams.tests.admin;

import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.core.driver.DriverManager;
import org.qateams.pages.components.admin.AdminLoginComponent;
import org.qateams.pages.BasePage;
import org.qateams.base.BaseTest;
import org.qateams.utils.Faker.AdminFakerData;

import org.qateams.utils.StringTrimmer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

@DisplayName("Тесты модального окна входа Админа")
public class AdminLoginTest extends BaseTest {

    private AdminLoginComponent ad = new AdminLoginComponent();

    private BasePage hp = new BasePage();

    private AdminLoginTest() {
        super();
    }

    @BeforeMethod
    public void setup() {
        // Беру данные для формы из TestUtils.prepareAdminData()
        // TestUtils.prepareAdminData().clickNextButton();
        hp.clickEnterAsAdmin();

    }


    @Test
    @DisplayName("Проверка на доступность нажатия на кнопку Далее")
    public void testFillAndGoNextAdminData() {

        Assert.assertFalse(ad.isNextButtonActive(), "Кнопка 'Далее' должна быть не активна, пока все поля формы не заполнены");

        // Заполнил все поля
        AdminFakerData.generateAndFillAdminData();

        Assert.assertTrue(ad.isNextButtonActive(), "Кнопка 'Далее' должна быть активна после заполнения всех полей формы");

        ad.clickNextButton();
    }

    @Test
    @DisplayName("Проверка кнопки закрытия модального окна")
    public void testCloseAdminData () {
        ad.clickCloseButton();
    }

    @Test
    @DisplayName("Проверка на клик на кнопку далее, когда есть незаполненные поля")
    public void testInvalidFormSubmission() {

        int fieldCount = 0;

        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        for (int i = 0; i < 5; i++) {
            AdminFakerData.generateAndFillAdminData(fieldCount);

            // Явное ожидание в 1 секунды
            wait.until(driver1 -> {
                try {
                    Thread.sleep(100);
                    return true;
                } catch (InterruptedException e) {
                    return false;
                }
            });

            softAssert.assertFalse(ad.isNextButtonActive(),
                    "Кнопка 'Далее' должна оставаться неактивной при не полностью заполненной форме");

            fieldCount++;
        }
    }


    @Test
    @DisplayName("Проверка поля 'Фамилия'")
    public void testSurnameFieldValidation() {

        // Сверхлимит 101 символ
        String longSurname = "A".repeat(101);
        ad.fillForm(longSurname, "", "", "", "", "");

        int lengthValidLongSurname = StringTrimmer.trimToMaxLength(longSurname, 100).length();
        int lengthInputSurname = ad.getEnteredSurnameText().length();

        // Проверка, что введенный текст соответствует обрезанной до 100 символов фамилии
        softAssert.assertEquals(lengthInputSurname, lengthValidLongSurname, "Длина фамилии должна быть ограничена 100 символами");

        // Удаление введённого текста из поля
        ad.clearEnteredSurnameText();

        // Валидная фамилия с цифрами и спецсимволами
        String inputSurname = "Иванов123@#";
        ad.fillForm(inputSurname, "", "", "", "", "");

        String outputSurname = ad.getEnteredSurnameText();

        softAssert.assertEquals(outputSurname, inputSurname, "При вводе фамилии цифры и спец. символы должны быть разрешены для ввода");
        softAssert.assertFalse(ad.isSurnameFieldInvalid(), "Поле не должно быть подсвечено красной рамкой");

        // Удаление текста из поля и клик на свободную область
        ad.clearEnteredSurnameText();
        ad.clickOnFreeArea();
        softAssert.assertTrue(ad.isSurnameFieldInvalid(), "Пустое поле должно быть подсвечено красной рамкой");


    }


    @Test
    @DisplayName("Проверка поля 'Имя'")
    public void testFirstNameFieldValidation() {
        // Проверка превышения лимита символов
        String longFirstName = "A".repeat(101);
        ad.fillForm("", longFirstName, "", "", "", "");

        int lengthValidLongFirstName = StringTrimmer.trimToMaxLength(longFirstName, 100).length();
        int lengthInputFirstName = ad.getEnteredFirstNameText().length();

        softAssert.assertEquals(lengthInputFirstName, lengthValidLongFirstName, "Длина имени должна быть ограничена 100 символами"
        );

        ad.clearEnteredFirstNameText();

        // Проверка ввода с цифрами и латиницей
        String inputFirstName = "John123";
        ad.fillForm("", inputFirstName, "", "", "", "");

        String outputFirstName = ad.getEnteredFirstNameText();

        softAssert.assertEquals(outputFirstName, inputFirstName, "При вводе имени цифры и спец. символы должны быть разрешены для ввода");
        softAssert.assertFalse(ad.isFirstNameFieldInvalid(), "Поле не должно быть подсвечено красной рамкой");

        // Удаление текста из поля и клик на свободную область
        ad.clearEnteredFirstNameText();
        ad.clickOnFreeArea();
        softAssert.assertTrue(ad.isFirstNameFieldInvalid(), "Пустое поле должно быть подсвечено красной рамкой");
    }


    @Test
    @DisplayName("Проверка поля 'Отчество'")
    public void testSecondNameFieldValidation() {

        // Проверка превышения лимита символов
        String longSecondName = "A".repeat(101);
        ad.fillForm("", "", longSecondName, "", "", "");

        int lengthValidLongSecondName = StringTrimmer.trimToMaxLength(longSecondName, 100).length();
        int lengthInputSecondName = ad.getEnteredSecondNameText().length();

        softAssert.assertEquals(lengthInputSecondName, lengthValidLongSecondName, "Длина отчества должна быть ограничена 100 символами");

        ad.clearEnteredSecondNameText();


        // Проверка ввода с латиницей без спецсимволов
        String validSecondName = "Petrovich";
        ad.fillForm("", "", validSecondName, "", "", "");

        softAssert.assertTrue(ad.isNextButtonActive(), "Отчество должно быть только на латинице без спецсимволов"
        );

        ad.clearEnteredSecondNameText();

        // Проверка ввода со спецсимволами
        String invalidSecondName = "Petr@ovиЧ!";
        ad.fillForm("", "", invalidSecondName, "", "", "");

        String outputSecondName = ad.getEnteredSecondNameText();

        softAssert.assertNotEquals(outputSecondName, invalidSecondName, "При вводе отчества кириллица и спец. символы должны быть запрещены для ввода");

        ad.clearEnteredSecondNameText();
        ad.clickOnFreeArea();
        softAssert.assertTrue(ad.isSecondNameFieldInvalid(), "Поле должно быть подсвечено красной рамкой");

    }


    @Test
    @DisplayName("Проверка поля 'Телефон'")
    public void testPhoneFieldValidation() {

        String longPhoneNumber = "+7999123456";

        ad.fillForm("", "", "", longPhoneNumber, "", "");
        int lengthValidLongPhone = StringTrimmer.trimToMaxLength(longPhoneNumber, 11).length();
        int lengthInputPhone = ad.getEnteredPhone().length();

        softAssert.assertEquals(lengthInputPhone, lengthValidLongPhone, "Длина номера телефона не должна превышать 11 символов");

        ad.clearEnteredPhone();

        //Проверка допустимых символов
        String[] validInputs = {
                "+79991234567",   // Корректный номер с плюсом
                "89991234567",    // Корректный номер с 8
                "1234567890",     // Только цифры
                "+7999abc12345",  // Английские буквы
                "12-45-78-90"     // Номер с дефисами
        };

        String[] invalidInputs = {
                "проверка",       // Русские буквы
                "@#$%^&*"        // Спецсимволы
        };

        // Проверка валидных входных данных
        for (String validInput : validInputs) {
            ad.clearEnteredPhone();
            ad.fillForm("", "", "", validInput, "", "");
            ad.clickOnFreeArea();

            boolean isFieldValid = !ad.isPhoneFieldInvalid();
            softAssert.assertTrue(isFieldValid, "Номер телефона '" + validInput + "' должен быть валидным");
        }

        // Проверка невалидных входных данных
        for (String invalidInput : invalidInputs) {
            ad.clearEnteredPhone();
            ad.fillForm("", "", "", invalidInput, "", "");
            ad.clickOnFreeArea();

            boolean isFieldInvalid = ad.isPhoneFieldInvalid();
            softAssert.assertTrue(isFieldInvalid, "Номер телефона '" + invalidInput + "' должен быть невалидным");
        }

        // Удаление текста из поля и клик на свободную область
        ad.clearEnteredPhone();
        ad.clickOnFreeArea();
        softAssert.assertTrue(ad.isPhoneFieldInvalid(), "Пустое поле должно быть подсвечено красной рамкой");

    }


    @Test
    @DisplayName("Проверка поля 'Номер паспорта'")
    public void testPassportFieldValidation() {

        String longNumberOfPassport = "AB123456А";

        ad.fillForm("", "", "", "", longNumberOfPassport, "");

        int lengthValidNumberOfPassport = StringTrimmer.trimToMaxLength(longNumberOfPassport, 8).length();
        int lengthInputNumberOfPassport = ad.getEnteredNumberOfPassport().length();

        softAssert.assertEquals(lengthInputNumberOfPassport, lengthValidNumberOfPassport, "Номер паспорта не должен превышать 8 символов\"");

        ad.clearEnteredNumberOfPassport();

        String invalidNumberOfPassport = "S@%12D";
        ad.fillForm("", "", "", "", invalidNumberOfPassport, "");
        String outputNumberOfPassport = ad.getEnteredNumberOfPassport();

        softAssert.assertNotEquals(outputNumberOfPassport, invalidNumberOfPassport, "При вводе номера паспорта латинские символы и спец. символы должны быть запрещены для ввода");

        // Удаление текста из поля и клик на свободную область
        ad.clearEnteredNumberOfPassport();
        ad.clickOnFreeArea();
        softAssert.assertTrue(ad.isNumberOfPassportFieldInvalid(), "Поле должно быть подсвечено красной рамкой");



    }


    @Test
    @DisplayName("Тесты для датапикера даты рождения")
    public void testDatePickerFunctionality() {
        // Тест 1: Проверка ввода корректной даты

            String validDate = "01.01.1990";

            ad.fillForm("", "", "", "", "", validDate);

            String enteredDate = ad.getEnteredBirthDate();
            softAssert.assertEquals(enteredDate, validDate,
                    "Введенная дата должна соответствовать корректному формату");


        // Тест 2: Проверка ограничений ввода даты
            // Сценарии невалидных дат
            String[] invalidDates = {
                    "32.13.2000",   // Некорректный день и месяц
                    "29.02.2023",   // Високосный год
                    "31.04.2023",   // Апрель не может иметь 31 день
                    "00.00.0000",   // Нулевые значения
                    "1.1.1",        // Некорректный формат
                    "01/01/1990",   // Неправильный разделитель
                    "01.13.1990"    // Некорректный месяц
            };

            for (String invalidDate : invalidDates) {
                ad.fillForm("", "", "", "", "", invalidDate);

                boolean isDateInvalid = ad.isDateFieldInvalid();
                softAssert.assertTrue(isDateInvalid,
                        "Дата '" + invalidDate + "' должна быть невалидной");
            }


        // Тест 3: Проверка возрастных ограничений
            // Сценарии с разными возрастами
            String[] ageLimitDates = {
                    ad.getCurrentDateMinusYears(18),   // Минимальный допустимый возраст
                    ad.getCurrentDateMinusYears(100),  // Максимальный допустимый возраст
                    ad.getCurrentDateMinusYears(17),   // Недостаточный возраст
                    ad.getCurrentDateMinusYears(101)   // Превышение максимального возраста
            };

            // Проверка первых двух дат (должны быть валидными)
            for (int i = 0; i < 2; i++) {
                ad.fillForm("", "", "", "", "", ageLimitDates[i]);

                boolean isDateValid = !Boolean.parseBoolean(ad.getEnteredBirthDate());
                softAssert.assertTrue(isDateValid,
                        "Дата '" + ageLimitDates[i] + "' должна быть валидной");
            }

            // Проверка последних двух дат (должны быть невалидными)
            for (int i = 2; i < 4; i++) {
                ad.fillForm("", "", "", "", "", ageLimitDates[i]);

                boolean isDateInvalid = ad.isDateFieldInvalid();
                softAssert.assertTrue(isDateInvalid,
                        "Дата '" + ageLimitDates[i] + "' должна быть невалидной");
            }

        // Тест 4: Интерактивный выбор даты через датапикер
            // Открытие датапикера
            WebElement datepickerTrigger = DriverManager.getDriver()
                    .findElement(By.xpath("//input[@id='birthDate']//following-sibling::button"));
            datepickerTrigger.click();

            // Выбор конкретной даты через UI
            ad.selectDateThroughDatepicker("2000", "Январь", "15");

            // Проверка выбранной даты
            String selectedDate = ad.getEnteredBirthDate();
            softAssert.assertEquals(selectedDate, "15.01.2000",
                    "Выбранная дата должна соответствовать ожидаемой");


    }
}
