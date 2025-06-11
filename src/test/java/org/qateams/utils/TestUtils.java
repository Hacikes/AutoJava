package org.qateams.utils;

import org.qateams.pages.BasePage;
import org.qateams.pages.components.admin.AdminLoginComponent;


public class TestUtils {

    public TestUtils() {
    }

    public static AdminLoginComponent prepareAdminData() {
        BasePage hp = new BasePage();
        hp.clickEnterAsAdmin();

        AdminLoginComponent ad = new AdminLoginComponent();
        ad.fillForm(
                "Иванов",
                "Иван",
                "Иванович",
                "89990009900",
                "1234123456",
                "01.01.2000"
        );
        return ad;
    }
}
