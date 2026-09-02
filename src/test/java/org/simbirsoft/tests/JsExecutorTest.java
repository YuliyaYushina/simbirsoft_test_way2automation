package org.simbirsoft.tests;

import io.qameta.allure.*;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.authorization.AuthorizationPage;
import org.simbirsoft.pages.main.MainPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Epic("Вспомогательные тесты для JavaScriptExecutor")
@Feature("Проверка работы JavaScriptExecutor")
public class JsExecutorTest extends BaseTest {

    @BeforeMethod
    public void openUrl() {
        webDriver.get(ParameterProvider.get("base.url"));
    }

    @Test(description = "Проверка снятия фокуса с элемента и наличия скролла на странице")
    @Story("Сценарий JavaScriptExecutor")
    @Severity(SeverityLevel.NORMAL)
    void checkJavaScriptExecutorTest() {
        MainPage mainPage = new MainPage(webDriver, webDriverWait);
        mainPage.closeFlyer();

        //Проверка скролла на главной странице
        boolean isScrollOnMain = mainPage.isVerticalScrollPresent();
        assertTrue(isScrollOnMain, "На главной странице отсутствует вертикальный скролл");

        webDriver.get(ParameterProvider.get("authorization.url"));
        AuthorizationPage authorizationPage = new AuthorizationPage(webDriver, webDriverWait);

        //Проверка фокуса элемента на странице авторизации
        waitHelper.waitForVisibility(authorizationPage.getUsername());
        authorizationPage.getUsername().click();
        assertTrue(authorizationPage.isElementFocused(authorizationPage.getUsername()),
                "Поле ввода Username должно быть в фокусе после клика");

        authorizationPage.removeFocus(authorizationPage.getUsername());

        assertFalse(authorizationPage.isElementFocused(authorizationPage.getUsername()),
                "Поле ввода Username не должно быть в фокусе");
    }
}
