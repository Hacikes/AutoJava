package org.qateams.pages.components.user;

import org.openqa.selenium.Keys;
import org.qateams.core.driver.DriverManager;
import org.qateams.pages.components.user.valueobject.ApplicantData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.utils.InputValidationComponent;

import java.time.Duration;
import java.util.Optional;

// Данные заявителя
public class UserRegistrationPage {

    private final WebDriver driver;
    public final WebDriverWait wait;
    private final InputValidationComponent inputValidationComponent;

    // Заголовок модального окна (текст "Вы вошли как Пользоватиль")
    // Используем XPath, т.к. ID динамический, а текст стабилен.
    @FindBy(xpath = "//h2[contains(@class, 'MuiDialogTitle-root')]/span")
    private WebElement pageTitleHint;

    // Текст выбранной услуги
    // Используем XPath, т.к. класс динамический, а текст стабилен.
    @FindBy(xpath = "//span[contains(text(), 'Вы выбрали услугу:')]")
    private WebElement selectedServiceText;

    // Поля ввода данных заявителя
    // Предпочитаем ID, если они стабильны. Если нет, перейдем на placeholder или комбинации с label.
    @FindBy(id = "TextInputField-1") // Фамилия
    public WebElement lastNameField; // Сделал public для прямого доступа в тесте для .clear() и .getAttribute()

    @FindBy(id = "TextInputField-2") // Имя
    public WebElement firstNameField; // Сделал public

    @FindBy(id = "TextInputField-3") // Отчество
    public WebElement middleNameField; // Сделал public

    @FindBy(id = "TextInputField-4") // Телефон
    public WebElement phoneField; // Сделал public

    @FindBy(id = "TextInputField-5") // Номер паспорта
    public WebElement passportNumberField; // Сделал public

    @FindBy(id = "TextInputField-6") // Адрес прописки
    public WebElement addressField; // Сделал public

    // Кнопки
    // Используем XPath по тексту, т.к. кнопки не имеют уникальных ID или стабильных классов для прямого доступа.
    @FindBy(xpath = "//button[contains(., 'Далее') and not(contains(@class, 'Mui-disabled'))]")
    private WebElement nextButton;

    @FindBy(xpath = "//button[contains(., 'Закрыть')]")
    private WebElement closeButton;

    public UserRegistrationPage() {
        this.inputValidationComponent = new InputValidationComponent();
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public String getPageTitleHint() {
        // Убедимся, что модальное окно видимо, ожидая заголовок.
        wait.until(ExpectedConditions.visibilityOf(pageTitleHint));
        return pageTitleHint.getText(); // Возвращаем текст "Вы вошли как Пользоватиль"
    }

    public String getSelectedServiceText() {
        wait.until(ExpectedConditions.visibilityOf(selectedServiceText));
        return selectedServiceText.getText();
    }

    /**
     * Заполняет форму данных заявителя, используя Value Object.
     * @param data Объект ApplicantData, содержащий данные заявителя.
     */
    public void fillApplicantData(ApplicantData data) {
        // Ожидаем видимости хотя бы одного поля, чтобы убедиться, что форма загружена
        wait.until(ExpectedConditions.visibilityOf(passportNumberField)); // Ждем именно паспортного поля, так как оно обязательное

        // Для каждого поля используем Optional.ofNullable для безопасного заполнения,
        // только если данные не null.
        // Очищаем поля перед вводом, чтобы избежать наложения данных в многошаговых тестах
        if (data.getLastName() != null) { lastNameField.clear(); lastNameField.sendKeys(data.getLastName()); }
        if (data.getFirstName() != null) { firstNameField.clear(); firstNameField.sendKeys(data.getFirstName()); }
        if (data.getMiddleName() != null) { middleNameField.clear(); middleNameField.sendKeys(data.getMiddleName()); }
        if (data.getPhone() != null) { phoneField.clear(); phoneField.sendKeys(data.getPhone()); }
        if (data.getPassportNumber() != null) { passportNumberField.clear(); passportNumberField.sendKeys(data.getPassportNumber()); }
        if (data.getAddress() != null) { addressField.clear(); addressField.sendKeys(data.getAddress()); }
    }

    // Методы для очистки полей
    public void clearLastNameField() { lastNameField.clear(); }
    public void clearFirstNameField() { firstNameField.clear(); }
    public void clearMiddleNameField() { middleNameField.clear(); }
    public void clearPhoneField() { phoneField.clear(); }
    public void clearPassportNumberField() {
        passportNumberField.sendKeys(Keys.CONTROL + "a");
        passportNumberField.sendKeys(Keys.DELETE);
    }
    public void clearAddressField() { addressField.clear(); }


    public void clickNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    public void clickCloseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
    }

    /**
     * Проверяет, доступна ли кнопка "Далее" для клика.
     * Мы используем тот же локатор, что и для кнопки, который специально исключает "disabled" состояние.
     * Если кнопка disabled, `nextButton` не будет найден по этому локатору.
     * @return true, если кнопка "Далее" активна, иначе false.
     */
    public boolean isNextButtonEnabled() {
        try {
            return nextButton.isEnabled();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // Если элемент не найден, значит он либо не существует, либо disabled.
            // В нашем случае локатор уже исключает disabled состояние, так что это значит, что кнопка неактивна.
            return false;
        }
    }

    /**
     * Проверяет, подсвечено ли поле красным (для обязательных полей).
     *
     * @param fieldName Название поля (Фамилия, Имя, Отчество, Телефон, Номер паспорта, Адрес прописки).
     * @return true, если поле подсвечено красным, иначе false.
     */
    public boolean isFieldHighlightedRed(String fieldName) {
        By fieldLocator;
        switch (fieldName.toLowerCase()) {
            case "фамилия": fieldLocator = By.id("TextInputField-1"); break;
            case "имя": fieldLocator = By.id("TextInputField-2"); break;
            case "отчество": fieldLocator = By.id("TextInputField-3"); break;
            case "телефон": fieldLocator = By.id("TextInputField-4"); break;
            case "номер паспорта": fieldLocator = By.id("TextInputField-5"); break;
            case "адрес прописки": fieldLocator = By.id("TextInputField-6"); break;
            default: throw new IllegalArgumentException("Неизвестное поле для проверки подсветки: " + fieldName);
        }
        // Используем компонент для проверки
        return inputValidationComponent.isFieldHighlightedInRed(fieldLocator);
    }

    // Клик на свободную область
    public void clickOnFreeArea() {
        WebElement freeArea = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[contains(@class, 'MuiDialogTitle-root')]/span")));
        freeArea.click();

    }
}
