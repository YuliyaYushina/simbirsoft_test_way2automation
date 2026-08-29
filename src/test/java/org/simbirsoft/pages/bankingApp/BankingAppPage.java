package org.simbirsoft.pages.bankingApp;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

@Getter
public class BankingAppPage extends BasePage {

    @FindBy(linkText = "Sample Form")
    private WebElement sampleForm;

    @FindBy(css = "button[ng-click='manager()']")
    private WebElement bankManagerLogin;

    @FindBy(css = "button[ng-click='customer()']")
    private WebElement customerLogin;

    public BankingAppPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Переход в раздел Sample Form
     * @return страница Sample Form
     */
    public SampleFormPage goToSampleForm() {
        waitHelper.waitForClickable(sampleForm).click();
        return new SampleFormPage(webDriver, webDriverWait);
    }

    /**
     * Переход в раздел Bank Manager Login
     * @return страница Bank Manager Login
     */
    public BankManagerLoginPage goToBankManagerLogin() {
        waitHelper.waitForClickable(bankManagerLogin).click();
        return new BankManagerLoginPage(webDriver, webDriverWait);
    }

    /**
     * Переход в раздел Customer Login
     * @return страница Customer Login
     */
    public CustomerLoginPage goToCustomerLogin() {
        waitHelper.waitForClickable(customerLogin).click();
        return new CustomerLoginPage(webDriver, webDriverWait);
    }
}
