package banking;

/**
 * Extends the account class
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class Savings extends Account {

    /**
     * Represents the interest rate applied to savings accounts for loyal customers.
     */
    private static final double LOYAL_INTEREST_RATE = 0.0275;

    /**
     * Represents the interest rate applied to savings accounts for non-loyal customers.
     */
    private static final double NOT_LOYAL_INTEREST_RATE = 0.025;

    /**
     * Represents the fixed account fee applied to savings accounts.
     */
    private static final double ACCOUNT_FEE = 25;

    /**
     * Represents the minimum account balance threshold required to avoid fees.
     * If the account balance meets or exceeds this value, no fees will be applied.
     */
    private static final double ACCOUNT_THRESHOLD = 500;

    /**
     * Loyal customer status that will result in a higher interest rate.
     */
    protected boolean isLoyal;

    /**
     * Constructs a Savings account object.
     *
     * @param branch  The branch where the account is created.
     * @param type    The type of account being opened, typically AccountType.SAVINGS.
     * @param holder  The profile of the account holder.
     * @param balance The initial balance in the savings account.
     */
    public Savings(Branch branch, AccountType type, Profile holder, double balance) {
        super(branch, type, holder, balance);
        isLoyal = TransactionManager.accountDatabase.contains(holder, AccountType.CHECKING);
    }

    /**
     * Updates the loyalty status of the savings account holder.
     * A loyal customer is eligible for a higher interest rate.
     *
     * @param isLoyal a boolean indicating whether the account holder is a loyal customer
     */
    public void setIsLoyal(boolean isLoyal) {
        this.isLoyal = isLoyal;
    }

    /**
     * Checks and returns the given loyalty status.
     *
     * @return the provided loyalty status as a boolean
     */
    public boolean isLoyal() {
        return isLoyal;
    }

    /**
     * Determines the interest rate applied to the savings account.
     * The interest rate varies based on whether the account holder is loyal or not.
     *
     * @return the interest rate applicable for the account;
     * returns a higher interest rate if the account holder is loyal,
     * otherwise returns the standard interest rate for non-loyal customers
     */
    @Override
    public double interestRate() {
        return isLoyal ? LOYAL_INTEREST_RATE : NOT_LOYAL_INTEREST_RATE;
    }

    /**
     * Calculates and returns the monthly interest accrued on the savings account balance.
     *
     * @return the monthly interest amount based on the account balance and interest rate
     */
    @Override
    public double interest() {
        return balance * this.interestRate() / MONTHS_IN_YEAR;
    }

    /**
     * Calculates the monthly fee for the savings account.
     *
     * @return the fee amount for the savings account; either 0 for accounts meeting the threshold
     * or the predefined account fee for accounts below the threshold
     */
    @Override
    public double fee() {
        return balance >= ACCOUNT_THRESHOLD ? NO_FEE : ACCOUNT_FEE;
    }

    /**
     * Generates a string representation of the Savings account object.
     *
     * @return a string containing a formatted representation of the savings account information,
     * including the loyalty status if applicable.
     */
    @Override
    public String toString() {
        return super.toString() + (isLoyal ? " [LOYAL]" : "");
    }
}
