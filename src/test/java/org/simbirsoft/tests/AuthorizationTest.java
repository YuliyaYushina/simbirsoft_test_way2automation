package org.simbirsoft.tests;

import io.qameta.allure.*;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.authorization.AuthorizationPage;
import org.simbirsoft.pages.authorization.LoggedInPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertEquals;

@Epic("Страница авторизации сайта")
@Feature("Проверка авторизации")
public class AuthorizationTest extends BaseTest {

    @BeforeMethod
    public void openUrl() {
        webDriver.get(ParameterProvider.get("authorization.url"));
    }

    @Test(description = "Проверка полей ввода")
    @Story("Проверка полей ввода")
    @Severity(SeverityLevel.NORMAL)
    void checkFieldsAuthorizationTest() {
        AuthorizationPage authorizationPage = new AuthorizationPage(webDriver, webDriverWait);

        //Проверка элементов страницы
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(authorizationPage.checkDisplayed(authorizationPage.getUsername()));
        softAssert.assertTrue(authorizationPage.checkDisplayed(authorizationPage.getPassword()));
        softAssert.assertTrue(authorizationPage.checkDisplayed(authorizationPage.getLoginButton()));
        softAssert.assertFalse(authorizationPage.getLoginButton().isEnabled());
        softAssert.assertAll();
    }

    @Test(description = "Проверка успешной авторизации")
    @Story("Успешная авторизация")
    @Severity(SeverityLevel.CRITICAL)
    void checkSuccessfulAuthorizationTest() {
        AuthorizationPage authorizationPage = new AuthorizationPage(webDriver, webDriverWait);

        //Заполнение формы авторизации
        waitHelper.waitForVisibility(authorizationPage.getUsername());

        authorizationPage.auth(ParameterProvider.get("username"), ParameterProvider.get("password"));

        LoggedInPage loggedInPage = authorizationPage.goToLoggedIn();
        waitHelper.waitForVisibility(loggedInPage.getLoggedIn());

        String expectedLogIn = "You're logged in!!";
        String actualLogIn = loggedInPage.getLoggedIn().getText();
        assertEquals(actualLogIn, expectedLogIn, "Текст сообщения об успешном входе не совпадает!");
    }

    @Test(description = "Проверка ошибки авторизации")
    @Story("Ошибка авторизации")
    @Severity(SeverityLevel.NORMAL)
    void checkExceptionAuthorizationTest() {
        AuthorizationPage authorizationPage = new AuthorizationPage(webDriver, webDriverWait);

        String incorrectUserName = "incorrectuser";
        String incorrectPassword = "incorrectpassword";

        //Заполнение формы авторизации
        authorizationPage.getWaitHelper().waitForVisibility(authorizationPage.getUsername());

        authorizationPage.auth(incorrectUserName, incorrectPassword);

        authorizationPage.getLoginButton().click();
        waitHelper.waitForVisibility(authorizationPage.getErrorMessage());

        String expectedTextMessage = "Username or password is incorrect";
        String actualTextMessage = authorizationPage.getErrorMessage().getText();
        assertEquals(actualTextMessage, expectedTextMessage, "Текст ошибки не совпадает!");
    }

    @Test(description = "Проверка успешного разлогирования")
    @Story("Разлогинивание")
    @Severity(SeverityLevel.CRITICAL)
    void checkLogoutTest() {
        AuthorizationPage authorizationPage = new AuthorizationPage(webDriver, webDriverWait);

        //Заполнение формы авторизации
        authorizationPage.getWaitHelper().waitForVisibility(authorizationPage.getUsername());

        authorizationPage.auth(ParameterProvider.get("username"), ParameterProvider.get("password"));

        LoggedInPage loggedInPage = authorizationPage.goToLoggedIn();
        AuthorizationPage authorizationPageAfterLogout = loggedInPage.clickLogout();

        //Проверка элементов страницы
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(authorizationPageAfterLogout.checkDisplayed(authorizationPageAfterLogout.getUsername()));
        softAssert.assertTrue(authorizationPageAfterLogout.checkDisplayed(authorizationPageAfterLogout.getPassword()));
        softAssert.assertTrue(authorizationPageAfterLogout.checkDisplayed(authorizationPageAfterLogout.getLoginButton()));
        softAssert.assertFalse(authorizationPageAfterLogout.getLoginButton().isEnabled());
        softAssert.assertAll();
    }
}
