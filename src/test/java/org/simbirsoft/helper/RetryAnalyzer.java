package org.simbirsoft.helper;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 2;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            System.out.printf("Тест %s упал. Попытка перезапуска %d из %d...%n",
                    iTestResult.getName(), retryCount, MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }
}
