package UnitTesting;

import banking.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import util.Date;

/**
 * The AccountDatabaseTest class is a test suite for validating the behavior of the AccountDatabase class.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class AccountDatabaseTest {

    /**
     * The AccountDatabase instance used for testing account-related operations.
     */
    AccountDatabase db;

    /**
     * Represents a Savings account that can be used in the context of the AccountDatabaseTest.
     */
    Savings accountReg;

    /**
     * Represents a non-loyal Money Market account instance used in tests.
     */
    MoneyMarket accountMoneyMarketNonLoyal;

    /**
     * Represents a MoneyMarket account with loyalty status for testing purposes within the AccountDatabaseTest class.
     */
    MoneyMarket accountMoneyMarketLoyal;

    /**
     * Creates AccountDatabase object along with a Savings and MoneyMarket object to populate it
     */
    @Before
    public void setUp() {
        db = new AccountDatabase();
        accountReg = new Savings(Branch.EDISON,
                AccountType.SAVINGS,
                new Profile("John", "Doe", new Date(1, 1, 2000)),
                1000.0);
        accountMoneyMarketNonLoyal = new MoneyMarket(Branch.EDISON,
                AccountType.MONEY_MARKET,
                new Profile("John", "Doe", new Date(1, 1, 2000)),
                4900.0);
        accountMoneyMarketLoyal = new MoneyMarket(Branch.EDISON,
                AccountType.MONEY_MARKET,
                new Profile("John", "Doe", new Date(1, 1, 2000)),
                5100.0);
        db.add(accountReg);
        db.add(accountMoneyMarketNonLoyal);
        db.add(accountMoneyMarketLoyal);
    }

    /**
     * Test case #1:
     * Tests the case where $100 is deposited into a Savings account.
     */
    @Test
    public void testDeposit_SavingsAccount() {
        db.deposit(accountReg.getAccountNumber(), 100);
        assertEquals(accountReg.getBalance(), 1100.0, 0.01);
    }

    /**
     * Test case #2:
     * Tests the case where $200 is deposited into a MoneyMarket account and the loyalty status is changed.
     */
    @Test
    public void testDeposit_MoneyMarketAccount() {
        assertEquals(accountMoneyMarketNonLoyal.isLoyal(), false);
        db.deposit(accountMoneyMarketNonLoyal.getAccountNumber(), 200);
        assertEquals(accountMoneyMarketNonLoyal.getBalance(), 5100.0, 0.01);
        assertEquals(accountMoneyMarketNonLoyal.isLoyal(), true);
    }

    /**
     * Test case #3:
     * Tests the case where $50 is withdrawn from an Account.
     */
    @Test
    public void testWithdrawalValid() {
        boolean isWithdrawn = db.withdraw(accountReg.getAccountNumber(), 50.0);
        assertTrue(isWithdrawn);
        assertEquals(accountReg.getBalance(), 950.0, 0.01);
    }

    /**
     * Test case #4:
     * Tests the case where $20000 is withdrawn from an Account but there is not enough money in the Account.
     */
    @Test
    public void testWithdrawalInvalid() {
        boolean isWithdrawn = db.withdraw(accountReg.getAccountNumber(), 20000.0);
        assertFalse(isWithdrawn);
        assertEquals(accountReg.getBalance(), 1000.0, 0.01);
    }

    /**
     * Test case #5:
     * Tests the case where $200 is withdrawn from an Account and the customer loses loyalty status.
     */
    @Test
    public void testWithdrawal_MoneyMarketAccount() {
        assertEquals(accountMoneyMarketLoyal.isLoyal(), true);
        db.withdraw(accountMoneyMarketLoyal.getAccountNumber(), 200);
        assertEquals(accountMoneyMarketLoyal.getBalance(), 4900.0, 0.01);
        assertEquals(accountMoneyMarketLoyal.isLoyal(), false);
    }
}
