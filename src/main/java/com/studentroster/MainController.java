package com.studentroster;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.DatePicker;
import javafx.event.ActionEvent;

/**
 * The MainController class, which acts as the brain of the GUI. Handles all GUI inputs and outputs,
 * running the appropriate methods from the Roster class when needed.
 */
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

    /**
     * Constructor for the MainController, which initializes the user's student roster
     */
    public MainController() {
        this.studentRoster = new Roster();
    }

    /**
     * Resets and disables all NonResident Inputs
     * @param event Event of user clicking Resident radio button
     */
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

    /**
     * Enables TriState and International radio buttons
     * @param event Event of user clicking NonResident radio button
     */
    @FXML
    void enableNonResident(ActionEvent event) {
        t0TristateRadio.setDisable(false);
        t0InternationalRadio.setDisable(false);
    }

    /**
     * Enables Tri-State inputs, disables and resets International inputs
     * @param event Event of user clicking Tri-State radio button
     */
    @FXML
    void enableTriState(ActionEvent event) {
        t0NYRadio.setDisable(false);
        t0CTRadio.setDisable(false);
        t0AbroadCheckbox.setDisable(true);
        t0AbroadCheckbox.setSelected(false);
    }

    /**
     * Enables Study Abroad input, disables and resets Tri-State inputs
     * @param event Event of user clicking International radio button
     */
    @FXML
    void enableInternational(ActionEvent event) {
        t0AbroadCheckbox.setDisable(false);
        t0NYRadio.setDisable(true);
        t0NYRadio.setSelected(false);
        t0CTRadio.setDisable(true);
        t0CTRadio.setSelected(false);
    }

    /**
     * Helper method to check a student profile to see if all necessary inputs are valid, then make the
     * student's profile
     * @param NameField TextField containing student's name
     * @param MajorGroup ToggleGroup containing major selections
     * @return Student profile on success, null on failure
     */
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

    /**
     * Helper method to check for and return a Major selected by the user
     * @param majorGroup ToggleGroup containing major selections
     * @return Major enum on success, null on failure
     */
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

    /**
     * Helper method to check and return the number of credits student is taking
     * @param creditString String of user inputted credit hours
     * @param isInternational True if student is international, false if not
     * @return Number of credits on success, -1 on failure
     */
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

    /**
     * Sends a students profile and credits to respective helper method to be added, if they are valid
     * @param event Event when user presses Add Student button
     */
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

    /**
     * Sends a resident to addStudentToRoster to be added
     * @param profile Student's profile
     * @param credits Student's credit hours
     */
    private void addResident(Profile profile, int credits) {
        Resident student = new Resident(profile, credits);
        addStudentToRoster(student);
    }

    /**
     * Sends student to Roster class to be added, unless student is already in roster
     * @param student Student to be added
     */
    private void addStudentToRoster(Student student) {
        if (studentRoster.add(student)) {
            textArea0.appendText("Student added.\n");
        }
        else textArea0.appendText("Student is already in roster.\n");
    }

    /**
     * Sends a Non-Resident of type Non-Resident, Tri-State, or International to addStudentToRoster, if
     * all respective inputs are valid.
     * @param profile Student's profile
     * @param credits Student's credit hours
     */
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

    /**
     * Creates student object and sends to roster to be removed, succeeds if the student is already in the
     * roster.
     * @param event Event when user presses Remove student button
     */
    @FXML
    void removeStudent(ActionEvent event) {
        Profile profile = makeProfile(t0NameField, t0MajorGroup);
        if (profile == null) return;
        Student student = new Student(profile);
        if (studentRoster.remove(student)) textArea0.appendText("Student removed from the roster.\n");
        else textArea0.appendText("Student is not in the roster.\n");
    }

    /**
     * Calls calculateIndividual() in roster class to calculate a student's individual tuition due, if they
     * are in the roster
     * @param event Event when user presses Tuition Due button
     */
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

    /**
     * Helper method to check if the user's payment is a valid number and returns the payment
     * @param paymentString String of payment value
     * @return Payment value double on success, -1 on failure
     */
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

    /**
     * Checks to see if the payment date chosen by the user in the DatePicker object is valid and returns
     * the Date
     * @return Date object of user inputted payment date
     */
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

    /**
     * Takes a student and payment amount and sends it to Roster class to be paid, or prints appropriate error
     * message
     * @param event Event when user presses Pay button
     */
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

    /**
     * Helper method to check if the user-inputted financial aid amount is a valid number, and returns it
     * @param finAidString String of financial aid amount
     * @return Financial aid value on success, -1 on failure
     */
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

    /**
     * Takes a student and financial aid amount and sends it to Roster class to be applied, if valid amount and
     * student, or prints appropriate error message
     * @param event Event when user presses Set button
     */
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

    /**
     * Takes a student and study abroad status and sends it to Roster class to be updated, if valid
     * international student
     * @param value Study abroad status boolean
     */
    private void setStudyAbroadStatus(boolean value) {
        Profile profile = makeProfile(t1NameField, t1MajorGroup);
        if (profile == null) return;

        Student student = new Student(profile);

        if (studentRoster.setAbroad(student, value) == false)
            textArea0.appendText("Couldn't find the International student.\n");
        else textArea0.appendText("Study abroad status and tuition updated.\n");
    }

    /**
     * Calls setStudyAbroadStatus() for student who is studying abroad
     * @param event Event when user presses Studying Abroad button
     */
    @FXML
    void studyAbroadTrue(ActionEvent event) {
        setStudyAbroadStatus(true);
    }

    /**
     * Calls setStudyAbroadStatus() for a student who is not studying abroad
     * @param event Event when user presses Not Studying Abroad button
     */
    @FXML
    void studyAbroadFalse(ActionEvent event) {
        setStudyAbroadStatus(false);
    }

    /**
     * Calls calculate() in Roster class on the entire roster
     * @param event Event when user presses Tuition - Calculate tuition dues menu option
     */
    @FXML
    void calculateTuition(ActionEvent event) {
        studentRoster.calculate();
        textArea0.appendText("Calculation completed.\n");
    }

    /**
     * Prints the student roster without any specified order
     * @param event Event when user presses Print - Roster menu option
     */
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

    /**
     * Prints the student roster by their alphabetical name order
     * @param event Event when user presses Print - Roster by Name menu option
     */
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

    /**
     * Prints the student roster ordered by their last payment date
     * @param event Event when user presses Print - Roster by Payments menu option
     */
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