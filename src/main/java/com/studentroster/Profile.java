package com.studentroster;

/**
 * Profile class; handles the implementation of a student profile
 * stores the name and the major of a student.
 * @author Neel prabhu, Saipranav Kalapala
 */
public class Profile {
    private String name;
    private Major major;

    /**
     * Constructor Method to create the profile instance.
     * @param name Name of the student.
     * @param major Major instance of the student.
     */
    public Profile(String name, Major major) {
        this.name = name;
        this.major = major;
    }

    /**
     * Method to check whether two instances of Profile are the same.
     * check the name and major of each instances to see if they match.
     * @param object Profile instance.
     * @return boolean; true if both name and major of both instances match.
     */
    @Override
    public boolean equals(Object object) {
        if ((object == null) || (this == null)) return false;

        if (!(object instanceof Profile)) return false;

        Profile obj = (Profile) object;

        if ((obj.name == null) || (this.name == null)) return false;
        if ((obj.major == null) || (this.major ==  null)) return false;

        return this.name.equals(obj.name) && (this.major == obj.major);
    }

    /**
     * String representation of the Profile instance.
     * @return String; format: Name:Major
     */
    @Override
    public String toString() {
        return this.name + ":" + this.major;
    }
}
