package org.simbirsoft.pages.bankingApp;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

import java.util.Random;

@Getter
public class CustomerPage extends BasePage {

    @FindBy(css = ".fontBig")
    private WebElement nameUser;

    @FindBy(css = "button[ng-click='deposit()']")
    private WebElement deposit;

    @FindBy(css = "input[ng-model='amount']")
    private WebElement amount;

    @FindBy(css = "button[type='submit']")
    private WebElement depositButton;

    @FindBy(css = "span[ng-show='message']")
    private WebElement statusMessageForDeposit;

    @FindBy(css = "button[ng-click='transactions()']")
    private WebElement transactionButton;

    @FindBy(xpath = "//div[@class='center']/strong[2]")
    private WebElement balanceValue;

    @FindBy(css = "button[ng-click='withdrawl()']")
    private WebElement withdrawl;

    @FindBy(css = "input[ng-model='amount']")
    private WebElement inputAmountForWithdrawl;

    @FindBy(css = "button[type='submit']")
    private WebElement withdrawButton;

    @FindBy(css = "span[ng-show='message']")
    private WebElement statusMessageForWithdraw;

    @FindBy(css = "button[ng-click='home()']")
    private WebElement homeButton;

    public CustomerPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Выбор раздела Депозит
     */
    public void selectDeposit() {
        deposit.click();
    }

    /**
     * Подтверждение депозита
     */
    public void selectDepositAmount() {
        depositButton.click();
    }

    /**
     * Переход в раздел Транзакции
     * @return страница Транзакции
     */
    public TransactionsPage selectTransactions() {
        transactionButton.click();
        return new TransactionsPage(webDriver, webDriverWait);
    }

    /**
     * Выбор раздела Вывод средст
     */
    public void selectWithdrawl() {
        withdrawl.click();
    }

    /**
     * Получение случайного числа в диапозоне от 1 до баланса покупателя
     * @param balance баланс покупателя
     * @return случайное число
     */
    public String randomAmount(Integer balance) {
        Random random = new Random();
        Integer randomAmount = random.nextInt(1, balance);
        return randomAmount.toString();
    }

    /**
     * Потверждение вывода средств
     */
    public void selectWithdrawButton() {
        withdrawButton.click();
    }

    /**
     * Возвращение на страницу Banking App
     */
    public void selectHomeButton() {
        homeButton.click();
        new BankingAppPage(webDriver, webDriverWait);
    }
}
