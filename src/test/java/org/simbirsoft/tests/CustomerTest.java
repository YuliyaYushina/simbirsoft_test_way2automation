package org.simbirsoft.tests;

import com.google.common.util.concurrent.Uninterruptibles;
import org.openqa.selenium.WebElement;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.bankingApp.*;
import org.simbirsoft.tests.base.BaseClassTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.AssertJUnit.assertEquals;

public class CustomerTest extends BaseClassTest {

    private final String FIRST_NAME = "Ivan";
    private final String LAST_NAME = "Ivanov";
    private final String EMAIL = "ivan_ivanov@yandex.ru";
    private final String PASSWORD = "ivan_ivanov_123";
    private final String POST_CODE = "123456";
    String customer = String.format("%s %s", FIRST_NAME, LAST_NAME);

    @BeforeClass
    public void openUrl() {
        webDriver.get(ParameterProvider.get("banking.app.url"));
    }

    @Test(description = "Проверка Sample Form", priority = 1)
    void checkSampleFormTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);

        SampleFormPage sampleFormPage = bankingAppPage.selectSampleForm();
        sampleFormPage.getWaitHelper().waitForVisibility(sampleFormPage.getFirstName());

        //Заполнение полей формы
        sampleFormPage.sendKeys(FIRST_NAME, sampleFormPage.getFirstName());
        sampleFormPage.sendKeys(LAST_NAME, sampleFormPage.getLastName());
        sampleFormPage.sendKeys(EMAIL, sampleFormPage.getEmail());
        sampleFormPage.sendKeys(PASSWORD, sampleFormPage.getPassword());
        sampleFormPage.selectSportsCheckbox(true);
        sampleFormPage.selectGenderMale();
        sampleFormPage.getAboutYourself().sendKeys(sampleFormPage.writeAboutYourself(sampleFormPage.getCheckboxHobbies()));

        sampleFormPage.getRegisterButton().click();

        String expectedSuccessMessage = "User registered successfully!";
        sampleFormPage.getWaitHelper().waitForVisibility(sampleFormPage.getSuccessMessage());
        assertEquals("Текст сообщения об успешной регистрации не совпадает",
                sampleFormPage.getSuccessMessage().getText(), expectedSuccessMessage);

        //Возвращение на страницу bankingAppPage
        webDriver.navigate().back();
    }

    @Test(description = "Проверка Bank Manager Login", priority = 2)
    void checkBankManagerLoginTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);

        BankManagerLoginPage bankManagerLoginPage = bankingAppPage.selectBankManagerLogin();

        bankManagerLoginPage.selectAddCustomer();

        //Заполнение полей
        bankManagerLoginPage.getWaitHelper().waitForVisibility(bankManagerLoginPage.getFirstName());
        bankManagerLoginPage.sendKeys(FIRST_NAME, bankManagerLoginPage.getFirstName());
        bankManagerLoginPage.sendKeys(LAST_NAME, bankManagerLoginPage.getLastName());
        bankManagerLoginPage.sendKeys(POST_CODE, bankManagerLoginPage.getPostCode());

        bankManagerLoginPage.selectAddCustomerButton();

        SoftAssert softAssert = new SoftAssert();

        checkAlert(softAssert, "successfully");

        bankManagerLoginPage.selectOpenAccount();
        bankManagerLoginPage.getWaitHelper().waitForVisibility(bankManagerLoginPage.getCustomerSelect());

        bankManagerLoginPage.selectCustomerSelect(customer);
        bankManagerLoginPage.selectCurrency("Dollar");
        bankManagerLoginPage.selectProcess();

        checkAlert(softAssert, "successfully");

        bankManagerLoginPage.selectHomeButton();
        softAssert.assertAll();

    }

    @Test(description = "Проверка успешного пополнения счета", priority = 3)
    void checkSuccessfulDepositTest() throws InterruptedException {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);

        CustomerLoginPage customerLoginPage = bankingAppPage.selectCustomerLogin();

        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        SoftAssert softAssert = new SoftAssert();

        customerPage.getWaitHelper().waitForVisibility(customerPage.getNameUser());
        softAssert.assertEquals("Welcome " + customerPage.getNameUser().getText() + " !!", "Welcome Ivan Ivanov !!", "екст приветствия не совпадает");

        customerPage.selectDeposit();

        String inputAmount = "100321";
        customerPage.getWaitHelper().waitForVisibility(customerPage.getAmount());
        customerPage.sendKeys(inputAmount, customerPage.getAmount());
        customerPage.selectDepositAmount();

        //Проверка сообщения об успешном поступлении
        String actualStatusMessage = customerPage.getWaitHelper().waitForVisibility(customerPage.getStatusMessageForDeposit()).getText();
        softAssert.assertEquals(actualStatusMessage, "Deposit Successful",
                "Тескт сообщения об успешном попоплнении счета не совпадает");

        //Ожидание обновления транзакций в списке
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        TransactionsPage transactionsPage = customerPage.selectTransactions();

        List<WebElement> actualRows = transactionsPage.getWaitHelper().waitForVisibilityOfAllElements(transactionsPage.getRows());
        List<String> actualAmounts = transactionsPage.getActualAmountsCredit(actualRows);

        softAssert.assertTrue(actualAmounts.contains(inputAmount),
                String.format("Список не содержит пополнение на %s", inputAmount));

        transactionsPage.selectHomeButton();
        softAssert.assertAll();
    }

    @Test(description = "Проверка неуспешного пополнения счета", priority = 4)
    void checkUnsuccessfulDepositTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);

        CustomerLoginPage customerLoginPage = bankingAppPage.selectCustomerLogin();

        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        customerPage.selectDeposit();

        customerPage.getWaitHelper().waitForVisibility(customerPage.getAmount());
        customerPage.sendKeys("0", customerPage.getAmount());
        customerPage.selectDepositAmount();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertFalse(customerPage.getStatusMessageForDeposit().isDisplayed(),
                "Текст сообщения об успешном пополнении счета отобразился");

        //Ожидание обновления транзакций в списке
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        TransactionsPage transactionsPage = customerPage.selectTransactions();

        List<WebElement> actualRows = transactionsPage.getWaitHelper().waitForVisibilityOfAllElements(transactionsPage.getRows());
        List<String> actualAmounts = transactionsPage.getActualAmountsCredit(actualRows);

        softAssert.assertFalse(actualAmounts.contains("0"),
                "Пополнение на сумму 0 есть в списке операций");

        transactionsPage.selectHomeButton();
        softAssert.assertAll();
    }

    @Test(description = "Проверка успешного снятия средств", priority = 5)
    void checkSuccessfulWithdrawlTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);

        CustomerLoginPage customerLoginPage = bankingAppPage.selectCustomerLogin();

        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        Integer balance = Integer.parseInt(customerPage.getBalanceValue().getText());
        String randomAmount = customerPage.randomAmount(balance);

        customerPage.selectWithdrawl();

        customerPage.getWaitHelper().waitForVisibility(customerPage.getInputAmountForWithdrawl());
        customerPage.sendKeys(randomAmount, customerPage.getInputAmountForWithdrawl());

        customerPage.selectWithdrawButton();

        SoftAssert softAssert = new SoftAssert();

        String expectedStatusMessageForWithdraw = "Transaction successful";
        String actualStatusMessageForWithdraw = customerPage.getWaitHelper().waitForVisibility(customerPage.getStatusMessageForWithdraw()).getText();
        softAssert.assertEquals(actualStatusMessageForWithdraw, expectedStatusMessageForWithdraw,
                "Тескт сообщения об успешном снятии средств не совпадает");

        //Ожидание обновления транзакций в списке
        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        TransactionsPage transactionsPage = customerPage.selectTransactions();

        List<WebElement> actualRows = transactionsPage.getWaitHelper().waitForVisibilityOfAllElements(transactionsPage.getRows());
        List<String> actualAmounts = transactionsPage.getActualAmountsDebit(actualRows);

        softAssert.assertTrue(actualAmounts.contains(randomAmount),
                String.format("Список транзакций не содержит снятия на %s", randomAmount));

        transactionsPage.selectHomeButton();
        softAssert.assertAll();
    }

    @Test(description = "Проверка неуспешного снятия средств", priority = 6)
    void checkUnsuccessfulWithdrawlTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);

        CustomerLoginPage customerLoginPage = bankingAppPage.selectCustomerLogin();

        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        String inputAmount = "1000000";

        customerPage.selectWithdrawl();

        customerPage.getWaitHelper().waitForVisibility(customerPage.getInputAmountForWithdrawl());
        customerPage.sendKeys(inputAmount, customerPage.getInputAmountForWithdrawl());

        customerPage.selectWithdrawButton();

        SoftAssert softAssert = new SoftAssert();

        String expectedStatusMessage = "Transaction Failed. You can not withdraw amount more than the balance.";
        String actualStatusMessage = customerPage.getWaitHelper().waitForVisibility(customerPage.getStatusMessageForWithdraw()).getText();
        softAssert.assertEquals(actualStatusMessage, expectedStatusMessage,
                "Тескт сообщения об ошибке снятии средств не совпадает");

        //Ожидание обновления транзакций в списке
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        TransactionsPage transactionsPage = customerPage.selectTransactions();

        List<WebElement> actualRows = transactionsPage.getWaitHelper().waitForVisibilityOfAllElements(transactionsPage.getRows());
        List<String> actualAmounts = transactionsPage.getActualAmountsDebit(actualRows);

        softAssert.assertFalse(actualAmounts.contains(inputAmount),
                String.format("Список транзакций содержит снятия на %s", inputAmount));

        transactionsPage.selectHomeButton();
        softAssert.assertAll();
    }

    @Test(description = "Проверка подсчета баланса", priority = 7)
    void checkBalanceTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);

        CustomerLoginPage customerLoginPage = bankingAppPage.selectCustomerLogin();

        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        Integer expectedBalance = Integer.parseInt(customerPage.getBalanceValue().getText());

        TransactionsPage transactionsPage = customerPage.selectTransactions();

        //Расчет баланса по списку транзакций
        List<WebElement> actualRows = transactionsPage.getWaitHelper().waitForVisibilityOfAllElements(transactionsPage.getRows());
        Integer actualDebit = transactionsPage.getActualAmountsDebit(actualRows).stream()
                .mapToInt(Integer::parseInt)
                .sum();
        Integer actualCredit = transactionsPage.getActualAmountsCredit(actualRows).stream()
                .mapToInt(Integer::parseInt)
                .sum();

        Integer actualBalance = actualCredit - actualDebit;

        assertEquals("Баланс в таблице не совпадает с балансов в шапке страницы",
                actualBalance, expectedBalance);
        transactionsPage.selectHomeButton();
    }

    @Test(description = "Проверка снятия оставшихся средств", priority = 8)
    void checkWithdrawAllBalanceTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);

        CustomerLoginPage customerLoginPage = bankingAppPage.selectCustomerLogin();

        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        String balanceBeforeWithdraw = customerPage.getBalanceValue().getText();

        customerPage.selectWithdrawl();

        customerPage.getWaitHelper().waitForVisibility(customerPage.getInputAmountForWithdrawl());
        customerPage.sendKeys(balanceBeforeWithdraw, customerPage.getInputAmountForWithdrawl());

        customerPage.selectWithdrawButton();

        SoftAssert softAssert = new SoftAssert();

        //Снятие всех оставшихся средств на балансе покупателя
        String expectedStatusMessageForWithdraw = "Transaction successful";
        String actualStatusMessageForWithdraw = customerPage.getWaitHelper().waitForVisibility(customerPage.getStatusMessageForWithdraw()).getText();
        softAssert.assertEquals(actualStatusMessageForWithdraw, expectedStatusMessageForWithdraw,
                "Тескт сообщения об успешном снятии средств не совпадает");

        String balanceAfterWithdraw = customerPage.getBalanceValue().getText();

        softAssert.assertEquals(balanceAfterWithdraw, "0",
                "Баланс после снятия оставшихся средст не равен 0");

        customerPage.selectHomeButton();
        softAssert.assertAll();
    }

    @Test(description = "Проверка очистки списка транзакций", priority = 9)
    void checkResetListTransactionsTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);

        CustomerLoginPage customerLoginPage = bankingAppPage.selectCustomerLogin();

        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        TransactionsPage transactionsPage = customerPage.selectTransactions();

        List<WebElement> actualRowsBeforeReset = transactionsPage.getWaitHelper().waitForVisibilityOfAllElements(transactionsPage.getRows());
        int transactionsBeforeReset = actualRowsBeforeReset.size();

        transactionsPage.selectResetButton();

        //Очистка списка транзакций
        transactionsPage.getWaitHelper().waitForTableToBeEmpty(transactionsPage.getRows());
        int transactionsAfterReset = transactionsPage.getRows().size();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(transactionsAfterReset, 0,
                "Список транзакций не пуст после нажатия Reset");
        softAssert.assertNotEquals(transactionsBeforeReset, transactionsAfterReset,
                "Количество транзакций не изменилось");

        CustomerPage customerPageAfterResetTransactions = transactionsPage.selectBackButton();

        //Проверка баланса
        String actualBalance = customerPageAfterResetTransactions.getWaitHelper()
                .waitForVisibility(customerPageAfterResetTransactions.getBalanceValue())
                .getText();
        softAssert.assertEquals(actualBalance, "0",
                "Баланс после очистки транзакций должен быть 0");

        customerPageAfterResetTransactions.selectHomeButton();
        softAssert.assertAll();
    }

    @Test(description = "Проверка удаления созданного покупателя", priority = 10)
    void checkDeleteCustomerTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);

        BankManagerLoginPage bankManagerLoginPage = bankingAppPage.selectBankManagerLogin();

        bankManagerLoginPage.selectCustomerButton();

        //Поиск покупателя по имени
        bankManagerLoginPage.getWaitHelper().waitForVisibility(bankManagerLoginPage.getSearchCustomer());
        bankManagerLoginPage.sendKeys(FIRST_NAME, bankManagerLoginPage.getSearchCustomer());

        SoftAssert softAssert = new SoftAssert();

        bankManagerLoginPage.getWaitHelper().waitForRowNotToBeEmpty(bankManagerLoginPage.getFirstRowCells());
        String actualFirstName = bankManagerLoginPage.getFirstRowCells().get(0).getText();

        softAssert.assertEquals(actualFirstName, FIRST_NAME,
                "Имя покупателя не совпадает");

        //Удаление покупателя из списка
        WebElement rowToBeDeleted = bankManagerLoginPage.getAllRows().get(0);
        bankManagerLoginPage.selectDeleteButton();

        bankManagerLoginPage.getWaitHelper().waitForStalenessOf(rowToBeDeleted);
        bankManagerLoginPage.getSearchCustomer().clear();

        bankManagerLoginPage.getWaitHelper().waitForListToPopulate(bankManagerLoginPage.getAllRows());

        List<String> firstNameList = bankManagerLoginPage.getStringFirstNames(bankManagerLoginPage.getAllRows());

        softAssert.assertFalse(firstNameList.contains(FIRST_NAME),
                "Удаленный покупатель есть в списке!");
        softAssert.assertAll();
    }
}
