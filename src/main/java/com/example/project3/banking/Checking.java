package banking;

/**
 * The Checking class represents a type of bank account with a specific interest rate,
 * fee structure, and conditions related to balance thresholds.
 * It extends the Account class and provides implementations for calculating interest
 * and account fees specific to Checking accounts.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class Checking extends Account {

    /**
     * A constant representing the annual interest rate applicable to the Checking account type.
     */
    private static final double INTEREST_RATE = 0.015;

    /**
     * Represents the balance threshold above which no account fee is charged.
     */
    private static final double FEE_THRESHOLD = 1000;

    /**
     * A constant representing the fixed account fee charged for the Checking account
     * if the balance does not meet the specified threshold.
     */
    private static final double ACCOUNT_FEE = 15;

    /**
     * Constructs a new Checking account with the specified branch, account type, account holder,
     * and initial balance.
     *
     * @param branch  The branch location where the account is held.
     * @param type    The type of account being created (e.g., Checking).
     * @param holder  The profile of the account holder.
     * @param balance The initial balance of the account.
     */
    public Checking(Branch branch, AccountType type, Profile holder, double balance) {
        super(branch, type, holder, balance);
    }

    /**
     * Retrieves the annual interest rate applicable to the Checking account type.
     *
     * @return the annual interest rate as a double value
     */
    @Override
    public double interestRate() {
        return INTEREST_RATE;
    }

    /**
     * Calculates the monthly interest for the account based on the annual interest rate
     * and the current balance.
     *
     * @return the monthly interest amount as a double value
     */
    @Override
    public double interest() {
        return this.interestRate() * balance / MONTHS_IN_YEAR;
    }

    /**
     * Calculates the applicable fee for the Checking account based on the account balance.
     * If the balance meets or exceeds the fee threshold, no fee is charged; otherwise, a fixed account fee applies.
     *
     * @return the account fee as a double value, either no fee or the fixed account fee
     */
    @Override
    public double fee() {
        return this.balance >= FEE_THRESHOLD ? NO_FEE : ACCOUNT_FEE;
    }
}
