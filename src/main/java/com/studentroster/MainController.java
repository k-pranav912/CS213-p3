package com.studentroster;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;

public class MainController {

    @FXML
    private TextArea textArea0;

    @FXML
    private Tab t0;

    @FXML
    private CheckBox t0AbroadCheckbox;

    @FXML
    private Button t0AddStudentButton;

    @FXML
    private RadioButton t0BARadio;

    @FXML
    private RadioButton t0CSRadio;

    @FXML
    private RadioButton t0CTRadio;

    @FXML
    private TextField t0CreditField;

    @FXML
    private RadioButton t0EERadio;

    @FXML
    private RadioButton t0ITRadio;

    @FXML
    private HBox t0InternationalHbox;

    @FXML
    private RadioButton t0InternationalRadio;

    @FXML
    private RadioButton t0MERadio;

    @FXML
    private ToggleGroup t0MajorGroup;

    @FXML
    private HBox t0MajorHbox;

    @FXML
    private RadioButton t0NYRadio;

    @FXML
    private TextField t0NameField;

    @FXML
    private RadioButton t0NoResidentRadio;

    @FXML
    private Button t0RemoveStudentButton;

    @FXML
    private ToggleGroup t0ResidencyGroup;

    @FXML
    private HBox t0ResidencyHbox;

    @FXML
    private RadioButton t0ResidentRadio;

    @FXML
    private ToggleGroup t0TristateGroup;

    @FXML
    private HBox t0TristateHbox;

    @FXML
    private RadioButton t0TristateRadio;

    @FXML
    private Button t0TuitionDueButton;

    @FXML
    private TextField t0TuitionField;

    @FXML
    private Tab t1;

    @FXML
    private TextField t1AidField;

    @FXML
    private HBox t1AidHbox;

    @FXML
    private Button t1AidSetButton;

    @FXML
    private RadioButton t1BARadio;

    @FXML
    private RadioButton t1CSRadio;

    @FXML
    private Button t1DatePayButton;

    @FXML
    private DatePicker t1DatePicker;

    @FXML
    private RadioButton t1EERadio;

    @FXML
    private RadioButton t1ITRadio;

    @FXML
    private RadioButton t1MERadio;

    @FXML
    private HBox t1MajorHbox;

    @FXML
    private TextField t1NameField;

    @FXML
    private TextField t1PaymentAmountField;

    @FXML
    private HBox t1PaymentDateHBox;

    @FXML
    private Tab t2;

    @FXML
    private MenuItem t2CalculateTuitionMenuItem;

    @FXML
    private MenuBar t2MenuBar;

    @FXML
    private Menu t2PrintMenu;

    @FXML
    private MenuItem t2RosterMenuItem;

    @FXML
    private MenuItem t2RosterNameMenuItem;

    @FXML
    private MenuItem t2RosterPaymentMenuItem;

    @FXML
    private Menu t2TuitionMenu;

    private Roster studentRoster;

    public MainController() {
        this.studentRoster = new Roster();
    }


    @FXML
    void addition(ActionEvent event) {
        int result = Integer.parseInt(t0NameField.getText()) + Integer.parseInt(t0CreditField.getText());
        t0TuitionField.setText(String.valueOf(result));
    }

    @FXML
    void calculateTuition(ActionEvent event) {
        studentRoster.calculate();
        textArea0.appendText("Calculation completed.\n");
    }

    @FXML
    void printRoster(ActionEvent event) {
        if (studentRoster.getRosterSize() <= 0) {
            textArea0.appendText("Student roster is empty!\n");
            return;
        }
        textArea0.appendText("* list of students in the roster **\n");
        for (int i = 0; i < studentRoster.getRosterLength(); i++) {
            if (studentRoster.getRosterStudent(i) != null) textArea0.appendText(studentRoster.getRosterStudent(i).toString() + "\n");
        }
        textArea0.appendText("* end of roster **\n");
    }

    @FXML
    void printByName(ActionEvent event) {
        if (studentRoster.getRosterSize() <= 0) {
            textArea0.appendText("Student roster is empty!\n");
            return;
        }

        String[] result = new String[studentRoster.getRosterSize()];

        textArea0.appendText("* list of students ordered by name **\n");;
        int counterIndex = 0;
        for (int i = 0; i < studentRoster.getRosterLength(); i++) {
            if (studentRoster.getRosterStudent(i) != null) {
                result[counterIndex] = studentRoster.getRosterStudent(i).toString();
                counterIndex++;
            }
        }

        for(int i = 0; i < result.length; i++) {
            for (int j = i + 1; j < result.length; j++) {
                if(result[i].compareTo(result[j]) > 0) {
                    String temp = result[i];
                    result[i] = result[j];
                    result[j] = temp;
                }
            }
        }

        for (int i = 0; i < result.length; i++) {
            textArea0.appendText(result[i] + "\n");
        }

        textArea0.appendText("* end of roster **\n");

    }

    @FXML
    void printByDate(ActionEvent event)
    {
        if (studentRoster.getRosterSize() <= 0)
        {
            textArea0.appendText("Student roster is empty!\n");
            return;
        }

        int[] releaseDates = studentRoster.genDateArray();

        textArea0.appendText("* list of students made payments ordered by payment date **\n");
        for (int i = 0; i < releaseDates.length; i++)
        {
            if (releaseDates[i] == 0) continue;

            for (int j = 0; j < studentRoster.getRosterLength(); j++)
            {
                if ((studentRoster.getRosterStudent(j) != null && studentRoster.getRosterStudent(j).getLastPayment() != null)
                        && studentRoster.getRosterStudent(j).getDateInt() == releaseDates[i]) {
                    textArea0.appendText(studentRoster.getRosterStudent(j).toString() + "\n");}
            }
        }
        textArea0.appendText("* end of roster **\n");

    }


}