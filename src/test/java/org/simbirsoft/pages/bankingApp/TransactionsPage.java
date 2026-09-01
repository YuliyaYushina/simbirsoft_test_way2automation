package org.simbirsoft.pages.bankingApp;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.simbirsoft.pages.BasePage;

import java.util.List;

@Getter
public class TransactionsPage extends BasePage {

    @FindBy(xpath = "//tbody/tr")
    private List<WebElement> rows;

    @FindBy(css = "button[ng-click='home()']")
    private WebElement homeButton;

    @FindBy(css = "button[ng-click='reset()']")
    private WebElement resetButton;

    @FindBy(css = "button[ng-click='back()']")
    private WebElement backButton;

    public TransactionsPage(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
        PageFactory.initElements(webDriver, this);
    }

    /**
     * Возвращение на страницу Banking App
     */
    @Step("Возвращение на страницу Banking App")
    public void returnBankingApp() {
        homeButton.click();
        new BankingAppPage(webDriver, webDriverWait);
    }

    /**
     * Очистка списка транзакций
     */
    @Step("Очистка списка транзакций")
    public void clickResetButton() {
        resetButton.click();
    }

    /**
     * Возвращение на страницу Customer
     * @return страница Customer
     */
    @Step("Возвращение на страницу Customer")
    public CustomerPage returnCustomerPage() {
        waitHelper.waitForClickable(backButton).click();
        return new CustomerPage(webDriver, webDriverWait);
    }

    /**
     * Получение списка сумм всех поступлений в списке транзакций
     * @param rows список транзакций на странице
     * @return список сумм всех поступлений в списке транзакций
     */
    @Step("Получение списка сумм всех поступлений в списке транзакций")
    public List<String> getActualAmountsCredit(List<WebElement> rows) {
        List<String> actualAmounts = rows.stream()
                .filter(webElement -> webElement.findElement((By.xpath("./td[3]"))).getText().equals("Credit"))
                .map(row -> row.findElement(By.xpath("./td[2]")).getText())
                .toList();
        return actualAmounts;
    }

    /**
     * Получение списка сумм всех снятий в списке транзакций
     * @param rows список транзакций на странице
     * @return список сумм всех снятий в списке транзакций
     */
    @Step("Получение списка сумм всех снятий в списке транзакций")
    public List<String> getActualAmountsDebit(List<WebElement> rows) {
        List<String> actualAmounts = rows.stream()
                .filter(webElement -> webElement.findElement((By.xpath("./td[3]"))).getText().equals("Debit"))
                .map(row -> row.findElement(By.xpath("./td[2]")).getText())
                .toList();
        return actualAmounts;
    }
}
