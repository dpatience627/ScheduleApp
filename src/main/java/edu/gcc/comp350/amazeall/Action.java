package edu.gcc.comp350.amazeall;

public class Action implements java.io.Serializable {

    private String name;
    private String action;
    private Course course;
    private int semester;

    public Action (String name, String action, Course course, int semester) {
        this.name = name;
        this.action = action;
        this.course = course;
        this.semester = semester;
    }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getSemester() { return semester; }

    public void setSemester(int semester) { this.semester = semester; }
}
