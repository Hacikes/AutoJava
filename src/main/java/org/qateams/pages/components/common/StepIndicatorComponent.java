package org.qateams.pages.components.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.core.driver.DriverManager;

import java.time.Duration;

public class StepIndicatorComponent {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public StepIndicatorComponent() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Возвращает корневой WebElement для указанного шага.
     * @param stepNumber Номер шага (1-5).
     * @return WebElement, представляющий корневой элемент шага.
     */
    private WebElement getStepRootElement(int stepNumber) {
        // Находим div.MuiStep-root, который содержит текст с номером шага
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format("//div[contains(@class, 'MuiStep-root') and .//text()='%d']", stepNumber))
        ));
    }

    /**
     * Возвращает SVG-элемент иконки для указанного шага.
     * @param stepNumber Номер шага (1-5).
     * @return WebElement, представляющий SVG-иконку шага.
     */
    private WebElement getStepIconSvg(int stepNumber) {
        // Находим SVG-элемент внутри корневого элемента шага
        return getStepRootElement(stepNumber).findElement(By.cssSelector("svg.MuiStepIcon-root"));
    }

    /**
     * Возвращает элемент MuiStepLabel-root span для указанного шага.
     * @param stepNumber Номер шага (1-5).
     * @return WebElement, представляющий MuiStepLabel-root span.
     */
    private WebElement getStepLabelRootSpan(int stepNumber) {
        // Находим span.MuiStepLabel-root внутри корневого элемента шага
        return getStepRootElement(stepNumber).findElement(By.cssSelector("span.MuiStepLabel-root"));
    }

    /**
     * Проверяет, является ли иконка указанного шага активной (зеленой/текущей).
     * В Mui компонентах "зеленый" цвет активного или завершенного шага часто определяется классом 'Mui-active'
     * на SVG-иконке.
     *
     * @param stepNumber Номер шага (1-5).
     * @return true, если иконка шага активна, иначе false.
     */
    public boolean isStepIconActive(int stepNumber) {
        WebElement iconSvg = getStepIconSvg(stepNumber);
        // Проверяем наличие класса 'Mui-active'
        return iconSvg.getAttribute("class").contains("Mui-active");
    }

    /**
     * Проверяет, является ли метка (label) указанного шага неактивной (серой/незавершенной).
     * В Mui компонентах "серый" цвет незавершенного шага часто определяется классом 'Mui-disabled'
     * на родительском элементе метки (MuiStepLabel-root).
     *
     * @param stepNumber Номер шага (1-5).
     * @return true, если метка шага неактивна (серая), иначе false.
     */
    public boolean isStepLabelDisabled(int stepNumber) {
        WebElement labelRootSpan = getStepLabelRootSpan(stepNumber);
        // Проверяем наличие класса 'Mui-disabled'
        return labelRootSpan.getAttribute("class").contains("Mui-disabled");
    }

    /**
     * Проверяет, что шаг завершен (стал зеленым).
     * Это может быть комбинация классов, например, отсутствие 'Mui-disabled' и/или наличие 'Mui-active'.
     * В текущем HTML для первого шага (активного) есть `Mui-active` на SVG.
     * Для завершенных шагов, если они остаются зелеными, также может присутствовать `Mui-active`
     * или `Mui-completed` (хотя последнего нет в предоставленном HTML).
     * В данном контексте, "зеленый" означает "активный или пройденный".
     *
     * @param stepNumber Номер шага (1-5).
     * @return true, если шаг завершен и отображается как "зеленый", иначе false.
     */
    public boolean isStepGreen(int stepNumber) {
        // Шаг считается "зеленым", если его иконка активна (Mui-active)
        // и его метка не имеет класса Mui-disabled (если это применимо к пройденным шагам)
        // Для простоты, пока будем использовать isStepIconActive как главный индикатор "зеленого".
        // Если логика UI сложнее, можно добавить проверку цвета или дополнительный класс.
        return isStepIconActive(stepNumber);
    }

    /**
     * Проверяет, что шаг отображается как серый/незавершенный.
     * Это означает, что его метка имеет класс 'Mui-disabled'.
     *
     * @param stepNumber Номер шага (1-5).
     * @return true, если шаг серый, иначе false.
     */
    public boolean isStepGray(int stepNumber) {
        return isStepLabelDisabled(stepNumber);
    }

    /**
     * Получает текст метки шага.
     * @param stepNumber Номер шага (1-5).
     * @return Текст метки шага (например, "Данные заявителя").
     */
    public String getStepLabelText(int stepNumber) {
        // Находим span.MuiStepLabel-label внутри корневого элемента шага и получаем его текст
        WebElement labelTextSpan = getStepRootElement(stepNumber).findElement(By.cssSelector("span.MuiStepLabel-label span"));
        return labelTextSpan.getText();
    }
}
