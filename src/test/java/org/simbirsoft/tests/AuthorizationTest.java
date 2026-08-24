package org.simbirsoft.tests;

import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.authorization.AuthorizationPage;
import org.simbirsoft.pages.authorization.LoggedInPage;
import org.simbirsoft.tests.base.BaseMethodTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertEquals;

public class AuthorizationTest extends BaseMethodTest {

    @BeforeMethod
    public void openUrl() {
        webDriver.get(ParameterProvider.get("authorization.url"));
    }

    @Test(description = "Проверка полей ввода")
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
    void checkSuccessfulAuthorizationTest() {
        AuthorizationPage authorizationPage = new AuthorizationPage(webDriver, webDriverWait);

        //Заполнение формы авторизации
        authorizationPage.getWaitHelper().waitForVisibility(authorizationPage.getUsername());

        authorizationPage.sendKeys(ParameterProvider.get("username"), authorizationPage.getUsername());
        authorizationPage.sendKeys(ParameterProvider.get("password"), authorizationPage.getPassword());
        authorizationPage.sendKeys(ParameterProvider.get("username"), authorizationPage.getUsername2());

        LoggedInPage loggedInPage = authorizationPage.selectLoggedIn();
        String expectedLogIn = "You're logged in!!";

        loggedInPage.getWaitHelper().waitForVisibility(loggedInPage.getLoggedIn());
        String actualLogIn = loggedInPage.getLoggedIn().getText();
        assertEquals(actualLogIn, expectedLogIn,
                "Текст сообщения об успешном входе не совпадает!");
    }

    @Test(description = "Проверка ошибки авторизации")
    void checkExceptionAuthorizationTest() {
        AuthorizationPage authorizationPage = new AuthorizationPage(webDriver, webDriverWait);

        String incorrectUserName = "incorrectuser";
        String incorrectPassword = "incorrectpassword";

        //Заполнение формы авторизации
        authorizationPage.getWaitHelper().waitForVisibility(authorizationPage.getUsername());

        authorizationPage.sendKeys(incorrectUserName, authorizationPage.getUsername());
        authorizationPage.sendKeys(incorrectPassword, authorizationPage.getPassword());
        authorizationPage.sendKeys(incorrectUserName, authorizationPage.getUsername2());

        authorizationPage.getLoginButton().click();

        String expectedTextMessage = "Username or password is incorrect";
        authorizationPage.getWaitHelper().waitForVisibility(authorizationPage.getErrorMessage());
        String actualTextMessage = authorizationPage.getErrorMessage().getText();
        assertEquals(actualTextMessage, expectedTextMessage,
                "Текст ошибки не совпадает!");
    }

    @Test(description = "Проверка успешного разлогирования")
    void checkLogoutTest() {
        AuthorizationPage authorizationPage = new AuthorizationPage(webDriver, webDriverWait);

        //Заполнение формы авторизации
        authorizationPage.getWaitHelper().waitForVisibility(authorizationPage.getUsername());

        authorizationPage.sendKeys(ParameterProvider.get("username"), authorizationPage.getUsername());
        authorizationPage.sendKeys(ParameterProvider.get("password"), authorizationPage.getPassword());
        authorizationPage.sendKeys(ParameterProvider.get("username"), authorizationPage.getUsername2());

        LoggedInPage loggedInPage = authorizationPage.selectLoggedIn();
        AuthorizationPage authorizationPageAfterLogout = loggedInPage.selectLogout();

        //Проверка элементов страницы
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(authorizationPageAfterLogout.checkDisplayed(authorizationPageAfterLogout.getUsername()));
        softAssert.assertTrue(authorizationPageAfterLogout.checkDisplayed(authorizationPageAfterLogout.getPassword()));
        softAssert.assertTrue(authorizationPageAfterLogout.checkDisplayed(authorizationPageAfterLogout.getLoginButton()));
        softAssert.assertFalse(authorizationPageAfterLogout.getLoginButton().isEnabled());
        softAssert.assertAll();
    }
}
