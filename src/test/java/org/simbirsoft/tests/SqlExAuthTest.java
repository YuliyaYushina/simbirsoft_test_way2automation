package org.simbirsoft.tests;

import io.qameta.allure.*;
import org.simbirsoft.helper.CookieHelper;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.sql.SqlExPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Epic("Страница сайта https://www.sql-ex.ru/")
@Feature("Проверка авторизации")
public class SqlExAuthTest extends BaseTest {

    private final String COOKIES_FILE = "target/sqlex_cookies.data";

    @BeforeMethod
    public void openUrl() {
        getDriver().get(ParameterProvider.get("sql.url"));
    }

    @Test(description = "Проверка авторизации через cookie (два прогона)")
    @Story("Сценарии авторизации")
    @Severity(SeverityLevel.CRITICAL)
    void checkAuthWithCookieTest() {
        SqlExPage sqlExPage = new SqlExPage(getDriver(), webDriverWait);

        //Загрузка сохраненных куков
        CookieHelper.loadCookiesAndRefresh(getDriver(), COOKIES_FILE);

        //Авторизация, если отсутсвует сохраненные куки и сохранение куков
        String login = ParameterProvider.get("login.sql");
        String password = ParameterProvider.get("password.sql");
        sqlExPage.loginIfNeeded(login, password, COOKIES_FILE);

        assertTrue(sqlExPage.isUserLoggedIn(), "Пользователь должен быть авторизован!");
    }
}
