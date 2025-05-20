package org.qateams.utils;

import org.openqa.selenium.WebDriver;
import org.qateams.components.admin.AdminData;
import org.qateams.driver.DriverManager;
import org.qateams.pages.HomePage;


public class TestUtils {

    public TestUtils() {
    }

    public static AdminData prepareAdminData() {
        HomePage hp = new HomePage();
        hp.clickEnterAsAdmin();

        AdminData ad = new AdminData();
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
