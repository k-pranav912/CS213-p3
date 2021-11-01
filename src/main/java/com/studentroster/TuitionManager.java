package com.studentroster;

import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * TuitionManager class, which is called by RunProject2 and handles the inputs from the user, turning them into commands
 * to run in the Roster class. Parses the user input to find the command, as well as the information relevant to the
 * command, and sends that information to roster.
 * @author Neel Prabhu, Saipranav Kalapala
 */
public class TuitionManager {

    /**
     * Exits the process. Called when the user enters "Q"
     */
    private static void exitManager()
    {
        System.out.println("Tuition Manager terminated.");
        System.exit(0);
    }

    /**
     * Parses and creates a new profile for a student, given a tokenized input, as long as the input is valid
     * @param strTokens Tokenized input, starting after the comma proceeding the command
     * @return Newly made profile. null if faulty input
     */
    private static Profile makeProfile(StringTokenizer strTokens)
    {
        if (strTokens.hasMoreTokens() == false) {
            System.out.println("Missing data in command line.");
            return null;
        }
        String name = strTokens.nextToken();
        if (strTokens.hasMoreTokens() == false) {
            System.out.println("Missing data in command line.");
            return null;
        }
        String majorString = strTokens.nextToken();
        Major major = Major.toMajor(majorString);
        if (major == null)
        {
            System.out.println("'" + majorString + "' is not a valid major.");
            return null;
        }
        Profile profile = new Profile(name, major);
        return profile;
    }

    /**
     * Parses and checks to see if the credits are a valid number of credits, as long as the input is valid
     * @param strTokens Tokenized input, starting with the credits
     * @param isInternational Boolean if the student is international, to check if the student must have at least
     *                        MIN_INTERNATIONAL_CREDITS
     * @return the number of credits the student is taking. -1 if faulty input or not the right number of credits
     */
    private static int checkCredits(StringTokenizer strTokens, boolean isInternational)
    {
        if (strTokens.hasMoreTokens() == false) {
            System.out.println("Credit hours missing.");
            return -1;
        }

        String creditHours = strTokens.nextToken();
        int credits;
        try {
            credits = Integer.parseInt(creditHours);
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid credit hours.");
            return -1;
        }
        if (credits < 0) {
            System.out.println("Credit hours cannot be negative.");
            return -1;
        }
        else if (credits < Student.getMinCredits()) {
            System.out.println("Minimum credit hours is " + Student.getMinCredits() + ".");
            return -1;
        }
        else if (isInternational == true && credits < International.getMinInternationalCredits())
        {
            System.out.println("International students must enroll at least "
                    + International.getMinInternationalCredits() + " credits.");
            return -1;
        }
        else if (credits > Student.getMaxCredits())
        {
            System.out.println("Credit hours exceed the maximum " + Student.getMaxCredits() + ".");
            return -1;
        }
        return credits;
    }

    /**
     * Parses and checks for a valid state, NY or CT, for tri-state students, as long as input is valid
     * @param strTokens Tokenized string, starting at the state initials
     * @return 1 if NY, 0 if CT, -1 if invalid
     */
    private static int checkState(StringTokenizer strTokens) {
        if (strTokens.hasMoreTokens() == false) {
            System.out.println("Missing data in command line.");
            return -1;
        }
        String state = strTokens.nextToken();

        if (state.toUpperCase().equals("NY"))
            return 1;
        else if (state.toUpperCase().equals("CT"))
            return 0;

        System.out.println("Not part of the tri-state area.");
        return -1;
    }

    /**
     * Parses and checks for a valid study abroad status, true or false, for international students, as long as input
     * is valid
     * @param strTokens Tokenized string, starting at the status
     * @return 1 if studying abroad, 0 if not studying abroad, -1 if invalid
     */
    private static int checkStudyAbroad(StringTokenizer strTokens) {
        if (strTokens.hasMoreTokens() == false) {
            System.out.println("Missing data in command line.");
            return -1;
        }
        String studyAbroad = strTokens.nextToken();

        if (studyAbroad.equals("true"))
            return 1;
        else if (studyAbroad.equals("false"))
            return 0;
        else {
            System.out.println("Invalid status for studying abroad.");
            return -1;
        }
    }

    /**
     * Calls add() from the roster class and prints appropriate result
     * @param student Student to be added
     * @param studentRoster Current user roster
     */
    private static void addStudent(Student student, Roster studentRoster)
    {
        if (studentRoster.add(student) == false)
            System.out.println("Student is already in the roster.");
        else
            System.out.println("Student added.");
    }

