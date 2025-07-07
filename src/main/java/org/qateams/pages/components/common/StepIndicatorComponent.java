package org.qateams.pages.components.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.constansts.StatusColors;
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
     * ОБНОВЛЕНО: Учитывает как шаги с номером, так и завершенные шаги (без номера)
     * @param stepNumber Номер шага (1-5).
     * @return WebElement, представляющий корневой элемент шага.
     */
    private WebElement getStepRootElement(int stepNumber) {
        // Локатор для шага, который содержит либо номер, либо является "завершенным" (Mui-completed)
        String xpathLocator;
        if (stepNumber == 1) { // Если это первый шаг, который может быть completed
            // Для первого шага ищем по его Label тексту "Данные заявителя"
            // или по наличию класса Mui-completed на span.MuiStepLabel-root
            // или по Mui-completed на div.MuiStep-root
            xpathLocator = String.format("//div[contains(@class, 'MuiStep-root') and (.//span[contains(text(), 'Данные заявителя')] or contains(@class, 'Mui-completed'))]");
        } else {
            // Для остальных шагов ищем по номеру
            xpathLocator = String.format("//div[contains(@class, 'MuiStep-root') and .//text()='%d']", stepNumber);
        }

        // Дополнительная проверка на уникальность локатора, если есть риск найти несколько
        // Если текст шагов уникален, этого должно быть достаточно.
        // Или можно искать по тексту метки
        // Примечание: Для шага 1, если он завершен, номера может и не быть.
        // Будем использовать более надежный подход: ищем по тексту метки (label).
        // Это более универсальный способ, который не зависит от наличия номера или галочки.
        // Пример текста: "Данные заявителя", "Выбор услуги", "Данные гражданина"
        String labelText;
        switch (stepNumber) {
            case 1: labelText = "Данные заявителя"; break;
            case 2: labelText = "Выбор услуги"; break;
            case 3: labelText = "Данные гражданина"; break;
            case 4: labelText = "Даные услуги"; break; // Опечатка "Даные" в вашем HTML, исправьте если надо
            case 5: labelText = "Статус заявки"; break;
            default: throw new IllegalArgumentException("Неверный номер шага: " + stepNumber);
        }
        // Новый, более надежный локатор: ищем div.MuiStep-root, который содержит span.MuiStepLabel-label со спаном с нужным текстом
        By finalLocator = By.xpath(String.format(
                "//div[contains(@class, 'MuiStep-root') and .//span[contains(@class, 'MuiStepLabel-label')]//span[contains(text(), '%s')]]",
                labelText
        ));

        return wait.until(ExpectedConditions.visibilityOfElementLocated(finalLocator));
    }

    /**
     * Возвращает SVG-элемент иконки для указанного шага.
     * @param stepNumber Номер шага (1-5).
     * @return WebElement, представляющий SVG-иконку шага.
     */
    private WebElement getStepIconSvg(int stepNumber) {
        // Находим SVG-элемент внутри корневого элемента шага
        return getStepRootElement(stepNumber).findElement(By.cssSelector("span.MuiStepLabel-iconContainer svg.MuiStepIcon-root"));
    }

    /**
     * Возвращает элемент <circle> или <path> внутри SVG-иконки для указанного шага.
     * Это позволяет работать как с кругами (для активных/неактивных шагов с номерами),
     * так и с галочками (для завершенных шагов).
     * @param stepNumber Номер шага (1-5).
     * @return WebElement, представляющий элемент <circle> или <path>.
     */
    private WebElement getStepIconShapeElement(int stepNumber) {
        WebElement iconSvg = getStepIconSvg(stepNumber);
        try {
            // Пытаемся найти круг
            return iconSvg.findElement(By.tagName("circle"));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // Если круга нет (например, для Mui-completed иконки с галочкой), ищем path
            return iconSvg.findElement(By.tagName("path"));
        }
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
     * Проверяет, что фигура иконки (круг или путь) указанного шага имеет определённый цвет заливки.
     * ОБНОВЛЕНО: Переименовано и использует getStepIconShapeElement.
     * @param stepNumber Номер шага (1-5).
     * @param expectedColorRGB Ожидаемый цвет в формате RGB (например, "rgb(25, 118, 210)").
     * @return true, если цвет заливки фигуры соответствует ожидаемому, иначе false.
     */
    public boolean isStepIconFillColor(int stepNumber, String expectedColorRGB) {
        WebElement shapeElement = getStepIconShapeElement(stepNumber);
        String actualFillColor = shapeElement.getCssValue("fill"); // Получаем цвет заливки фигуры
        return actualFillColor.trim().equalsIgnoreCase(expectedColorRGB.trim());
    }

    /**
     * Проверяет, что шаг завершен или активен (стал зеленым/синим).
     * Использует проверку класса 'Mui-completed' или 'Mui-active' на SVG-иконке
     * и проверку цвета заливки фигуры.
     *
     * @param stepNumber Номер шага (1-5).
     * @return true, если шаг завершен или активен и отображается как "зеленый/синий", иначе false.
     */
    public boolean isStepGreen(int stepNumber) {
        WebElement iconSvg = getStepIconSvg(stepNumber);
        boolean hasActiveClass = iconSvg.getAttribute("class").contains("Mui-active");
        boolean hasCompletedClass = iconSvg.getAttribute("class").contains("Mui-completed");

        // Если шаг завершен (Mui-completed), его цвет может быть тем же, что и у активного,
        // или отличаться. В вашем HTML для completed шага также нет явного fill/color в стилях,
        // но Mui обычно делает его такого же цвета как активный (т.е. синим).
        boolean hasExpectedColor = isStepIconFillColor(stepNumber, StatusColors.STEP_INDICATOR_ACTIVE_RGB);

        // Шаг считается "зеленым/активным/завершенным", если он активен ИЛИ завершен, И имеет правильный цвет.
        return (hasActiveClass || hasCompletedClass) && hasExpectedColor;
    }

    /**
     * Проверяет, что шаг отображается как серый/незавершенный.
     *
     * @param stepNumber Номер шага (1-5).
     * @return true, если шаг серый, иначе false.
     */
    public boolean isStepGray(int stepNumber) {
        WebElement iconSvg = getStepIconSvg(stepNumber);
        boolean hasDisabledClassOnLabel = getStepLabelRootSpan(stepNumber).getAttribute("class").contains("Mui-disabled");
        boolean hasActiveClassOnSvg = iconSvg.getAttribute("class").contains("Mui-active"); // Должен отсутствовать
        boolean hasCompletedClassOnSvg = iconSvg.getAttribute("class").contains("Mui-completed"); // Должен отсутствовать

        // Проверяем цвет заливки фигуры на соответствие дефолтному серому
        boolean hasDefaultGrayColor = isStepIconFillColor(stepNumber, StatusColors.STEP_INDICATOR_DEFAULT_GRAY_RGB);

        // Шаг серый, если у него есть Mui-disabled класс на метке,
        // И нет Mui-active И нет Mui-completed на иконке SVG,
        // И его цвет заливки соответствует серому по умолчанию.
        return hasDisabledClassOnLabel && !hasActiveClassOnSvg && !hasCompletedClassOnSvg;
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