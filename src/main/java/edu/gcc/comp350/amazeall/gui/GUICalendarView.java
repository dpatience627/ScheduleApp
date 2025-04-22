package edu.gcc.comp350.amazeall.gui;

import edu.gcc.comp350.amazeall.Course;
import edu.gcc.comp350.amazeall.SemesterSchedule;

import edu.gcc.comp350.amazeall.VisualMain;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.ArrayList;

public class GUICalendarView {
    Pane calendarRoot;
    Pane courseInfoRoot;

    GUISemesterView parent;
    SemesterSchedule semester;
    ArrayList<Group> recs;

    public GUICalendarView() {
        recs = new ArrayList<>();
        calendarRoot = new Pane();
    }

    public void setSemester(SemesterSchedule semester) {
        this.semester = semester;
    }

    public Pane getRoot() {
        return calendarRoot;
    }

    /**
     * Used by GUIEditCourseView to switch back to the calendar.
     */
    /*public void showCalendar() {
        *//*root.getChildren().remove(0);
        root.getChildren().add(calendarRoot);*//*
        //visualDriver.loadRoot(parent.getRoot()); //TODO
        courseInfoRoot = null;
    }*/

    /**
     * Redraws the calendar view to reflect new data.
     */
    public void redrawCalendar() {
        //parent.updateCredits();
        calendarRoot.getChildren().clear();
        recs.clear();
        ArrayList<Course> courses = semester.getCourseList();

        //for scaling, find the earliest start time and latest end time.
        long start = earliestStart(courses);
        long end = latestEnd(courses);

        //Draw the courses
        for (Course i : courses) {
            for (int k = 0; k < i.getDaysMet().length(); k++) {
                //Find the dimensions of this rectangle
                double xPos = 0;
                double xWidth = 120;
                switch (i.getDaysMet().charAt(k)) {
                    case 'M': xPos = 0; break;
                    case 'T': xPos = 120; break;
                    case 'W': xPos = 240; break;
                    case 'R': xPos = 360; break;
                    case 'F': xPos = 480; break;
                }
                double yStartPos = ((double)i.getStartTime().getTime() - start) / (end - start) * 300;
                double yHeight = ((double)i.getEndTime().getTime() - (double)i.getStartTime().getTime()) / (end - start) * 300;
                if (yHeight < 25) { yHeight = 25; }; //The rectangle must have a minimum height in order to fit all required text.
                //Shrink the dimensions slightly for borders
                xPos += 2;
                xWidth -= 4;
                yStartPos += 2;
                yHeight -= 4;

                //Create a rectangle and its contents.
                Group s = new Group();
                Rectangle rec = new Rectangle(xPos, yStartPos, xWidth, yHeight);
                rec.setFill(Color.RED);
                Text timeText = new Text(xPos + 1, yStartPos+10, "" + i.getDaysMet().charAt(k) + ": " +
                        formatTimeFromCourse(i.getStartTime().getHours(), i.getStartTime().getMinutes()) + "-" +
                        formatTimeFromCourse(i.getEndTime().getHours(), i.getEndTime().getMinutes()) + "");
                Text codeText = new Text(xPos + 1, yStartPos+20 ,i.getCourseID());
                timeText.setFill(Color.WHITE);
                codeText.setFill(Color.WHITE);
                s.getChildren().add(rec);
                s.getChildren().add(timeText);
                s.getChildren().add(codeText);
                //If the rectangle is large enough, include the course name.
                if (yHeight >= 35) {
                    Text nameText = new Text(xPos + 1, yStartPos+30, doName(i.getCourseName()));
                    nameText.setFill(Color.WHITE);
                    s.getChildren().add(nameText);
                }

                //Set the event for this rectangle.
                final GUICalendarView calViewRef = this;
                s.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent t) {
                        //TODO: Show the course info on click
                        //courseInfoRoot = new GUIEditCourseView(calViewRef, semester, i).getRoot();
                        /*root.getChildren().remove(0);
                        root.getChildren().add(courseInfoRoot);*/
                        //visualDriver.loadRoot(courseInfoRoot);
                        GUISceneSingleton.getInstance().setCurrentCourse(i);
                        GUISceneSingleton.getInstance().switchScene("GUIEditCourseView");
                    }
                });

                recs.add(s);
                calendarRoot.getChildren().add(s);

            }
        }
    }

    /**
     * Finds the earliest starting time of the given courses.
     * @param courses A list of courses to check
     * @return The earliest starting time of all courses
     */
    private long earliestStart(ArrayList<Course> courses) {
        long earliest = Long.MAX_VALUE;

        for (Course i : courses) {
            //Don't include online courses in the calculation.
            if (i.getStartTime().equals(i.getEndTime())) { continue; }

            long time = i.getStartTime().getTime();
            if (time < earliest) {
                earliest = time;
            }
        }

        return earliest;
    }

    /**
     * Finds the latest ending time of the given courses.
     * @param courses A list of courses to check
     * @return The latest ending time of all courses
     */
    private long latestEnd(ArrayList<Course> courses) {
        long latest = Long.MIN_VALUE;

        for (Course i : courses) {
            //Don't include online courses in the calculation.
            if (i.getStartTime().equals(i.getEndTime())) { continue; }

            long time = i.getEndTime().getTime();
            if (time > latest) {
                latest = time;
            }
        }

        return latest;
    }

    /**
     * Formats the given hour and time in a way that looks nice for the user.
     * @param hour Hour of the time
     * @param min Minute of the time
     * @return String representation of time that's nicely formatted for the user.
     */
    private String formatTimeFromCourse(int hour, int min) {
        String ret = "";

        //Format the hour
        boolean pm = hour > 12;
        String amPM = "AM";
        if (pm) {
            amPM = "PM";
            hour -= 12;
        }
        if (hour == 0) { hour = 12; }
        ret += hour + ":";

        //Format the minute
        if (min < 10) {
            ret += "0" + min;
        } else {
            ret += min;
        }

        if (pm) {
            ret += "PM";
        } else {
            ret += "AM";
        }

        return ret;
    }

    /**
     * Formats the given String to fit withing a certain number of characters.
     * This is intended to be used for the name of a course.
     * @param name The String to shrink
     * @return The resulting String
     */
    private String doName(String name) {
        String ret = name;
        if (name.length() > 15) {
            ret = name.substring(0, 12) + "...";
        }
        return ret;

        //TODO: There might be a better way to go about this.
    }
}