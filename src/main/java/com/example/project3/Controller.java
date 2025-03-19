package com.example.project3;

import com.example.project3.banking.*;
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
    private Button open;

    @FXML
    private DatePicker dob;



    @FXML
    public void initialize() {
        Integer[] terms = new Integer[] {3, 6, 9, 12};
        Branch[] branches = new Branch[] {Branch.EDISON, Branch.BRIDGEWATER, Branch.PRINCETON, Branch.PISCATAWAY, Branch.WARREN};
        ObservableList<Integer> termsList = FXCollections.observableArrayList(terms);
        ObservableList<Branch> branchList = FXCollections.observableArrayList(branches);
        termComboBox.setItems(termsList);
        branchComboBox.setItems(branchList);
    }

    @FXML
    public void openAccount() {
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

}


