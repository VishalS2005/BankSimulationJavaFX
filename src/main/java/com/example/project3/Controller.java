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
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

public class Controller {
    public static final AccountDatabase accountDatabase = new AccountDatabase();

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

        disableCampusToggle(true);
        at_types.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == rb_cc) {
                disableCampusToggle(false);
            } else {
                disableCampusToggle(true);
                cm_types.selectToggle(null);
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
        List<String> errors = new List<>();
        String first = firstName.getText();
        checkName(first, "First name", errors);
        String last = lastName.getText();
        checkName(last, "Last name", errors);
        String date = dob.getValue().toString();
        String[] dateArray = date.split("-");
        Date dateOfBirth = new Date(Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0]));
        checkFields(errors);
        AccountType accountType = getAccountType();
        checkDateOfBirth(accountType, dateOfBirth, errors);
        Branch branch = branchComboBox.getSelectionModel().getSelectedItem();
        double balanceNum;
        try {
            balanceNum = Double.parseDouble(balance.getText());
        } catch (NumberFormatException e) {
            errors.add("The following balance: \"" + balance.getText() + "\" - not a valid amount.");
            return;
        }
        Account account = createAccount(first, last, dateOfBirth, accountType, branch, balanceNum);
        accountDatabase.add(account);
        resultText.appendText(account.getAccountNumber().getType() + " account " + account.getAccountNumber() + " has been opened.");
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
            errors.add(fieldName + " cannot be empty.");
            return;
        }
        if (name.matches(".*\\d.*")) { // Check if the name contains numbers
            errors.add(fieldName + " cannot contain numbers.");
        }
        if (!name.matches("^[a-zA-Z\\s'-]+$")) {
            errors.add(fieldName + " cannot contain special characters.");
        }
    }

    /**
     * Creates an Account object based on provided parameters.
     * Delegates to the getAccount method to initialize and return the appropriate Account instance.
     *
     * @param firstName    the first name of the account holder
     * @param lastName     the last name of the account holder
     * @param dateOfBirth  a Date object representing the birthdate of the account holder
     * @param branch       the Branch object representing the bank branch where the account is opened
     * @param balance      the initial balance for the account
     * @return the created Account object
     */
    private Account createAccount(String firstName, String lastName, Date dateOfBirth, AccountType accountType, Branch branch, double balance) {
        return getAccount(firstName, lastName, dateOfBirth, branch, accountType, balance);
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
            case COLLEGE_CHECKING -> new CollegeChecking(branch, AccountType.COLLEGE_CHECKING, holder, balance);
            case CD -> new CertificateDeposit(branch, AccountType.CD, holder, balance);
        };
    }

    /**
     * Checks the Account Type radio button toggle group, the Campus location toggle group,
     * the Branch Combo box, and the Term Combo box.
     *
     * @param errors Collection of error messages
     */
    private void checkFields(List<String> errors) {
        if (at_types.getSelectedToggle() == null) {
            errors.add("You must select an account type.");
        } else if (branchComboBox.getSelectionModel().getSelectedItem() == null) {
            errors.add("You must select a branch.");
        }
    }

    /**
     * Checks a Date object if the birthdate provided is after the current date,
     * or if the birthdate provided indicates that the person is 18 years old.
     *
     * @param dob Date object which represents the date of birth
     * @param accountType AccountType object which represents the type of Account
     * false otherwise
     */
    private void checkDateOfBirth(AccountType accountType, Date dob, List<String> errors) {
        if (dob.isAfterToday()) {
            errors.add("DOB invalid: " + dob + " cannot be today or a future day.");
        } else if (!dob.isEighteen()) {
            errors.add("Not eligible to open: " + dob + " under 18.");
        } else if (accountType == AccountType.COLLEGE_CHECKING && !dob.isOverTwentyFour()) {
            errors.add("Not eligible to open: " + dob + " over 24.");
        }
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
     * Prints all the Accounts in the AccountDatabase from the beginning of AccountDatabase to the end.
     */
    private void print() {
        for (int i = 0; i < accountDatabase.size(); i++) {
            System.out.println(accountDatabase.get(i).toString());
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
            accountDatabase.get(i).statement();
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

        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(stage);

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

    }

    public static Account createAccount(String[] commandArray) {
        String accountType = commandArray[0];
        Branch branch = createBranch(commandArray[1]); //second input is the Branch
        String firstName = commandArray[2]; //third input is the first name of the holder
        String lastName = commandArray[3]; //fourth input is the last name of the holder
        Date dateOfBirth = createDate(commandArray[4]); //fifth input is the Date of Birth of the holder
        double amount = Double.parseDouble(commandArray[5]);
        return getAccount(commandArray, firstName, lastName, dateOfBirth, branch, accountType, amount);
    }

    public static Branch createBranch(String branchName) {
        Branch branch = null;
        try {
            branch = Branch.valueOf(branchName.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println(branchName + " - invalid branch.");
        }
        return branch;
    }

    public static Date createDate(String date) {
        String[] dateParts = date.split("/");
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        return new Date(month, day, year);
    }

    private static Account getAccount(String[] commandArray, String firstName, String lastName, Date dateOfBirth, Branch branch, String accountType, double balance) {
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

        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(stage);

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
    }
}



