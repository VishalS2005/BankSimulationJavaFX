package com.example.project3;

import com.example.project3.banking.AccountDatabase;
import com.example.project3.banking.Branch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.project3.banking.AccountDatabase;

public class Controller {
    public static final AccountDatabase accountDatabase = new AccountDatabase();
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

        } catch (NullPointerException e) {
            resultText.setText("Please provide more information to open account.");
        }
    }

}