    /**
     * Parses through the rest of a tokenized string for when the user is adding a resident. Stops parsing if any of
     * the input was invalid
     * @param strTokens Tokenized string, starting after the initial command
     * @param studentRoster Current user roster
     */
    private static void addResident(StringTokenizer strTokens, Roster studentRoster) {
        Profile profile = makeProfile(strTokens);
        if (profile == null) return;

        int credits = checkCredits(strTokens, false);
        if (credits < 0) return;
        Resident student = new Resident(profile, credits);
        addStudent(student, studentRoster);
    }

    /**
     * Parses through the rest of a tokenized string for when the user is adding a nonresident. Stops parsing if any of
     * the input was invalid
     * @param strTokens Tokenized string, starting after the initial command
     * @param studentRoster Current user roster
     */
    private static void addNonResident(StringTokenizer strTokens, Roster studentRoster) {
        Profile profile = makeProfile(strTokens);
        if (profile == null) return;

        int credits = checkCredits(strTokens, false);
        if (credits < 0) return;

        NonResident student = new NonResident(profile, credits);

        addStudent(student, studentRoster);
    }

    /**
     * Parses through the rest of a tokenized string for when the user is adding a tristate student, including a new
     * input: the state the non-resident is from. Stops parsing if any of the input was invalid
     * @param strTokens Tokenized string, starting after the initial command
     * @param studentRoster Current user roster
     */
    private static void addTriState(StringTokenizer strTokens, Roster studentRoster) {
        Profile profile = makeProfile(strTokens);
        if (profile == null) return;

        int credits = checkCredits(strTokens, false);
        if (credits < 0) return;

        boolean stateBoolean = true;
        int stateInteger = checkState(strTokens);
        if(stateInteger < 0) return;
        else if (stateInteger == 0) stateBoolean = false;

        TriState student = new TriState(profile, credits, stateBoolean);
        addStudent(student, studentRoster);
    }

    /**
     * Parses through the rest of a tokenized string for when the user is adding an international student, including a new
     * input: the boolean if they study abroad or not. Stops parsing if any of the input was invalid
     * @param strTokens Tokenized string, starting after the initial command
     * @param studentRoster Current user roster
     */
    private static void addInternational(StringTokenizer strTokens, Roster studentRoster) {
        Profile profile = makeProfile(strTokens);
        if (profile == null) return;

        int credits = checkCredits(strTokens, true);
        if (credits < 0) return;

        boolean studyAbroadBoolean = true;
        int studyAbroadInteger = checkStudyAbroad(strTokens);
        if (studyAbroadInteger < 0) return;
        else if (studyAbroadInteger == 0) studyAbroadBoolean = false;

        International student = new International(profile, credits, studyAbroadBoolean);

        addStudent(student, studentRoster);
    }

    /**
     * Parses through the rest of a tokenized string for when the user is removing a student with appropriate print
     * statements on failure and success
     * @param strTokens Tokenized string, starting after the initial command
     * @param studentRoster Current user roster
     */

    private static void removeStudent(StringTokenizer strTokens, Roster studentRoster) {
        Profile profile = makeProfile(strTokens);
        if (profile == null) return;

        Student student = new Student(profile);

        if (studentRoster.remove(student) == false) System.out.println("Student is not in the roster.");
        else System.out.println("Student removed from the roster.");
    }

    /**
     * Calls calculate() in the Roster class and prints when successful
     * @param studentRoster Current user roster
     */
    private static void calculateTuition(Roster studentRoster) {
        studentRoster.calculate();
        System.out.println("Calculation completed.");
    }

    /**
     * Parses through the payment of tuition or financial aid for valid inputs
     * @param strTokens Tokenized string, starting where the payment/financial aid is
     * @param isFinancialAid True if the value is financial aid, false if it is a payment
     * @return The integer value of the payment/financial aid. -1 if invalid
     */
    private static double checkPayment(StringTokenizer strTokens, boolean isFinancialAid) {
        if (strTokens.hasMoreTokens() == false) {
            if (isFinancialAid) {
                System.out.println("Missing the amount.");
                return -1;
            }
            System.out.println("Payment amount missing.");
            return -1;
        }
        double payment;
        try {
            payment = Double.parseDouble(strTokens.nextToken());
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return -1;
        }
        if (payment <= 0) {
            System.out.println("Invalid amount.");
            return -1;
        }
        return payment;
    }

    /**
     * Parses through the date of a payment and checks for validity
     * @param strTokens Tokenized string, starting at the date of payment
     * @return Date object containing the payment date
     */
    private static Date checkDate(StringTokenizer strTokens) {
        if (strTokens.hasMoreTokens() == false) {
            System.out.println("Payment date is missing.");
            return null;
        }
        Date paymentDate = new Date(strTokens.nextToken());
        if (paymentDate.isValid() == false) {
            System.out.println("Payment date invalid.");
            return null;
        }
        return paymentDate;
    }

