package edu.gcc.comp350.amazeall;

import java.sql.Time;
import java.util.ArrayList;

public class Course implements java.io.Serializable{
    private String courseID;
    private String courseName;
    private int credits;
    private int totalSeats;
    private String daysMet;
    private Time startTime;
    private Time endTime;
    private String profName;
    private String prerequisites;

    public Course(String courseID, String courseName, int credits, int totalSeats, String daysMet, Time startTime, Time endTime, String profName, String prerequisites) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.credits = credits;
        this.totalSeats = totalSeats;
        this.daysMet = daysMet;
        this.startTime = startTime;
        this.endTime = endTime;
        this.profName = profName;
        this.prerequisites = prerequisites;
    }
    //DO NOT REMOVE, NEEDED FOR HIBERNATE
    public Course(){}
    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public String getDaysMet() {
        return daysMet;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public Time getEndTime() {
        return endTime;
    }

    public String getProfName() {
        return profName;
    }

    public String getPrerequisites() {
        return prerequisites;
    }
}