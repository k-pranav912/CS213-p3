package com.studentroster;

import java.text.DecimalFormat;

/**
 * Resident class, builds upon the student class.
 * Defines and creates further methods specific to residents on top of students.
 * @author Neel Prabhu, Saipranav Kalapala
 */
public class Resident extends Student{

    private double tuitionDue = 0;

    private static final int MAXFIN_AID = 10000;
    private static final int TUITION = 12536;
    private static final int TUITION_RATE = 404;

    private double finAid;

    /**
     * Constructor method, creates an instance of resident class based off of student superclass.
     * @param profile Profile instance; contains name and major.
     * @param credits Number of credits the student is taking.
     */
    public Resident(Profile profile, int credits) {
        super(profile, credits);
        this.finAid = 0;
    }

    /**
     * Getter method to return the max amount of financial aid a student can receive.
     * @return
     */
    public static int getMaxfinAid() {
        return MAXFIN_AID;
    }

    /**
     * Setter method to set the amount of financial aid a student receives.
     * @param amount amount of financial aid
     * @return true if student has not already taken aid.
     */
    public boolean setFinAid(double amount) {
        if (finAid > 0) return false;
        finAid = amount;
        tuitionDue();
        return true;
    }

    /**
     * Calculates the tuition due for a resident based off of the fee structure.
     */
    @Override
    public void tuitionDue() {
        if (super.getCreditHours() > super.getMaxPartTimeCredits()) {
            tuitionDue = TUITION + super.getUniversityFeeFullTime();
            int over16Credits = super.getCreditHours() - super.getFreeFullTimeCredits();
            if (over16Credits > 0) {
                tuitionDue += over16Credits * TUITION_RATE;
            }
        } else {
            tuitionDue = (TUITION_RATE * super.getCreditHours()) + super.getUniversityFeePartTime();
        }
        tuitionDue -= getTuitionPaid();
        tuitionDue -= finAid;
    }

    /**
     * Method to return the current tuition due for a resident student instance.
     * @return
     */
    @Override
    public double getTuition() {return tuitionDue;}



    /**
     * String representation of the Student instance.
     * Subclasses add onto this.
     * format: Name:Major:Credits:Tuition-Due:Total-Payment:Last-Payment-Date:Residency-Status
     * @return
     */
    @Override
    public String toString() {
        String result = super.toString();
        DecimalFormat df = new DecimalFormat("#,##0.00");

        result += ":tuition due:" + df.format(tuitionDue);

        result += ":total payment:" + df.format(getTuitionPaid());

        result += ":last payment date:";

        if (getLastPayment() == null) {
            result += " --/--/--:";
        } else {
            result += " " + getLastPayment().toString() + ":";
        }

        result += "resident";

        if (finAid > 0) {
            result += ":" + "financial aid $" + df.format(finAid);
        }

        return result;
    }
}
