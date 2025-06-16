package org.qateams.tests.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.core.driver.Driver;
import org.qateams.core.driver.DriverManager;
import org.qateams.pages.components.admin.AdminLoginComponent;
import org.qateams.pages.BasePage;
import org.qateams.base.BaseTest;
import org.qateams.pages.components.admin.ApplicationManagementComponent;
import org.qateams.pages.components.admin.ApplicationTableComponent;
import org.qateams.utils.Faker.AdminFakerData;

import org.qateams.utils.StringTrimmer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class AdminLoginTest extends BaseTest {

    private AdminLoginComponent ad = new AdminLoginComponent();

    private BasePage hp = new BasePage();

    private AdminLoginTest() {
        super();
    }

    @BeforeMethod
    public void setup() {
        // Беру данные для формы из TestUtils.prepareAdminData()
//        TestUtils.prepareAdminData().clickNextButton();
        hp.clickEnterAsAdmin();

    }


    // Проверка на доступность перейти далее, без заполнения формы
    @Test
    public void testFillAndGoNextAdminData() {

        Assert.assertFalse(ad.nextIsButtonActive(), "Кнопка 'Далее' должна быть не активна, пока все поля формы не заполнены");

        // Заполнил все поля
        AdminFakerData.generateAndFillAdminData();

        Assert.assertTrue(ad.nextIsButtonActive(), "Кнопка 'Далее' должна быть активна после заполнения всех полей формы");

        ad.clickNextButton();
    }

    // Проверка кнопки закрытия модалки
    @Test
    public void testCloseAdminData () {
        ad.clickCloseButton();
    }

    // Проверка на клик на кнопку далее, когда есть незаполненные поля
    @Test
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


    // Проверка поля "Фамилия"
    @Test
    public void testSurnameFieldValidation() {

        // Сверхлимит 101 символ
        String longSurname = "A".repeat(101);
        StringTrimmer.trimToMaxLength(longSurname, 100);
        softAssert.assertFalse(ad.isNextButtonActive(), "Фамилия не должна превышать 100 символов");

        // Валидная фамилия с цифрами и спецсимволами
        ad.fillForm("Иванов123@#", "Иван", "Иванович", "+79991234567", "АБ123456", "01.01.1990");
        softAssert.assertTrue(ad.isNextButtonActive(), "Допустимы латиница, цифры и спецсимволы");

    }

    // Аналогично можно протестировать каждое поле:
    @Test
    public void testFirstNameFieldValidation() {

        ad.fillForm("Иванов", "A".repeat(101), "Иванович", "+79991234567", "АБ123456", "01.01.1990");
        softAssert.assertFalse(ad.isNextButtonActive(), "Имя не должно превышать 100 символов");

        ad.fillForm("Иванов", "John123", "Иванович", "+79991234567", "АБ123456", "01.01.1990");
        softAssert.assertTrue(ad.isNextButtonActive(), "Допустимы латиница и цифры");

    }

    @Test
    public void testSecondNameFieldValidation() {

        ad.fillForm("Иванов", "Иван", "A".repeat(101), "+79991234567", "АБ123456", "01.01.1990");
        softAssert.assertFalse(ad.isNextButtonActive(), "Отчество не должно превышать 100 символов");

        ad.fillForm("Иванов", "Иван", "Petrovich", "+79991234567", "АБ123456", "01.01.1990");
        softAssert.assertTrue(ad.isNextButtonActive(), "Отчество должно быть только на латинице и без спецсимволов");

        ad.fillForm("Иванов", "Иван", "Petr@ovich!", "+79991234567", "АБ123456", "01.01.1990");
        softAssert.assertFalse(ad.isNextButtonActive(), "Отчество не должно содержать спецсимволы");

    }

    @Test
    public void testPhoneFieldValidation() {

        ad.fillForm("Иванов", "Иван", "Ivanovich", "+7999", "АБ123456", "01.01.1990");
        softAssert.assertFalse(ad.isNextButtonActive(), "Телефон должен быть не менее 11 символов");

        ad.fillForm("Иванов", "Иван", "Ivanovich", "+79991234567", "АБ123456", "01.01.1990");
        softAssert.assertTrue(ad.isNextButtonActive(), "Валидный номер телефона");

    }

    @Test
    public void testPassportFieldValidation() {

        ad.fillForm("Иванов", "Иван", "Ivanovich", "+79991234567", "АБ1234567", "01.01.1990");
        softAssert.assertFalse(ad.isNextButtonActive(), "Номер паспорта не должен превышать 8 символов");

        ad.fillForm("Иванов", "Иван", "Ivanovich", "+79991234567", "AB123456", "01.01.1990");
        softAssert.assertFalse(ad.isNextButtonActive(), "Допускаются только русские буквы и цифры 0–8");

        ad.fillForm("Иванов", "Иван", "Ivanovich", "+79991234567", "АБ123408", "01.01.1990");
        softAssert.assertTrue(ad.isNextButtonActive(), "Валидный паспорт");

    }

    @Test
    public void testBirthDateOptionalField() {
        ad.fillForm("Иванов", "Иван", "Ivanovich", "+79991234567", "АБ123408", "");
        assert ad.isNextButtonActive() : "Дата рождения не обязательна, форма должна быть валидна";
    }



}
