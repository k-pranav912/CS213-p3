package com.studentroster;

/**
 * Roster Class; handles the implementation of a roster of students.
 * Includes different students such as residents, non-residents, tri-state non-residents and international students.
 * @authors Neel Prabhu, Saipranav Kalapala
 */
public class Roster {
    private Student[] roster;
    private int size;

    private static final int ROSTER_INCREASE_SIZE = 4;
    private static final int NOT_FOUND = -1;
    public static final int SUCCESS = 0;
    public static final int NOT_IN_ROSTER = -1;
    public static final int NOT_RESIDENT = -2;
    public static final int PARTTIME = -3;
    public static final int ALREADY_AWARDED = -4;
    public static final int PAYMENT_INVALID = -5;


    /**
     * Constructor method that initializes a Roster instance.
     */
    public Roster() {
        this.size = 0;
        this.roster = new Student[ROSTER_INCREASE_SIZE];
    }

    public int getRosterSize() {
        return this.size;
    }

    public int getRosterLength() {
        return roster.length;
    }

    public Student getRosterStudent(int index) {
        return roster[index];
    }

    /**
     * Searchs through the roster to find the student
     * @param student Target student
     * @return Index of student if found, NOT_FOUND if not found.
     */
    private int find(Student student){
        for (int i = 0; i < roster.length; i++) {
            if (roster[i] != null && student.equals(roster[i])) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Grows the roster array by a size of ROSTER_INCREASE_SIZE
     */
    private void grow() {
        Student[] tempRoster;
        tempRoster = roster;
        roster = new Student[tempRoster.length + ROSTER_INCREASE_SIZE];

        for (int i = 0; i < tempRoster.length; i++) {
            roster[i] = tempRoster[i];
        }

    }

    /**
     * Finds the next empty element in the roster array
     * @return Index of empty element, NOT_FOUND if there are none
     */
    private int findNextEmpty() {
        for (int i = 0; i < roster.length; i++) {
            if (roster[i] == null) return i;
        }
        return NOT_FOUND;
    }

    /**
     * Adds a student to the roster array, if the student is not already in the array
     * @param student Student to be added
     * @return true if student was added, false if they already were in the roster
     */
    public boolean add(Student student) {
        if (find(student) >= 0) return false;
        int newStudentIndex = findNextEmpty();
        if (newStudentIndex < 0) {
            grow();
            newStudentIndex = findNextEmpty();
        }
        size++;
        roster[newStudentIndex] = student;
        return true;
    }

    /**
     * Removes the student from the roster array, if they were present
     * @param student Student to be removed
     * @return true if student was removed, false if they weren't in the array
     */
    public boolean remove(Student student) {
        int deletionIndex = find(student);
        if (deletionIndex < 0) return false;
        size--;
        roster[deletionIndex] = null;
        return true;
    }

    /**
     * Calculates the tuition of all students in the roster
     */
    public void calculate() {
        for (int i = 0; i < roster.length; i++) {
            if (roster[i] != null) roster[i].tuitionDue();
        }
    }

    /**
     * Calculates the individual tuition due of student
     * @param student Student who's tuition is to be calculated
     * @return tuition due if student is roster, false otherwise
     */
    public double calculateIndividual(Student student) {
        int calcIndex = find(student);
        if (calcIndex < 0) return -1;
        roster[calcIndex].tuitionDue();
        return roster[calcIndex].getTuition();
    }

    /**
     * Pays some/all of the tuition of the student in question, if the payment is less than or equal to the tuition due
     * @param student Student who's tuition is being paid
     * @param amountPaid Amount of tuition being paid
     * @param newPayment Date of the new payment
     * @return true if the pay was successful, false if the payment was greater than the amount of tuition required
     */
    public int pay(Student student, double amountPaid, Date newPayment) {
        int studentIndex = find(student);
        if (studentIndex < 0) return NOT_IN_ROSTER;
        if (amountPaid > roster[studentIndex].getTuition()) return PAYMENT_INVALID;
        roster[studentIndex].addTuitionPaid(amountPaid, newPayment);
        roster[studentIndex].tuitionDue();
        return SUCCESS;
    }

    /**
     * Sets the students study abroad status to true, if they were an international student
     * @param student Student to be set to studying abroad
     * @return true if successful, false if the student was not an international student
     */
    public boolean setAbroad(Student student, boolean value) {
        int studentIndex = find(student);
        if (studentIndex < 0) return false;
        if (roster[studentIndex] instanceof International) {
            ((International) roster[studentIndex]).setStudyAbroadStatus(value);
            return true;
        }
        return false;
    }

    /**
     * Sets the financial aid amount for the resident student, if it is a valid amount and resident
     * @param student Student to be given financial aid
     * @param finAidAmount Amount of financial aid given
     * @return SUCCESS - On success
     *         NOT_IN_ROSTER - Student was not in the roster
     *         NOT_RESIDENT - Student is not a resident
     *         PARTTIME - Student is not a full time student
     *         ALREADY_AWARDED - Student has already been given financial aid
     */
    public int setFinancialAid(Student student, double finAidAmount) {
        int studentIndex = find(student);
        if (studentIndex < 0) return NOT_IN_ROSTER;

        if (!(roster[studentIndex] instanceof Resident)) {
            return NOT_RESIDENT;
        }
        if (roster[studentIndex].isPartTime()) {
            return PARTTIME;
        }
        if (((Resident) roster[studentIndex]).setFinAid(finAidAmount) == false) {
            return ALREADY_AWARDED;
        }
        return SUCCESS;
    }

    /**
     * Prints the roster of students
     */
    public void printRoster() {
        if (size <= 0) {
            System.out.println("Student roster is empty!");
            return;
        }
        System.out.println("* list of students in the roster **");
        for (int i = 0; i < roster.length; i++) {
            if (roster[i] != null) System.out.println(roster[i]);
        }
        System.out.println("* end of roster **");
    }

    /**
     * Prints the roster of students, sorted by their names
     */
    public void printByName() {
        if (size <= 0) {
            System.out.println("Student roster is empty!");
            return;
        }

        String[] result = new String[size];

        System.out.println("* list of students ordered by name **");;
        int counterIndex = 0;
        for (int i = 0; i < roster.length; i++) {
            if (roster[i] != null) {
                result[counterIndex] = roster[i].toString();
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
            System.out.println(result[i]);
        }

        System.out.println("* end of roster **");

    }

    /**
     * Method to check if a certain value (key) exists in the integer array.
     * @param arr Integer array
     * @param key Integer value
     * @return boolean; true if key exists in the array, false otherwise.
     */
    private boolean checkArray(int[] arr, int key)
    {
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] == key) return true;
        }
        return false;
    }

