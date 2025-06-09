package org.qateams.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.qateams.core.driver.DriverManager;

public class BasePage {

    /*
    * Порядок выбора локаторов:
    * id
    * name
    * classname
    * linktext
    * partiallinktext
    * css or xpath
    */

    private final By drpdwnLanguage = By.xpath("//*[@id=\"root\"]/div/div[1]/div/select");
    private final By buttonEnterAsUser = By.xpath("//div/button[1]");
    private final By buttonEnterAsAdmin = By.xpath("//div/button[2]");
    private final By buttonOrderDocument = By.xpath("//div/button[3]");

    // Метод выбора языка системы
    public void changeSystemLanguage(String languageValue) {
        Select languageDropdown  = new Select(DriverManager.getDriver().findElement(drpdwnLanguage)) ;
        languageDropdown.selectByValue(languageValue);
    }

    public BasePage checkEnterAsUser() {
        DriverManager.getDriver().findElement(buttonEnterAsUser);
        return new BasePage();
    }

    public BasePage checkEnterAsAdmin() {
        DriverManager.getDriver().findElement(buttonEnterAsAdmin);
        return new BasePage();
    }

    // В доке и в задании такой нет, мб надо будет выпилить
    public void checkOrderDocumentation() {
        /*
        * Закомменченным кодом ниже можно задать ожидание в течении какого-то времени (120 сек),
        * если элемент элемент загрузиться раньше,
        * То код дальше будет выполнен сразу после загрузки
        * без обязательного ожидания в 120 сек
        *
        * */
//        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(120));
        DriverManager.getDriver().findElement(buttonOrderDocument);
    }

    public void clickEnterAsAdmin() {
        DriverManager.getDriver().findElement(buttonEnterAsAdmin).click();
//        return new HomePage();
    }



}
