package org.simbirsoft.tests;

import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.windows.WindowsPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Epic("Интерактивные элементы на странице")
@Feature("Работа с окнами и вкладками")
public class WindowsAndTabsTest extends BaseTest {

    @BeforeMethod
    public void openUrl() {
        getDriver().get(ParameterProvider.get("windows.url"));
    }

    @Test(description = "Проверка открытия нескольких вкладок и переключения фокуса между ними")
    @Story("Переключение между вкладками")
    @Severity(SeverityLevel.NORMAL)
    void checkTabsTest() {
        WindowsPage windowsPage = new WindowsPage(getDriver(), webDriverWait);

        windowsPage.switchToDemoFrame();

        windowsPage.clickNewBrowserTab();

        windowsPage.goToWindow(1);

        windowsPage.clickClickLinkOnTab();

        int expectedTotalTabs = 3;
        int actualTotalTabs = getDriver().getWindowHandles().size();
        assertEquals(actualTotalTabs, expectedTotalTabs, "Общее количество открытых вкладок не равно 3");
    }
}
