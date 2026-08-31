package org.simbirsoft.tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.WebElement;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.main.ContactPage;
import org.simbirsoft.pages.main.MainPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class FailedTest extends BaseTest {
    @BeforeMethod
    public void openUrl() {
        webDriver.get(ParameterProvider.get("base.url"));
    }

    @Test(description = "Проверка отображения основных элементов на главной странице")
    @Story("Падающий тест для демонстрации скриншота")
    @Severity(SeverityLevel.MINOR)
    void failedDisplayedTest() {
        MainPage mainPage = new MainPage(webDriver, webDriverWait);
        mainPage.closeFlyer();

        //Проверка отображения элементов
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertFalse(mainPage.checkDisplayed(mainPage.getHeader()),
                "Header не отображается");
        softAssert.assertFalse(mainPage.checkDisplayed(mainPage.getNavigation()),
                "Навигация не отображается");
        softAssert.assertFalse(mainPage.checkDisplayed(mainPage.getButtonLogin()),
                "Кнопка регистрации не отображается");
        softAssert.assertFalse(mainPage.checkDisplayed(mainPage.getFooter()),
                "Footer не отображается");
        softAssert.assertFalse(mainPage.checkDisplayed(mainPage.getFooterCoursesBlock()),
                "Блок списка курсов не отображается");
        softAssert.assertAll();
    }

    @Test(description = "Проверка отображения списка курсов")
    @Story("Падающий тест для демонстрации скриншота")
    @Severity(SeverityLevel.MINOR)
    void failedCoursesTest() {
        MainPage mainPage = new MainPage(webDriver, webDriverWait);

        //Проверка количества курсов в списке
        SoftAssert softAssert = new SoftAssert();
        final int EXPECTED_COUNT_COURSES = 4;
        int actualCountCourses = mainPage.getListCourses().size();
        softAssert.assertEquals(actualCountCourses, EXPECTED_COUNT_COURSES,
                "Количество курсов не совпадает");

        //Список ожидаемых курсов
        String expectedCourse1 = "Selenium";
        String expectedCourse2 = "Playwright";
        String expectedCourse3 = "Appium";
        String expectedCourse4 = "API Testing";
        String expectedCourse5 = "AI";

        //Получение списка курсов на странице
        List<String> actualListCourses = mainPage.getListCourses().stream()
                .map(WebElement::getText)
                .toList();

        softAssert.assertTrue(actualListCourses.contains(expectedCourse1),
                String.format("Курса %s нет в списке", expectedCourse1));
        softAssert.assertTrue(actualListCourses.contains(expectedCourse2),
                String.format("Курса %s нет в списке", expectedCourse2));
        softAssert.assertTrue(actualListCourses.contains(expectedCourse3),
                String.format("Курса %s нет в списке", expectedCourse3));
        softAssert.assertTrue(actualListCourses.contains(expectedCourse4),
                String.format("Курса %s нет в списке", expectedCourse4));
        softAssert.assertTrue(actualListCourses.contains(expectedCourse5),
                String.format("Курса %s нет в списке", expectedCourse5));
        softAssert.assertAll();
    }
}
