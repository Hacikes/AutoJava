package org.qateams.pages.components.user;

import org.qateams.core.driver.DriverManager;
import org.qateams.pages.components.user.valueobject.BirthServiceData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Данные услуги для рождения
public class BirthServiceDataPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "birthPlaceOfBirth")
    private WebElement placeOfBirthField;
    @FindBy(id = "birthMother")
    private WebElement motherField;
    @FindBy(id = "birthFather")
    private WebElement fatherField;
    @FindBy(id = "birthGrandmother")
    private WebElement grandmotherField;
    @FindBy(id = "birthGrandfather")
    private WebElement grandfatherField;

    @FindBy(id = "completeButton")
    private WebElement completeButton;

    // Конструктор теперь не принимает WebDriver
    public BirthServiceDataPage() {
        this.driver = DriverManager.getDriver(); // Получаем драйвер
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Заполняет форму данных услуги "Регистрация рождения", используя Value Object.
     * @param data Объект BirthServiceData.
     */
    public void fillBirthServiceData(BirthServiceData data) {
        wait.until(ExpectedConditions.visibilityOf(placeOfBirthField));

        placeOfBirthField.sendKeys(data.getPlaceOfBirth());
        motherField.sendKeys(data.getMother());
        fatherField.sendKeys(data.getFather());
        grandmotherField.sendKeys(data.getGrandmother());
        grandfatherField.sendKeys(data.getGrandfather());
    }

    public void clickCompleteButton() {
        wait.until(ExpectedConditions.elementToBeClickable(completeButton)).click();
    }

    public boolean isCompleteButtonEnabled() {
        return completeButton.isEnabled();
    }

    /**
     * Проверяет, подсвечено ли поле красным.
     * @param fieldName Название поля.
     * @return true, если поле подсвечено красным, иначе false.
     */
    public boolean isFieldHighlightedRed(String fieldName) {
        WebElement field;
        switch (fieldName.toLowerCase()) {
            case "место рождения": field = placeOfBirthField; break;
            case "мать": field = motherField; break;
            case "отец": field = fatherField; break;
            case "бабушка": field = grandmotherField; break;
            case "дедушка": field = grandfatherField; break;
            default: throw new IllegalArgumentException("Неизвестное поле для проверки подсветки: " + fieldName);
        }
        String borderColor = field.getCssValue("border-color");
        return borderColor.contains("rgb(255, 0, 0)") || borderColor.contains("#FF0000");
    }
}
