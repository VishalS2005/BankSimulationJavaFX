package com.example.project3;

import com.example.project3.banking.*;
import com.example.project3.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Controller class handles the functions in the GUI.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class Controller {
    /**
     * Account Database holds each account.
     */
    public static final AccountDatabase accountDatabase = new AccountDatabase();

    private static final DecimalFormat df = new DecimalFormat("#,##0.00");

    private static final double TEN_PERCENT = 0.1;

    private static final double DAYS_IN_YEAR = 365;

    public Stage stage;

    /**
     * Text field that will take in a decimal value representative of a client's initial deposit.
     */
    @FXML
    private TextField balance;

    /**
     * Text field that will take in the client's first name.
     */
    @FXML
    private TextField firstName;

    /**
     * Text field that will take in the client's last name.
     */
    @FXML
    private TextField lastName;

    /**
     * Text area that will display all the output.
     */
    @FXML
    private TextArea resultText;

    /**
     * Radio button that represents the "Checking" Account Type.
     */
    @FXML
    private RadioButton rb_checking;

    /**
     * Radio button that represents the "College Checking" Account Type.
     */
    @FXML
    private RadioButton rb_cc;

    /**
     * Radio button that represents the "Savings" Account Type.
     */
    @FXML
    private RadioButton rb_savings;

    /**
     * Radio button that represents the "Money Market" Account Type.
     */
    @FXML
    private RadioButton rb_mm;

    /**
     * Radio button that represents the "Certificate Deposit" Account Type.
     */
    @FXML
    private RadioButton rb_cd;

    /**
     * Radio button that represents the "New Brunswick" Campus.
     */
    @FXML
    private RadioButton rb_nb;

    /**
     * Radio button that represents the "Newark" Campus.
     */
    @FXML
    private RadioButton rb_nw;

    /**
     * Radio button that represents the "Camden" Campus.
     */
    @FXML
    private RadioButton rb_cam;

    /**
     * Toggle group for the Account Type buttons.
     */
    @FXML
    private ToggleGroup at_types;

    /**
     * Toggle group for the Campus buttons.
     */
    @FXML
    private ToggleGroup cm_types;

    /**
     * Drop down that represents the 5 different Branch locations that can be chosen.
     */
    @FXML
    private ComboBox<Branch> branchComboBox;

    /**
     * Drop down that represents the term of a year for Certificate Deposit Account.
     */
    @FXML
    private ComboBox<Integer> termComboBox;

    /**
     * Date picker that represents the date of birth of a client.
     */
    @FXML
    private DatePicker dob;

    @FXML
    private DatePicker openDate;

    @FXML
    private CheckBox loyal;

    /**
     * Text field that takes in Account Number of holder.
     */
    @FXML
    private TextField dwc_accnum;

    /**
     * Text field that takes in amount to be deposited or withdrawn.
     */
    @FXML
    private TextField dwc_amount;

    /**
     * Text field that takes in first name of holder.
     */
    @FXML
    private TextField dwc_firstName;

    /**
     * Text field that takes in last name of holder.
     */
    @FXML
    private TextField dwc_lastName;

    /**
     * Button used to deposit money into an Account.
     */
    @FXML
    private Button dwc_deposit;

    /**
     * Button used to deposit money into an Account.
     */
    @FXML
    private Button dwc_withdraw;

    /**
     * Button used to close all accounts associated with a Profile.
     */
    @FXML
    private Button dwc_closeAll;

    /**
     * Date picker where holder chooses the date they close the Account.
     */
    @FXML
    private DatePicker dwc_dateClose;

    /**
     * Date picker where holder chooses the date they were born.
     */
    @FXML
    private DatePicker dwc_dob;

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
     * When the user interface is created, the program initializes the items in the combo boxes.
     */
    @FXML
    private void initialize() {
        Integer[] terms = new Integer[] {3, 6, 9, 12};
        Branch[] branches = new Branch[] {Branch.EDISON, Branch.BRIDGEWATER, Branch.PRINCETON, Branch.PISCATAWAY, Branch.WARREN};
        ObservableList<Integer> termsList = FXCollections.observableArrayList(terms);
        ObservableList<Branch> branchList = FXCollections.observableArrayList(branches);
        termComboBox.setItems(termsList);
        branchComboBox.setItems(branchList);
        rb_checking.setSelected(true);
        disableCampusToggle(true);
        loyal.setDisable(true);
        termComboBox.setDisable(true);
        openDate.setDisable(true);
        branchComboBox.setEditable(false);
        termComboBox.setEditable(false);
        at_types.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == rb_checking) {
                disableCampusToggle(true);
                termComboBox.setDisable(true);
                loyal.setDisable(true);
                openDate.setDisable(true);
            }
            else if (newValue == rb_cc) {
                disableCampusToggle(false);
                termComboBox.setDisable(true);
                loyal.setDisable(true);
                openDate.setDisable(true);
                rb_nb.setSelected(true);
            } else if (newValue == rb_cd) {
                termComboBox.setDisable(false);
                disableCampusToggle(true);
                cm_types.selectToggle(null);
                loyal.setDisable(true);
                openDate.setDisable(false);
            } else {
                termComboBox.setDisable(true);
                disableCampusToggle(true);
                cm_types.selectToggle(null);
                loyal.setDisable(false);
                openDate.setDisable(true);
            }
        });
    }

    /**
     * Enables/disables the campus toggle group.
     */
    private void disableCampusToggle(boolean disable){
        rb_nb.setDisable(disable);
        rb_nw.setDisable(disable);
        rb_cam.setDisable(disable);
    }

    /**
     * Executed to open a new Account using user input from the Open Account tab pane.
     * Checks for valid inputs values and empty arguments.
     * Checks for duplicate account, minimum balance, and money market specifications.
     * Adds the opened account to the database.
     */
    @FXML
    private void openAccount() {
        AccountType accountType = getAccountType();

        Branch branch = branchComboBox.getSelectionModel().getSelectedItem();

        if(branch == null) {
            resultText.appendText("Select Branch\n");
            return;
        }
        if(dob.getValue() == null) {
            resultText.appendText("Date input Invalid\n");
            return;
        }

        Date dateOfBirth = getDate(dob);

        if (!checkDateOfBirth(accountType, dateOfBirth)) { return; }
        String first = firstName.getText();
        String last = lastName.getText();
        if(first.isEmpty() || last.isEmpty()) {
            resultText.appendText("Name invalid\n");
            return;
        }

        double balanceNum;
        try { balanceNum = Double.parseDouble(balance.getText()); }
        catch (NumberFormatException e) {
            resultText.appendText("For input string \"" + balance.getText() + "\" - not a valid amount.\n");
            return;
        }
        if (accountType != AccountType.CD && accountDatabase.contains(first, last, dateOfBirth, accountType)) {
            resultText.appendText(first + " " + last + " already has a " + accountType + " account.\n");
            return;
        }
        if (!checkBalance(balanceNum, accountType)) { return; }
        Account account = getAccount(first, last, dateOfBirth, branch,accountType, balanceNum);
        accountDatabase.add(account);
        resultText.appendText(account.getAccountNumber().getType() + " account " + account.getAccountNumber() + " has been opened.\n");
        clearArgumentsOpen();
    }



    private Date getDate(DatePicker datePicker) {
        String date = datePicker.getValue().toString();
        String[] dateArray = date.split("-");
        return new Date(Integer.parseInt(dateArray[1]),
                Integer.parseInt(dateArray[2]),
                Integer.parseInt(dateArray[0]));
    }

    private boolean checkDateOfBirth(AccountType accountType, Date dob) {
        if (!dob.isValid()) {
            resultText.appendText("DOB invalid: " + dob + " not a valid calendar date!\n");
            return false;
        } else if (dob.isAfterToday()) {
            resultText.appendText("DOB invalid: " + dob + " cannot be today or a future day.\n");
            return false;
        } else if (!dob.isEighteen()) {
            resultText.appendText("Not eligible to open: " + dob + " under 18.\n");
            return false;
        } else if (accountType == AccountType.COLLEGE_CHECKING && !dob.isOverTwentyFour()) {
            resultText.appendText("Not eligible to open: " + dob + " over 24.\n");
            return false;
        }
        return true;
    }

    /**
     * Checks a string for a number.
     *
     * @param name input string
     * @param fieldName specifies whether first or last name
     * @param errors Collection of error messages
     */
    private void checkName(String name, String fieldName, List<String> errors)  {
        if (name == null || name.trim().isEmpty()) {
            errors.add(fieldName + " cannot be empty.\n");
            return;
        }
        if (name.matches(".*\\d.*")) { // Check if the name contains numbers
            errors.add(fieldName + " cannot contain numbers.\n");
        }
        if (!name.matches("^[a-zA-Z\\s'-]+$")) {
            errors.add(fieldName + " cannot contain special characters.\n");
        }
    }



    /**
     * Creates and returns an Account object based on the provided parameters.
     * Determines the account type, creates a profile for the holder, and initializes
     * the appropriate Account subtype with the provided details.
     * Used in the above method.
     *
     * @param firstName    the first name of the account holder
     * @param lastName     the last name of the account holder
     * @param dateOfBirth  a Date object representing the birthdate of the account holder
     * @param branch       the Branch object representing the bank branch where the account is opened
     * @param accountType  a String representation of the type of account to create
     * @param balance      the initial balance to set for the account
     * @return the created Account object, initialized based on the provided parameters
     * @throws IllegalStateException if the provided accountType is not a valid account type
     */
    private Account getAccount(String firstName, String lastName, Date dateOfBirth, Branch branch, AccountType accountType, double balance) {
        Profile holder = new Profile(firstName, lastName, dateOfBirth);
        return switch (accountType) {
            case CHECKING ->  new Checking(branch, AccountType.CHECKING, holder, balance);
            case SAVINGS -> new Savings(branch, AccountType.SAVINGS, holder, balance);
            case MONEY_MARKET -> new MoneyMarket(branch, AccountType.MONEY_MARKET, holder, balance);
            case COLLEGE_CHECKING -> new CollegeChecking(branch, AccountType.COLLEGE_CHECKING, holder, getCampus(), balance);
            case CD -> new CertificateDeposit(branch, AccountType.CD, holder, termComboBox.getValue(), getDate(openDate), balance);
        };
    }



    private Campus getCampus() {
        if(rb_nb.isSelected()) {
            return Campus._1;
        }
        else if (rb_nw.isSelected()) {
            return Campus._2;
        }
        else {
            return Campus._3;
        }
    }


    private boolean checkBalance(double balance, AccountType acctType) {
        if (balance <= 0) { //balance must be more than 0
            resultText.appendText("Initial deposit cannot be 0 or negative.\n");
            return false;
        } else if (balance < MONEY_MARKET_MINIMUM && acctType.equals(AccountType.MONEY_MARKET)) { //Money Market account must have at least $2000
            resultText.appendText("Minimum of $2,000 to open a Money Market account.\n");
            return false;
        } else if (balance < CD_MINIMUM && acctType.equals(AccountType.CD)) {
            resultText.appendText("Minimum of $1,000 to open a Certificate Deposit account.\n");
            return false;
        }
        return true;
    }

    /**
     * Uses the client's selection in the account type toggle group to determine the type of account they want to open.
     *
     * @return AccountType that represents the type of Account to be opened
     */
    private AccountType getAccountType() {
        if(rb_checking.isSelected()) {
            return AccountType.CHECKING;
        } else if (rb_cc.isSelected()){
            return AccountType.COLLEGE_CHECKING;
        } else if (rb_savings.isSelected()) {
            return AccountType.SAVINGS;
        } else if (rb_mm.isSelected()) {
            return AccountType.SAVINGS;
        }
        return AccountType.CD;
    }

    /**
     * Prints the collection of errors if there are any.
     */
    private void printErrors(List<String> errors) {
       if (!errors.isEmpty()) {
           for (int i = 0; i < errors.size(); i++) {
               resultText.appendText(errors.get(i));
           }
           resultText.appendText("Please provide necessary information and click " +
                   "the \"open\" button once again.\n\n");
       }
    }

    /**
     * Prints all the Accounts in the AccountDatabase from the beginning of AccountDatabase to the end.
     */
    private void print() {
        for (int i = 0; i < accountDatabase.size(); i++) {
            resultText.appendText(accountDatabase.get(i) + "\n");
        }
        resultText.appendText("*end of list.\n\n");
    }

    /**
     * Orders and prints AccountDatabase by the 2-digit String, Branch.
     * Bubble Sort implementation is used to iterate through the array
     * and swap adjacent elements if they are out of order.
     * To print, iterate through AccountDatabase and print County followed by City.
     */
    @FXML
    private void printByBranch() {
        Sort.account(accountDatabase, 'B');
        String currentCounty = null;
        resultText.appendText("\n*List of accounts ordered by branch location (county, city).\n");
        for (int i = 0; i < accountDatabase.size(); i++) {
            Account account = accountDatabase.get(i);
            String county = account.getAccountNumber().getBranch().getCounty();
            if (currentCounty == null || !currentCounty.equals(county)) { // Print county header when encountering a new county
                resultText.appendText("County: " + county + "\n");
                currentCounty = county;
            }
            resultText.appendText(account + "\n");
        }
        resultText.appendText("*end of list.\n\n");
    }

    /**
     * Orders and prints AccountDatabase by the name and date of birth of the account.
     * Bubble Sort implementation is used to iterate through the array
     * and swap adjacent elements if they are out of order.
     * Calls the print() method.
     */
    @FXML
    private void printByHolder() {
        resultText.appendText("\n*List of accounts ordered by account holder and number.\n");
        Sort.account(accountDatabase, 'H');
        print();
    }

    /**
     * Orders and prints AccountDatabase by the AccountType.
     * Bubble Sort implementation is used to iterate through the array
     * and swap adjacent elements if they are out of order.
     * The compareTo method referenced below compares by AccountNumber.
     * Prints sorted by AccountType.
     */
    @FXML
    private void printByType() {
        Sort.account(accountDatabase, 'T');
        resultText.appendText("\n*List of accounts ordered by account type and number.\n");

        AccountType currentType = null;
        for (int i = 0; i < accountDatabase.size(); i++) {
            Account account = accountDatabase.get(i);
            AccountType accountType = account.getAccountNumber().getType();
            if (currentType == null || !currentType.equals(accountType)) { // Print type header when encountering a new type
                resultText.appendText("Account Type: " + accountType + "\n");
                currentType = accountType;
            }
            resultText.appendText(account + "\n");
        }
        resultText.appendText("*end of list.\n\n");
    }

    /**
     * Prints the statements of all accounts in the AccountDatabase in a formatted manner.
     * The method iterates through all accounts in the Account
     */
    @FXML
    private void printStatements() {
        resultText.appendText("*Account statements by account holder.\n");
        int holderCount = 0;
        for (int i = 0; i < accountDatabase.size(); i++) {
            if (i == 0 || !accountDatabase.get(i).getHolder().equals(accountDatabase.get(i - 1).getHolder())) {
                holderCount++;
                resultText.appendText(holderCount + "." + accountDatabase.get(i).getHolder() + "\n");
            }
            resultText.appendText("\t[Account#] " + accountDatabase.get(i).getAccountNumber() + "\n");
            resultText.appendText(accountDatabase.get(i).statement());
            resultText.appendText("\n");
        }
        resultText.appendText("*end of statements.\n\n");
    }

    /**
     * Prints all the Accounts that have been closed and are in the Archive
     */
    @FXML
    private void printArchive() {
        resultText.appendText("\n*List of closed accounts in the archive.\n");
        AccountNode current = accountDatabase.getArchive().getFirst();
        while (current != null) {
            resultText.appendText(current + "\n\n");
            current = current.getNext();
        }
        resultText.appendText("*end of list.\n\n");
    }

    @FXML
    private void loadAccounts() throws FileNotFoundException {
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            if (file == null) {
                return;
            }
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                Account account = createAccount(parts);
                accountDatabase.add(account);
            }
            resultText.appendText("Accounts in \"" + file + "\" loaded to the database.\n");
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            resultText.appendText("Incorrect File Format\n");
        }
    }

    public Account createAccount(String[] commandArray) {
        String accountType = commandArray[0];
        Branch branch = createBranch(commandArray[1]); //second input is the Branch
        String firstName = commandArray[2]; //third input is the first name of the holder
        String lastName = commandArray[3]; //fourth input is the last name of the holder
        Date dateOfBirth = createDate(commandArray[4]); //fifth input is the Date of Birth of the holder
        double amount = Double.parseDouble(commandArray[5]);
        return getAccount(commandArray, firstName, lastName, dateOfBirth, branch, accountType, amount);
    }

    public Branch createBranch(String branchName) {
        Branch branch = null;
        try {
            branch = Branch.valueOf(branchName.toUpperCase());
        } catch (IllegalArgumentException e) {
            resultText.appendText(branchName + " - invalid branch.\n");
        }
        return branch;
    }

    public Date createDate(String date) {
        String[] dateParts = date.split("/");
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        return new Date(month, day, year);
    }

    private Account getAccount(String[] commandArray, String firstName, String lastName, Date dateOfBirth, Branch branch, String accountType, double balance) {
        Profile holder = new Profile(firstName, lastName, dateOfBirth);
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

    @FXML
    public void processActivities() throws IOException {
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            if (file == null) {
                return;
            }
            resultText.appendText("Processing \"" + file + "\"...\n");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                Date date = createDate(parts[2]);
                Branch branch = createBranch(parts[3]);
                char type = parts[0].charAt(0);
                double amount = Double.parseDouble(parts[4]);
                Activity activity = new Activity(date, branch, type, amount, true);
                AccountNumber accountNumber = new AccountNumber(parts[1]);
                int index = accountDatabase.find(accountNumber);
                if (index == -1) {
                    continue;
                }
                if (type == 'W') {
                    accountDatabase.get(index).withdraw(date, branch, amount);
                } else {
                    accountDatabase.get(index).deposit(date, branch, amount);
                }
                resultText.appendText(accountNumber + "::" + activity + "\n");
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            resultText.appendText("Incorrect File Format\n");
        }
    }

    @FXML
    private void closeSingleAccount() {
        try {
            String num = dwc_accnum.getText();
            if(dwc_dateClose.getValue() == null) {
                resultText.appendText("Date input Invalid\n");
                return;
            }
            Date closeDate = getDate(dwc_dateClose);
            AccountNumber accountNumber = new AccountNumber(num);
            if (!accountDatabase.contains(accountNumber)) {
                resultText.appendText(accountNumber + " account does not exist.\n");
                return;
            }
            resultText.appendText("Closing account " + accountNumber + "\n");
            int index = accountDatabase.find(accountNumber);
            resultText.appendText("--\n");
            printInterest(accountDatabase.get(index), closeDate);
            accountDatabase.closeAccount(accountDatabase.get(index), closeDate);
            clearArgumentsClose();
        }catch(StringIndexOutOfBoundsException  | NullPointerException e) {
            resultText.appendText("For input string: \"" + dwc_accnum.getText() + "\" - not a valid account number.\n");
        }
    }


    private void printInterest(Account account, Date closeDate) {
        double interestRate;
        resultText.appendText("interest earned: $\n");

        if (account.getType() == AccountType.CD) {
            CertificateDeposit cd = (CertificateDeposit) account;
            Date openDate = cd.getOpen();
            int daysHeld = closeDate.daysFrom(openDate);
            double interest;
            if (closeDate.isAfter(openDate.addMonths(cd.getTerm()))) {
                interestRate = account.interestRate();
                interest = account.getBalance() * interestRate / DAYS_IN_YEAR * daysHeld;
                resultText.appendText(df.format(interest) + "\n");
            } else {
                interestRate = cd.interestRate(closeDate);
                interest = account.getBalance() * interestRate / DAYS_IN_YEAR * daysHeld;
                resultText.appendText(df.format(interest) + "\n");
                resultText.appendText("  [penalty] $" + df.format(TEN_PERCENT * interest) + "\n");
            }
        } else {
            interestRate = account.interestRate();
            double interest = account.getBalance() * interestRate / DAYS_IN_YEAR * closeDate.getDay();
            resultText.appendText(df.format(interest)+ "\n");
        }
    }



    @FXML
    private void closeMultipleAccounts() {
            String firstName = this.dwc_firstName.getText();
            String lastName = this.dwc_lastName.getText();
            if (firstName.isEmpty() || lastName.isEmpty()) {
                resultText.appendText("Name Invalid\n");
                return;
            }
            if (dwc_dob.getValue() == null || dwc_dateClose.getValue() == null) {
                resultText.appendText("Date input Invalid\n");
                return;
            }
            Date dateOfBirth = getDate(dwc_dob);
            Date closeDate = getDate(dwc_dateClose);
            Profile holder = new Profile(firstName, lastName, dateOfBirth);
            List<Account> accounts = findAllAccounts(holder);
            if (accounts.isEmpty()) {
                resultText.appendText(firstName + " " + lastName + " " + dateOfBirth + " does not have any accounts in the database.\n");
            } else {
                resultText.appendText("Closing accounts for " + firstName + " " + lastName + " " + dateOfBirth + "\n");
                for (Account account : accounts) {
                    resultText.appendText("--" + account.getAccountNumber() + "\n");
                    printInterest(account, closeDate);
                }
                int index = accountDatabase.find(firstName, lastName, dateOfBirth);
                while (index != -1) {
                    accountDatabase.closeAccount(accountDatabase.get(index), closeDate);
                    index = accountDatabase.find(firstName, lastName, dateOfBirth);
                }
                resultText.appendText("All accounts for " + firstName + " " + lastName + " " + dateOfBirth + " are closed and moved to archive.\n");
            }
            clearArgumentsClose();
    }



    private List<Account> findAllAccounts(Profile holder) {
        List<Account> accounts = new List<>();
        for (int i = 0; i < accountDatabase.size(); i++) {
            if (accountDatabase.get(i).getHolder().equals(holder)) {
                accounts.add(accountDatabase.get(i));
            }
        }
        return accounts;
    }



    @FXML
    private void withdrawMoney() {
        try {
            double withdrawalAmount = Double.parseDouble(dwc_amount.getText());
            if (withdrawalAmount <= 0) {
                resultText.appendText(withdrawalAmount + " withdrawal amount cannot be 0 or negative.\n");
                return;
            }
            AccountNumber accountNumber = new AccountNumber(dwc_accnum.getText());
            int index = accountDatabase.find(accountNumber);
            if (index == -1) {
                resultText.appendText(accountNumber + " does not exist.\n");
                return;
            }
            boolean sufficientFunds = accountDatabase.get(index).getBalance() >= withdrawalAmount;
            if (accountDatabase.get(index).getBalance() - withdrawalAmount < MONEY_MARKET_MINIMUM && accountDatabase.get(index).getType() == AccountType.MONEY_MARKET) {
                if (sufficientFunds) {
                    resultText.appendText(accountNumber + " balance below $2,000 - $" + df.format(withdrawalAmount) + " withdrawn from " + accountNumber + "\n");
                    accountDatabase.withdraw(accountNumber, withdrawalAmount);
                } else {
                    resultText.appendText(accountNumber + " balance below $2,000 - withdrawing $" + df.format(withdrawalAmount) + " - insufficient funds." + "\n");
                }
            } else {
                if (sufficientFunds) {
                    resultText.appendText("$" + df.format(withdrawalAmount) + " withdrawn from " + accountNumber + "\n");
                    accountDatabase.withdraw(accountNumber, withdrawalAmount);
                } else {
                    resultText.appendText("$" + df.format(withdrawalAmount) + " - insufficient funds.\n");
                }
            }
        }catch(StringIndexOutOfBoundsException  | NullPointerException e) {
            resultText.appendText("For input string: \"" + dwc_accnum.getText() + "\" - not a valid account number.\n");
        }
        catch (NumberFormatException e) {
            resultText.appendText("For input string: \"" + dwc_amount.getText() + "\" - not a valid amount.\n");
        }
    }


    @FXML
    private void depositMoney() {
        try {
            double depositAmount = Double.parseDouble(dwc_amount.getText());
            if (depositAmount <= 0) {
                resultText.appendText(depositAmount + " - deposit amount cannot be 0 or negative.\n");
                return;
            }
            AccountNumber accountNumber = new AccountNumber(dwc_accnum.getText());
            if (!accountDatabase.contains(accountNumber)) {
                resultText.appendText(accountNumber + " does not exist.\n");
                return;
            }
            accountDatabase.deposit(accountNumber, depositAmount);
            resultText.appendText("$" + df.format(depositAmount) + " deposited to " + accountNumber + "\n");
        } catch(StringIndexOutOfBoundsException | NullPointerException e) {
            resultText.appendText("For input string: \"" + dwc_accnum.getText() + "\" - not a valid account number.\n");
        }
        catch (NumberFormatException e) {
            resultText.appendText("For input string: \"" + dwc_amount.getText() + "\" - not a valid amount." + "\n");
        }
    }

    @FXML
    private void clearArgumentsOpen() {
        initialize();
        firstName.clear();
        lastName.clear();
        dob.getEditor().clear();
        balance.clear();
        openDate.getEditor().clear();
        loyal.setSelected(false);
        rb_nb.setSelected(false);
        rb_cam.setSelected(false);
        rb_nw.setSelected(false);
        branchComboBox.setValue(null);
        termComboBox.setValue(null);
    }

    @FXML
    private void clearArgumentsClose() {
        dwc_amount.clear();
        dwc_accnum.clear();
        dwc_dateClose.getEditor().clear();
        dwc_dob.getEditor().clear();
        dwc_firstName.clear();
        dwc_lastName.clear();

    }
}



