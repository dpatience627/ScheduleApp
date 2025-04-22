package edu.gcc.comp350.amazeall;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import com.mysql.cj.log.Log;
import javafx.scene.Parent;
import org.apache.logging.log4j.*;
public class SemesterSchedule implements java.io.Serializable {
    private AllSemesters parent;
    private ArrayList<Course> courseList;
    private ArrayList<Event> eventList;
    private int credits;
    private static Logger schLog = LogManager.getLogger("SemesterSchLog");
    private Stack<Action> stack;
    static private LogEntry log;


    public SemesterSchedule(AllSemesters parent) {
        this.parent = parent;
        courseList = new ArrayList<Course>();
        credits = 0;
        stack = new Stack<Action>();
        log = new LogEntry();
    }

    /**
     * Adds a course to schedule
     * @param toAdd
     * @throws RuntimeException
     */
    public void addCourse(Course toAdd) throws RuntimeException {
        if (isFreeTimeslot(toAdd) && !isSameCourse(toAdd)) {
            courseList.add(toAdd);
            credits += toAdd.getCredits();
            parent.addTakenCourse(toAdd);

            // undo function setup
            Action action = new Action(parent.getScheduleName(), "ADD", toAdd, parent.getCurrentSemester());
            action.setName(parent.getScheduleName());
            action.setAction("ADD");
            action.setCourse(toAdd);
            stack.push(action);

            System.out.println("Action being sent: " + action.getName() + " " + action.getAction() + " " + action.getCourse().getCourseID() + " " + parent.getCurrentSemester());
            try {
                LogEntry.sendToDatabase(action.getName(), action.getAction(), toAdd.getCourseID(), parent.getCurrentSemester());
            }catch (Exception e){
                e.printStackTrace();
            }
            schLog.log(Level.INFO, "ADD: " + toAdd.getCourseID(), toAdd.getCourseID());
        } else {
            schLog.info("Unable to add course to schedule: " + toAdd.getCourseID());
            throw new RuntimeException("addCourse() threw an error");
        }
    }

    /**
     * This add function is only for the restore method,
     * so that duplicate entries are not added to the database
     * @param toAdd
     * @throws RuntimeException
     */
    public void restoreAdd(Course toAdd) throws RuntimeException {
        if (isFreeTimeslot(toAdd) && !isSameCourse(toAdd)) {
            courseList.add(toAdd);
            credits += toAdd.getCredits();
            parent.addTakenCourse(toAdd);

            // undo function setup
            Action action = new Action(parent.getScheduleName(), "ADD", toAdd, parent.getCurrentSemester());
            action.setName(parent.getScheduleName());
            action.setAction("ADD");
            action.setCourse(toAdd);
            stack.push(action);

            schLog.log(Level.INFO, "ADD: " + toAdd.getCourseID(), toAdd.getCourseID());
        } else {
            schLog.info("Unable to add course to schedule: " + toAdd.getCourseID());
            throw new RuntimeException("addCourse() threw an error");
        }
    }

    /**
     * Removes the specified course from the class schedule
     * @param toRemove
     * @throws RuntimeException
     */
    public void removeCourse(Course toRemove) throws RuntimeException {

        if (courseList.contains(toRemove)) {
            courseList.remove(toRemove);
            credits -= toRemove.getCredits();
            parent.removeTakenCourse(toRemove);

            // undo function setup
            Action action = new Action(parent.getScheduleName(), "REMOVE", toRemove, parent.getCurrentSemester());
            action.setName(parent.getScheduleName());
            action.setAction("REMOVE");
            action.setCourse(toRemove);
            stack.push(action);

            System.out.println("Action being sent: " + action.getName() + " " + action.getAction() + " " + action.getCourse().getCourseID());
            LogEntry.sendToDatabase(parent.getScheduleName(), "REMOVE", toRemove.getCourseID(), parent.getCurrentSemester());
            schLog.log(Level.INFO, "REMOVE: " + toRemove.getCourseID(), toRemove.getCourseID());
        } else {
            schLog.info(toRemove.getCourseID() + " is not a part of the schedule");
            throw new RuntimeException("removeCourse threw an error");

        }
    }

