package banking;

import util.Date;
import util.List;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Transaction Manager class that reads in transactions from the command line.
 * Depending on the action, completes transaction or does not execute it.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class TransactionManager {

    /**
     * The Account Database holds all the Accounts.
     */
    public static final AccountDatabase accountDatabase = new AccountDatabase();

    /**
     * Formats numbers for easy readability.
     */
    private static final DecimalFormat df = new DecimalFormat("#,##0.00");

    /**
     * Represents the minimum initial deposit required to open a Money Market account.
     * A Money Market account cannot be created if the provided balance is below this threshold.
     */
    private static final double MONEY_MARKET_MINIMUM = 2000;

    /**
     * Represents the minimum balance required to open or maintain a Certificate of Deposit (CD) account
     */
    private static final double CD_MINIMUM = 1000;

    /**
     * Represents the number of days in a year, typically used for calculations
     * involving annual durations. This value is based on a standard year with
     * 365 days.
     */
    private static final double DAYS_IN_YEAR = 365;

    /**
     * Represents the decimal value for ten percent, used to calculate a percentage or ratio.
     * Typically employed in financial calculations or percentage adjustments throughout the system.
     */
    private static final double TEN_PERCENT = 0.1;

    /**
     * Executes the transaction manager process.
     * This method loads account data from an external file, initializes the system,
     * and begins a command-line interaction for processing transactions.
     * The system continuously listens for user input and processes commands
     * until a termination command ("Q") is received. Commands are interpreted and
     * executed in real-time.
     *
     * @throws IOException if there is an issue accessing or reading the account file.
     */
    public static void run() throws IOException {
        accountDatabase.loadAccounts(new File("accounts.txt"));
        System.out.println("Accounts in \"accounts.txt\" loaded to the database.");
        System.out.println("Transaction Manager is running.");

        Scanner scanner = new Scanner(System.in);
        while (true) { // loop only ends when a "Q" is read
            String command = scanner.nextLine();
            String[] commandArray = command.split("\\s+");
            if (command.trim().isEmpty()) {
                continue;
            }
            if (commandArray[0].equals("Q")) {
                System.out.println("Transaction Manager is terminated. ");
                return;
            }
            processCommand(commandArray);
        }
    }

    /**
     * Creates a Branch object from a provided String representation of the branch.
     * Throws exception if the Branch name provided is invalid.
     *
     * @param branchName String representation of the Branch where the Account was opened
     * @return Branch of the Account
     */
    public static Branch createBranch(String branchName) {
        Branch branch = null;
        try {
            branch = Branch.valueOf(branchName.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println(branchName + " - invalid branch.");
        }
        return branch;
    }

    /**
     * Creates a Date object based on String representation of the date.
     * Date of birth formatted as such: ##/##/####
     *
     * @param date String representation of the date
     * @return Date object that represents the date of birth
     */
    public static Date createDate(String date) {
        String[] dateParts = date.split("/");
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        return new Date(month, day, year);
    }

    /**
     * Creates an Account object based on provided parameters.
     * Delegates to the getAccount method to initialize and return the appropriate Account instance.
     *
     * @param commandArray an array of Strings containing command details, where the account type is specified as the second element
     * @param firstName    the first name of the account holder
     * @param lastName     the last name of the account holder
     * @param dateOfBirth  a Date object representing the birthdate of the account holder
     * @param branch       the Branch object representing the bank branch where the account is opened
     * @param balance      the initial balance for the account
     * @return the created Account object
     */
    public static Account createAccount(String[] commandArray, String firstName, String lastName, Date dateOfBirth, Branch branch, double balance) {
        String accountType = commandArray[1];
        return getAccount(commandArray, firstName, lastName, dateOfBirth, branch, accountType, balance);
    }

    /**
     * Creates a new account using the provided command array.
     *
     * @param commandArray an array of strings containing account details in the following order:
     *                     1. Account type
     *                     2. Branch identifier
     *                     3. First name of the account holder
     *                     4. Last name of the account holder
     *                     5. Date of birth of the account holder (formatted as a string)
     *                     6. Initial deposit amount
     * @return an Account object representing the newly created account
     */
    public static Account createAccount(String[] commandArray) {
        String accountType = commandArray[0];
        Branch branch = createBranch(commandArray[1]); //second input is the Branch
        String firstName = commandArray[2]; //third input is the first name of the holder
        String lastName = commandArray[3]; //fourth input is the last name of the holder
        Date dateOfBirth = createDate(commandArray[4]); //fifth input is the Date of Birth of the holder
        double amount = Double.parseDouble(commandArray[5]);
        return getAccount(commandArray, firstName, lastName, dateOfBirth, branch, accountType, amount);
    }

    /**
     * Processes account activities from the file "activities.txt".
     * <p>
     * This method reads the account activity data contained in the "activities.txt"
     * file and processes it using the accountDatabase's processActivities method.
     * It outputs log messages to the console indicating the start and completion
     * of activity processing.
     *
     * @throws IOException if an I/O error occurs while processing the file.
     */
    private static void processActivities() throws IOException {
        System.out.println("Processing \"activities.txt\"...");
        accountDatabase.processActivities(new File("activities.txt"));
        System.out.println("Account activities in \"activities.txt\" processed.");
    }

    /**
     * Determines which type of print command is needed and prints the Accounts in a certain order.
     * VALID COMMANDS: P, PA ,PB, PH, PT.
     * P --> prints the AccountDatabase
     * PA --> prints the Archive
     * PB --> prints ordered by Branch (county, then city)
     * PH --> prints by holder then AccountNumber
     * PT --> prints by AccountType then AccountNumber
     *
     * @param command 1-2 letter String command to determine what action to take
     */
    private static void printAccounts(String command) {
        switch (command) {
            case "PA" -> accountDatabase.printArchive();
            case "PB" -> {
                System.out.println("\n*List of accounts ordered by branch location (county, city).");
                accountDatabase.printByBranch();
            }
            case "PH" -> {
                System.out.println("\n*List of accounts ordered by account holder and number.");
                accountDatabase.printByHolder();
            }
            case "PT" -> {
                System.out.println("\n*List of accounts ordered by account type and number.");
                accountDatabase.printByType();
            }
            case "PS" -> {
                System.out.println("*Account statements by account holder.");
                accountDatabase.printStatements();
            }
        }
    }

    /**
     * Determines if the command provided is valid.
     * VALID COMMANDS: O, C, D, W, P, PA, PB, PH, PT.
     * Otherwise, will identify that the command was invalid.
     *
     * @param command 1-2 letter String command to determine what action to take
     * @return true if the command is recognized and can be executed
     * false otherwise
     */
    private static boolean isValidCommand(String command) {
        return switch (command) {
            case "O", "C", "D", "W", "PA", "PB", "PH", "PT", "PS", "A" -> true;
            case "P" -> {
                System.out.println("P command is deprecated!");
                yield false;
            }
            default -> {
                System.out.println("Invalid command!");
                yield false;
            }
        };
    }

    /**
     * Executed to open a new Account when the first command is "O".
     * Checks for valid inputs including a valid AccountType, Branch, Date, and balance.
     * Checks for duplicate account, minimum balance, and money market specifications.
     * Adds the opened account to the database.
     *
     * @param commandArray Holds the input that has been extracted and put into a String array
     */
    private static void openAccount(String[] commandArray) {
        AccountType acctType = createAccountType(commandArray[1]); //first input is the AccountType
        if (acctType == null) { return; }
        if ((acctType == AccountType.COLLEGE_CHECKING && commandArray.length != 8) ||
                (acctType == AccountType.CD && commandArray.length != 9) ||
                (acctType != AccountType.COLLEGE_CHECKING && acctType != AccountType.CD && commandArray.length != 7)) {
            System.out.println("Missing data tokens for opening an account.");
            return;
        }
        Branch branch = createBranch(commandArray[2]); //second input is the Branch
        if (branch == null) { return; }
        Date dob = createDate(commandArray[5]); //fifth input is the Date of Birth of the holder
        if (!checkDateOfBirth(acctType, dob)) { return; }
        String firstName = commandArray[3]; //third input is the first name of the holder
        String lastName = commandArray[4]; //fourth input is the last name of the holder
        double balance;
        try {
            balance = Double.parseDouble(commandArray[6]); //sixth input is the balance of the holder when opening
        } catch (NumberFormatException e) {
            System.out.println("For input string: \"" + commandArray[6] + "\" - not a valid amount.");
            return;
        }
        if (acctType != AccountType.CD && accountDatabase.contains(firstName, lastName, dob, acctType)) { //checking for a duplicate account
            System.out.println(firstName + " " + lastName + " already has a " + acctType + " account.");
            return;
        }
        if (!checkBalance(balance, acctType)) { return; }
        if (acctType == AccountType.CD) {
            int value = Integer.parseInt(commandArray[7]);
            if (value != CertificateDeposit.THREE_MONTH_TERM && value != CertificateDeposit.SIX_MONTH_TERM && value != CertificateDeposit.NINE_MONTH_TERM && value != CertificateDeposit.TWELVE_MONTH_TERM) {
                System.out.println(commandArray[7] + " is not a valid term.");
                return;
            }
        }
        Account account = createAccount(commandArray, firstName, lastName, dob, branch, balance);
        accountDatabase.add(account); //adds the Account to the database
        System.out.println(account.getAccountNumber().getType() + " account " + account.getAccountNumber() + " has been opened.");
    }

    /**
     * Checks a Date object to see if it is a valid date.
     * Returns false if not a valid calendar date,
     * if the birthdate provided is after the current date,
     * or if the birthdate provided indicates that the person is 18 years old.
     *
     * @param dob Date object which represents the date of birth
     * @param accountType AccountType object which represents the type of Account
     * @return true if valid date of birth
     * false otherwise
     */
    private static boolean checkDateOfBirth(AccountType accountType, Date dob) {
        if (!dob.isValid()) {
            System.out.println("DOB invalid: " + dob + " not a valid calendar date!");
            return false;
        } else if (dob.isAfterToday()) {
            System.out.println("DOB invalid: " + dob + " cannot be today or a future day.");
            return false;
        } else if (!dob.isEighteen()) {
            System.out.println("Not eligible to open: " + dob + " under 18.");
            return false;
        } else if (accountType == AccountType.COLLEGE_CHECKING && !dob.isOverTwentyFour()) {
            System.out.println("Not eligible to open: " + dob + " over 24.");
            return false;
        }
        return true;
    }

    /**
     * Checks if balance of the Account being opened is valid.
     * A starting balance of 0 or less is invalid.
     * A Money Market Account with less than 2000 is invalid.
     *
     * @param balance  value of money stored in Account being opened
     * @param acctType type of Account being opened
     * @return true if balance is a valid amount
     * false otherwise
     */
    private static boolean checkBalance(double balance, AccountType acctType) {
        if (balance <= 0) { //balance must be more than 0
            System.out.println("Initial deposit cannot be 0 or negative.");
            return false;
        } else if (balance < MONEY_MARKET_MINIMUM && acctType.equals(AccountType.MONEY_MARKET)) { //Money Market account must have at least $2000
            System.out.println("Minimum of $2,000 to open a Money Market account.");
            return false;
        } else if (balance < CD_MINIMUM && acctType.equals(AccountType.CD)) {
            System.out.println("Minimum of $1,000 to open a Certificate Deposit account.");
            return false;
        }
        return true;
    }

    /**
     * Executed to close an exiting Account when the first command is "C".
     * Closes an Account by removing it from the AccountDatabase and adding it to the Archive.
     * Accounts for two different types of formatting in closing an Account:
     * C accountNumber
     * or C firstName, lastName, dateOfBirth
     *
     * @param commandArray Holds the input that has been extracted and put into a String array
     */
    private static void closeAccount(String[] commandArray) {
        Date closeDate = createDate(commandArray[1]);
        if (commandArray.length == 3) {
            closeSingleAccount(new AccountNumber(commandArray[2]), closeDate);
        } else if (commandArray.length == 5) {
            String firstName = commandArray[2];
            String lastName = commandArray[3];
            Date dateOfBirth = createDate(commandArray[4]);
            closeMultipleAccounts(firstName, lastName, dateOfBirth, closeDate);
        } else {
            System.out.println("Missing data for closing an account.");
        }
    }

    /**
     * Closes a single account with the given account number and close date.
     *
     * @param accountNumber the account number of the account to be closed
     * @param closeDate     the date on which the account is being closed
     */
    private static void closeSingleAccount(AccountNumber accountNumber, Date closeDate) {
        if (!accountDatabase.contains(accountNumber)) {
            System.out.println(accountNumber + " account does not exist.");
            return;
        }
        System.out.println("Closing account " + accountNumber);
        int index = accountDatabase.find(accountNumber);
        System.out.print("--");
        printInterest(accountDatabase.get(index), closeDate);
        accountDatabase.closeAccount(accountDatabase.get(index), closeDate);
    }

    /**
     * Closes all accounts associated with a specified account holder and logs the closure process.
     *
     * @param firstName   the first name of the account holder whose accounts are to be closed
     * @param lastName    the last name of the account holder whose accounts are to be closed
     * @param dateOfBirth the date of birth of the account holder
     * @param closeDate   the date on which the accounts are being closed
     */
    private static void closeMultipleAccounts(String firstName, String lastName, Date dateOfBirth, Date closeDate) {
        Profile holder = new Profile(firstName, lastName, dateOfBirth);

        List<Account> accounts = findAllAccounts(holder);
        if (accounts.isEmpty()) {
            System.out.println(firstName + " " + lastName + " " + dateOfBirth + " does not have any accounts in the database.");
        } else {
            System.out.println("Closing accounts for " + firstName + " " + lastName + " " + dateOfBirth);
            for (Account account : accounts) {
                System.out.print("--" + account.getAccountNumber() + " ");
                printInterest(account, closeDate);
            }
            int index = accountDatabase.find(firstName, lastName, dateOfBirth);
            while (index != -1) {
                accountDatabase.closeAccount(accountDatabase.get(index), closeDate);
                index = accountDatabase.find(firstName, lastName, dateOfBirth);
            }
            System.out.println("All accounts for " + firstName + " " + lastName + " " + dateOfBirth + " are closed and moved to archive.");
        }
    }

    /**
     * Retrieves a list of all accounts associated with the specified profile holder.
     *
     * @param holder the profile holder whose accounts need to be retrieved
     * @return a list of accounts associated with the provided profile holder
     */
    private static List<Account> findAllAccounts(Profile holder) {
        List<Account> accounts = new List<>();
        for (int i = 0; i < accountDatabase.size(); i++) {
            if (accountDatabase.get(i).getHolder().equals(holder)) {
                accounts.add(accountDatabase.get(i));
            }
        }
        return accounts;
    }

    /**
     * Prints the interest earned on the given account based on its type, interest rate, balance,
     * and the provided close date. It handles penalties for certificates of deposit if closed early.
     *
     * @param account   The account for which the interest is to be calculated and printed.
     * @param closeDate The date on which the account is being evaluated or closed.
     */
    private static void printInterest(Account account, Date closeDate) {
        double interestRate;
        System.out.print("interest earned: $");

        if (account.getType() == AccountType.CD) {
            CertificateDeposit cd = (CertificateDeposit) account;
            Date openDate = cd.getOpen();
            int daysHeld = closeDate.daysFrom(openDate);
            double interest;
            if (closeDate.isAfter(openDate.addMonths(cd.getTerm()))) {
                interestRate = account.interestRate();
                interest = account.getBalance() * interestRate / DAYS_IN_YEAR * daysHeld;
                System.out.println(df.format(interest));
            } else {
                interestRate = cd.interestRate(closeDate);
                interest = account.getBalance() * interestRate / DAYS_IN_YEAR * daysHeld;
                System.out.println(df.format(interest));
                System.out.println("  [penalty] $" + df.format(TEN_PERCENT * interest));
            }
        } else {
            interestRate = account.interestRate();
            double interest = account.getBalance() * interestRate / DAYS_IN_YEAR * closeDate.getDay();
            System.out.println(df.format(interest));
        }
    }

    /**
     * Executed to deposit money to an existing Account when the first command is "D".
     * Checks for a deposit amount less than or equal to 0.
     * Formatting of input:
     * D accountNumber, depositAmount
     *
     * @param commandArray Holds the input that has been extracted and put into a String array
     */
    private static void depositMoney(String[] commandArray) {
        try {
            double depositAmount = Double.parseDouble(commandArray[2]);
            if (depositAmount <= 0) {
                System.out.println(depositAmount + " - deposit amount cannot be 0 or negative.");
                return;
            }
            AccountNumber accountNumber = new AccountNumber(commandArray[1]);
            if (!accountDatabase.contains(accountNumber)) {
                System.out.println(accountNumber + " does not exist.");
                return;
            }
            accountDatabase.deposit(accountNumber, depositAmount);
            System.out.println("$" + df.format(depositAmount) + " deposited to " + accountNumber);
        } catch (NumberFormatException e) {
            System.out.println("For input string: \"" + commandArray[2] + "\" - not a valid amount.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Missing data tokens for the deposit.");
        }
    }

    /**
     * Executed to deposit money to an existing Account when the first command is "W".
     * Checks for a withdrawal amount less than or equal to 0.
     * Additionally, checks if the Account has sufficient funds or needs to be downgraded from
     * a money market account to a savings account.
     * Formatting of input:
     * W accountNumber, withdrawalAmount
     *
     * @param commandArray Holds the input that has been extracted and put into a String array
     */
    private static void withdrawMoney(String[] commandArray) {
        try {
            double withdrawalAmount = Double.parseDouble(commandArray[2]);
            if (withdrawalAmount <= 0) {
                System.out.println(withdrawalAmount + " withdrawal amount cannot be 0 or negative.");
                return;
            }
            AccountNumber accountNumber = new AccountNumber(commandArray[1]);
            int index = accountDatabase.find(accountNumber);
            if (index == -1) {
                System.out.println(accountNumber + " does not exist.");
                return;
            }
            boolean sufficientFunds = accountDatabase.get(index).getBalance() >= withdrawalAmount;
            if (accountDatabase.get(index).getBalance() - withdrawalAmount < MONEY_MARKET_MINIMUM && accountDatabase.get(index).getType() == AccountType.MONEY_MARKET) {
                if (sufficientFunds) {
                    System.out.println(accountNumber + " balance below $2,000 - $"
                            + df.format(withdrawalAmount) + " withdrawn from " + accountNumber);
                    accountDatabase.withdraw(accountNumber, withdrawalAmount);
                } else {
                    System.out.println(accountNumber + " balance below $2,000 - withdrawing $"
                            + df.format(withdrawalAmount) + " - insufficient funds.");
                }
            } else {
                if (sufficientFunds) {
                    System.out.println("$" + df.format(withdrawalAmount) + " withdrawn from " + accountNumber);
                    accountDatabase.withdraw(accountNumber, withdrawalAmount);
                } else {
                    System.out.println("$" + df.format(withdrawalAmount) + " - insufficient funds.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("For input string: \"" + commandArray[2] + "\" - not a valid amount.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Missing data tokens for the withdrawal.");
        }
    }

    /**
     * Creates an AccountType object using a provided String representation of the three types of Accounts.
     * Types of accounts: Checking(01), Savings(02), Money Market(03).
     *
     * @param type String representation of the type of Account
     * @return AccountType of the Account
     */
    private static AccountType createAccountType(String type) {
        String typeToken = type.toLowerCase(); //AccountType is case-insensitive
        return switch (typeToken) {
            case "checking" -> AccountType.CHECKING;
            case "savings" -> AccountType.SAVINGS;
            case "moneymarket" -> AccountType.MONEY_MARKET;
            case "college" -> AccountType.COLLEGE_CHECKING;
            case "certificate" -> AccountType.CD;
            default -> {
                System.out.println(typeToken + " - invalid account type.");
                yield null;
            }
        };
    }

    /**
     * Creates a Profile object based on first name, last name, and dateOfBirth.
     * Creates a new Profile object.
     *
     * @param firstName   String representation of first name of Account holder
     * @param lastName    String representation of last name of Account holder
     * @param dateOfBirth Date object that represents date of birth of Account holder
     * @return Profile for the Account
     */
    private static Profile createProfile(String firstName, String lastName, Date dateOfBirth) {
        return new Profile(firstName, lastName, dateOfBirth);
    }

    /**
     * Creates and returns an Account object based on the provided parameters.
     * Determines the account type, creates a profile for the holder, and initializes
     * the appropriate Account subtype with the provided details.
     *
     * @param commandArray an array of Strings containing command details, where some commands are used
     *                     to specify additional account attributes such as campus code or term length
     * @param firstName    the first name of the account holder
     * @param lastName     the last name of the account holder
     * @param dateOfBirth  a Date object representing the birthdate of the account holder
     * @param branch       the Branch object representing the bank branch where the account is opened
     * @param accountType  a String representation of the type of account to create
     * @param balance      the initial balance to set for the account
     * @return the created Account object, initialized based on the provided parameters
     * @throws IllegalStateException if the provided accountType is not a valid account type
     */
    private static Account getAccount(String[] commandArray, String firstName, String lastName, Date dateOfBirth, Branch branch, String accountType, double balance) {
        Profile holder = createProfile(firstName, lastName, dateOfBirth);
        accountType = accountType.toLowerCase();
        return switch (accountType) {
            case "checking" -> new Checking(branch, AccountType.CHECKING, holder, balance);
            case "savings" -> new Savings(branch, AccountType.SAVINGS, holder, balance);
            case "moneymarket" ->
                    new MoneyMarket(branch, AccountType.MONEY_MARKET, holder, balance);
            case "college" ->
                    new CollegeChecking(branch, AccountType.COLLEGE_CHECKING, holder, Campus.fromCode(commandArray[commandArray.length - 1]), balance);
            case "certificate" ->
                    new CertificateDeposit(branch, AccountType.CD, holder, Integer.parseInt(commandArray[commandArray.length - 2]), createDate(commandArray[commandArray.length - 1]), balance);
            default -> throw new IllegalStateException("Unexpected value: " + accountType);
        };
    }

    /**
     * Chooses which action to complete depending on a single input line that has been read.
     * VALID COMMANDS: O, C, D, W, P, PA, PB, PH, PT
     * Otherwise, will identify that the command was invalid.
     * O --> opens a new Account, if not already there
     * C --> closes an Account, if in the database
     * D --> deposits money into an Account, if in the database
     * W --> withdraws money from an Account, if in the database and money available
     * P --> prints the AccountDatabase
     * PA --> prints the Archive
     * PB --> prints ordered by Branch (county, then city)
     * PH --> prints by holder then AccountNumber
     * PT --> prints by AccountType then AccountNumber
     *
     * @param commandArray Holds the input that has been extracted and put into a String array
     * @throws IOException if an I/O error occurs during command processing
     */
    private static void processCommand(String[] commandArray) throws IOException {
        if (isValidCommand(commandArray[0])) {
            switch (commandArray[0]) {
                case "O" -> openAccount(commandArray);
                case "C" -> closeAccount(commandArray);
                case "D" -> depositMoney(commandArray);
                case "W" -> withdrawMoney(commandArray);
                case "A" -> processActivities();
            }
            if (accountDatabase.isEmpty()) {
                System.out.println("Account database is empty!");
            } else {
                printAccounts(commandArray[0]);
            }
        }
    }
}
