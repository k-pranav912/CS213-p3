package com.studentroster;

/**
 * The enumertated class Major, which contains the different majors, as well as methods to convert
 * a string into a major and a major into an index or string
 * @author Neel Prabhu, Saipranav Kalapala
 */
public enum Major {
    CS, IT, BA, EE, ME;
    public static final int numMajors = 5;
    private static final int CSINDEX = 0;
    private static final int ITINDEX = 1;
    private static final int BAINDEX = 2;
    private static final int EEINDEX = 3;
    private static final int MEINDEX = 4;

    /**
     * Takes a major and returns its appropriate index.
     * @return the major index
     */
    public int toIndex() {
        switch (this) {
            case CS:
                return CSINDEX;
            case IT:
                return ITINDEX;
            case BA:
                return BAINDEX;
            case EE:
                return EEINDEX;
            case ME:
                return MEINDEX;
            default:
                return -1;
        }
    }

    /**
     * Takes a string from user input and converts the string into a major.
     * @param majorString the user inputted string that specifies the major of the album
     * @return the major that the string represents
     */
    public static Major toMajor(String majorString) {
        switch (majorString.toUpperCase()) {
            case "CS":
                return CS;
            case "IT":
                return IT;
            case "BA":
                return BA;
            case "EE":
                return EE;
            case "ME":
                return ME;
            default:
                return null;
        }
    }

    /**
     * Converts a major into the string.
     * @return the string name of the major given
     */
    @Override
    public String toString() {
        switch (this) {
            case CS:
                return "CS";
            case IT:
                return "IT";
            case BA:
                return "BA";
            case EE:
                return "EE";
            case ME:
                return "ME";
            default:
                return "";
        }
    }
}
