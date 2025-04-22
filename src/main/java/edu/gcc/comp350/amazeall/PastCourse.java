package edu.gcc.comp350.amazeall;

public class PastCourse {
    private String courseName;
    private int courseID;
    private int credits;

    public PastCourse(String courseName, int courseID, int credits) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.credits = credits;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCourseID() {
        return courseID;
    }

    public int getCredits() {
        return credits;
    }
}
