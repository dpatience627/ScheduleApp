package edu.gcc.comp350.amazeall;

import edu.gcc.comp350.amazeall.AllSemesters;
import edu.gcc.comp350.amazeall.SemesterSchedule;

import java.util.ArrayList;

public class Student {

    private String name;
    private String major;
    private String minor;
    private ArrayList<AllSemesters> careers = new ArrayList<AllSemesters>();
    static private CourseSearch courseSearch = new CourseSearch();

    public Student (String name, String major, String minor) {
        this.name = name;
        this.major = major;
        this.minor = minor;
    }

    public ArrayList<Course> getAllCourses () {
        ArrayList<Course> results = new ArrayList<Course>();
        // set max results to 1000
        results = (courseSearch.searchCourses("", "", "", "", "", ""));

        return results;
    }

    public ArrayList<Course> checkSemesters (AllSemesters a) {
        ArrayList<SemesterSchedule> listOfSemesters = a.getSemesterList();
        for (int i = 0; i < listOfSemesters.size(); i++) {
            // checking individual semesters
            SemesterSchedule semester = listOfSemesters.get(i);
            for (int j = 0; j < semester.getCourseList().size(); j++) {
                // going through courseList from each semester
                for (Course course : semester.getCourseList()) {
                    // add course to a checked arraylist

                }
            }
        }

        return null;
    }

}
