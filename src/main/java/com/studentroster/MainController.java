package com.studentroster;

import javafx.fxml.FXML;
import javafx.scene.control.*; //Delete unneccessary stuff
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;

public class MainController {

    @FXML
    private TextArea textArea0;

    @FXML
    private CheckBox t0AbroadCheckbox;

    @FXML
    private RadioButton t0CTRadio;

    @FXML
    private TextField t0CreditField;

    @FXML
    private RadioButton t0InternationalRadio;

    @FXML
    private ToggleGroup t0MajorGroup;

    @FXML
    private RadioButton t0NYRadio;

    @FXML
    private TextField t0NameField;

    @FXML
    private RadioButton t0NoResidentRadio;


    @FXML
    private RadioButton t0ResidentRadio;

    @FXML
    private RadioButton t0TristateRadio;

    @FXML
    private TextField t0TuitionField;

    @FXML
    private Tab t1;

    @FXML
    private TextField t1AidField;

    @FXML
    private ToggleGroup t1MajorGroup;

    @FXML
    private TextField t1NameField;

    @FXML
    private TextField t1PaymentAmountField;

    @FXML
    private DatePicker t1DatePicker;

    private Roster studentRoster;

    public MainController() {
        this.studentRoster = new Roster();
    }

    @FXML
    void disableNonResident(ActionEvent event) {
        t0TristateRadio.setDisable(true);
        t0TristateRadio.setSelected(false);
        t0InternationalRadio.setDisable(true);
        t0InternationalRadio.setSelected(false);
        t0NYRadio.setDisable(true);
        t0NYRadio.setSelected(false);
        t0CTRadio.setDisable(true);
        t0CTRadio.setSelected(false);
        t0AbroadCheckbox.setDisable(true);
        t0AbroadCheckbox.setSelected(false);
    }

    @FXML
    void enableNonResident(ActionEvent event) {
        t0TristateRadio.setDisable(false);
        t0InternationalRadio.setDisable(false);
    }

    @FXML
    void enableTriState(ActionEvent event) {
        t0NYRadio.setDisable(false);
        t0CTRadio.setDisable(false);
        t0AbroadCheckbox.setDisable(true);
        t0AbroadCheckbox.setSelected(false);
    }

    @FXML
    void enableInternational(ActionEvent event) {
        t0AbroadCheckbox.setDisable(false);
        t0NYRadio.setDisable(true);
        t0NYRadio.setSelected(false);
        t0CTRadio.setDisable(true);
        t0CTRadio.setSelected(false);
    }

    private Profile makeProfile(TextField NameField, ToggleGroup MajorGroup) {
        String name = NameField.getText();
        if (name.replaceAll("\\s+","").equals("")) {
            textArea0.appendText("Student Name is Missing\n");
            return null;
        }
        Major major = parseMajor(MajorGroup);
        if (major == null)
        {
            textArea0.appendText("Major is Missing\n");
            return null;
        }
        return new Profile(name, major);
    }

    private Major parseMajor(ToggleGroup majorGroup) {
        Toggle[] majorToggles = majorGroup.getToggles().toArray(new Toggle[0]);
        if (majorToggles[0].isSelected()) {
            return Major.CS;
        }
        else if (majorToggles[1].isSelected()) {
            return Major.EE;
        }
        else if (majorToggles[2].isSelected()) {
            return Major.ME;
        }
        else if (majorToggles[3].isSelected()) {
            return Major.IT;
        }
        else if (majorToggles[4].isSelected()) {
            return Major.BA;
        }
        else return null;
    }

    private int checkCredits(String creditString, boolean isInternational)
    {
        if (creditString.replaceAll("\\s+","").equals("")) {
            textArea0.appendText("Credit hours missing.\n");
            return -1;
        }

        int credits;
        try {
            credits = Integer.parseInt(creditString);
        }
        catch (NumberFormatException e) {
            textArea0.appendText("Invalid credit hours.\n");
            return -1;
        }
        if (credits < 0) {
            textArea0.appendText("Credit hours cannot be negative.\n");
            return -1;
        }
        else if (credits < Student.getMinCredits()) {
            textArea0.appendText("Minimum credit hours is " + Student.getMinCredits() + ".\n");
            return -1;
        }
        else if (isInternational == true && credits < International.getMinInternationalCredits())
        {
            textArea0.appendText("International students must enroll at least "
                    + International.getMinInternationalCredits() + " credits.\n");
            return -1;
        }
        else if (credits > Student.getMaxCredits())
        {
            textArea0.appendText("Credit hours exceed the maximum " + Student.getMaxCredits() + ".\n");
            return -1;
        } else if (isInternational == true && t0AbroadCheckbox.isSelected() && credits != 12) {
            textArea0.appendText("Study abroad students must take 12 credits.\n");
            return -1;
        }
        return credits;
    }

    @FXML
    void addStudent(ActionEvent event) {
        Profile profile = makeProfile(t0NameField, t0MajorGroup);
        if (profile == null) return;

        int credits = checkCredits(t0CreditField.getText(), t0InternationalRadio.isSelected());
        if (credits < 0) return;

        if (t0ResidentRadio.isSelected()) addResident(profile, credits);
        else if (t0NoResidentRadio.isSelected()) addNonResident(profile, credits);
        else textArea0.appendText("Status is not selected.\n");
    }

    private void addResident(Profile profile, int credits) {
        Resident student = new Resident(profile, credits);
        addStudentToRoster(student);
    }

    private void addStudentToRoster(Student student) {
        if (studentRoster.add(student)) {
            textArea0.appendText("Student added.\n");
        }
        else textArea0.appendText("Student is already in roster.\n");
    }

