package edu.gcc.comp350.amazeall;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class SemesterScheduleTest {

    @Test
    void removeCourse1() {
        SemesterSchedule semester = new SemesterSchedule(new AllSemesters("",new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        ArrayList<Course> results = new ArrayList<Course>();
        String string = "WRIT101B";
        for (Iterator<Course> iterator = semester.getCourseList().iterator(); iterator.hasNext();) {
            Course course = iterator.next();
            if (string.equals(course.getCourseID())) {
                iterator.remove();
            }
        }
        assertEquals(0, semester.getCourseList().size());
    }

    @Test
    void removeCourse2() {
        SemesterSchedule semester = new SemesterSchedule(new AllSemesters("",new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        CourseSearch courseSearch = new CourseSearch();
        String string = "WRIT101B";
        ArrayList<Course> results = (courseSearch.searchCourses(string, "", "", "", "",""));
        System.out.println(string);
        for (int i = 0; i < results.size(); i++) {
            if (string.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
                System.out.println("hi" + semester.getCourseList());
            }
        }
        String string1 = "COMP350A";
        results = (courseSearch.searchCourses(string1, "", "", "", "",""));
        for (int i = 0; i < results.size(); i++) {
            if (string1.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }
        for (Iterator<Course> iterator = semester.getCourseList().iterator(); iterator.hasNext();) {
            Course course = iterator.next();
            if (string.equals(course.getCourseID())) {
                iterator.remove();
            }
        }
        assertEquals(1, semester.getCourseList().size());
    }

    @Test
    void changeSection() {
        SemesterSchedule semester = new SemesterSchedule(new AllSemesters("",new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        CourseSearch courseSearch = new CourseSearch();

        String currentlyTaking = "COMP350A";
        ArrayList<Course> results = (courseSearch.searchCourses(currentlyTaking, "", "", "", "",""));
        for (int i = 0; i < results.size(); i++) {
            if (currentlyTaking.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }
        String toSwitchToo = "COMP350B";
        results = (courseSearch.searchCourses(toSwitchToo, "", "", "", "",""));
        // searching through added courses
        for (Course course: semester.getCourseList()) {

            if (currentlyTaking.equals(course.getCourseID())) {
                System.out.println("true");
                // searching through searched courses
                for (Course course1: results) {

                    if (toSwitchToo.equals(course1.getCourseID())) {
                        System.out.println("true");
                        semester.changeSection(course, course1);
                        System.out.println(semester.getCourseList().get(0).getCourseID());
                    }
                }
            }
        }
        assertEquals("COMP350B", semester.getCourseList().get(0).getCourseID());
    }

    @Test
    void addCourse1() {
        SemesterSchedule semester = new SemesterSchedule(new AllSemesters("",new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        CourseSearch courseSearch = new CourseSearch();
        String string = "ACCT403B";
        ArrayList<Course> results = (courseSearch.searchCourses(string, "", "", "", "",""));
        for (int i = 0; i < results.size(); i++) {
            if (string.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }
        assertEquals("ACCT403B", semester.getCourseList().get(0).getCourseID());
    }

    @Test
    void addCourse2() {
        SemesterSchedule semester = new SemesterSchedule(new AllSemesters("",new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        CourseSearch courseSearch = new CourseSearch();
        String string = "COMP220A";
        ArrayList<Course> results = (courseSearch.searchCourses(string, "", "", "", "",""));
        for (int i = 0; i < results.size(); i++) {
            if (string.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }
        String string1 = "COMP220B";
        for (int i = 0; i < results.size(); i++) {
            if (string1.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }
        assertEquals(1, semester.getCourseList().size());
    }

    /*@Test
    void addCourse3() {
        SemesterSchedule semester = new SemesterSchedule();
        CourseSearch courseSearch = new CourseSearch();
        String string = "COMP220B";
        ArrayList<Course> results = (courseSearch.searchCourses(string, "", "", "", ""));
        for (int i = 0; i < results.size(); i++) {
            if (string.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }
        String string1 = "COMP340A";
        results = (courseSearch.searchCourses(string1, "", "", "", ""));
        for (int i = 0; i < results.size(); i++) {
            if (string1.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }
        System.out.println(semester.getCourseList().get(0).getCourseID());
        System.out.println(semester.getCourseList().get(0).getStartTime());
        System.out.println(semester.getCourseList().get(1).getCourseID());
        System.out.println(semester.getCourseList().get(1).getStartTime());
        assertEquals(1, semester.getCourseList().size());
    }*/

    @Test
    void getCredits() {
        SemesterSchedule semester = new SemesterSchedule(new AllSemesters("",new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        CourseSearch courseSearch = new CourseSearch();
        String string = "HUMA303B";
        ArrayList<Course> results = (courseSearch.searchCourses(string, "", "", "", "",""));
        for (int i = 0; i < results.size(); i++) {
            if (string.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }
        assertEquals(3, semester.getCredits());
    }

    @Test
    void undoAction1() {
        AllSemesters all = new AllSemesters("",new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        SemesterSchedule semester = new SemesterSchedule(all);
        CourseSearch courseSearch = new CourseSearch();
        String string = "HUMA301A";
        ArrayList<Course> results = (courseSearch.searchCourses(string, "", "", "", "",""));
        for (int i = 0; i < results.size(); i++) {
            if (string.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }
        semester.undoAction();
        assertEquals(0, semester.getCredits());
    }

    @Test
    void undoAction2() {
        AllSemesters all = new AllSemesters("",new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        SemesterSchedule semester = new SemesterSchedule(all);
        CourseSearch courseSearch = new CourseSearch();
        String string = "HUMA301A";
        ArrayList<Course> results = (courseSearch.searchCourses(string, "", "", "", "",""));
        for (int i = 0; i < results.size(); i++) {
            if (string.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }
        string = "COMP350A";
        results = (courseSearch.searchCourses(string, "", "", "", "",""));
        for (int i = 0; i < results.size(); i++) {
            if (string.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }

//        System.out.print("Courses before removing: ");
//        for (int i = 0; i < semester.getCourseList().size(); i++) {
//            System.out.print(semester.getCourseList().get(i).getCourseID() + ", ");
//        }
        System.out.println("This is the stack: " + semester.getStack());
        semester.setStack(semester.undoAction());
        //semester.stack.pop();
        System.out.println("after removing once: " + semester.getStack());
        semester.setStack(semester.undoAction());
        //semester.stack.pop();
        System.out.println("after removing twice: " + semester.getStack());


        assertEquals(0, semester.getCredits());
    }

    @Test
    void undoAction3() {
        AllSemesters all = new AllSemesters("undo3",new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        SemesterSchedule semester = new SemesterSchedule(all);
        CourseSearch courseSearch = new CourseSearch();
        String string = "HUMA301A";
        ArrayList<Course> results = (courseSearch.searchCourses(string, "", "", "", "",""));
        for (int i = 0; i < results.size(); i++) {
            if (string.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }
        string = "COMP350A";
        results = (courseSearch.searchCourses(string, "", "", "", "",""));
        for (int i = 0; i < results.size(); i++) {
            if (string.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }

        string = "HUMA301A";
        for (int i = 0; i < semester.getCourseList().size(); i++) {
            if (string.equals(semester.getCourseList().get(i).getCourseID())) {
                semester.removeCourse(semester.getCourseList().get(i));
            }
        }
//        System.out.print("Courses before removing: ");
//        for (int i = 0; i < semester.getCourseList().size(); i++) {
//            System.out.print(semester.getCourseList().get(i).getCourseID() + ", ");
//        }
        System.out.println("This is the stack: " + semester.getStack());

        semester.setStack(semester.undoAction());
        System.out.println("after undoing once: " + semester.getStack());

        semester.setStack(semester.undoAction());
        System.out.println("after undoing twice: " + semester.getStack());

        assertEquals("HUMA301A", semester.getCourseList().get(0).getCourseID());
    }

    @Test
    void Logging() throws FileNotFoundException {
        ArrayList<String> takenCourses = new ArrayList<String>();
        AllSemesters parent = new AllSemesters("hello",new ArrayList<>(), new ArrayList<>(), takenCourses);
        SemesterSchedule semester = new SemesterSchedule(parent);
        CourseSearch courseSearch = new CourseSearch();
        String string = "HUMA301B";
        ArrayList<Course> results = (courseSearch.searchCourses(string, "", "", "", "",""));
        for (int i = 0; i < results.size(); i++) {
            if (string.equals(results.get(i).getCourseID())) {
                semester.addCourse(results.get(i));
            }
        }

        semester.setCourseList(semester.restoreCourses());
        System.out.println(semester.getCourseList());
        assertEquals("HUMA301B", semester.getCourseList().get(0).getCourseID());
    }
}