    /**
     * This remove function is only for the restore method,
     * so that duplicate entries are not added to the database
     * @param toRemove
     * @throws RuntimeException
     */
    public void restoreRemove(Course toRemove) throws RuntimeException {

        if (courseList.contains(toRemove)) {
            courseList.remove(toRemove);
            credits -= toRemove.getCredits();
            parent.removeTakenCourse(toRemove);

            // undo function setup
            Action action = new Action(parent.getScheduleName(), "REMOVE", toRemove, parent.getCurrentSemester());
            action.setName(parent.getScheduleName());
            action.setAction("REMOVE");
            action.setCourse(toRemove);
            stack.push(action);

            System.out.println("Action being sent: " + action.getName() + " " + action.getAction() + " " + action.getCourse().getCourseID());
            LogEntry.sendToDatabase(parent.getScheduleName(), "REMOVE", toRemove.getCourseID(), parent.getCurrentSemester());
            schLog.log(Level.INFO, "REMOVE: " + toRemove.getCourseID(), toRemove.getCourseID());
        } else {
            schLog.info(toRemove.getCourseID() + " is not a part of the schedule");
            throw new RuntimeException("removeCourse threw an error");

        }
    }

    /**
     * Switches the section of a course
     * @param currentlyTaking
     * @param toSwitchToo
     * @throws RuntimeException
     */
    public void changeSection(Course currentlyTaking, Course toSwitchToo) throws RuntimeException {
        //checks if change section will work on this data
        if (isFreeTimeslot(toSwitchToo) &&
                courseList.contains(currentlyTaking) &&
                currentlyTaking.getCourseName().equals(toSwitchToo.getCourseName())) {
            removeCourse(currentlyTaking);
            addCourse(toSwitchToo);
        } else {
            schLog.info("Timeslot is not available");
            throw new RuntimeException("changeSection() threw an error");
        }
    }



    public Stack<Action> undoAction() {
        if (stack.lastElement().getAction().equals("ADD")) {
            // remove course
            removeCourse(stack.lastElement().getCourse());
            stack.pop();
            stack.pop();
        }
        else if (stack.lastElement().getAction().equals("REMOVE")) {
            // add course
            addCourse(stack.lastElement().getCourse());
            stack.pop();
            stack.pop();
        }
        return stack;
    }

    /**
     * Checks if the course overlaps with any courses that are currently being taken
     * @param course
     * @return a boolean
     */
    public boolean isFreeTimeslot(Course course) {
        boolean free = true;
        for (Course takingCourse : courseList) {
            //checks to see if there is a time overlap in the courses
            if (isSameDay(takingCourse.getDaysMet(), course.getDaysMet()) && !isValidTime(takingCourse, course)) {
                free = false;
            }
        }

        return free;
    }

