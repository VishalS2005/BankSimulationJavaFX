package banking;

import util.Date;
import util.List;
import util.Sort;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * The AccountDatabase class holds the information regarding all the bank accounts.
 * A new account is always added to the end of the array. An instance of this class is a growable list
 * with an initial array capacity of 4, and it automatically increases the capacity by 4 whenever it is full.
 * The list does not decrease in capacity.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class AccountDatabase extends List<Account> {

    /**
     * Represents the archive of closed accounts within the AccountDatabase.
     */
    private final Archive archive;

    /**
     * Constructs an empty AccountDatabase object.
     * Initializes a new Archive instance that will store accounts that have been closed.
     */
    public AccountDatabase() {
        super();
        this.archive = new Archive();
    }

    /**
     * Deposits money into an Account which will increase the Account's balance.
     * Searches through the AccountDatabase for the Account before depositing the amount of money into that Account.
     * Does nothing if Account is not found using the number.
     *
     * @param number AccountNumber that identifies the account
     * @param amount value of money that will be deposited
     */
    public void deposit(AccountNumber number, double amount) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getAccountNumber().equals(number)) {
                this.get(i).deposit(amount);
                return;
            }
        }
    }

    /**
     * Checks if money can be withdrawn from an account.
     * If the AccountType is Money Market and the balance falls below 2000, changes the AccountType to a savings account.
     *
     * @param number AccountNumber that identifies the Account
     * @param amount value of money that will be withdrawn
     * @return true if the amount can be withdrawn
     * false otherwise
     */
    public boolean withdraw(AccountNumber number, double amount) {
        int index = find(number);
        if (index == -1) {
            return false;
        }
        Account account = this.get(index);

        return account.withdraw(amount);
    }

    /**
     * Closes the specified account and removes it from the AccountDatabase.
     *
     * @param account   the Account object to be closed
     * @param closeDate the Date on which the account is closed
     */
    public void closeAccount(Account account, Date closeDate) {
        if (account.getAccountNumber().getType() == AccountType.CHECKING) {
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i).getHolder().equals(account.getHolder()) && this.get(i).getAccountNumber().getType() == AccountType.SAVINGS) {
                    ((Savings) this.get(i)).setIsLoyal(false);
                }
            }
        }
        archive.add(account, closeDate);
        this.remove(account);
    }

    /**
     * Finds the index of an account in the AccountDatabase based on the holder's first name,
     * last name, and date of birth. The search is case-insensitive for the names.
     *
     * @param firstName   the first name of the account holder to search for
     * @param lastName    the last name of the account holder to search for
     * @param dateOfBirth the date of birth of the account holder to search for
     * @return the index of the account if found in the AccountDatabase,
     * -1 if the account is not found
     */
    public int find(String firstName, String lastName, Date dateOfBirth) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getFirstName().equalsIgnoreCase(firstName)
                    && this.get(i).getLastName().equalsIgnoreCase(lastName)
                    && this.get(i).getDateOfBirth().equals(dateOfBirth)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if Account is contained in the AccountDatabase and returns index of location in database.
     * Searches for an Account in the AccountDatabase using AccountNumber.
     *
     * @param accountNumber that is being searched for
     * @return index of the Account in the AccountDatabase when found,
     * -1 otherwise
     */
    public int find(AccountNumber accountNumber) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getAccountNumber().equals(accountNumber)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if Account is contained in the AccountDatabase.
     * Searches through the AccountDatabase for an Account using accountNumber.
     *
     * @param accountNumber that is being searched for
     * @return true if Account is found in AccountDatabase
     * false otherwise
     */
    public boolean contains(AccountNumber accountNumber) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getAccountNumber().equals(accountNumber)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if Account is contained in the AccountDatabase.
     * Searches through the AccountDatabase for an Account based on first name,
     * last name, dateOfBirth, and account type.
     *
     * @param firstName   String that is being searched for, comparison is case-insensitive
     * @param lastName    String that is being searched for, comparison is case-insensitive
     * @param dateOfBirth object that is being searched for
     * @param type        AccountType object that is being searched for
     * @return true if Account is found in AccountDatabase
     * false otherwise
     */
    public boolean contains(String firstName, String lastName, Date dateOfBirth, AccountType type) {
        Profile holder = new Profile(firstName, lastName, dateOfBirth);
        return contains(holder, type);
    }

    /**
     * Checks if an Account with the specified holder and account type exists in the AccountDatabase.
     * Iterates through the list of accounts stored in the database.
     *
     * @param holder Profile object representing the account holder to search for
     * @param type   AccountType object representing the type of account to search for
     * @return true if an account with the specified holder and type is found in the database,
     * false otherwise
     */
    public boolean contains(Profile holder, AccountType type) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getAccountNumber().getType().equals(type) && this.get(i).getHolder().equals(holder)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prints all the Accounts in the AccountDatabase from the beginning of AccountDatabase to the end.
     */
    public void print() {
        for (int i = 0; i < this.size(); i++) {
            System.out.println(this.get(i).toString());
        }
        System.out.println("*end of list.\n");
    }

    /**
     * Orders and prints AccountDatabase by the 2-digit String, Branch.
     * Bubble Sort implementation is used to iterate through the array
     * and swap adjacent elements if they are out of order.
     * To print, iterate through AccountDatabase and print County followed by City.
     */
    public void printByBranch() {
        Sort.account(this, 'B');
        String currentCounty = null;
        for (int i = 0; i < this.size(); i++) {
            Account account = this.get(i);
            String county = account.getAccountNumber().getBranch().getCounty();
            if (currentCounty == null || !currentCounty.equals(county)) { // Print county header when encountering a new county
                System.out.println("County: " + county);
                currentCounty = county;
            }
            System.out.println(account);
        }
        System.out.println("*end of list.\n");
    }

    /**
     * Orders and prints AccountDatabase by the name and date of birth of the account.
     * Bubble Sort implementation is used to iterate through the array
     * and swap adjacent elements if they are out of order.
     * Calls the print() method.
     */
    public void printByHolder() {
        Sort.account(this, 'H');
        this.print();
    }

    /**
     * Orders and prints AccountDatabase by the AccountType.
     * Bubble Sort implementation is used to iterate through the array
     * and swap adjacent elements if they are out of order.
     * The compareTo method referenced below compares by AccountNumber.
     * Prints sorted by AccountType.
     */
    public void printByType() {
        Sort.account(this, 'T');

        AccountType currentType = null;
        for (int i = 0; i < this.size(); i++) {
            Account account = this.get(i);
            AccountType accountType = account.getAccountNumber().getType();
            if (currentType == null || !currentType.equals(accountType)) { // Print type header when encountering a new type
                System.out.println("Account Type: " + accountType);
                currentType = accountType;
            }
            System.out.println(account);
        }
        System.out.println("*end of list.\n");
    }

    /**
     * Prints all the Accounts that have been closed and are in the Archive
     */
    public void printArchive() {
        this.archive.print();
    }

    /**
     * Prints the statements of all accounts in the AccountDatabase in a formatted manner.
     * The method iterates through all accounts in the Account
     */
    public void printStatements() {
        int holderCount = 0;
        for (int i = 0; i < this.size(); i++) {
            if (i == 0 || !this.get(i).getHolder().equals(this.get(i - 1).getHolder())) {
                holderCount++;
                System.out.println(holderCount + "." + this.get(i).getHolder());
            }
            System.out.println("\t[Account#] " + this.get(i).getAccountNumber());
            this.get(i).statement();
            System.out.println();
        }
        System.out.println("*end of statements.\n");
    }

    /**
     * Loads account data from the provided file and adds the corresponding Account objects to the database.
     * Each line in the file should represent an account in a comma-separated value format.
     *
     * @param file the File object containing account information to load
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void loadAccounts(File file) throws IOException {
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split(",");
            Account account = TransactionManager.createAccount(parts);
            this.add(account);
        }

    }

    /**
     * Processes activities from a file and applies them to accounts in the AccountDatabase.
     * The method reads a file containing activity information in a comma-separated
     * format and performs operations such as deposits or withdrawals on corresponding accounts.
     *
     * @param file the File object from which activity data is read
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void processActivities(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split(",");
            Date date = TransactionManager.createDate(parts[2]);
            Branch branch = TransactionManager.createBranch(parts[3]);
            char type = parts[0].charAt(0);
            double amount = Double.parseDouble(parts[4]);
            Activity activity = new Activity(date, branch, type, amount, true);
            AccountNumber accountNumber = new AccountNumber(parts[1]);
            int index = find(accountNumber);
            if (index == -1) {
                continue;
            }
            if (type == 'W') {
                this.get(index).withdraw(date, branch, amount);
            } else {
                this.get(index).deposit(date, branch, amount);
            }
            System.out.println(accountNumber + "::" + activity);
        }
    }
}
