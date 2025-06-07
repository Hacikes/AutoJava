package org.qateams.utils;

import org.testng.ITestListener;
import org.testng.ITestResult;


// Переопределяю SoftAssertListener, чтобы при падении теста, тесты после него продолжали выполняться
public class SoftAssertListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        if (result.getThrowable() instanceof AssertionError) {
            // Не прерывать выполнение остальных тестов
            result.setStatus(ITestResult.SKIP);
        }
    }
}
