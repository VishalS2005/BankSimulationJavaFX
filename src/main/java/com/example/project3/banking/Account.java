package banking;

import util.Date;
import util.List;

import java.text.DecimalFormat;

/**
 * The Account class holds the information regarding a bank account
 * The account number, Profile object of the holder, and the balance are held in each account
 *
 * @author Vishal Saravanan, Yining Chen
 */
public abstract class Account implements Comparable<Account> {

    /**
     * Represents the amount of months in one year.
     */
    public static final int MONTHS_IN_YEAR = 12;

    /**
     * A constant representing the absence of any applicable fees for an account.
     * Used to signify that no fees are applied during certain operations or account types.
     */
    public static final double NO_FEE = 0;

    /**
     * formatted in a readable manner for money
     */
    private static final DecimalFormat df = new DecimalFormat("#,##0.00");

    /**
     * AccountNumber object with information about 9-digit number that represents bank account.
     */
    protected AccountNumber number;

    /**
     * Profile object with information about account holder.
     */
    protected Profile holder;

    /**
     * Amount of money currently in bank account.
     */
    protected double balance;

    /**
     * List of account activities:
     * Deposit or Withdraw.
     */
    protected List<Activity> activities;

    /**
     * Creates an Account object.
     *
     * @param branch 3-digit integer representation of a Branch
     * @param type   2-digit integer representation of the type of Account
     * @param holder Profile object that represents the full name and date of birth of an Account holder
     * @param balance amount of money that is in an Account
     */
    public Account(Branch branch, AccountType type, Profile holder, double balance) {
        this.number = new AccountNumber(branch, type);
        this.holder = holder;
        this.activities = new List<>();
        this.balance = balance;
    }

    /**
     * Retrieves the Profile object representing the account holder.
     *
     * @return the Profile object of the account holder
     */
    public Profile getHolder() {
        return holder;
    }

    /**
     * Retrieves the list of activities associated with this account.
     *
     * @return a List of Activity objects representing the transactions performed on this account
     */
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * Extracts the statement detailing activities of a bank.
     */
    public final void statement() {
        printActivities();
        double interest = interest();
        double fee = fee();
        printInterestFee(interest, fee);
        printBalance(interest, fee);
    }

    /**
     * Adds an activity to the account's record.
     *
     * @param activity the Activity object representing the transaction to be added
     */
    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    /**
     * Has the monthly interest of an Account.
     *
     * @return amount of interest to be paid for an Account
     */
    public abstract double interest();

    /**
     * Has the value of the fee on an Account if there is one.
     *
     * @return amount required to be paid on an Account
     */
    public abstract double fee();

    /**
     * Determines the interest rate applicable to this account.
     *
     * @return the interest rate as a double value
     */
    public abstract double interestRate();

    /**
     * Deducts money from the account and updates the balance.
     *
     * @param amount quantity of money deducted from the account
     * @return true if successfully withdrawn, false otherwise
     */
    public boolean withdraw(double amount) { //to update the balance
        if (this.balance < amount) {
            return false;
        }
        this.balance -= amount;
        Activity activity = new Activity(new Date(), this.getAccountNumber().getBranch(), 'W', amount, false);
        addActivity(activity);
        return true;
    }

    /**
     * Deducts the specified amount from the account balance and records the transaction
     * as an activity in the account's activity log.
     *
     * @param date   the date when the withdrawal takes place
     * @param branch the branch where the withdrawal occurs
     * @param amount the amount to be withdrawn from the account balance
     */
    public void withdraw(Date date, Branch branch, double amount) { //to update the balance
        this.balance -= amount;
        Activity activity = new Activity(date, branch, 'W', amount, true);
        addActivity(activity);
    }

    /**
     * Adds money to the account and updates the balance.
     *
     * @param amount quantity of money added to the account
     */
    public void deposit(double amount) { //to update the balance
        this.balance += amount;
        Activity activity = new Activity(new Date(), this.getAccountNumber().getBranch(), 'D', amount, false);
        addActivity(activity);
    }

    /**
     * Adds money to the account, updates the balance, and records the transaction
     * as a deposit activity in the account's activity log.
     *
     * @param date   the date on which the deposit occurs
     * @param branch the branch where the deposit is made
     * @param amount the amount of money to be deposited into the account
     */
    public void deposit(Date date, Branch branch, double amount) { //to update the balance
        this.balance += amount;
        Activity activity = new Activity(date, branch, 'D', amount, true);
        addActivity(activity);
    }

    /**
     * Returns the AccountNumber object associated with this Account.
     *
     * @return the AccountNumber object of this Account
     */
    public AccountNumber getAccountNumber() {
        return this.number;
    }

    /**
     * Returns a double value that represents the balance of an Account.
     *
     * @return balance of an Account
     */
    public double getBalance() {
        return this.balance;
    }

