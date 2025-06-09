package org.qateams.tests.admin;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.qateams.admin.applicationmanagment.ApplicationManagement;
import org.qateams.constansts.ApplicationStatus;
import org.qateams.base.BaseTest;
import org.qateams.utils.ApplicationStatusService;
import org.qateams.utils.DataValidationUtils;
import org.qateams.utils.DateProcessingService;
import org.qateams.utils.TestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ApplicationManagementTest extends BaseTest {

    private ApplicationManagement am;


    @BeforeMethod
    public void setup() {
        // Беру данные для формы из TestUtils.prepareAdminData()
        TestUtils.prepareAdminData().clickNextButton();
        am = new ApplicationManagement();
    }


    // Проверка на заполненность таблицы
    @Test
    public void testAllCellsAreFilled() {

        // Получаю таблицу с заявками
        List<List<String>> actualData = am.getTableData();

        actualData.forEach(row ->
                IntStream.range(0, row.size() - 1)
                        .forEach(colIndex ->
                                softAssert.assertFalse(row.get(colIndex).trim().isEmpty(),
                                        "Ячейка [" + row.indexOf(row) + "," + colIndex + "] пуста!")
                        )
        );
    }

    // Тест на типы данных в таблице
    @Test
    public void testColumnDataTypes() {
        List<List<String>> table = am.getTableData();

        softAssert.assertTrue(
                table.stream().allMatch(row -> DataValidationUtils.isInteger(row.getFirst())),
                "Первый столбец должен содержать только целые числа"
        );

        softAssert.assertTrue(
                table.stream().allMatch(row -> DataValidationUtils.isInteger(row.get(1))),
                "Второй столбец должен содержать только целые числа"
        );

        softAssert.assertTrue(
                table.stream().allMatch(row -> DataValidationUtils.isValidApplicationType(row.get(2))),
                "Третий столбец должен содержать корректные типы заявок"
        );

        table.forEach(row ->
                softAssert.assertTrue(
                        DataValidationUtils.isValidCustomDateFormat(row.get(3)),
                        "Значение " + row.get(3) + " не соответствует формату DD.MM.YYYY HH:MM:SS"
                )
        );

        softAssert.assertTrue(
                table.stream().allMatch(row -> DataValidationUtils.isValidStatus(row.get(4))),
                "Пятый столбец должен содержать корректные статусы"
        );
    }

    // Проверка сортировки строк по дате
    @Test
    public void testDateSortingFromCurrentToEarliest() {
        List<List<String>> actualData = am.getTableData();

        List<Long> unixTimestamps = DateProcessingService.convertToUnixTimestamps(actualData, 3);

        softAssert.assertFalse(unixTimestamps.isEmpty(), "Список дат не должен быть пустым");

        softAssert.assertTrue(
                DataValidationUtils.isDescendingSorted(unixTimestamps),
                "Даты должны быть отсортированы от текущей до самой ранней"
        );
    }


    // Проверка цвета текста статуса заявки
    @Test
    public void testApplicationStatusAndColors() {
        List<List<String>> actualData = am.getTableData();

        actualData.forEach(row -> {
            String statusText = row.get(4);

            try {
                ApplicationStatusService.validateStatusAndColor(statusText);
            } catch (Exception e) {
                softAssert.fail(e.getMessage());
            }
        });
    }


    // Одобрение и отклонение заявки
    @Test
    public void testApplicantApprove() {
        performApplicationAction(true);
    }


    // Отклонение заявки
    @Test
    public void testApplicantReject() {
        performApplicationAction(false);
    }


    // Метод нажатия на кнопки одобрения или отклонения и проверки актуального статуса после нажатия
    private void performApplicationAction(boolean isApprove) {
        List<List<String>> table = am.getTableData();
        int randomRowIndex = new Random().nextInt(table.size());

        int changedApplicationNumber = am.getApplicationNumber(randomRowIndex);
        am.performActionOnApplication(randomRowIndex, isApprove);

        String expectedStatus = isApprove
                ? ApplicationStatus.APPROVED.getRussianName()
                : ApplicationStatus.REJECTED.getRussianName();

        String actualStatus = am.getStatusByApplicationNumber(changedApplicationNumber);
        softAssert.assertEquals(actualStatus, expectedStatus);
    }


    // Проверка обновление таблицы
    @Test
    public void testRefreshButton() {

        // Выполняем обновление
        am.clickRefreshButton();

        // Получаем данные после обновления
        List<List<String>> refreshedTableData = am.getTableData();

        softAssert.assertNotNull(refreshedTableData, "Таблица после обновления не должна быть null");
        softAssert.assertFalse(refreshedTableData.isEmpty(), "Таблица после обновления не должна быть пустой");
        softAssert.assertAll("Ошибка при обновлении таблицы заявок");
    }


    // Проверка закрытия модалки на кнопку "Закрыть"
    @Test
    public void clickCloseButton() {
        am.clickCloseButton();

        try {
            WebElement amWindow = am.getWindowTitle();
            softAssert.assertFalse(amWindow.isDisplayed(), "Окно должно быть закрыто");
        } catch (NoSuchElementException e) {
            // Если элемент не найден, это означает, что окно закрыто
            softAssert.assertTrue(true, "Окно успешно закрыто");
        }
    }


    // Проверка отображения и активности кнопок закрытия и обновления
    @Test
    public void testButtonsVisibilityAndEnabled() {

        // Проверка кнопки обновления
        WebElement refreshButton = am.getRefreshButton();
        softAssert.assertTrue(refreshButton.isDisplayed(), "Кнопка обновления должна быть видима");
        softAssert.assertTrue(refreshButton.isEnabled(), "Кнопка обновления должна быть активна");

        // Проверка кнопки закрытия
        WebElement closeButton = am.getRefreshButton();
        softAssert.assertTrue(closeButton.isDisplayed(), "Кнопка закрытия должна быть видима");
        softAssert.assertTrue(closeButton.isEnabled(), "Кнопка закрытия должна быть активна");
    }

    // Проверка доступности нажатия на кнопки пагинации и нажатия на них
    @Test
    public void testPaginationDisplayed() {

        int applicantCount = am.getTableData().size();

        if(applicantCount == 200) {
            softAssert.assertFalse(am.checkedPaginationButtonFocus(true).isEnabled(), "Кнопка пагинации вперёд должна быть неактивна");
            softAssert.assertFalse(am.checkedPaginationButtonFocus(false).isEnabled(), "Кнопка пагинации назад должна быть неактивна");
        } else {
            softAssert.assertTrue(am.checkedPaginationButtonFocus(true).isEnabled(), "Кнопка пагинации вперёд должна быть активна");
            softAssert.assertTrue(am.checkedPaginationButtonFocus(false).isEnabled(), "Кнопка пагинации назад должна быть активна");
        }
    }


    // Проверка, что пагинация пагинирует и отображает отличные от прошлого списка номера заявок
    @Test
    public void testClickPaginationButtons() {

        // Проверка доступности пагинации
        WebElement nextPageButton = am.checkedPaginationButtonFocus(true);

        softAssert.assertTrue(nextPageButton.isEnabled(), "Кнопка перехода на следующую страницу должна быть активна");

        if(am.getPaginationButton(true).isEnabled()) {

            // Получение initial данных
            List<List<String>> initialTableData = am.getTableData();
            List<String> initialApplicationNumbers = extractApplicationNumbers(initialTableData);

            // Переход на следующую страницу при нажатии на стрелочку назад
            am.clickPaginationButton(true);

            // Получение данных следующей страницы
            List<List<String>> nextTableData = am.getTableData();
            List<String> nextApplicationNumbers = extractApplicationNumbers(nextTableData);

            // Проверки
            softAssert.assertNotNull(nextTableData, "Данные следующей страницы не должны быть null");
            softAssert.assertFalse(nextTableData.isEmpty(), "Данные следующей страницы не должны быть пустыми");
            softAssert.assertNotEquals(initialApplicationNumbers, nextApplicationNumbers, "Номера заявок на страницах должны отличаться"
            );

            // Проверка доступности пагинации назад
            WebElement backPageButton = am.checkedPaginationButtonFocus(false);
            softAssert.assertTrue(backPageButton.isEnabled(), "Кнопка перехода на предыдущую страницу должна быть активна"
            );

            // Переход на предыдущую страницу при нажатии на стрелочку назад
            am.clickPaginationButton(false);

            // Получение данных предыдущей страницы
            List<List<String>> backTableData = am.getTableData();
            List<String> backApplicationNumbers = extractApplicationNumbers(backTableData);

            // Проверки
            softAssert.assertNotNull(backTableData, "Данные предыдущую страницы не должны быть null");
            softAssert.assertFalse(backTableData.isEmpty(), "Данные предыдущую страницы не должны быть пустыми");
            softAssert.assertNotEquals(nextApplicationNumbers, backApplicationNumbers, "Номера заявок на страницах должны отличаться"
            );
        } else {
            softAssert.fail("Пагинация отсутствует или недоступна");
        }
    }

    private List<String> extractApplicationNumbers(List<List<String>> tableData) {
        return tableData.stream()
                .map(List::getFirst)
                .collect(Collectors.toList());
    }
}


