package org.qateams.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.driver.DriverManager;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class AdminData {

    // Метод для построения относительных XPath по базовому локатору
    private By buildXpath(By baseElement, String relativeXpath) {
        return By.xpath("(" + baseElement.toString().replace("By.xpath: ", "") + ")" + relativeXpath);
    }


    // Базовый локатор (общий предок)
    private final By formContainer = By.xpath("//div[contains(@class, 'MuiPaper-root')]");

//    private final By formTitle = buildXpath(formContainer, "//span/descendant-or-self::b");
    private final By textSurname = buildXpath(formContainer, "//div[1]/input");
    private final By textFirstName = buildXpath(formContainer, "//div[2]/input");
    private final By textSecondName = buildXpath(formContainer, "//div[3]/input");
    private final By textPhone = buildXpath(formContainer, "//div[4]/input");
    private final By textNumberOfPassport = buildXpath(formContainer, "//div[5]/input");
    private final By dateOfBirth = buildXpath(formContainer, "//div[6]/input");

    private final By buttonClose = buildXpath(formContainer, "/child::div/button[1]");
    private final By buttonNext = buildXpath(formContainer, "/child::div/button[2]");

//    // Проверка заголовка формы
//    public boolean isFormTitleCorrect() {
//        WebElement titleElement = DriverManager.getDriver().findElement(formTitle);
//        return titleElement.isDisplayed() && titleElement.getText().equals("Вы вошли как администратор");
//    }

    // Заполнение формы
    public void fillForm(String surname, String firstName, String secondName, String phone, String passportNumber, String birthDate) {
        DriverManager.getDriver().findElement(textSurname).sendKeys(surname);
        DriverManager.getDriver().findElement(textFirstName).sendKeys(firstName);
        DriverManager.getDriver().findElement(textSecondName).sendKeys(secondName);
        DriverManager.getDriver().findElement(textPhone).sendKeys(phone);
        DriverManager.getDriver().findElement(textNumberOfPassport).sendKeys(passportNumber);
        DriverManager.getDriver().findElement(dateOfBirth).sendKeys(birthDate);
    }

    // Нажатие на кнопку "Далее"
    public void clickNextButton() {
        DriverManager.getDriver().findElement(buttonNext).click();
//        DriverManager.getDriver().manage().timeouts().getPageLoadTimeout();
        DriverManager.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        ReactWaits.waitForReactComponent(DriverManager.getDriver(), "[data-testid='admin-button']");
    }

    // Проверка активности кнопки "Далее
    public boolean nextIsButtonActive() {
        WebElement nextButton = DriverManager.getDriver().findElement(buttonNext);
        return nextButton.isEnabled();
    }

    // Нажатие на кнопку "Закрыть"
    public void clickCloseButton() {
        DriverManager.getDriver().findElement(buttonClose).click();
    }

}
