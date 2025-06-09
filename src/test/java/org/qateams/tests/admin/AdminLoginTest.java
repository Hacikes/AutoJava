package org.qateams.tests.admin;

import org.qateams.pages.components.admin.AdminLoginComponent;
import org.qateams.pages.BasePage;
import org.qateams.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AdminLoginTest extends BaseTest {

    private AdminLoginTest() {
        super();
    }

    @Test
    public void testFillAndGoNextAdminData() {
        BasePage hp = new BasePage();
        hp.clickEnterAsAdmin();

        AdminLoginComponent ad = new AdminLoginComponent();

        Assert.assertFalse(ad.nextIsButtonActive(), "Кнопка 'Далее' должна быть не активна, пока все поля формы не заполнены");
        ad.fillForm("Иванов", "Иван", "Иванович", "89990009900", "1234123456", "01.01.2000");

        Assert.assertTrue(ad.nextIsButtonActive(), "Кнопка 'Далее' должна быть активна после заполнения всех полей формы");

        ad.clickNextButton();
    }

    @Test
    public void testCloseAdminData () {
        BasePage hp = new BasePage();
        hp.clickEnterAsAdmin();

        AdminLoginComponent ad = new AdminLoginComponent();
        ad.clickCloseButton();
    }


    @Test
    public void testInvalidFormSubmission() {
        // Инициализация страницы и компонента
        BasePage homePage = new BasePage();
        homePage.clickEnterAsAdmin();

        // Создание компонента с использованием текущего драйвера
        AdminLoginComponent adminLoginComponent = new AdminLoginComponent();

        // Частичное заполнение формы (не все поля)
        adminLoginComponent.fillForm(
                "Иванов",      // Фамилия
                "Иван",        // Имя
                "",            // Пустое отчество
                "89990009900", // Телефон
                "",            // Пустой номер паспорта
                "01.01.2000"   // Дата рождения
        );

        // Проверка, что кнопка "Далее" остается неактивной
        Assert.assertFalse(adminLoginComponent.isNextButtonActive(),
                "Кнопка 'Далее' должна оставаться неактивной при неполностью заполненной форме");
    }

}
