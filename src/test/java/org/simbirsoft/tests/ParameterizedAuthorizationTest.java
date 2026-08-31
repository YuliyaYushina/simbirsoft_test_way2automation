package org.simbirsoft.tests;

import io.qameta.allure.*;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.authorization.AuthorizationPage;
import org.simbirsoft.pages.authorization.LoggedInPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Epic("Страница авторизации сайта")
@Feature("Параметризированнная проверка авторизации")
public class ParameterizedAuthorizationTest extends BaseTest {
    @BeforeMethod
    public void openUrl() {
        webDriver.get(ParameterProvider.get("authorization.url"));
    }

    @DataProvider(name = "authSuccessDataProvider")
    public Object[][] authSuccessDataProvider() {
        return new Object[][] {
                {ParameterProvider.get("username"), ParameterProvider.get("password"), ParameterProvider.get("username"),
                        "You're logged in!!"},
                {ParameterProvider.get("username"), ParameterProvider.get("password"), ParameterProvider.get("incorrect.username"),
                        "You're logged in!!"}
        };
    }

    @DataProvider(name = "authErrorDataProvider")
    public Object[][] authErrorDataProvider() {
        return new Object[][] {
                {ParameterProvider.get("username"), ParameterProvider.get("incorrect.password"), ParameterProvider.get("username"),
                        "Username or password is incorrect"},
                {ParameterProvider.get("incorrect.username"), ParameterProvider.get("password"), ParameterProvider.get("username"),
                        "Username or password is incorrect"},
                {ParameterProvider.get("incorrect.username"), ParameterProvider.get("incorrect.password"), ParameterProvider.get("username"),
                        "Username or password is incorrect"},
                {ParameterProvider.get("username"), ParameterProvider.get("incorrect.password"), ParameterProvider.get("incorrect.username"),
                        "Username or password is incorrect"},
                {ParameterProvider.get("incorrect.username"), ParameterProvider.get("password"), ParameterProvider.get("incorrect.username"),
                        "Username or password is incorrect"},
                {ParameterProvider.get("incorrect.username"), ParameterProvider.get("incorrect.password"), ParameterProvider.get("incorrect.username"),
                        "Username or password is incorrect"}
        };
    }

    @Test(dataProvider = "authSuccessDataProvider", description = "Проверка успешной авторизации с разными наборами данных")
    @Story("Проверка авторизации")
    @Severity(SeverityLevel.CRITICAL)
    void authSuccessTest(String username, String password, String username2, String expectedMessage) {
        AuthorizationPage authorizationPage = new AuthorizationPage(webDriver, webDriverWait);
        waitHelper.waitForVisibility(authorizationPage.getUsername());
        authorizationPage.auth(username, password, username2);

        LoggedInPage loggedInPage = authorizationPage.goToLoggedIn();
        waitHelper.waitForVisibility(loggedInPage.getLoggedIn());
        String actualLogIn = loggedInPage.getLoggedIn().getText();
        assertEquals(actualLogIn, expectedMessage, "Текст сообщения об успешном входе не совпадает!");
    }

    @Test(dataProvider = "authErrorDataProvider", description = "Проверка ошибки авторизации с разными наборами данных")
    @Story("Проверка авторизации")
    @Severity(SeverityLevel.CRITICAL)
    void authErrorTest(String username, String password, String username2, String expectedMessage) {
        AuthorizationPage authorizationPage = new AuthorizationPage(webDriver, webDriverWait);
        waitHelper.waitForVisibility(authorizationPage.getUsername());
        authorizationPage.auth(username, password, username2);

        authorizationPage.getLoginButton().click();
        waitHelper.waitForVisibility(authorizationPage.getErrorMessage());
        String actualTextMessage = authorizationPage.getErrorMessage().getText();
        assertEquals(actualTextMessage, expectedMessage, "Текст ошибки не совпадает!");
    }
}
