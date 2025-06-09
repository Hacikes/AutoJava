package org.qateams.utils;

import org.qateams.admin.AdminData;
import org.qateams.pages.BasePage;


public class TestUtils {

    public TestUtils() {
    }

    public static AdminData prepareAdminData() {
        BasePage hp = new BasePage();
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
