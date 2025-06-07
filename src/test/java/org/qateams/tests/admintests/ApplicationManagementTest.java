package org.qateams.tests.admintests;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.qateams.components.admin.ApplicationManagement;
import org.qateams.constansts.ApplicationStatus;
import org.qateams.constansts.ApplicationType;
import org.qateams.tests.BaseTest;
import org.qateams.utils.TestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    // Тест на типы данных в таблице
    @Test
    public void testColumnDataTypes() {
        List<List<String>> table = am.getTableData();

        // Проверка первого столбца (ID) - целое число
        boolean validFirstColumn = table.stream()
                .allMatch(row -> isInteger(row.get(0)));
        softAssert.assertTrue(validFirstColumn, "Первый столбец должен содержать только целые числа");

        // Проверка второго столбца (ID) - целое число
        boolean validSecondColumn = table.stream()
                .allMatch(row -> isInteger(row.get(1)));
        softAssert.assertTrue(validSecondColumn, "Второй столбец должен содержать только целые числа");

        // Проверка третьего столбца (тип заявки) - строка
        boolean validThirdColumn = table.stream()
                .allMatch(row -> isValidApplicationType(row.get(2)));
        softAssert.assertTrue(validThirdColumn, "Третий столбец должен содержать корректные типы заявок");

        // Проверка даты в четвертом столбце на соответствие формату DD.MM.YYYY HH:MM:SS
        table.forEach(row -> {
                    softAssert.assertTrue(
                            isValidCustomDateFormat(row.get(3)),
                            "Значение " + row.get(3) + " не соответствует формату DD.MM.YYYY HH:MM:SS"
                    );
                });

        // Проверка пятого столбца (статус) - допустимые статусы
        boolean validStatusColumn = table.stream()
                .allMatch(row -> isValidStatus(row.get(4)));
        softAssert.assertTrue(validStatusColumn, "Пятый столбец должен содержать корректные статусы");
    }

    // Вспомогательные методы для проверки
    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidApplicationType(String value) {
        List<String> validTypes = ApplicationType.getAllType();
        return validTypes.contains(value);
    }

    private boolean isValidDateTime(String value) {
        try {
            Instant.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidCustomDateFormat(String value) {
        try {
            // Создаем форматтер для формата dd.MM.yyyy HH:mm:ss
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

            // Пытаемся распарсить дату
            LocalDateTime.parse(value, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidStatus(String value) {
        List<String> validStatuses = ApplicationStatus.getAllStatus();
        return validStatuses.contains(value);
    }

    // Проверка сортировки строк по дате
    @Test
    public void testDateSortingFromCurrentToEarliest() {
        // Получаем данные таблицы
        List<List<String>> actualData = am.getTableData();

        // Форматтер для парсинга даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        // Список для хранения Unix-timestamp
        List<Long> unixTimestamps = new ArrayList<>();

        // Парсим даты в Unix-timestamp
        for (List<String> row : actualData) {
            try {
                // Парсим дату из строки
                LocalDateTime dateTime = LocalDateTime.parse(row.get(3), formatter);

                // Конвертируем в Unix-timestamp (секунды)
                long unixTimestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
                unixTimestamps.add(unixTimestamp);
            } catch (DateTimeParseException e) {
                softAssert.fail("Ошибка парсинга даты(дата отсутствует или в неверном формате): " + row.get(3));
            }
        }

        // Проверяем, что список не пустой
        softAssert.assertFalse(unixTimestamps.isEmpty(), "Список дат не должен быть пустым");

        // Проверяем сортировку от текущей даты до самой ранней
        boolean isSortedDescending = isDescendingSorted(unixTimestamps);

        softAssert.assertTrue(
                isSortedDescending,
                "Даты должны быть отсортированы от текущей до самой ранней"
        );
    }

    // Метод для проверки убывающей сортировки
    private boolean isDescendingSorted(List<Long> timestamps) {
        if (timestamps == null || timestamps.size() <= 1) {
            return true;
        }

        for (int i = 0; i < timestamps.size() - 1; i++) {
            // Каждый следующий timestamp должен быть меньше или равен предыдущему
            if (timestamps.get(i) < timestamps.get(i + 1)) {
                return false;
            }
        }

        return true;
    }

    // Проверка цвета текста статуса заявки
    @Test
    public void testApplicationStatusAndColors() {
        List<List<String>> actualData = am.getTableData();

        actualData.forEach(row -> {
            String statusText = row.get(4); // Предполагаем, что статус в 5-м столбце

            // Проверка, что статус существует в enum
            softAssert.assertTrue(
                    ApplicationStatus.getAllStatus().contains(statusText),
                    "Статус '" + statusText + "' не является допустимым"
            );

            // Проверка цвета для каждого статуса
            Optional<String> statusColor = ApplicationStatus.getColorByRussianName(statusText);

            softAssert.assertTrue(
                    statusColor.isPresent(),
                    "Не найден цвет для статуса: " + statusText
            );

            // Дополнительная проверка конкретных цветов
            if (statusColor.isPresent()) {
                switch (statusText) {
                    case "На рассмотрении":
                        softAssert.assertEquals(
                                statusColor.get(),
                                "#1b7eaf",
                                "Неверный цвет для статуса 'На рассмотрении'"
                        );
                        break;
                    case "Одобрена":
                        softAssert.assertEquals(
                                statusColor.get(),
                                "#088e08",
                                "Неверный цвет для статуса 'Одобрена'"
                        );
                        break;
                    case "Отклонена":
                        softAssert.assertEquals(
                                statusColor.get(),
                                "#c33117f2",
                                "Неверный цвет для статуса 'Отклонена'"
                        );
                        break;
                }
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


    // Проверка отображения и активности кнопок закрытия и обновления
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
                .map(List::getFirst)
                .collect(Collectors.toList());
    }

}


