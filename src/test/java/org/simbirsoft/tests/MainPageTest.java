package org.simbirsoft.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.WebElement;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.main.ContactPage;
import org.simbirsoft.pages.main.LifetimeMembershipPage;
import org.simbirsoft.pages.main.MainPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static org.testng.AssertJUnit.assertTrue;

@Epic("Главная страница сайта")
@Feature("Проверка содержания главной страницы")
public class MainPageTest extends BaseTest {

    private final String EXPECTED_EMAIL = "trainer@way2automation.com";
    private final String EXPECTED_PHONE = "+91 9711111558";
    private final String EXPECTED_WEBSITE = "www.way2automation.com";

    @BeforeMethod
    public void openUrl() {
        getDriver().get(ParameterProvider.get("base.url"));
    }

    @Test(description = "Проверка открытия страницы и отображения основных элементов")
    @Story("Проверка отображения основных элементов на главной странице")
    @Severity(SeverityLevel.BLOCKER)
    void openMainPageTest() {
        MainPage mainPage = new MainPage(getDriver(), webDriverWait);
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
    @Story("Проверка отображения основных элементов на главной странице")
    @Severity(SeverityLevel.MINOR)
    void checkContactHeaderTest() {
        MainPage mainPage = new MainPage(getDriver(), webDriverWait);
        mainPage.closeFlyer();

        ContactPage contactPage = mainPage.goToContactHeader();

        //Получение контактов со страницы
        String actualEmail = contactPage.getActualEmail();
        String actualPhone = contactPage.getActualPhone();
        String actualWebsite = contactPage.getActualWebsite();

        //Проверка контактов
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualEmail, EXPECTED_EMAIL, "Email не совпадает");
        softAssert.assertEquals(actualPhone, EXPECTED_PHONE, "Номер телефона не сопадает");
        softAssert.assertEquals(actualWebsite, EXPECTED_WEBSITE, "Адрес вебсайта не совпадает");
        softAssert.assertAll();

    }

    @Test(description = "Проверка отображения списка курсов в футере")
    @Story("Проверка отображения основных элементов на главной странице")
    @Severity(SeverityLevel.MINOR)
    void checkListCoursesTest() {
        MainPage mainPage = new MainPage(getDriver(), webDriverWait);
        mainPage.closeFlyer();

        mainPage.scrollToElement(mainPage.getFooter());
        mainPage.checkDisplayed(mainPage.getFooter());

        //Проверка количества курсов в списке
        SoftAssert softAssert = new SoftAssert();
        final int EXPECTED_COUNT_COURSES = 5;
        int actualCountCourses = mainPage.getListCourses().size();
        softAssert.assertEquals(actualCountCourses, EXPECTED_COUNT_COURSES,
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

    @Test(description = "Проверка перехода на страницу Contact из футера и отображение email, телефона и веб-сайта")
    @Story("Проверка отображения основных элементов на главной странице")
    @Severity(SeverityLevel.MINOR)
    void checkContactFooterTest() {
        MainPage mainPage = new MainPage(getDriver(), webDriverWait);
        mainPage.closeFlyer();

        mainPage.scrollToElement(mainPage.getFooter());

        ContactPage contactPage = mainPage.goToContactFooter();

        //Получение контактов со страницы
        String actualEmail = contactPage.getActualEmail();
        String actualPhone = contactPage.getActualPhone();
        String actualWebsite = contactPage.getActualWebsite();

        //Проверка контактов
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualEmail, EXPECTED_EMAIL, "Email не совпадает");
        softAssert.assertEquals(actualPhone, EXPECTED_PHONE, "Номер телефона не сопадает");
        softAssert.assertEquals(actualWebsite, EXPECTED_WEBSITE, "Адрес вебсайта не совпадает");
        softAssert.assertAll();
    }

    @Test(description = "Проверка отображения меню навигации при прокрутке страницы")
    @Story("Проверка отображения основных элементов на главной странице")
    @Severity(SeverityLevel.MINOR)
    void checkNavigationAfterScrollTest() {
        MainPage mainPage = new MainPage(getDriver(), webDriverWait);
        mainPage.closeFlyer();

        mainPage.scrollToBottom();

        //Проверка меню навигации
        assertTrue("Меню навигации не отображается при прокрутке страницы",
                mainPage.checkDisplayed(mainPage.getNavigation()));
    }

    @Test(description = "Проверка перехода на страницу Lifetime Membership из блока навигации")
    @Story("Проверка отображения основных элементов на главной странице")
    @Severity(SeverityLevel.NORMAL)
    void checkLifetimeMembershipNavigationTest() {
        MainPage mainPage = new MainPage(getDriver(), webDriverWait);
        mainPage.closeFlyer();

        LifetimeMembershipPage lifetimeMembershipPage = mainPage.goToLifetimeMembershipPage();

        waitHelper.waitForUrlContains("lifetime-membership-club");

        //Проверка ссылки на страницу
        SoftAssert softAssert = new SoftAssert();
        String actualUrl = getDriver().getCurrentUrl();
        final String EXPECTED_URL = "https://www.way2automation.com/lifetime-membership-club/";
        softAssert.assertEquals(actualUrl, EXPECTED_URL, "Адрес не сопадает");

        //Проверка заголовка
        String expectedHeaderPage = "The Lifetime Membership Club";
        String actualHeaderPage = lifetimeMembershipPage.getHeaderPage().getText();
        softAssert.assertEquals(actualHeaderPage, expectedHeaderPage, "Заголовок не совпадает");
        softAssert.assertAll();
    }
}