    /**
     * Returns a 2-digit AccountType object representation of the AccountType.
     * The three types are: Checking(01), Savings(02), and Money_Market(03).
     *
     * @return AccountType of the account
     */
    public AccountType getType() {
        return this.number.getType();
    }

    /**
     * Returns a string that represents the first name in an Account.
     *
     * @return first name of an Account
     */
    public String getFirstName() {
        return this.holder.getFirstName();
    }

    /**
     * Returns a string that represents the last name in an Account.
     *
     * @return last name of an Account
     */
    public String getLastName() {
        return this.holder.getLastName();
    }

    /**
     * Returns a date of birth of Account holder.
     *
     * @return Date object representation of date of birth
     */
    public Date getDateOfBirth() {
        return this.holder.getDateOfBirth();
    }

    /**
     * Compares two accounts by the AccountType parameter.
     *
     * @param other Account being compared to
     * @return 0 if the AccountType of both accounts are the same,
     * -1 if first AccountType is less than second,
     * 1 if first AccountType is greater than second
     */
    public int compareByAccountType(Account other) {
        int cmp = this.number.getType().compareTo(other.number.getType());
        if (cmp != 0) {
            return cmp;
        }
        return this.getAccountNumber().compareTo(other.getAccountNumber());
    }

    /**
     * Compares two accounts by the Branch parameter.
     * First checks if the Accounts are the same county before checking the Branch.
     * Comparison is case-insensitive.
     *
     * @param other Account being compared to
     * @return 0 if the Branch of both accounts are the same,
     * -1 if first Branch is less than second,
     * 1 if first Branch is greater than second
     */
    public int compareByBranch(Account other) {
        //gets the String representation of county based on the AccountNumber of the holder
        String countyA = this.getAccountNumber().getBranch().getCounty();
        String countyB = other.getAccountNumber().getBranch().getCounty();

        int countyComparison = countyA.compareToIgnoreCase(countyB);
        if (countyComparison != 0) {
            return countyComparison;
        }
        //County is the same, so now compare the Branch

        String cityA = this.getAccountNumber().getBranch().name();
        String cityB = other.getAccountNumber().getBranch().name();
        return cityA.compareToIgnoreCase(cityB);
    }

    /**
     * Prints the list of activities associated with this account.
     */
    private void printActivities() {
        if (!this.activities.isEmpty()) {
            System.out.println("\t[Activity]");
        }
        for (Activity activity : this.activities) {
            System.out.println("\t\t" + activity);
        }
    }

    /**
     * Prints the interest and fee amounts for the account in a formatted manner.
     *
     * @param interest the interest amount to be displayed
     * @param fee      the fee amount to be displayed
     */
    private void printInterestFee(double interest, double fee) {
        System.out.println("\t[interest] $" + df.format(interest) + " [Fee] $" + df.format(fee));
    }

    /**
     * Prints the account balance after adding the given interest and deducting the given fee.
     *
     * @param interest the interest amount to be added to the balance
     * @param fee      the fee amount to be deducted from the balance
     */
    private void printBalance(double interest, double fee) {
        System.out.println("\t[Balance] $" + df.format(balance + interest - fee));
    }

    /**
     * Compares the name and date of birth of two accounts.
     * First checks the first name, last name, and date of birth and returns if not equal to 'other'.
     *
     * @param other AccountNumber being compared with
     * @return 0 if AccountNumber are equal,
     * negative integer if first AccountNumber is less than AccountNumber,
     * positive integer if first AccountNumber is greater than AccountNumber
     */
    @Override
    public int compareTo(Account other) {
        int cmp = this.getLastName().compareToIgnoreCase(other.getLastName());
        if (cmp != 0) {
            return cmp;
        }
        cmp = this.getFirstName().compareToIgnoreCase(other.getFirstName());
        if (cmp != 0) {
            return cmp;
        }
        cmp = this.getDateOfBirth().compareTo(other.getDateOfBirth());
        if (cmp != 0) {
            return cmp;
        }
        int aNumber = Integer.parseInt(this.getAccountNumber().toString());
        int bNumber = Integer.parseInt(other.getAccountNumber().toString());
        return aNumber - bNumber;
    }

    /**
     * Converts Account to a string that can be printed.
     *
     * @return a String that contains the Account Number, the Account Holder, and the Account Balance
     */
    @Override
    public String toString() {
        return "Account#[" + this.number + "] Holder[" + this.holder + "] Balance[$" + df.format(this.balance) + "] Branch[" + this.getAccountNumber().getBranch() + "]";
    }

    /**
     * Compares two Account objects for equality.
     *
     * @param obj other Account being checked for equality
     * @return true if they are the same object: Account object is compared using the Account Number
     * false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account) {
            Account other = (Account) obj;
            return other.number.equals(this.number);
        }
        return false;
    }
}
