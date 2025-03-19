package com.example.project3;

import com.example.project3.banking.*;
import com.example.project3.util.Sort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.project3.banking.AccountDatabase;
import com.example.project3.util.Date;

public class Controller {
    public static final AccountDatabase accountDatabase = new AccountDatabase();

    @FXML
    private TextField balance;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextArea resultText;

    @FXML
    private RadioButton rb_cc;

    @FXML
    private RadioButton rb_cd;

    @FXML
    private RadioButton rb_checking;

    @FXML
    private RadioButton rb_mm;

    @FXML
    private RadioButton rb_savings;

    @FXML
    private ToggleGroup at_types;

    @FXML
    private ToggleGroup cm_types;

    @FXML
    private ComboBox<Branch> branchComboBox;

    @FXML
    private ComboBox<Integer> termComboBox; // Make sure to link this with your ComboBox in FXML

    @FXML
    private DatePicker dob;




    @FXML
    private void initialize() {
        Integer[] terms = new Integer[] {3, 6, 9, 12};
        Branch[] branches = new Branch[] {Branch.EDISON, Branch.BRIDGEWATER, Branch.PRINCETON, Branch.PISCATAWAY, Branch.WARREN};
        ObservableList<Integer> termsList = FXCollections.observableArrayList(terms);
        ObservableList<Branch> branchList = FXCollections.observableArrayList(branches);
        termComboBox.setItems(termsList);
        branchComboBox.setItems(branchList);
    }

    @FXML
    private void openAccount() {
        try {
            String first = firstName.getText();
            String last = lastName.getText();
            String date = dob.getValue().toString();
            String[] dateArray = date.split("-");
            Date dateOfBirth = new Date(Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0]));
            AccountType accountType = getAccountType();
            Branch branch = branchComboBox.getSelectionModel().getSelectedItem();
            double balance = Double.parseDouble(this.balance.getText());
            Account account = createAccount(first, last, dateOfBirth, accountType, branch, balance);
            accountDatabase.add(account);
            resultText.appendText(account.getAccountNumber().getType() + " account " + account.getAccountNumber() + " has been opened.");

        } catch (NullPointerException e) {
            resultText.appendText("Missing data tokens for opening an account.\n");
        }
    }

    private Account createAccount(String firstName, String lastName, Date dateOfBirth, AccountType accountType, Branch branch, double balance) {
        return getAccount(firstName, lastName, dateOfBirth, branch, accountType, balance);
    }

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

    @FXML
    private void printByHolder() {
        resultText.appendText("\n*List of accounts ordered by account holder and number.\n");
        Sort.account(accountDatabase, 'H');
        print();
    }

    private void print() {
        for (int i = 0; i < accountDatabase.size(); i++) {
            System.out.println(accountDatabase.get(i).toString());
        }
        resultText.appendText("*end of list.\n\n");
    }

    @FXML
    public void printByType() {
        Sort.account(accountDatabase, 'T');

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

    @FXML
    public void printStatements() {
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

    @FXML
    public void printArchive() {
        resultText.appendText("\n*List of closed accounts in the archive.");
        AccountNode current = accountDatabase.getArchive().getFirst();
        while (current != null) {
            resultText.appendText(current + "\n\n");
            current = current.getNext();
        }
        resultText.appendText("*end of list.\n\n");
    }

}


