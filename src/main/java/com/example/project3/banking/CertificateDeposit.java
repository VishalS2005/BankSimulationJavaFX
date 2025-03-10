package banking;

import util.Date;

/**
 * Represents a Certificate Deposit account, which is a time-bound savings account
 * with fixed term durations and varying interest rates based on the term selected.
 * Extends the functionality of the `Savings` class, providing additional attributes
 * and behaviors for managing Certificate Deposit-specific terms and rates.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class CertificateDeposit extends Savings {

    /**
     * Represents a term duration of three months for a Certificate Deposit account.
     */
    public static final int THREE_MONTH_TERM = 3;

    /**
     * Represents a fixed term duration of six months for Certificate Deposit accounts.
     */
    public static final int SIX_MONTH_TERM = 6;

    /**
     * Represents a constant value defining a nine-month term period for a certificate of deposit.
     */
    public static final int NINE_MONTH_TERM = 9;

    /**
     * Represents a 12-month term duration constant for a certificate of deposit.
     */
    public static final int TWELVE_MONTH_TERM = 12;

    /**
     * Represents the approximate number of days in a single month.
     */
    private static final double DAYS_IN_MONTHS = 30.0;

    /**
     * Represents the interest rate associated with a three-month term
     */
    private static final double THREE_MONTH_TERM_INTEREST_RATE = 0.03;

    /**
     * Represents the interest rate for a six-month term certificate of deposit.
     */
    private static final double SIX_MONTH_TERM_INTEREST_RATE = 0.0325;

    /**
     * Represents the interest rate for a nine-month term certificate of deposit.
     */
    private static final double NINE_MONTH_TERM_INTEREST_RATE = 0.035;

    /**
     * Represents the fixed annual interest rate for a twelve-month term Certificate of Deposit.
     */
    private static final double TWELVE_MONTH_TERM_INTEREST_RATE = 0.04;

    /**
     * Represents the term duration of the Certificate Deposit in months.
     */
    private int term;

    /**
     * Represents the date when the Certificate Deposit account was opened.
     */
    private Date open;

    /**
     * Constructs a CertificateDeposit object, which represents a time-bound deposit account
     * with a specified term, opening date, and initial balance.
     *
     * @param branch  the branch where the certificate deposit is opened
     * @param type    the type of account, typically Certificate Deposit
     * @param holder  the profile of the account holder
     * @param term    the term duration of the certificate deposit in months
     * @param open    the date when the certificate deposit account is opened
     * @param balance the initial balance of the certificate deposit
     */
    public CertificateDeposit(Branch branch, AccountType type, Profile holder, int term, Date open, double balance) {
        super(branch, type, holder, balance);
        this.term = term;
        this.open = open;
        this.isLoyal = false;
    }

    /**
     * Retrieves the opening date of the certificate deposit account.
     *
     * @return the opening date of the certificate deposit as a Date object
     */
    public Date getOpen() {
        return open;
    }

    /**
     * Retrieves the term duration of the certificate deposit.
     *
     * @return the term duration of the certificate deposit in months
     */
    public int getTerm() {
        return term;
    }

    /**
     * Calculates the interest rate applicable for a Certificate Deposit based on the
     * duration in months between the opening date and the provided closing date.
     *
     * @param closeDate the date when the certificate deposit is closed
     * @return the applicable interest rate as a double value
     */
    public double interestRate(Date closeDate) {
        int daysHeld = closeDate.daysFrom(open);
        double monthsHeld = daysHeld / DAYS_IN_MONTHS;

        if (monthsHeld <= SIX_MONTH_TERM) {
            return THREE_MONTH_TERM_INTEREST_RATE;
        } else if (monthsHeld <= NINE_MONTH_TERM) {
            return SIX_MONTH_TERM_INTEREST_RATE;
        } else if (monthsHeld < TWELVE_MONTH_TERM) {
            return NINE_MONTH_TERM_INTEREST_RATE;
        } else {
            return TWELVE_MONTH_TERM_INTEREST_RATE;
        }
    }

    /**
     * Determines the interest rate applicable for the certificate deposit based on the term duration.
     *
     * @return the applicable interest rate as a double
     */
    @Override
    public double interestRate() {
        return switch (term) {
            case THREE_MONTH_TERM -> THREE_MONTH_TERM_INTEREST_RATE;
            case SIX_MONTH_TERM -> SIX_MONTH_TERM_INTEREST_RATE;
            case NINE_MONTH_TERM -> NINE_MONTH_TERM_INTEREST_RATE;
            case TWELVE_MONTH_TERM -> TWELVE_MONTH_TERM_INTEREST_RATE;
            default -> throw new IllegalArgumentException("Invalid term");
        };
    }

    /**
     * Calculates the monthly interest for the Certificate Deposit account based on the current balance and
     * the applicable annual interest rate.
     *
     * @return the calculated monthly interest as a double
     */
    @Override
    public double interest() {
        return balance * interestRate() / MONTHS_IN_YEAR;
    }

    /**
     * Provides a string representation of the CertificateDeposit object, including details
     * about the term duration, the opening date, and the maturity date.
     *
     * @return a string representation of the CertificateDeposit object in the format:
     * superclass string representation followed by term duration, opening date,
     * and maturity date.
     */
    @Override
    public String toString() {
        return super.toString()
                + " Term[" + term + "]"
                + " Date opened[" + open + "]"
                + " Maturity date[" + open.addMonths(term) + "]";
    }
}
