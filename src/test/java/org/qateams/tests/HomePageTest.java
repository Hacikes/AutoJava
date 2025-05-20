package org.qateams.tests;

import org.qateams.pages.HomePage;
import org.qateams.utils.ReadProperties;
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
        HomePage hp = new HomePage();
        // установка языка, мб надо будет переместить в другое место или удалить
        hp.changeSystemLanguage(ReadProperties.getProperty("systemlanguage"));
        // В доке и в задании не сказано про checkOrderDocumentation, мб надо будет выпилить
        hp.checkEnterAsUser().checkEnterAsAdmin().checkOrderDocumentation();


    }
}
