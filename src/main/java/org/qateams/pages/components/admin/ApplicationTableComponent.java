package org.qateams.pages.components.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qateams.core.driver.DriverManager;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ApplicationTableComponent {
    // Методы работы с таблицей

    // Базовый метод построения XPath
    private By buildXpath(By baseElement, String relativeXpath) {
        String baseXpath = baseElement.toString().replace("By.xpath: ", "");
        return By.xpath(baseXpath + relativeXpath);
    }


    // Базовый локатор (общий предок)
    private final By tableContainer = By.xpath("//div[contains(@class, 'MuiPaper-root')]");
    private final By applicantTable = buildXpath(tableContainer, "//div/table");
    private final By tableRow = buildXpath(applicantTable, "/tr");
    private final By applicantEmptyTable = buildXpath(tableContainer, "//div/text");

    private WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));

    public WebDriverWait getWait() {
        return wait;
    }

    // Метод получения всей таблицы
    public List<List<String>> getTableData() {

        // Ожидание присутствия таблицы
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(tableRow));

        // Ожидание видимости всех строк
        wait.until(driver -> {
            List<WebElement> rows = driver.findElements(tableRow);
            return !rows.isEmpty() && rows.stream().allMatch(WebElement::isDisplayed);
        });

        // Дополнительная проверка на полную загрузку данных
        wait.until(driver -> {
            List<WebElement> rows = driver.findElements(tableRow);
            return rows.stream()
                    .noneMatch(row -> row.findElements(By.tagName("td")).isEmpty());
        });

        return DriverManager.getDriver()
                .findElements(tableRow)
                .stream()
                .map(row -> row.findElements(By.tagName("td"))
                        .stream()
                        .map(cell -> cell.getText().trim())
                        .collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }

    public WebElement getEmptyTableText() {
        return DriverManager.getDriver().findElement(applicantEmptyTable);
    }

    // Методы для получения локаторов ячеек строки
    public By getRowNumber(int rowIndex) {
        return buildXpath(tableRow, "[" + rowIndex + "]/td[1]");
    }

    public int getApplicationNumber(int rowIndex) {
        By applicationNumber = getRowNumber(rowIndex);
        WebElement numberElement = DriverManager.getDriver().findElement(applicationNumber);
        String tempString = numberElement.getText().trim();
        return Integer.parseInt(tempString);
    }

    public By getRowStatus(int rowIndex) {
        return buildXpath(tableRow, "[" + rowIndex + "]/td[5]");
    }

    public String getStatusText(int rowIndex) {
        By statusLocator = getRowStatus(rowIndex);
        WebElement statusElement = DriverManager.getDriver().findElement(statusLocator);
        return statusElement.getText().trim();
    }

    // Поиск строки по номеру заявки
    public int findRowIndexByApplicationNumber(int applicationNumber) {
        List<WebElement> rows = DriverManager.getDriver().findElements(tableRow);
        return IntStream.range(0, rows.size())
                .filter(i -> {
                    WebElement numberCell = rows.get(i).findElement(By.xpath("./td[1]"));
                    try {
                        return Integer.parseInt(numberCell.getText().trim()) == applicationNumber;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Заявка с номером " + applicationNumber + " не найдена")) + 1;
    }

    // Получение статуса по номеру заявки
    public String getStatusByApplicationNumber(int applicationNumber) {
        int rowIndex = findRowIndexByApplicationNumber(applicationNumber);
        return getStatusText(rowIndex);
    }
}