    private void addNonResident(Profile profile, int credits) {
        if (t0TristateRadio.isSelected()) {
            TriState student;
            if (t0NYRadio.isSelected()) student = new TriState(profile, credits, true);
            else if (t0CTRadio.isSelected()) student = new TriState(profile, credits, false);
            else {
                textArea0.appendText("State for Tri-State student is not selected.\n");
                return;
            }
            addStudentToRoster(student);
        }
        else if (t0InternationalRadio.isSelected()) {
            International student = new International(profile, credits, t0AbroadCheckbox.isSelected());
            addStudentToRoster(student);
        }
        else
        {
            NonResident student = new NonResident(profile, credits);
            addStudentToRoster(student);
        }
    }
    @FXML
    void removeStudent(ActionEvent event) {
        Profile profile = makeProfile(t0NameField, t0MajorGroup);
        if (profile == null) return;
        Student student = new Student(profile);
        if (studentRoster.remove(student)) textArea0.appendText("Student removed from the roster.\n");
        else textArea0.appendText("Student is not in the roster.\n");
    }

    @FXML
    void calcIndividualTuition(ActionEvent event) {
        Profile profile = makeProfile(t0NameField, t0MajorGroup);
        if (profile == null) return;
        Student student = new Student(profile);
        double tuition = studentRoster.calculateIndividual(student);
        if (tuition < 0) textArea0.appendText("Student is not in the roster.\n");
        else {
            t0TuitionField.setText(tuition + "\n");
            textArea0.appendText("Tuition calculated.\n");
        }
    }

    private double checkPayment(String paymentString) {
        if (paymentString.replaceAll("\\s+","").equals("")) {
            textArea0.appendText("Payment amount missing.\n");
            return -1;
        }
        double payment;
        try {
            payment = Double.parseDouble(paymentString);
        }
        catch (NumberFormatException e) {
            textArea0.appendText("Invalid payment amount.\n");
            return -1;
        }
        if (payment <= 0) {
            textArea0.appendText("Payment must be positive.\n");
            return -1;
        }
        return payment;
    }

    private Date checkDate() {
        if (t1DatePicker.getValue() == null || t1DatePicker.getValue().toString().equals("")) {
            textArea0.appendText("Payment date is missing.\n");
            return null;
        }
        String dateUnformatted = t1DatePicker.getValue().toString();
        String dateFormatted = dateUnformatted.substring(5, 7) + "/" + dateUnformatted.substring(8) + "/"
                                                                          + dateUnformatted.substring(0, 4);
        Date date = new Date(dateFormatted);
        if (!date.isValid()) {
            textArea0.appendText("Payment date invalid.\n");
            return null;
        }
        return date;
    }

    @FXML
    void payTuition(ActionEvent event) {
        Profile profile = makeProfile(t1NameField, t1MajorGroup);
        Student student = new Student(profile);
        double payment = checkPayment(t1PaymentAmountField.getText());
        if (payment < 0) return;
        Date paymentDate = checkDate();
        if (paymentDate == null) return;

        switch (studentRoster.pay(student, payment, paymentDate)) {
            case Roster.SUCCESS:
                textArea0.appendText("Payment Applied.\n");
                break;
            case Roster.NOT_IN_ROSTER:
                textArea0.appendText("Student is not in the roster.\n");
                break;
            case Roster.PAYMENT_INVALID:
                textArea0.appendText("Amount is greater than amount due.\n");
                break;
        }
    }

    private double checkFinAid(String finAidString) {
        if (finAidString.replaceAll("\\s+","").equals("")) {
            textArea0.appendText("Financial Aid amount missing.\n");
            return -1;
        }
        double finAid;
        try {
            finAid = Double.parseDouble(finAidString);
        }
        catch (NumberFormatException e) {
            textArea0.appendText("Invalid financial aid amount.\n");
            return -1;
        }
        if (finAid <= 0) {
            textArea0.appendText("Financial Aid must be positive.\n");
            return -1;
        }
        else if (finAid > Resident.getMaxfinAid()) {
            textArea0.appendText("Financial Aid must be less than " + Resident.getMaxfinAid() + ".\n");
            return -1;
        }
        return finAid;
    }

    @FXML
    void setFinancialAid(ActionEvent event) {
        Profile profile = makeProfile(t1NameField, t1MajorGroup);
        if (profile == null) return;

        Student student = new Student(profile);

        double finAid = checkFinAid(t1AidField.getText());
        if (finAid < 0) return;

        switch (studentRoster.setFinancialAid(student, finAid)) {
            case Roster.SUCCESS:
                textArea0.appendText("Tuition updated.\n");
                break;
            case Roster.NOT_IN_ROSTER:
                textArea0.appendText("Student not in the roster.\n");
                break;
            case Roster.NOT_RESIDENT:
                textArea0.appendText("Not a resident student.\n");
                break;
            case Roster.PARTTIME:
                textArea0.appendText("Parttime student doesn't qualify for the award.\n");
                break;
            case Roster.ALREADY_AWARDED:
                textArea0.appendText("Awarded once already.\n");
                break;
        }
    }

    private void setStudyAbroadStatus(boolean value) {
        Profile profile = makeProfile(t1NameField, t1MajorGroup);
        if (profile == null) return;

        Student student = new Student(profile);

        if (studentRoster.setAbroad(student, value) == false)
            textArea0.appendText("Couldn't find the International student.\n");
        else textArea0.appendText("Study abroad status and tuition updated.\n");
    }

    @FXML
    void studyAbroadTrue(ActionEvent event) {
        setStudyAbroadStatus(true);
    }

    @FXML
    void studyAbroadFalse(ActionEvent event) {
        setStudyAbroadStatus(false);
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