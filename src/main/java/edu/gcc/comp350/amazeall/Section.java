package edu.gcc.comp350.amazeall;

import java.util.ArrayList;

public class Section implements java.io.Serializable {
    private String CourseName;
    private ArrayList<Course> CourseSections;


    public Section(String name){
        this.CourseName = name;
        generateSections();
    }
    public Course selectCourseSection(int toChoose){
        return CourseSections.get(toChoose);
    }
    public void displaySections(){
        for(Course e : CourseSections){
            System.out.println(e.getCourseID());
        }
    }

    private void generateSections(){
        this.CourseSections = CourseSearch.simplifiedSearch(CourseName);
    }

    public String getCourseName() {
        return CourseName;
    }

    public ArrayList<Course> getCourseSections() {
        return CourseSections;
    }
}
