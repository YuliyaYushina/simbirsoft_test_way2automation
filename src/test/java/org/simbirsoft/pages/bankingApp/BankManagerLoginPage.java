package org.simbirsoft.pages.bankingApp;

import io.qameta.allure.Step;
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
    @Step("Выбор раздела Добавление покупателя")
    public void clickAddCustomer() {
        waitHelper.waitForClickable(addCustomer).click();
    }

    /**
     * Нажатие на кнопку Добавление покупателя
     */
    @Step("Нажатие на кнопку Добавление покупателя")
    public void clickAddCustomerButton() {
        addCustomerButton.click();
    }

    /**
     * Выбор раздела Открытие аккаунта покупателя
     */
    @Step("Выбор раздела Открытие аккаунта покупателя")
    public void clickOpenAccount() {
        openAccount.click();
    }

    /**
     * Выбор добаленного покупателя
     */
    @Step("Выбор покупателя")
    public void selectCustomerSelect(String customer) {
        Select select = new Select(customerSelect);
        select.selectByVisibleText(customer);
    }

    /**
     * Выбор валюты
     * @param currency валюта
     */
    @Step("Выбор валюты")
    public void selectCurrency(String currency) {
        Select select = new Select(this.currency);
        select.selectByValue(currency);
    }

    /**
     * Нажатие на кнопку Process
     */
    @Step("Нажатие на кнопку Process")
    public void clickProcess() {
        process.click();
    }

    /**
     * Возврат на страницу Banking App
     */
    @Step("Возврат на страницу Banking App")
    public BankingAppPage returnBankingApp() {
        homeButton.click();
        return new BankingAppPage(webDriver, webDriverWait);
    }

    /**
     * Выбор категории Покупатели
     */
    @Step("Выбор категории Customer")
    public void clickCustomerButton() {
        customerButton.click();
    }

    /**
     * Удаление покупателя
     */
    @Step("Удаление покупателя")
    public void clickDeleteButton() {
        deleteButton.click();
    }

    /**
     * Получение списка имен из списка покупателей
     * @param allRows список покупателей
     * @return список имен покупателей
     */
    @Step("Получение списка имен из списка покупателей")
    public List<String> getStringFirstNames(List<WebElement> allRows) {
        List<String> firstNamesList = allRows.stream()
                .map(element -> element.findElement(By.xpath("./td[1]")).getText())
                .toList();
        return firstNamesList;
    }

    /**
     * Добавление нового покупателя
     * @param FIRST_NAME имя покупателя
     * @param LAST_NAME фамилия покупателя
     * @param POST_CODE индекс покупателя
     */
    @Step("Добавление нового покупателя")
    public void addCustomer(String FIRST_NAME, String LAST_NAME, String POST_CODE) {
        clickAddCustomer();

        //Заполнение полей
        waitHelper.waitForVisibility(firstName);
        sendKeys(firstName, FIRST_NAME);
        sendKeys(lastName, LAST_NAME);
        sendKeys(postCode, POST_CODE);

        clickAddCustomerButton();
    }

    /**
     * Открытие аккаунта покупателя
     * @param customer имя и фамилия покупателя
     */
    @Step("Открытие аккаунта покупателя")
    public void openAccountCustomer(String customer, String currency) {
        clickOpenAccount();
        waitHelper.waitForVisibility(customerSelect);

        selectCustomerSelect(customer);
        selectCurrency(currency);
        clickProcess();
    }
}
