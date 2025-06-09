package org.qateams.tests;

import org.qateams.pages.BasePage;
import org.qateams.config.PropertyReader;
import org.qateams.base.BaseTest;
import org.testng.annotations.Test;


public final class HomePageTest extends BaseTest {

    private HomePageTest() {
        super();
    }

    /**
    *
     * Установка языка
     * Проверка наличия всех кнопок
     **/

    @Test
    public void viewHomePage() throws Exception {
        BasePage hp = new BasePage();
        // установка языка, мб надо будет переместить в другое место или удалить
        hp.changeSystemLanguage(PropertyReader.getProperty("systemlanguage"));
        // В доке и в задании не сказано про checkOrderDocumentation, мб надо будет выпилить
        hp.checkEnterAsUser().checkEnterAsAdmin().checkOrderDocumentation();


    }
}