    /**
     * Method to sort an integer array using Insertion Sort.
     * @param arr Integer array.
     * @return Integer array sorted in the ascending order.
     */
    public static int[] arrSort(int[] arr)
    {
        for (int i = 0; i < arr.length; i++)
        {
            for (int j = i+1; j < arr.length; j++)
            {
                int temp = 0;
                if (arr[i] > arr[j]) {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }

        return arr;
    }

    /**
     * Method to generate an array based on the indexed dates of all the student payments in the roster.
     * Parses through the collection, collects all the indexed dates from the albums into an array,
     * and sorts the array in ascending order.
     * @return sorted array containing all the indexed dates of the albums in the roster.
     */
    int[] genDateArray()
    {
        int[] tempArr = new int[size];
        int arrIndex = 0;

        for (int i = 0; i < roster.length; i++)
        {
            if ((roster[i] != null && roster[i].getLastPayment() != null) && !checkArray(tempArr, roster[i].getDateInt()))
            {
                tempArr[arrIndex] = roster[i].getDateInt();
                arrIndex++;
            }
        }

        tempArr = arrSort(tempArr);

        return tempArr;
    }

    /**
     * Prints the roster of students, sorted by their last payment date
     */
    public void printByDate()
    {
        if (size <= 0)
        {
            System.out.println("Student roster is empty!");
            return;
        }

        int[] releaseDates = genDateArray();

        System.out.println("* list of students made payments ordered by payment date **");
        for (int i = 0; i < releaseDates.length; i++)
        {
            if (releaseDates[i] == 0) continue;

            for (int j = 0; j < roster.length; j++)
            {
                if ((roster[j] != null && roster[j].getLastPayment() != null) && roster[j].getDateInt() == releaseDates[i]) System.out.println(roster[j]);
            }
        }
        System.out.println("* end of roster **");

    }

}
