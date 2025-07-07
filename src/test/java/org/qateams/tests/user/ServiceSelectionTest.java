package org.qateams.tests.user;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.qateams.base.BaseTest;
import org.qateams.core.driver.DriverManager;
import org.qateams.pages.BasePage;
import org.qateams.pages.components.admin.ApplicationManagementComponent;
import org.qateams.pages.components.admin.ApplicationTableComponent;
import org.qateams.pages.components.user.UserRegistrationPage;
import org.qateams.pages.components.user.ServiceSelectionPage; // <-- Импортируем ServiceSelectionPage
import org.qateams.pages.components.user.valueobject.ApplicantData;
import org.qateams.pages.components.common.StepIndicatorComponent;
import org.qateams.utils.Faker.AdminFakerData;
import org.qateams.utils.Faker.ApplicantFakerData;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ServiceSelectionTest extends BaseTest {

    private UserRegistrationPage userRegistrationPage;
    private ServiceSelectionPage serviceSelectionPage;
    private StepIndicatorComponent stepIndicatorComponent;

    @BeforeMethod
    public void setupServiceSelectionTest() {

        BasePage hp = new BasePage();
        userRegistrationPage = new UserRegistrationPage();

        // Заполнение формы и переход на экран выбора услуги. Сначала заполнение идёт через value-object, потом в page и только потом вызывается кнопка
        hp.clickEnterAsUser();
        ApplicantData fullApplicantData = ApplicantFakerData.generateFullApplicantData();
        userRegistrationPage.fillApplicantData(fullApplicantData);
        userRegistrationPage.clickNextButton();

        serviceSelectionPage = new ServiceSelectionPage(DriverManager.getDriver());
        stepIndicatorComponent = new StepIndicatorComponent();
    }

    @Test(description = "Проверка наличия и кликабельности кнопок выбора услуг")
    public void testServiceSelectionButtonsPresenceAndClickability() {
        // Проверка видимости кнопок услуг
        softAssert.assertTrue(serviceSelectionPage.isMarriageRegistrationButtonDisplayed(),
                "Кнопка 'Регистрация брака' должна быть видимой.");
        softAssert.assertTrue(serviceSelectionPage.isBirthRegistrationButtonDisplayed(),
                "Кнопка 'Регистрация рождения' должна быть видимой.");
        softAssert.assertTrue(serviceSelectionPage.isDeathRegistrationButtonDisplayed(),
                "Кнопка 'Регистрация смерти' должна быть видимой.");
    }

    @Test(description = "Проверка цветового индикатор этапов")
    public void testStepIndicator() {

        // Проверка индикаторов шагов при первом открытии модального окна (Шаг 2 активен)
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(1), "Индикатор этапов: Шаг 1 'Данные заявителя' должен быть зеленым.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(2), "Индикатор этапов: Шаг 2 'Выбор услуги' должен быть зеленым.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGray(3), "Индикатор этапов: Шаг 3 'Данные гражданина' должен быть серым.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGray(4), "Индикатор этапов: Шаг 4 'Данные услуги' должен быть серым.");
        softAssert.assertTrue(stepIndicatorComponent.isStepGray(5), "Индикатор этапов: Шаг 5 'Статус заявки' должен быть серым.");
    }


    // TODO: Реализовать проверки перехода на регистрацию брака, рождения и смерти

//    @Test(description = "Выбор услуги 'Регистрация брака' и проверка перехода")
//    public void testSelectMarriageRegistrationService() {
//        // Выбираем услугу "Регистрация брака"
//        serviceSelectionPage.selectMarriageRegistration();
//
//
//        // Проверка индикаторов шагов после выбора услуги
//
//        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(1), "Индикатор этапов: Шаг 1 'Данные заявителя' должен быть завершенным (зеленый).");
//        softAssert.assertTrue(stepIndicatorComponent.isStepGreen(2), "Индикатор этапов: Шаг 2 'Выбор услуги' должен быть активным (синий).");
//        softAssert.assertTrue(stepIndicatorComponent.isStepGray(3), "Индикатор этапов: Шаг 3 'Данные гражданина' должен быть серым.");
//        softAssert.assertTrue(stepIndicatorComponent.isStepGray(4), "Индикатор этапов: Шаг 4 'Даные услуги' должен быть серым.");
//        softAssert.assertTrue(stepIndicatorComponent.isStepGray(5), "Индикатор этапов: Шаг 5 'Статус заявки' должен быть серым.");
//    }


}