    /**
     * Parses through the input after the user calls to pay tuition, and calls pay() in the Roster class if valid.
     * Prints appropriate result on return of pay()
     * @param strTokens Tokenized string, starting after the initial pay command
     * @param studentRoster Current user roster
     */
    private static void payTuition(StringTokenizer strTokens, Roster studentRoster) {
        Profile profile = makeProfile(strTokens);
        if (profile == null) return;

        Student student = new Student(profile);

        double payment = checkPayment(strTokens, false);
        if (payment < 0) return;

        Date lastPaymentDate = checkDate(strTokens);
        if (lastPaymentDate == null) return;

        if (studentRoster.pay(student, payment, lastPaymentDate) < 0) {
            System.out.println("Amount is greater than amount due.");
        }
        else System.out.println("Payment applied.");
    }

    /**
     * Parses through the tokenized string and calls setAbroad() in Roster class, if valid. Prints appropriate result
     * on return of setAbroad()
     * @param strTokens Tokenized string, starting after the initial set study abroad command
     * @param studentRoster Current user roster
     */
    private static void setStudyAbroad(StringTokenizer strTokens, Roster studentRoster) {
        Profile profile = makeProfile(strTokens);
        if (profile == null) return;

        Student student = new Student(profile);

        if (studentRoster.setAbroad(student, true) == false) {
            System.out.println("Couldn't find the international student.");
        }
        else System.out.println("Tuition updated.");
    }

    /**
     * Parses through the string and calls setFinancialAid() in roster, if valid. Prints appropriate result
     * on return of setFinancialAid()
     * @param strTokens Tokenized string, starting after the set financial aid command
     * @param studentRoster Current user roster
     */
    private static void setFinancialAid(StringTokenizer strTokens, Roster studentRoster) {
        Profile profile = makeProfile(strTokens);
        if (profile == null) return;

        Student student = new Student(profile);

        double finAidAmount = checkPayment(strTokens, true);
        if (finAidAmount < 0) return;
        if (finAidAmount > Resident.getMaxfinAid()) {
            System.out.println("Invalid amount.");
            return;
        }

        switch (studentRoster.setFinancialAid(student, finAidAmount)) {
            case Roster.SUCCESS:
                System.out.println("Tuition updated.");
                break;
            case Roster.NOT_IN_ROSTER:
                System.out.println("Student not in the roster.");
                break;
            case Roster.NOT_RESIDENT:
                System.out.println("Not a resident student.");
                break;
            case Roster.PARTTIME:
                System.out.println("Parttime student doesn't qualify for the award.");
                break;
            case Roster.ALREADY_AWARDED:
                System.out.println("Awarded once already.");
                break;
        }

    }

    /**
     * Parses through the string the user inputted and calls the appropriate command from the roster class, sending
     * it to the appropriate helper method based on the command
     * @param strTokens the tokenized string, by commas, the user inputted
     * @param studentRoster the roster for this user
     * @return false if the user entered an invalid command, true otherwise
     */
    private boolean parseTokens(StringTokenizer strTokens, Roster studentRoster) {
        if(strTokens.hasMoreTokens() == false) return true;
        switch (strTokens.nextToken()) {
            case "AR":
                addResident(strTokens, studentRoster);
                break;
            case "AN":
                addNonResident(strTokens, studentRoster);
                break;
            case "AT":
                addTriState(strTokens, studentRoster);
                break;
            case "AI":
                addInternational(strTokens, studentRoster);
                break;
            case "R":
                removeStudent(strTokens, studentRoster);
                break;
            case "C":
                calculateTuition(studentRoster);
                break;
            case "T":
                payTuition(strTokens, studentRoster);
                break;
            case "S":
                setStudyAbroad(strTokens, studentRoster);
                break;
            case "F":
                setFinancialAid(strTokens, studentRoster);
                break;
            case "P":
                studentRoster.printRoster();
                break;
            case "PN":
                studentRoster.printByName();
                break;
            case "PT":
                studentRoster.printByDate();
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * The method called by RunProject2 that continuously runs in a while loop, taking in user inputs and calls
     * parseTokens for every line entered, until the user quits using "Q"
     */
    public void run() {
        Roster studentRoster = new Roster();
        System.out.println("Tuition Manager starts running.");
        Scanner sc = new Scanner(System.in);
        while(true) {
            String input = sc.nextLine();
            if (input.equals("Q")) exitManager();
            StringTokenizer strTokens = new StringTokenizer(input, ",");

            if (parseTokens(strTokens, studentRoster) == false)
                System.out.println("Command '" + input + "' not supported!");
        }
    }
}
