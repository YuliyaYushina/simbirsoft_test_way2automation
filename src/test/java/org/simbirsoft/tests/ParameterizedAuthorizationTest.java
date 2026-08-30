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

    @DataProvider(name = "authDataProvider")
    public Object[][] authDataProvider() {
        return new Object[][] {
                {ParameterProvider.get("username"), ParameterProvider.get("password"), ParameterProvider.get("username"),
                        true, "You're logged in!!"},
                {ParameterProvider.get("username"), ParameterProvider.get("incorrect.password"), ParameterProvider.get("username"),
                        false, "Username or password is incorrect"},
                {ParameterProvider.get("username"), ParameterProvider.get("password"), ParameterProvider.get("incorrect.username"),
                        true, "You're logged in!!"},
                {ParameterProvider.get("incorrect.username"), ParameterProvider.get("password"), ParameterProvider.get("username"),
                        false, "Username or password is incorrect"},
                {ParameterProvider.get("incorrect.username"), ParameterProvider.get("incorrect.password"), ParameterProvider.get("username"),
                        false, "Username or password is incorrect"},
                {ParameterProvider.get("username"), ParameterProvider.get("incorrect.password"), ParameterProvider.get("incorrect.username"),
                        false, "Username or password is incorrect"},
                {ParameterProvider.get("incorrect.username"), ParameterProvider.get("password"), ParameterProvider.get("incorrect.username"),
                        false, "Username or password is incorrect"},
                {ParameterProvider.get("incorrect.username"), ParameterProvider.get("incorrect.password"), ParameterProvider.get("incorrect.username"),
                        false, "Username or password is incorrect"}
        };
    }

    @Test(dataProvider = "authDataProvider", description = "Проверка авторизации с разными наборами данных")
    @Story("Проверка авторизации")
    @Severity(SeverityLevel.CRITICAL)
    void checkAuthorizationTest(String username, String password, String username2, boolean isSuccessExpected, String expectedMessage) {
        AuthorizationPage authorizationPage = new AuthorizationPage(webDriver, webDriverWait);
        waitHelper.waitForVisibility(authorizationPage.getUsername());
        authorizationPage.auth(username, password, username2);

        if (isSuccessExpected) {
            LoggedInPage loggedInPage = authorizationPage.goToLoggedIn();
            waitHelper.waitForVisibility(loggedInPage.getLoggedIn());
            String actualLogIn = loggedInPage.getLoggedIn().getText();
            assertEquals(actualLogIn, expectedMessage, "Текст сообщения об успешном входе не совпадает!");
        } else {
            authorizationPage.getLoginButton().click();
            waitHelper.waitForVisibility(authorizationPage.getErrorMessage());
            String actualTextMessage = authorizationPage.getErrorMessage().getText();
            assertEquals(actualTextMessage, expectedMessage, "Текст ошибки не совпадает!");
        }
    }
}
