package org.qateams.pages.components.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.core.driver.DriverManager;
import org.qateams.utils.InputValidationComponent;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class AdminLoginComponent {

    // Инициализация утилиты для проверки подсветки красным
    InputValidationComponent inputValidationComponent = new InputValidationComponent();

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
    private final By textBirthDate = buildXpath(formContainer, "//div[6]/input");

    private final By buttonClose = buildXpath(formContainer, "/child::div/button[1]");
    private final By buttonNext = buildXpath(formContainer, "/child::div/button[2]");

//    // Проверка заголовка формы
//    public boolean isFormTitleCorrect() {
//        WebElement titleElement = DriverManager.getDriver().findElement(formTitle);
//        return titleElement.isDisplayed() && titleElement.getText().equals("Вы вошли как администратор");
//    }


    // Заполнение формы
     public void fillForm(String surname, String firstName, String secondName, String phone, String passportNumber, String birthDate) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5));

        wait.until(ExpectedConditions.elementToBeClickable(textSurname)).sendKeys(surname);
        wait.until(ExpectedConditions.elementToBeClickable(textFirstName)).sendKeys(firstName);
        wait.until(ExpectedConditions.elementToBeClickable(textSecondName)).sendKeys(secondName);
        wait.until(ExpectedConditions.elementToBeClickable(textPhone)).sendKeys(phone);
        wait.until(ExpectedConditions.elementToBeClickable(textNumberOfPassport)).sendKeys(passportNumber);
        wait.until(ExpectedConditions.elementToBeClickable(textBirthDate)).sendKeys(birthDate);
    }

    // Методы получения введённого текста из поля
    public String getEnteredSurnameText() {

        WebElement surnameInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(textSurname));

        return surnameInput.getAttribute("value");
    }


    public String getEnteredFirstNameText() {
        WebElement firstNameInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(textFirstName));
        return firstNameInput.getAttribute("value");
    }

    public String getEnteredSecondNameText() {
        WebElement secondNameInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(textSecondName));
        return secondNameInput.getAttribute("value");
    }

    public String getEnteredPhone() {
        WebElement phoneInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(textPhone));
        return phoneInput.getAttribute("value");
    }

    public String getEnteredNumberOfPassport() {
        WebElement numberOfPassportInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(textNumberOfPassport));
        return numberOfPassportInput.getAttribute("value");
    }

    public String getEnteredBirthDate() {
        WebElement birthDateInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(textNumberOfPassport));
        return birthDateInput.getAttribute("value");
    }

    // Методы очистки полей
    public void clearEnteredSurnameText() {

        WebElement surnameInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(textSurname));

        surnameInput.sendKeys(Keys.CONTROL + "a");
        surnameInput.sendKeys(Keys.DELETE);
    }

    public void clearEnteredFirstNameText() {

        WebElement surnameInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(textFirstName));

        surnameInput.sendKeys(Keys.CONTROL + "a");
        surnameInput.sendKeys(Keys.DELETE);
    }

    public void clearEnteredSecondNameText() {

        WebElement surnameInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(textSecondName));

        surnameInput.sendKeys(Keys.CONTROL + "a");
        surnameInput.sendKeys(Keys.DELETE);
    }

    public void clearEnteredPhone() {

        WebElement surnameInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(textPhone));

        surnameInput.sendKeys(Keys.CONTROL + "a");
        surnameInput.sendKeys(Keys.DELETE);
    }

    public void clearEnteredNumberOfPassport() {

        WebElement surnameInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(textNumberOfPassport));

        surnameInput.sendKeys(Keys.CONTROL + "a");
        surnameInput.sendKeys(Keys.DELETE);
    }


//    // Метода очистки введённого текста в поле
//    public void clearEnteredFirstNameText() {
//        WebElement firstNameInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
//                .until(ExpectedConditions.presenceOfElementLocated(textFirstName));
//        firstNameInput.clear();
//    }
//
//    public void clearEnteredSecondNameText() {
//        WebElement firstNameInput = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
//                .until(ExpectedConditions.presenceOfElementLocated(textSecondName));
//        firstNameInput.clear();
//    }

    // Нажатие на кнопку "Далее"
    public void clickNextButton() {
        DriverManager.getDriver().findElement(buttonNext).click();
//        DriverManager.getDriver().manage().timeouts().getPageLoadTimeout();
        DriverManager.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        ReactWaits.waitForReactComponent(DriverManager.getDriver(), "[data-testid='admin-button']");
    }

//    // Проверка активности кнопки "Далее
//    public boolean nextIsButtonActive() {
//        WebElement nextButton = DriverManager.getDriver().findElement(buttonNext);
//        return nextButton.isEnabled();
//    }

    // Нажатие на кнопку "Закрыть"
    public void clickCloseButton() {
        DriverManager.getDriver().findElement(buttonClose).click();
    }

    // Проверка активности кнопки "Далее"
    public boolean isNextButtonActive() {
        WebElement nextButton = DriverManager.getDriver().findElement(buttonNext);
        return nextButton.isEnabled();
    }

    // Клик на свободную область
    public void clickOnFreeArea() {
                WebElement freeArea = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(formContainer));
        freeArea.click();

    }

    // Метод проверки подсветки поля
    public boolean isSurnameFieldInvalid() {
        return inputValidationComponent.isFieldHighlightedInRed(textSurname);
    }

    // Метод проверки подсветки поля
    public boolean isFirstNameFieldInvalid() {
        return inputValidationComponent.isFieldHighlightedInRed(textFirstName);
    }

    // Метод проверки подсветки поля
    public boolean isSecondNameFieldInvalid() {
        return inputValidationComponent.isFieldHighlightedInRed(textSecondName);
    }

    // Метод проверки подсветки поля
    public boolean isPhoneFieldInvalid() {
        return inputValidationComponent.isFieldHighlightedInRed(textPhone);
    }

    // Метод проверки подсветки поля
    public boolean isNumberOfPassportFieldInvalid() {
        return inputValidationComponent.isFieldHighlightedInRed(textNumberOfPassport);
    }

    // Метод проверки подсветки поля
    public boolean isDateFieldInvalid() {
        return inputValidationComponent.isFieldHighlightedInRed(textBirthDate);
    }


    // Метод для выбора даты через UI датапикера
    public void selectDateThroughDatepicker(String year, String month, String day) {
        // Выбор года
        WebElement yearDropdown = DriverManager.getDriver()
                .findElement(By.xpath("//select[@class='year-select']"));
        new Select(yearDropdown).selectByVisibleText(year);

        // Выбор месяца
        WebElement monthDropdown = DriverManager.getDriver()
                .findElement(By.xpath("//select[@class='month-select']"));
        new Select(monthDropdown).selectByVisibleText(month);

        // Выбор дня
        WebElement dayElement = DriverManager.getDriver()
                .findElement(By.xpath("//td[@data-date='15' and @data-month='0' and @data-year='2000']"));
        dayElement.click();
    }

    public String getCurrentDateMinusYears(int years) {
        LocalDate currentDate = LocalDate.now();
        LocalDate targetDate = currentDate.minusYears(years);
        return targetDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }



}
