package banking;

import util.Date;

/**
 * Represents a Money Market account, which is a specialized type of Savings account
 * that offers distinct features such as higher interest rates, specific fee structures,
 * and withdrawal limitations. The MoneyMarket account also includes a loyalty status
 * based on the account balance, which influences the applicable interest rate.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class MoneyMarket extends Savings {

    /**
     * Represents the maximum number of allowed withdrawals in a statement cycle
     * without incurring additional fees for a Money Market account.
     */
    private static final double WITHDRAWAL_THRESHOLD = 3;

    /**
     * Represents the fixed fee applied to withdrawals that exceed the allowed threshold
     * in a Money Market account's statement cycle.
     */
    private static final double WITHDRAWAL_FEE = 10;

    /**
     * Represents the fixed account maintenance fee applied to a Money Market account.
     */
    private static final double ACCOUNT_FEE = 25;

    /**
     * Represents the minimum balance required in a Money Market account
     * to achieve loyalty status. Loyalty status can influence aspects
     * such as interest rates and account benefits.
     */
    private static final double LOYALTY_THRESHOLD = 5000;

    /**
     * Represents the minimum account balance required to avoid incurring an
     * account maintenance fee in a Money Market account.
     */
    private static final double FEE_THRESHOLD = 2000;

    /**
     * Represents the annual interest rate applied to a Money Market account
     * when the account holder qualifies for loyalty status.
     */
    private static final double LOYAL_INTEREST_RATE = 0.0375;

    /**
     * Represents the annual interest rate applied to a Money Market account when
     * the account holder does not meet the criteria for loyalty status.
     * It is a lower interest rate compared to the one provided for loyal accounts.
     */
    private static final double NOT_LOYAL_INTEREST_RATE = 0.035;

    /**
     * Number of withdrawals in the current statement cycle.
     */
    private int withdrawal = 0;

    /**
     * Constructs a MoneyMarket account with the specified branch, account type, holder details, and initial balance.
     * If the initial balance is greater than or equal to the loyalty threshold, the account is marked as loyal.
     *
     * @param branch  the branch where the account is opened
     * @param type    the type of the account
     * @param holder  the profile of the account holder
     * @param balance the initial balance of the account
     */
    public MoneyMarket(Branch branch, AccountType type, Profile holder, double balance) {
        super(branch, type, holder, balance);
        if (balance >= LOYALTY_THRESHOLD) {
            this.isLoyal = true;
        }
    }

    /**
     * Calculates the interest rate for the MoneyMarket account based on loyalty status.
     *
     * @return the interest rate applicable for the account. If the account is marked
     */
    @Override
    public double interestRate() {
        return isLoyal ? LOYAL_INTEREST_RATE : NOT_LOYAL_INTEREST_RATE;
    }

    /**
     * Calculates the fee associated with the MoneyMarket account.
     * The fee is determined based on the account balance and the number of withdrawals.
     * If the account balance meets or exceeds a specified threshold,
     * a standard account fee is not applied; otherwise, a fee is charged.
     * Additional withdrawal fees may apply if the number of withdrawals exceeds the withdrawal threshold.
     *
     * @return the total fee amount as a double, combining account and withdrawal fees if applicable
     */
    @Override
    public double fee() {
        return (this.balance >= FEE_THRESHOLD ? NO_FEE : ACCOUNT_FEE) + (this.withdrawal > WITHDRAWAL_THRESHOLD ? WITHDRAWAL_FEE : NO_FEE);
    }

    /**
     * Deducts the specified amount from the account balance and updates the loyalty status
     * if the balance falls below the defined loyalty threshold. Additionally, increments
     * the withdrawal count for the account.
     *
     * @param amount the amount to be withdrawn from the account
     */
    @Override
    public boolean withdraw(double amount) {
        boolean success = super.withdraw(amount);

        if (success) {
            withdrawal++;
        }

        if (this.getBalance() < LOYALTY_THRESHOLD) {
            this.setIsLoyal(false);
        }

        return success;
    }

    /**
     * Processes a withdrawal from the MoneyMarket account on the given date, from a specified branch, and for a specified amount.
     * Updates the withdrawal count for the account after the transaction is completed.
     *
     * @param date   the date of the withdrawal transaction
     * @param branch the branch where the withdrawal is being made
     * @param amount the amount to withdraw from the account
     */
    @Override
    public void withdraw(Date date, Branch branch, double amount) {
        super.withdraw(date, branch, amount);
        withdrawal++;
    }

    /**
     * Deposits a specified amount into the MoneyMarket account and updates the loyalty status
     * if the account balance meets or exceeds the loyalty threshold.
     *
     * @param amount the amount of money to be deposited into the account
     */
    @Override
    public void deposit(double amount) {
        super.deposit(amount);
        if (this.getBalance() >= LOYALTY_THRESHOLD) {
            this.setIsLoyal(true);
        }
    }

    /**
     * Returns a string representation of the MoneyMarket object.
     *
     * @return a string representation of the MoneyMarket account including withdrawal count
     */
    @Override
    public String toString() {
        return super.toString() + " Withdrawal[" + withdrawal + "]";
    }
}
