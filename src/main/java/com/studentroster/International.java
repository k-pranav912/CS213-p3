package com.studentroster;

import java.text.DecimalFormat;

/**
 * Intenational class, builds upon the non-resident class.
 * Defines and creates further methods specific to non-resident international students on top of residents.
 * @author Neel Prabhu, Saipranav Kalapala
 */
public class International extends NonResident{
    private boolean isStudyAbroad;
    private double tuitionDue = 0;

    private static final int MIN_INTERNATIONAL_CREDITS = 12;
    private static final int ADDITIONAL_FEE = 2650;

    /**
     * Constructor method, creates an instance of international class based off of non-resident superclass.
     * @param profile Profile instance; contains name and major.
     * @param credits Number of credits the student is taking.
     * @param studyAbroad true if the student is currently studying abroad, false if not.
     */
    public International(Profile profile, int credits, boolean studyAbroad) {
        super(profile, credits);
        this.isStudyAbroad = studyAbroad;
    }

    /**
     * Getter method to return the min credits an international student can take.
     * @return min credits an international student can take.
     */
    public static int getMinInternationalCredits() { return MIN_INTERNATIONAL_CREDITS; }

    /**
     * Setter method to set the study-abroad status of an international student.
     */
    public void setStudyAbroadStatus(boolean value) {
        this.isStudyAbroad = value;

        if (value) {
            if (this.getCreditHours() > MIN_INTERNATIONAL_CREDITS) this.setCreditHours(MIN_INTERNATIONAL_CREDITS);
            this.resetTuitionPaid();
            this.tuitionDue = ADDITIONAL_FEE + getUniversityFeeFullTime();
        }
    }

    /**
     * Calculates the tuition due for a non-resident international student based off of the fee structure.
     */
    @Override
    public void tuitionDue() {
        if (!isStudyAbroad) {
            super.tuitionDue();
            tuitionDue = super.getTuition();
            tuitionDue += ADDITIONAL_FEE;
        } else {
            tuitionDue = 0;
            tuitionDue += ADDITIONAL_FEE;
            tuitionDue += getUniversityFeeFullTime();
            tuitionDue -= getTuitionPaid();
        }
        //tuitionDue -= getTuitionPaid();
    }

    /**
     * Method to return the current tuition due for a non-resident international student instance.
     * @return
     */
    @Override
    public double getTuition() {
        return tuitionDue;
    }

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
        String replacementString = "tuition due:" + df.format(super.getTuition());
        result = result.replaceAll(replacementString, "tuition due:" + df.format(this.tuitionDue));

        result += ":international";

        if (isStudyAbroad) {
            result += ":study abroad";
        }
        return result;
    }
}
