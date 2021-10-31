package com.studentroster;

/**
 * Student class; superclass of the students in the roster, handles the implementation of student objects.
 * Defines common instance variables and methods required by all the students.
 * @author Neel Prabhu, Saipranav Kalapala
 */
public class Student {
    private Profile profile;
    private int creditHours;
    private double tuitionPaid;
    private Date lastPayment;

    private static final int MIN_CREDITS = 3;
    private static final int MAX_CREDITS = 24;
    private static final int MAX_PART_TIME_CREDITS = 11;
    private static final int FREE_FULL_TIME_CREDITS = 16;
    private static final int UNIVERSITY_FEE_FULL_TIME = 3268;
    private static final double UNIVERSITY_FEE_PART_TIME = 0.80 * UNIVERSITY_FEE_FULL_TIME;

    /**
     * Constructor method; creates an instance of the student class with profile and the number of credits.
     * @param profile Profile instance; contains name and major.
     * @param credits Number of credits the student is taking.
     */
    public Student (Profile profile, int credits) {
        this.profile = profile;
        this.creditHours = credits;
        this.tuitionPaid = 0;
        this.lastPayment = null;
    }

    /**
     * Constructor method; creates an instance of the student class with just profile.
     * Used for comparing purposes.
     * @param profile Profile instance; contains name and major.
     */
    public Student (Profile profile) {
        this.profile = profile;
    }

    /**
     * Empty method which is overridden in subclasses.
     */
    public void tuitionDue(){
    }

    /**
     * Getter method to get the amount of tuition currently paid.
     * @return tuitionPaid; amount of tuition paid.
     */
    public double getTuitionPaid() {
        return tuitionPaid;
    }

    /**
     * Method to add to the amount of tuition paid, along with the date of the payment.
     * @param inputValue double value of the amount of tuition being paid.
     * @param date Date of the payment.
     */
    public void addTuitionPaid(double inputValue, Date date) {
        tuitionPaid += inputValue;
        lastPayment = date;
    }

    /**
     * Getter method to return the min credits a student can take.
     * @return min credits a student can take.
     */
    public static int getMinCredits() {
        return MIN_CREDITS;
    }

    /**
     * Getter method to return the max credits a student can take.
     * @return max credits a student can take.
     */
    public static int getMaxCredits() {
        return MAX_CREDITS;
    }

    /**
     * Getter method to return the max credits a student can take to be considered as a part-time student.
     * @return max credits a student can take to be considered as a part-time student.
     */
    public int getMaxPartTimeCredits() {
        return MAX_PART_TIME_CREDITS;
    }

    /**
     * Getter method to return the current number of credits a student is taking.
     * @return current number of credits a student is taking.
     */
    public int getCreditHours() {
        return this.creditHours;
    }

    /**
     * Setter method to set the number of credits a student is currently taking.
     * @param newCreditHours number of credits a student is currently taking.
     */
    public void setCreditHours(int newCreditHours) {
        this.creditHours = newCreditHours;
    }

    /**
     * Resets the total amount of tuition paid and set the last payment date to null.
     * Used for international student study abroad.
     */
    public void resetTuitionPaid() {
        this.tuitionPaid = 0;
        this.lastPayment = null;
    }

    /**
     * Getter method to return the university fee for full-time students.
     * @return university fee for full-time students
     */
    public static int getUniversityFeeFullTime() {
        return UNIVERSITY_FEE_FULL_TIME;
    }

    /**
     * Getter method to return the university fee for part-time students.
     * @return university fee for part-time students.
     */
    public static double getUniversityFeePartTime() {
        return UNIVERSITY_FEE_PART_TIME;
    }

    /**
     * Getter method to return the number of full-time credits a student can take without additional fees.
     * @return number of full-time credits a student can take without additional fees.
     */
    public static int getFreeFullTimeCredits() {
        return FREE_FULL_TIME_CREDITS;
    }

    /**
     * Empty method which is overridden in subclasses.
     * @return
     */
    protected double getTuition(){return 0;}

    /**
     * Mehtod to check whether a student is part-time
     * @return boolean; true if part-time; false otherwise.
     */
    public boolean isPartTime() {
        if (creditHours > MAX_PART_TIME_CREDITS) return false;
        return true;
    }

    /**
     * Getter method for returning the date in a "yyyymmdd" format for sorting purposes.
     * @return date in a "yyyymmdd" format
     */
    public int getDateInt() {
        return lastPayment.getDateIndex();
    }

    /**
     * Getter mehtod to return the date of the last payment.
     * @return Date instance of the last payment.
     */
    public Date getLastPayment() {
        return lastPayment;
    }

    /**
     * String representation of the Student instance.
     * Subclasses add onto this.
     * format: Name:Major:Credits
     * @return
     */
    @Override
    public String toString() {
        return this.profile.toString() + ":" + creditHours + " credit hours";
    }

    /**
     * Equals method to compare whether two instances of a Student are the same.
     * @param object Student object
     * @return boolean; true if name and major match; false otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null || this == null) return false;

        if (!(object instanceof Student)) return false;

        Student student = (Student) object;

        return student.profile.equals(this.profile);
    }
}
