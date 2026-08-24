package org.simbirsoft.pages.bankingApp;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

import java.util.List;

@Getter
public class BankManagerLoginPage extends BasePage {

    @FindBy(css = "button[ng-click='addCust()']")
    private WebElement addCustomer;

    @FindBy(css = "input[ng-model='fName']")
    private WebElement firstName;

    @FindBy(css = "input[ng-model='lName']")
    private WebElement lastName;

    @FindBy(css = "input[ng-model='postCd']")
    private WebElement postCode;

    @FindBy(css = "button.btn-default[type='submit']")
    private WebElement addCustomerButton;

    @FindBy(css = "button[ng-click='openAccount()']")
    private WebElement openAccount;

    @FindBy(id = "userSelect")
    private WebElement customerSelect;

    @FindBy(id = "currency")
    private WebElement currency;

    @FindBy(css = "button[type='submit']")
    private WebElement process;

    @FindBy(css = "button[ng-click='home()']")
    private WebElement homeButton;

    @FindBy(css = "button[ng-click='showCust()']")
    private WebElement customerButton;

    @FindBy(css = "input[ng-model='searchCustomer']")
    private WebElement searchCustomer;

    @FindBy(xpath = "//table/tbody/tr[1]/td")
    private List<WebElement> firstRowCells;

    @FindBy(css = "button[ng-click='deleteCust(cust)']")
    private WebElement deleteButton;

    @FindBy(xpath = "//table/tbody/tr")
    private List<WebElement> allRows;

    public BankManagerLoginPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Выбор раздела Добавление покупателя
     */
    public void selectAddCustomer() {
        waitHelper.waitForClickable(addCustomer).click();
    }

    /**
     * Нажатие на кнопку Добавление покупателя
     */
    public void selectAddCustomerButton() {
        addCustomerButton.click();
    }

    /**
     * Выбор раздела Открытие аккаунта покупателя
     */
    public void selectOpenAccount() {
        openAccount.click();
    }

    /**
     * Выбор добаленного покупателя
     */
    public void selectCustomerSelect(String customer) {
        Select select = new Select(customerSelect);
        select.selectByVisibleText(customer);
    }

    /**
     * Выбор валюты
     * @param currency валюта
     */
    public void selectCurrency(String currency) {
        Select select = new Select(this.currency);
        select.selectByValue(currency);
    }

    /**
     * Нажатие на кнопку Process
     */
    public void selectProcess() {
        process.click();
    }

    /**
     * Возврат на страницу Banking App
     */
    public void selectHomeButton() {
        homeButton.click();
        new BankingAppPage(webDriver, webDriverWait);
    }

    /**
     * Выбор категории Покупатели
     */
    public void selectCustomerButton() {
        customerButton.click();
    }

    /**
     * Удаление покупателя
     */
    public void selectDeleteButton() {
        deleteButton.click();
    }

    /**
     * Получение списка имен из списка
     * @param allRows список покупателей
     * @return список имен покупателей
     */
    public List<String> getStringFirstNames(List<WebElement> allRows) {
        List<String> firstNamesList = allRows.stream()
                .map(element -> element.findElement(By.xpath("./td[1]")).getText())
                .toList();
        return firstNamesList;
    }
}
