package org.simbirsoft.tests;

import com.google.common.util.concurrent.Uninterruptibles;
import org.openqa.selenium.WebElement;
import org.simbirsoft.helper.ParameterProvider;
import org.simbirsoft.pages.bankingApp.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertEquals;

public class CustomerTest extends BaseTest {

    private final String FIRST_NAME = "Ivan";
    private final String LAST_NAME = "Ivanov";
    private final String EMAIL = "ivan_ivanov@yandex.ru";
    private final String PASSWORD = "ivan_ivanov_123";
    private final String POST_CODE = "123456";
    private final String CURRENCY = "Dollar";
    private String INPUT_AMOUNT = "100321";
    String customer = String.format("%s %s", FIRST_NAME, LAST_NAME);

    @BeforeMethod
    public void openUrl() {
        webDriver.get(ParameterProvider.get("banking.app.url"));
    }

    @Test(description = "Проверка Sample Form")
    void checkSampleFormTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);

        SampleFormPage sampleFormPage = bankingAppPage.goToSampleForm();
        waitHelper.waitForVisibility(sampleFormPage.getFirstName());

        //Заполнение полей формы
        sampleFormPage.sendKeys(sampleFormPage.getFirstName(), FIRST_NAME);
        sampleFormPage.sendKeys(sampleFormPage.getLastName(), LAST_NAME);
        sampleFormPage.sendKeys(sampleFormPage.getEmail(), EMAIL);
        sampleFormPage.sendKeys(sampleFormPage.getPassword(), PASSWORD);
        sampleFormPage.selectSportsCheckbox(true);
        sampleFormPage.selectGenderMale();
        String inputAboutYourself = sampleFormPage.writeAboutYourself(sampleFormPage.getCheckboxHobbies());
        sampleFormPage.sendKeys(sampleFormPage.getAboutYourself(), inputAboutYourself);

        sampleFormPage.getRegisterButton().click();
        waitHelper.waitForVisibility(sampleFormPage.getSuccessMessage());

        String expectedSuccessMessage = "User registered successfully!";
        String actualSuccessMessage = sampleFormPage.getSuccessMessage().getText();
        assertEquals("Текст сообщения об успешной регистрации не совпадает",
               actualSuccessMessage, expectedSuccessMessage);
    }

    @Test(description = "Проверка Bank Manager Login")
    void checkBankManagerLoginTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);

        BankManagerLoginPage bankManagerLoginPage = bankingAppPage.goToBankManagerLogin();

        bankManagerLoginPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);

        checkAlert("successfully");

        bankManagerLoginPage.openAccountCustomer(customer, CURRENCY);

        checkAlert("successfully");
    }

    @Test(description = "Проверка успешного пополнения счета")
    void checkSuccessfulDepositTest() throws InterruptedException {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);
        //Создание покупателя и открытие аккаунта
        BankManagerLoginPage bankManagerLoginPage = bankingAppPage.goToBankManagerLogin();
        bankManagerLoginPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);
        checkAlert("successfully");
        bankManagerLoginPage.openAccountCustomer(customer, CURRENCY);
        checkAlert("successfully");
        bankingAppPage = bankManagerLoginPage.returnBankingApp();

        //Вход в созданный аккаунт покупателя
        waitHelper.waitForVisibility(bankingAppPage.getCustomerLogin());
        CustomerLoginPage customerLoginPage = bankingAppPage.goToCustomerLogin();
        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        waitHelper.waitForVisibility(customerPage.getNameUser());
        String expectedGreetingText = "Welcome Ivan Ivanov !!";
        String actualGreetingText = String.format("Welcome %s !!", customerPage.getNameUser().getText());
        assertEquals("Текст приветствия не совпадает", actualGreetingText, expectedGreetingText);

        customerPage.addDepositCustomer(INPUT_AMOUNT);

        //Проверка сообщения об успешном поступлении
        String expectedStatusMessage = "Deposit Successful";
        String actualStatusMessage = waitHelper.waitForVisibility(customerPage.getStatusMessageForDeposit()).getText();
        assertEquals("Тескт сообщения об успешном попоплнении счета не совпадает",
                actualStatusMessage, expectedStatusMessage);

        //Ожидание обновления транзакций в списке
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        TransactionsPage transactionsPage = customerPage.goToTransactions();

        List<WebElement> actualRows = waitHelper.waitForVisibilityOfAllElements(transactionsPage.getRows());
        List<String> actualAmounts = transactionsPage.getActualAmountsCredit(actualRows);

        assertTrue(actualAmounts.contains(INPUT_AMOUNT),
                String.format("Список не содержит пополнение на %s", INPUT_AMOUNT));
    }

    @Test(description = "Проверка неуспешного пополнения счета")
    void checkUnsuccessfulDepositTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);
        //Создание покупателя и открытие аккаунта
        BankManagerLoginPage bankManagerLoginPage = bankingAppPage.goToBankManagerLogin();
        bankManagerLoginPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);
        checkAlert("successfully");
        bankManagerLoginPage.openAccountCustomer(customer, CURRENCY);
        checkAlert("successfully");
        bankingAppPage = bankManagerLoginPage.returnBankingApp();

        //Вход в созданный аккаунт покупателя
        waitHelper.waitForVisibility(bankingAppPage.getCustomerLogin());
        CustomerLoginPage customerLoginPage = bankingAppPage.goToCustomerLogin();
        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        //Успешное пополнение счета
        waitHelper.waitForVisibility(customerPage.getNameUser());
        customerPage.addDepositCustomer(INPUT_AMOUNT);
        TransactionsPage transactions = customerPage.goToTransactions();
        waitHelper.waitForClickable(transactions.getBackButton());
        customerPage = transactions.returnCustomerPage();

        //Попытка пополнения баланса на 0
        waitHelper.waitForVisibility(customerPage.getNameUser());
        customerPage.addDepositCustomer("0");

        WebElement actualStatusMessageForDeposit = customerPage.getStatusMessageForDeposit();
        assertFalse(actualStatusMessageForDeposit.isDisplayed(),
                "Текст сообщения об успешном пополнении счета отобразился");

        //Ожидание обновления транзакций в списке
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        TransactionsPage transactionsPage = customerPage.goToTransactions();

        List<WebElement> actualRows = waitHelper.waitForVisibilityOfAllElements(transactionsPage.getRows());
        List<String> actualAmounts = transactionsPage.getActualAmountsCredit(actualRows);

        assertFalse(actualAmounts.contains("0"), "Пополнение на сумму 0 есть в списке операций");
    }

    @Test(description = "Проверка успешного снятия средств")
    void checkSuccessfulWithdrawlTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);
        //Создание покупателя и открытие аккаунта
        BankManagerLoginPage bankManagerLoginPage = bankingAppPage.goToBankManagerLogin();
        bankManagerLoginPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);
        checkAlert("successfully");
        bankManagerLoginPage.openAccountCustomer(customer, CURRENCY);
        checkAlert("successfully");
        bankingAppPage = bankManagerLoginPage.returnBankingApp();

        //Вход в созданный аккаунт покупателя
        waitHelper.waitForVisibility(bankingAppPage.getCustomerLogin());
        CustomerLoginPage customerLoginPage = bankingAppPage.goToCustomerLogin();
        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        //Успешное пополнение счета
        waitHelper.waitForVisibility(customerPage.getNameUser());
        customerPage.addDepositCustomer(INPUT_AMOUNT);
        TransactionsPage transactions = customerPage.goToTransactions();
        waitHelper.waitForClickable(transactions.getBackButton());
        customerPage = transactions.returnCustomerPage();

        //Проверка баланса и определение суммы снятия
        waitHelper.waitForVisibility(customerPage.getNameUser());
        Integer balance = Integer.parseInt(customerPage.getBalanceValue().getText());
        String randomAmount = customerPage.getRandomAmount(balance);

        //Снятие средств со счета
        customerPage.withDrawAmountCustomer(randomAmount);

        String expectedStatusMessageForWithdraw = "Transaction successful";
        String actualStatusMessageForWithdraw = waitHelper.waitForVisibility(customerPage.getStatusMessageForWithdraw()).getText();
        assertEquals("Тескт сообщения об успешном снятии средств не совпадает",
                expectedStatusMessageForWithdraw, actualStatusMessageForWithdraw);

        //Ожидание обновления транзакций в списке
        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        TransactionsPage transactionsPage = customerPage.goToTransactions();

        List<WebElement> actualRows = waitHelper.waitForVisibilityOfAllElements(transactionsPage.getRows());
        List<String> actualAmounts = transactionsPage.getActualAmountsDebit(actualRows);

        assertTrue(actualAmounts.contains(randomAmount),
                String.format("Список транзакций не содержит снятия на %s", randomAmount));
    }

    @Test(description = "Проверка неуспешного снятия средств")
    void checkUnsuccessfulWithdrawlTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);
        //Создание покупателя и открытие аккаунта
        BankManagerLoginPage bankManagerLoginPage = bankingAppPage.goToBankManagerLogin();
        bankManagerLoginPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);
        checkAlert("successfully");
        bankManagerLoginPage.openAccountCustomer(customer, CURRENCY);
        checkAlert("successfully");
        bankingAppPage = bankManagerLoginPage.returnBankingApp();

        //Вход в созданный аккаунт покупателя
        waitHelper.waitForVisibility(bankingAppPage.getCustomerLogin());
        CustomerLoginPage customerLoginPage = bankingAppPage.goToCustomerLogin();
        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        //Успешное пополнение счета
        waitHelper.waitForVisibility(customerPage.getNameUser());
        customerPage.addDepositCustomer(INPUT_AMOUNT);
        TransactionsPage transactions = customerPage.goToTransactions();
        waitHelper.waitForClickable(transactions.getBackButton());
        customerPage = transactions.returnCustomerPage();

        //Попытка снятия суммы большей, чем баланс покупателя
        waitHelper.waitForVisibility(customerPage.getNameUser());
        String inputAmount = "1000000";
        customerPage.withDrawAmountCustomer(inputAmount);

        String expectedStatusMessage = "Transaction Failed. You can not withdraw amount more than the balance.";
        String actualStatusMessage = waitHelper.waitForVisibility(customerPage.getStatusMessageForWithdraw()).getText();
        assertEquals("Тескт сообщения об ошибке снятии средств не совпадает",
                actualStatusMessage, expectedStatusMessage);

        //Ожидание обновления транзакций в списке
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        TransactionsPage transactionsPage = customerPage.goToTransactions();

        List<WebElement> actualRows = waitHelper.waitForVisibilityOfAllElements(transactionsPage.getRows());
        List<String> actualAmounts = transactionsPage.getActualAmountsDebit(actualRows);

       assertFalse(actualAmounts.contains(inputAmount),
                String.format("Список транзакций содержит снятия на %s", inputAmount));
    }

    @Test(description = "Проверка подсчета баланса")
    void checkBalanceTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);
        //Создание покупателя и открытие аккаунта
        BankManagerLoginPage bankManagerLoginPage = bankingAppPage.goToBankManagerLogin();
        bankManagerLoginPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);
        checkAlert("successfully");
        bankManagerLoginPage.openAccountCustomer(customer, CURRENCY);
        checkAlert("successfully");
        bankingAppPage = bankManagerLoginPage.returnBankingApp();

        //Вход в созданный аккаунт покупателя
        waitHelper.waitForVisibility(bankingAppPage.getCustomerLogin());
        CustomerLoginPage customerLoginPage = bankingAppPage.goToCustomerLogin();
        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        //Успешное пополнение счета
        waitHelper.waitForVisibility(customerPage.getNameUser());
        customerPage.addDepositCustomer(INPUT_AMOUNT);
        TransactionsPage transactions = customerPage.goToTransactions();
        waitHelper.waitForClickable(transactions.getBackButton());
        customerPage = transactions.returnCustomerPage();

        //Проверка баланса и определение суммы снятия
        waitHelper.waitForVisibility(customerPage.getNameUser());
        Integer balance = Integer.parseInt(customerPage.getBalanceValue().getText());
        String randomAmount = customerPage.getRandomAmount(balance);

        //Снятие средств со счета
        customerPage.withDrawAmountCustomer(randomAmount);

        Integer expectedBalance = Integer.parseInt(customerPage.getBalanceValue().getText());

        //Ожидание обновления транзакций в списке
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        TransactionsPage transactionsPage = customerPage.goToTransactions();

        //Расчет баланса по списку транзакций
        List<WebElement> actualRows = waitHelper.waitForVisibilityOfAllElements(transactionsPage.getRows());
        Integer actualDebit = transactionsPage.getActualAmountsDebit(actualRows).stream()
                .mapToInt(Integer::parseInt)
                .sum();
        Integer actualCredit = transactionsPage.getActualAmountsCredit(actualRows).stream()
                .mapToInt(Integer::parseInt)
                .sum();

        Integer actualBalance = actualCredit - actualDebit;

        assertEquals("Баланс в таблице не совпадает с балансов в шапке страницы",
                actualBalance, expectedBalance);
    }

    @Test(description = "Проверка снятия оставшихся средств")
    void checkWithdrawAllBalanceTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);
        //Создание покупателя и открытие аккаунта
        BankManagerLoginPage bankManagerLoginPage = bankingAppPage.goToBankManagerLogin();
        bankManagerLoginPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);
        checkAlert("successfully");
        bankManagerLoginPage.openAccountCustomer(customer, CURRENCY);
        checkAlert("successfully");
        bankingAppPage = bankManagerLoginPage.returnBankingApp();

        //Вход в созданный аккаунт покупателя
        waitHelper.waitForVisibility(bankingAppPage.getCustomerLogin());
        CustomerLoginPage customerLoginPage = bankingAppPage.goToCustomerLogin();
        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        //Успешное пополнение счета
        waitHelper.waitForVisibility(customerPage.getNameUser());
        customerPage.addDepositCustomer(INPUT_AMOUNT);
        TransactionsPage transactions = customerPage.goToTransactions();
        waitHelper.waitForClickable(transactions.getBackButton());
        customerPage = transactions.returnCustomerPage();

        //Проверка баланса и определение суммы снятия
        waitHelper.waitForVisibility(customerPage.getNameUser());
        Integer balance = Integer.parseInt(customerPage.getBalanceValue().getText());
        String randomAmount = customerPage.getRandomAmount(balance);

        //Снятие средств со счета
        customerPage.withDrawAmountCustomer(randomAmount);

        String balanceBeforeWithdraw = customerPage.getBalanceValue().getText();

        //Снятие оставшихся средств
        customerPage.withDrawAmountCustomer(balanceBeforeWithdraw);
        String expectedStatusMessageForWithdraw = "Transaction successful";
        String actualStatusMessageForWithdraw = waitHelper.waitForVisibility(customerPage.getStatusMessageForWithdraw()).getText();
        assertEquals("Тескт сообщения об успешном снятии средств не совпадает",
                expectedStatusMessageForWithdraw, actualStatusMessageForWithdraw);

        String balanceAfterWithdraw = customerPage.getBalanceValue().getText();

        assertEquals("Баланс после снятия оставшихся средст не равен 0",
                balanceAfterWithdraw, "0");
    }

    @Test(description = "Проверка очистки списка транзакций")
    void checkResetListTransactionsTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);
        //Создание покупателя и открытие аккаунта
        BankManagerLoginPage bankManagerLoginPage = bankingAppPage.goToBankManagerLogin();
        bankManagerLoginPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);
        checkAlert("successfully");
        bankManagerLoginPage.openAccountCustomer(customer, CURRENCY);
        checkAlert("successfully");
        bankingAppPage = bankManagerLoginPage.returnBankingApp();

        //Вход в созданный аккаунт покупателя
        waitHelper.waitForVisibility(bankingAppPage.getCustomerLogin());
        CustomerLoginPage customerLoginPage = bankingAppPage.goToCustomerLogin();
        CustomerPage customerPage = customerLoginPage.loginCustomer(customer);

        //Успешное пополнение счета
        waitHelper.waitForVisibility(customerPage.getNameUser());
        customerPage.addDepositCustomer(INPUT_AMOUNT);
        TransactionsPage transactions = customerPage.goToTransactions();
        waitHelper.waitForClickable(transactions.getBackButton());
        customerPage = transactions.returnCustomerPage();

        //Проверка баланса и определение суммы снятия
        waitHelper.waitForVisibility(customerPage.getNameUser());
        Integer balance = Integer.parseInt(customerPage.getBalanceValue().getText());
        String randomAmount = customerPage.getRandomAmount(balance);

        //Снятие средств со счета
        customerPage.withDrawAmountCustomer(randomAmount);

        //Ожидание обновления транзакций в списке
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        TransactionsPage transactionsPage = customerPage.goToTransactions();

        List<WebElement> actualRowsBeforeReset = waitHelper.waitForVisibilityOfAllElements(transactionsPage.getRows());
        int transactionsBeforeReset = actualRowsBeforeReset.size();

        transactionsPage.clickResetButton();

        //Очистка списка транзакций
        waitHelper.waitForTableToBeEmpty(transactionsPage.getRows());
        int transactionsAfterReset = transactionsPage.getRows().size();

        assertEquals("Список транзакций не пуст после нажатия Reset",
                0, transactionsAfterReset);
        assertNotEquals(transactionsBeforeReset, transactionsAfterReset,
                "Количество транзакций не изменилось");

        customerPage = transactionsPage.returnCustomerPage();

        //Проверка баланса
        String actualBalance = waitHelper.waitForVisibility(customerPage.getBalanceValue()).getText();
        assertEquals("Баланс после очистки транзакций должен быть 0",
                actualBalance, "0");
    }

    @Test(description = "Проверка удаления созданного покупателя", priority = 10)
    void checkDeleteCustomerTest() {
        BankingAppPage bankingAppPage = new BankingAppPage(webDriver, webDriverWait);
        //Создание покупателя и открытие аккаунта
        BankManagerLoginPage bankManagerLoginPage = bankingAppPage.goToBankManagerLogin();
        bankManagerLoginPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);
        checkAlert("successfully");
        bankManagerLoginPage.openAccountCustomer(customer, CURRENCY);
        checkAlert("successfully");

        bankManagerLoginPage.clickCustomerButton();

        //Поиск покупателя по имени
        waitHelper.waitForVisibility(bankManagerLoginPage.getSearchCustomer());
        bankManagerLoginPage.sendKeys(bankManagerLoginPage.getSearchCustomer(), FIRST_NAME);

        waitHelper.waitForRowNotToBeEmpty(bankManagerLoginPage.getFirstRowCells());
        String actualFirstName = bankManagerLoginPage.getFirstRowCells().get(0).getText();

        assertEquals("Имя покупателя не совпадает",
                actualFirstName, FIRST_NAME);

        //Удаление покупателя из списка
        WebElement rowToBeDeleted = bankManagerLoginPage.getAllRows().get(0);
        bankManagerLoginPage.clickDeleteButton();

        waitHelper.waitForStalenessOf(rowToBeDeleted);
        bankManagerLoginPage.getSearchCustomer().clear();

        waitHelper.waitForListToPopulate(bankManagerLoginPage.getAllRows());

        List<String> firstNameList = bankManagerLoginPage.getStringFirstNames(bankManagerLoginPage.getAllRows());

        assertFalse(firstNameList.contains(FIRST_NAME),
                "Удаленный покупатель есть в списке!");
    }
}
