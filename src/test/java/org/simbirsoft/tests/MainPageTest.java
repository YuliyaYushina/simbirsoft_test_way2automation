package org.simbirsoft.tests;

import org.openqa.selenium.WebElement;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.main.ContactPage;
import org.simbirsoft.pages.main.LifetimeMembershipPage;
import org.simbirsoft.pages.main.MainPage;
import org.simbirsoft.tests.base.BaseMethodTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static org.testng.AssertJUnit.assertTrue;

public class MainPageTest extends BaseMethodTest {

    private final String EXPECTED_EMAIL = "trainer@way2automation.com";
    private final String EXPECTED_PHONE = "+91 9711111558";
    private final String EXPECTED_WEBSITE = "www.way2automation.com";

    @BeforeMethod
    public void openUrl() {
        webDriver.get(ParameterProvider.get("base.url"));
    }

    @Test(description = "Проверка открытия страницы и отображения основных элементов")
    void openMainPageTest() {
        MainPage mainPage = new MainPage(webDriver, webDriverWait);
        mainPage.closeFlyer();

        //Проверка отображения элементов
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(mainPage.checkDisplayed(mainPage.getHeader()),
                "Header не отображается");
        softAssert.assertTrue(mainPage.checkDisplayed(mainPage.getNavigation()),
                "Навигация не отображается");
        softAssert.assertTrue(mainPage.checkDisplayed(mainPage.getButtonLogin()),
                "Кнопка регистрации не отображается");

        mainPage.scrollToElement(mainPage.getFooter());

        softAssert.assertTrue(mainPage.checkDisplayed(mainPage.getFooter()),
                "Footer не отображается");
        softAssert.assertTrue(mainPage.checkDisplayed(mainPage.getFooterCoursesBlock()),
                "Блок списка курсов не отображается");
        softAssert.assertAll();
    }

    @Test(description = "Проверка перехода на страницу Contact из блока навигации и отображения email, телефона и вебсайта")
    void checkContactHeaderTest() {
        MainPage mainPage = new MainPage(webDriver, webDriverWait);
        mainPage.closeFlyer();

        ContactPage contactPage = mainPage.selectContactHeader();

        //Проверка контактов
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(contactPage.getActualEmail(contactPage.getEmail()), EXPECTED_EMAIL,
                "Email не совпадает");
        softAssert.assertEquals(contactPage.getActualPhone(contactPage.getPhone()), EXPECTED_PHONE,
                "Номер телефона не сопадает");
        softAssert.assertEquals(contactPage.getActualWebsite(contactPage.getWebsite()), EXPECTED_WEBSITE,
                "Адрес вебсайта не совпадает");
        softAssert.assertAll();

    }

    @Test(description = "Проверка отображения списка курсов в футере")
    void checkListCoursesTest() {
        MainPage mainPage = new MainPage(webDriver, webDriverWait);
        mainPage.closeFlyer();

        mainPage.scrollToElement(mainPage.getFooter());
        mainPage.checkDisplayed(mainPage.getFooter());

        //Проверка количества курсов в списке
        SoftAssert softAssert = new SoftAssert();
        final int EXPECTED_COUNT_COURSES = 5;
        softAssert.assertEquals(mainPage.getListCourses().size(), EXPECTED_COUNT_COURSES,
                "Количество курсов не совпадает");

        //Список ожидаемых курсов
        String expectedCourse1 = "Selenium";
        String expectedCourse2 = "Playwright";
        String expectedCourse3 = "Appium";
        String expectedCourse4 = "API Testing";
        String expectedCourse5 = "AI Testing";

        //Получение списка курсов на странице
        List <String> actualListCourses = mainPage.getListCourses().stream()
                .map(WebElement::getText)
                .toList();

        softAssert.assertTrue(actualListCourses.contains(expectedCourse1),
                "Курса " +  expectedCourse1 +" нет в списке");
        softAssert.assertTrue(actualListCourses.contains(expectedCourse2),
                "Курса " + expectedCourse2 +" нет в списке");
        softAssert.assertTrue(actualListCourses.contains(expectedCourse3),
                "Курса " + expectedCourse3 +" нет в списке");
        softAssert.assertTrue(actualListCourses.contains(expectedCourse4),
                "Курса " + expectedCourse4 + " нет в списке");
        softAssert.assertTrue(actualListCourses.contains(expectedCourse5),
                "Курса " + expectedCourse5 + " нет в списке");
        softAssert.assertAll();
    }

    @Test(description = "Проверка перехода на страницу Contact из футера и отображение email, телефона и веб-сайта")
    void checkContactFooterTest() {
        MainPage mainPage = new MainPage(webDriver, webDriverWait);
        mainPage.closeFlyer();

        mainPage.scrollToElement(mainPage.getFooter());

        ContactPage contactPage = mainPage.selectContactFooter();

        //Проверка контактов
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(contactPage.getActualEmail(contactPage.getEmail()), EXPECTED_EMAIL,
                "Email не совпадает");
        softAssert.assertEquals(contactPage.getActualPhone(contactPage.getPhone()), EXPECTED_PHONE,
                "Номер телефона не сопадает");
        softAssert.assertEquals(contactPage.getActualWebsite(contactPage.getWebsite()), EXPECTED_WEBSITE,
                "Адрес вебсайта не совпадает");
        softAssert.assertAll();
    }

    @Test(description = "Проверка отображения меню навигации при прокрутке страницы")
    void checkNavigationAfterScrollTest() {
        MainPage mainPage = new MainPage(webDriver, webDriverWait);
        mainPage.closeFlyer();

        mainPage.scrollToBottom();

        //Проверка меню навигации
        assertTrue("Меню навигации не отображается при прокрутке страницы",
                mainPage.checkDisplayed(mainPage.getNavigation()));
    }

    @Test(description = "Проверка перехода на страницу Lifetime Membership из блока навигации")
    void checkLifetimeMembershipNavigationTest() {
        MainPage mainPage = new MainPage(webDriver, webDriverWait);
        mainPage.closeFlyer();

        LifetimeMembershipPage lifetimeMembershipPage = mainPage.selectLifetimeMembershipPage();

        lifetimeMembershipPage.getWaitHelper().waitForUrlContains("lifetime-membership-club");

        //Проверка ссылки на страницу
        SoftAssert softAssert = new SoftAssert();
        String actualUrl = webDriver.getCurrentUrl();
        final String EXPECTED_URL = "https://www.way2automation.com/lifetime-membership-club/";
        softAssert.assertEquals(actualUrl, EXPECTED_URL, "Адрес не сопадает");

        //Проверка заголовка
        String expectedHeaderPage = "The Lifetime Membership Club";
        softAssert.assertEquals(lifetimeMembershipPage.getHeaderPage().getText(), expectedHeaderPage, "Заголовок не совпадает");
        softAssert.assertAll();
    }
}
