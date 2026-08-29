package org.simbirsoft.pages.bankingApp;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

public class CustomerLoginPage extends BasePage {

    @FindBy(id = "userSelect")
    private WebElement userSelect;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    public CustomerLoginPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Выбор покупателя и переход на страницу Customer
     * @param customer Имя и фамилия покупателя
     * @return страница Customer
     */
    @Step("Выбор покупателя и переход на страницу Customer")
    public CustomerPage loginCustomer(String customer) {
        waitHelper.waitForVisibility(userSelect);
        Select select = new Select(userSelect);
        select.selectByVisibleText(customer);

        waitHelper.waitForClickable(loginButton).click();
        return new CustomerPage(webDriver, webDriverWait);
    }
}