    /**
     * checks if 2 courses share a same day
     * @param a daysMet by course 1
     * @param b daysMet by course 2
     * @return a boolean
     */
    private boolean isSameDay(String a, String b) {
        boolean same = false;
        // looping through DaysMet a
        for (int i = 0; i < a.length(); i++) {
            // looping through DaysMet b
            for (int j = 0; j < b.length(); j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    // if at any point chars equal each other, the courses meet on the same day
                    same = true;
                }
            }
        }
        return same;
    }

    /**
     * Checks if the course doesn't overlap with a currently taking course
     * @param takingCourse
     * @param course
     * @return a boolean
     */
    private boolean isValidTime(Course takingCourse, Course course) {
        boolean validTime = false;

        boolean isBeforeStartTime = course.getStartTime().getTime() < takingCourse.getStartTime().getTime();
        boolean isBeforeEndTime = course.getEndTime().getTime() < takingCourse.getStartTime().getTime();
        boolean isAfterStartTime = course.getStartTime().getTime() > takingCourse.getEndTime().getTime();
        boolean isAfterEndTime = course.getEndTime().getTime() > takingCourse.getEndTime().getTime();
        if ((isBeforeStartTime && isBeforeEndTime) || (isAfterStartTime && isAfterEndTime)) {
            validTime = true;
        }

        return  validTime;
    }

    /**
     * Checks if the course is the same as another course already being taken
     * @param course the course in question
     * @return a boolean
     */
    public boolean isSameCourse(Course course) {
        boolean same = false;
        // taking the courseID and removing the course section
        String addCourse = course.getCourseID().substring(0, course.getCourseID().length() - 1);
        for (Course takingCourse : courseList) {
            // checks if user is adding the same courses, eg ACCT201A and ACCT201B
            String checkCourse = takingCourse.getCourseID().substring(0, takingCourse.getCourseID().length() - 1);
            if (addCourse.equals(checkCourse)) {
                same = true;
            }
        }
        return same;
    }

    public boolean takenPrerequisites(Course course, String[] takenCourses) {
        boolean taken = false;
        int numberOfChars = 8;
        for (int i = 0; i < course.getPrerequisites().length(); i += numberOfChars) {
            for (int j = 0; j < takenCourses.length; j++) {
                if (course.getPrerequisites().substring(i, i + numberOfChars).equals(takenCourses[j])) {
                    taken = true;
                }
            }
        }
        return taken;
    }

    public Stack<Action> getStack() {
        return stack;
    }

    public void setStack(Stack<Action> stack) {
        this.stack = stack;
    }

    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public int getCredits() {
        return credits;
    }


    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    public void autofill() {

    }

    // Constructor for creating a lost/deleted schedule from a log file
    // TODO: store log entries in a db, and parse from that instead
    public SemesterSchedule(AllSemesters parent, File logfile) throws FileNotFoundException {
        this.parent = parent;
        logfile = new File("SemesterSchLog");
        courseList = new ArrayList<Course>();
        credits = 0;
        log = new LogEntry();
    }

    public ArrayList<Course> restoreCourses() throws FileNotFoundException {
        // in addCourse/removeCourse, add statement: sendToDatabase(parent.getScheduleName, action, course.courseID)
        // set parameters to nothing?
        // calls getFromDatabase(parent.getScheduleName) which returns ArrayList<Action>
        // for each item in ArrayList<Action>, add or remove course depending on value

        ArrayList<LogEntry> actionList = new ArrayList<LogEntry>();
        actionList = log.getFromDatabase(parent.getScheduleName());
        //System.out.println("DATABASE ACTIONS " + actionList.get(0).toString());

        CourseSearch courseSearch = new CourseSearch();

        for (int i = 0; i < actionList.size(); i++) {
            String tokenFromList = actionList.get(i).getAction();
            if (tokenFromList.equals("ADD") && (parent.getCurrentSemester() == actionList.get(i).getSemesterNumber())) {
                // add to course list
                String courseCode = actionList.get(i).getCourseID();
                ArrayList<Course> results = (courseSearch.searchCourses(courseCode, "", "", "", "", ""));

                if (courseList.size() > 0) {
                    for (int j = 0; j < this.courseList.size(); j++) {
                        // if course codes are not equal
                        if (!(courseCode.equals(this.courseList.get(j).getCourseID()))) {
                            this.restoreAdd(results.get(0));
                            credits += results.get(0).getCredits();
                            System.out.println("COURSE ADDED");
                            System.out.println(results.get(0).getCourseID());
                        }
                    }
                }
                else {
                    this.restoreAdd(results.get(0));
                    credits += results.get(0).getCredits();
                    System.out.println("COURSE ADDED");
                    System.out.println(results.get(0).getCourseID());
                }

            }
            else if (tokenFromList.equals("REMOVE") && (parent.getCurrentSemester() == actionList.get(i).getSemesterNumber())) {
                // remove from course list
                String courseCode = actionList.get(i).getCourseID();
                ArrayList<Course> results = (courseSearch.searchCourses(courseCode, "", "", "", "", ""));

                this.restoreRemove(results.get(0));
                credits -= results.get(0).getCredits();
                System.out.println("COURSE REMOVED");
            }
        }
        System.out.println(this.courseList);
        return this.courseList;
    }

    public ArrayList<Event> getEventList() {
        return eventList;
    }
}
