package org.qateams.tests.admintests;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.qateams.components.admin.ApplicationManagement;
import org.qateams.constansts.ApplicationStatus;
import org.qateams.driver.DriverManager;
import org.qateams.tests.BaseTest;
import org.qateams.utils.TestUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ApplicationManagementTest extends BaseTest {

    private ApplicationManagement am;
//    SoftAssert softAssert = new SoftAssert();


    @BeforeMethod
    public void setup() {
        // Беру данные для формы из TestUtils.prepareAdminData()
        TestUtils.prepareAdminData().clickNextButton();
        am = new ApplicationManagement();
    }


    private ApplicationManagementTest() {
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
//        softAssert.assertAll("Ошибка получения таблицы заявок");
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
//        softAssert.assertAll("Ошибка при одобрении или отклонении заявки");
    }


    // Проверка обновление таблицы
    @Test
    public void testRefreshButton() {

        // Сохраняем initial состояние таблицы
        List<List<String>> initialTableData = am.getTableData();

        // Выполняем обновление
        am.clickRefreshButton();

        // Получаем данные после обновления
        List<List<String>> refreshedTableData = am.getTableData();
//

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

//        softAssert.assertAll();
    }


    // Проверка отображения и активности кнопок закрытия и обнволения
    @Test
    public void testButtonsVisibilityAndEnabled() {
//        SoftAssert softAssert = new SoftAssert();

        // Проверка кнопки обновления
        WebElement refreshButton = am.getRefreshButton();
        softAssert.assertTrue(refreshButton.isDisplayed(), "Кнопка обновления должна быть видима");
        softAssert.assertTrue(refreshButton.isEnabled(), "Кнопка обновления должна быть активна");

        // Проверка кнопки закрытия
        WebElement closeButton = am.getRefreshButton();
        softAssert.assertTrue(closeButton.isDisplayed(), "Кнопка закрытия должна быть видима");
        softAssert.assertTrue(closeButton.isEnabled(), "Кнопка закрытия должна быть активна");

//        softAssert.assertAll();
    }

    // Проверка доступности нажатия на кнопки пагинации и нажатия на них
    @Test
    public void testPaginationDisplayed() {

        int applicantCount = am.getTableData().size();

        if(applicantCount <= 200) {
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

        // Шаг 1: Проверка доступности пагинации
        WebElement nextPageButton = am.checkedPaginationButtonFocus(true);

        softAssert.assertTrue(nextPageButton.isEnabled(), "Кнопка перехода на следующую страницу должна быть активна");

        if(am.getPaginationButton(true).isEnabled()) {

            // Шаг 2: Получение initial данных
            List<List<String>> initialTableData = am.getTableData();
            List<String> initialApplicationNumbers = extractApplicationNumbers(initialTableData);

            // Шаг 3: Переход на следующую страницу
            am.clickPaginationButton(true);

            // Шаг 4: Получение данных следующей страницы
            List<List<String>> nextTableData = am.getTableData();
            List<String> nextApplicationNumbers = extractApplicationNumbers(nextTableData);

            // Шаг 5: Проверки
            softAssert.assertNotNull(nextTableData, "Данные следующей страницы не должны быть null");
            softAssert.assertFalse(nextTableData.isEmpty(), "Данные следующей страницы не должны быть пустыми");
            softAssert.assertNotEquals(initialApplicationNumbers, nextApplicationNumbers, "Номера заявок на страницах должны отличаться"
            );

            // Шаг 6: Проверка доступности пагинации назад
            WebElement backPageButton = am.checkedPaginationButtonFocus(false);
            softAssert.assertTrue(backPageButton.isEnabled(), "Кнопка перехода на предыдущую страницу должна быть активна"
            );

            // Шаг 7: Переход на предыдущую страницу
            am.clickPaginationButton(false);

            // Шаг 8: Получение данных предыдущей страницы
            List<List<String>> backTableData = am.getTableData();
            List<String> backApplicationNumbers = extractApplicationNumbers(backTableData);

            // Шаг 9: Проверки
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
                .map(row -> row.get(0))
                .collect(Collectors.toList());
    }

}


