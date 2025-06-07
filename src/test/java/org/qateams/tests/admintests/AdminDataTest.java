package org.qateams.tests.admintests;

import org.qateams.admin.AdminData;
import org.qateams.pages.HomePage;
import org.qateams.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AdminDataTest extends BaseTest {

    private AdminDataTest() {
        super();
    }

    @Test
    public void testFillAndGoNextAdminData() {
        HomePage hp = new HomePage();
        hp.clickEnterAsAdmin();

        AdminData ad = new AdminData();

        Assert.assertFalse(ad.nextIsButtonActive(), "Кнопка 'Далее' должна быть не активна, пока все поля формы не заполнены");
        ad.fillForm("Иванов", "Иван", "Иванович", "89990009900", "1234123456", "01.01.2000");

        Assert.assertTrue(ad.nextIsButtonActive(), "Кнопка 'Далее' должна быть активна после заполнения всех полей формы");

        ad.clickNextButton();
    }

    @Test
    public void testCloseAdminData () {
        HomePage hp = new HomePage();
        hp.clickEnterAsAdmin();

        AdminData ad = new AdminData();
        ad.clickCloseButton();
    }



}
