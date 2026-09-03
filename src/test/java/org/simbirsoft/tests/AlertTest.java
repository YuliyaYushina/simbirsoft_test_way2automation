package org.simbirsoft.tests;

import io.qameta.allure.*;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.alert.AlertPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Epic("нтерактивные элементы на странице")
@Feature("Работа с алертом")
public class AlertTest extends BaseTest {

    @BeforeMethod
    public void openUrl() {
        getDriver().get(ParameterProvider.get("alert.url"));
    }

    @Test(description = "Ввод теста через алерт и проверка отображения вводимого текста")
    @Story("Проверка работы алерта")
    @Severity(SeverityLevel.NORMAL)
    void checkAlertTest() {
        AlertPage alertPage = new AlertPage(getDriver(), webDriverWait);

        alertPage.clickInputAlert();

        alertPage.switchToDemoFrame();

        alertPage.clickInputBox();

        String expectedInputText = "Yuliya Yushina";
        alertPage.inputTextInAlert(expectedInputText);

        String expectedText = String.format("Hello %s! How are you today?", expectedInputText);
        String actualText = alertPage.getTextDemo();
        assertEquals(actualText, expectedText, "Текст из алерта не применился");
    }

}
