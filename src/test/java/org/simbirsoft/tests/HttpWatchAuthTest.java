package org.simbirsoft.tests;

import io.qameta.allure.*;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.http.HttpWatchPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Epic("Страница HTTP Authentication")
@Feature("Базовая аторизация")
public class HttpWatchAuthTest extends BaseTest {

    @BeforeMethod
    public void openUrl() {
        getDriver().get(ParameterProvider.get("basic.auth.url"));
    }

    @Test(description = "Проверка базовой авторизации")
    @Story("Сценарий авторизации")
    @Severity(SeverityLevel.CRITICAL)
    void checkBasicAuthTest() {
        HttpWatchPage httpWatchPage = new HttpWatchPage(getDriver(), webDriverWait);

        httpWatchPage.registerBasicAuth(ParameterProvider.get("login.basic.auth"), ParameterProvider.get("password.basic.auth"));

        httpWatchPage.clickDisplayImageButton();

        waitHelper.waitForVisibility(httpWatchPage.getDownloadImg());
        assertTrue(httpWatchPage.getDownloadImg().isDisplayed(),
                "Authenticated Image не отобразилась, авторизация не прошла");
    }
}
