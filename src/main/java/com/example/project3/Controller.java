package com.example.project3;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
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
    private ComboBox<String> branchComboBox;

    @FXML
    private ComboBox<Integer> termComboBox; // Make sure to link this with your ComboBox in FXML

    @FXML
    public void initialize() {
        termComboBox.getItems().addAll(3,6,9,12);
        branchComboBox.getItems().addAll("Edison", "Bridgewater", "Princeton", "Piscataway", "Warren");
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


