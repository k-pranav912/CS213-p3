package com.studentroster;
import java.util.Calendar;

/**
 * Date class, which handles the implementation of a date system in the mm/dd/yyyy format
 * The class either constructs an instance of date using a string in the format of "mm/dd/yyyy" or
 * uses the Calendar class to create a date from the current locale. Implements the Comparable method
 * to compare two instances of Date.
 * @author  Neel Prabhu, Saipranav Kalapala
 */
public class Date implements Comparable<Date>{
    private int year;
    private int month;
    private int day;

    private static final int QUADRENNIAL = 4;
    private static final int CENTENNIAL = 100;
    private static final int QUATERCENTENNIAL = 400;

    private static final int[] thirtyOneMonths = {1,3,5,7,8,10,12};
    private static final int maxMonths = 12;
    private static final int febMonth = 2;
    private static final int thirtyOneDays = 31;
    private static final int thirtyDays = 30;
    private static final int febNonLeapDays = 28;
    private static final int febLeapDays = 29;
    private static final int minYear = 2021;

    /**
     * Constructor method which creates an instance of Date using the String format "mm/dd/yyyy"
     * @param date String literal in "mm/dd/yyyy" format
     */
    public Date(String date)
    {
        String[] tokens = date.split("/");

        this.year = Integer.parseInt(tokens[2]);
        this.month = Integer.parseInt(tokens[0]);
        this.day = Integer.parseInt(tokens[1]);
    }

    /**
     * Constructor method which creates an instance of Date using the Calendar class.
     */
    public Date()
    {
        Calendar calInstance = Calendar.getInstance();

        this.year =  calInstance.get(Calendar.YEAR);
        this.month = calInstance.get(Calendar.MONTH) + 1;
        this.day = calInstance.get(Calendar.DATE);
    } //create an object with today's date (see Calendar class)

    /**
     * Method to check if the year in a given date is a leap year.
     * @param date An instance of Date.
     * @return boolean value; true if the year is a leap year, false otherwise.
     */
    private static boolean isLeap(Date date)
    {
        int start = 0;
        switch (start)
        {
            case 0:
                if (!(date.year % QUADRENNIAL == 0)) return false;
            case 1:
                if (!(date.year % CENTENNIAL == 0)) return true;
            case 2:
                if (!(date.year % QUATERCENTENNIAL == 0)) return false;
                else return true;
        }
        return false;
    }

    /**
     * Helper method to check if a certain value exists in an array.
     * @param arr integer array
     * @param key Integer value
     * @return boolean; true if key exists in arr, false otherwise.
     */
    private static boolean check(int[] arr, int key)
    {
        for (int j : arr) {
            if (j == key) return true;
        }
        return false;
    }

    /**
     * Method that check whether a Date instance is valid or not.
     * Conditions for validity:
     * Current year > Year > 2021
     * 0 < Day <= 28/29/30/31 (based on month)
     * 0 < Month <= 12
     * @return boolean; true is Date is a valid instance, false otherwise.
     */
    public boolean isValid()
    {
        if (this.year < minYear) return false;
        if (this.month <= 0 || this.day <= 0) return false;
        if (this.month > maxMonths) return false;

        Date tempDate = new Date();
        if (this.compareTo(tempDate) == 1) return false;

        if (this.month == febMonth)
        {
            if (isLeap(this))
            {
                return (this.day <= febLeapDays);
            }
            else
            {
                return (this.day <= febNonLeapDays);
            }
        }
        if (check(thirtyOneMonths, this.month))
        {
            return (this.day <= thirtyOneDays);
        }
        else
        {
            return this.day <= thirtyDays;
        }
    }

    /**
     * Mehtod to compare a Date Instance with another target Date instance.
     * @param date Date instance to be compared to.
     * @return Integer; < 1 if Target Date is later, 0 if Target date is same, 1 if Target Date is earlier.
     */
    @Override
    public int compareTo(Date date)
    {
        if (this.year == date.year)
        {
            if (this.month == date.month)
            {
                return Integer.compare(this.day, date.day);
            }
            else
            {
                return Integer.compare(this.month, date.month);
            }
        }
        else if (this.year < date.year)
        {
            return -1;
        }
        return 1;
    }

    /**
     * Method to return a String literal of the Date instance.
     * @return String literal in the "mm/dd/yyyy" format.
     */
    @Override
    public String toString()
    {
        return this.month + "/" + this.day + "/" + this.year;
    }

    /**
     * Method to return an indexed integer representation of the Date instance.
     * @return integer; yyyymmdd format.
     */
    public int getDateIndex()
    {
        return (this.year * 10000) + (this.month * 100) + (this.day);
    }

    /**
     * Testbed main function for the Date class.
     * @param args Commandline arguments
     */
    public static void main(String[] args)
    {

        // Test 1
        // valid date
        Date test1 = new Date("08/31/2000");
        if (test1.isValid()) {
            System.out.println("Test1: Valid Date!");
        } else {
            System.out.println("Test1: Invalid Date!");
        }

        // Test 2
        //valid date with Calendar
        Date test2 = new Date();
        if (test2.isValid()) {
            System.out.println("Test2: Valid Date!");
        } else {
            System.out.println("Test2: Invalid Date!");
        }

        // Test 3
        // Year less than 1980
        Date test3 = new Date("08/31/1979");
        if (test3.isValid()) {
            System.out.println("Test3: Valid Date!");
        } else {
            System.out.println("Test3: Invalid Date!");
        }

        // Test 4
        // Invalid month (<= 0)
        Date test4 = new Date("0/12/1999");
        if (test4.isValid()) {
            System.out.println("Test4: Valid Date!");
        } else {
            System.out.println("Test4: Invalid Date!");
        }

        // Test 5
        // Invalid month (> 12)
        Date test5 = new Date("13/31/1999");
        if (test5.isValid()) {
            System.out.println("Test5: Valid Date!");
        } else {
            System.out.println("Test5: Invalid Date!");
        }

        // Test 6
        // Invalid day (<= 0)
        Date test6 = new Date("08/0/1999");
        if (test6.isValid()) {
            System.out.println("Test6: Valid Date!");
        } else {
            System.out.println("Test6: Invalid Date!");
        }

        //Test 7
        // Invalid day (> 31)
        Date test7 = new Date("08/32/1999");
        if (test7.isValid()) {
            System.out.println("Test7: Valid Date!");
        } else {
            System.out.println("Test7: Invalid Date!");
        }

        //Test 8
        // Invalid date (> Current Calendar Date)
        Date test8 = new Date("08/31/2025");
        if (test8.isValid()) {
            System.out.println("Test8: Valid Date!");
        } else {
            System.out.println("Test8: Invalid Date!");
        }

        //Test 9
        // Invalid Feb date (>28 on non-Leap Year)
        Date test9 = new Date("02/29/2001");
        if (test9.isValid()) {
            System.out.println("Test9: Valid Date!");
        } else {
            System.out.println("Test9: Invalid Date!");
        }

        //Test 10
        // Invalid date (31 days in April)
        Date test10 = new Date("04/31/1999");
        if (test10.isValid()) {
            System.out.println("Test10: Valid Date!");
        } else {
            System.out.println("Test10: Invalid Date!");
        }

    }

}
