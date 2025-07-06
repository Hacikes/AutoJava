package org.qateams.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.qateams.core.driver.DriverManager;

import java.util.Arrays;
import java.util.List;

public class InputValidationComponent {
    private By inputLocator;

    public InputValidationComponent() {

    }

    public boolean isFieldHighlightedInRed(By inputLocator) {
        WebElement inputElement =  DriverManager.getDriver().findElement(inputLocator);

        // Проверка наличия атрибута aria-invalid
        boolean hasAriaInvalid = inputElement.getAttribute("aria-invalid") != null
                && inputElement.getAttribute("aria-invalid").equals("true");

        // Проверка классов, отвечающих за красную рамку
        List<String> redBorderClasses = Arrays.asList(
                "ub-b-rgt-clr_D14343_fpx4do",
                "ub-b-lft-clr_D14343_fpx4do",
                "ub-b-top-clr_D14343_fpx4do",
                "ub-b-btm-clr_D14343_fpx4do"
        );

        boolean hasRedBorderClasses = redBorderClasses.stream()
                .allMatch(cls -> inputElement.getAttribute("class").contains(cls));

        return hasAriaInvalid && hasRedBorderClasses;
    }

    public boolean isFieldNormalState() {
        WebElement inputElement =  DriverManager.getDriver().findElement(inputLocator);

        // Проверка отсутствия атрибута aria-invalid
        boolean noAriaInvalid = inputElement.getAttribute("aria-invalid") == null
                || inputElement.getAttribute("aria-invalid").equals("false");

        // Проверка отсутствия классов красной рамки
        List<String> redBorderClasses = Arrays.asList(
                "ub-b-rgt-clr_D14343_fpx4do",
                "ub-b-lft-clr_D14343_fpx4do",
                "ub-b-top-clr_D14343_fpx4do",
                "ub-b-btm-clr_D14343_fpx4do"
        );

        boolean noRedBorderClasses = redBorderClasses.stream()
                .noneMatch(cls -> inputElement.getAttribute("class").contains(cls));

        return noAriaInvalid && noRedBorderClasses;
    }

//    // Метод для получения текста ошибки, если есть
//    public String getErrorMessage() {
//        try {
//            WebElement errorElement =  DriverManager.getDriver().findElement(By.xpath(
//                    "//input[@id='" +
//                            DriverManager.getDriver().findElement(inputLocator).getAttribute("id") +
//                            "']/following-sibling::div[contains(@class, 'error-message')]"
//            ));
//            return errorElement.getText();
//        } catch (NoSuchElementException e) {
//            return null;
//        }
//    }
}

