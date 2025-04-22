package edu.gcc.comp350.amazeall.gui;

import edu.gcc.comp350.amazeall.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class GUIEditCourseView extends BaseGUI {
    @FXML private Button addButton;
    @FXML private Text courseSearchInfoText;
    @FXML private Text courseText;
    @FXML ListView<String> searchResultsView;

    private ObservableList<String> resultsList;
    private ArrayList<Course> resultsArrayList;
    private Course thisCourse;
    private SemesterSchedule thisSemester;
    private Course courseToAdd;

    private GUISceneSingleton gss;
    private Driver driver;

    @Override
    public void refreshVisuals() {

    }

    @Override
    public void initializeData() {
        gss = GUISceneSingleton.getInstance();
        driver = gss.getDriver();
        thisCourse = gss.getCurrentCourse();
        thisSemester = gss.getCurrentSemester();
        courseSearchInfoText.setText("");
        courseText.setText(formatCourseString(thisCourse));

        resultsList.clear();

        searchResultsView.setItems(resultsList);

        searchResultsView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            //When a career is selected, update toLoad and loadButton
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<Integer> selected = searchResultsView.getSelectionModel().getSelectedIndices();

                //Check if they've selected a course.
                if (selected.size() >= 1) {
                    courseToAdd = resultsArrayList.get(selected.get(0));

                    courseSearchInfoText.setText("Professor: " + courseToAdd.getProfName() + "; " + courseToAdd.getTotalSeats() + " Seats" +
                            "\nPrerequisites: " + courseToAdd.getPrerequisites());

                    //Check if that time slot is free
                    if (thisSemester.isFreeTimeslot(courseToAdd)) {
                        courseToAdd = resultsArrayList.get(selected.get(0));
                        addButton.setText("Add " + courseToAdd.getCourseID());
                        addButton.setDisable(false);
                    } else {
                        addButton.setText("This course's time is not available.");
                        courseToAdd = null;
                        addButton.setDisable(true);
                    }
                } else {
                    courseToAdd = null;
                    addButton.setText("~ Select a Course ~");
                    courseSearchInfoText.setText("");
                    addButton.setDisable(true);
                }
            }
        });
    }

    public GUIEditCourseView() {
        resultsList = FXCollections.observableArrayList();
    }

    @FXML
    private void goBack() {
        gss.switchScene("GUISemesterView");
    }

    @FXML
    private void removeCourse() {
        try {
            thisSemester.removeCourse(thisCourse);
        } catch (Exception e) {
            System.err.println("GUI ERROR: semester.removeCourse threw an exception somehow." +
                    "\n - Tell Connor Felton about this error. It's probably his fault!" +
                    "\n - See GUIEditCourseView.java");
        }

        driver.saveCareers();

        gss.switchScene("GUISemesterView");
    }

    @FXML
    private void switchCourse() {
        if (courseToAdd == null) {
            System.err.println("GUI Edit Course View error: No course selected.");
            return;
        }
        try {
            thisSemester.changeSection(thisCourse, courseToAdd);
        } catch (RuntimeException e) {
            System.err.println("GUI Edit Course View error: Time slot not available.");
            return;
        }

        driver.saveCareers();

        gss.switchScene("GUISemesterView");
    }

    @FXML
    private void doSearch() {
        int codeLength = thisCourse.getCourseID().length();
        String toSearch = (
                Character.isAlphabetic(thisCourse.getCourseID().charAt(codeLength - 1))
        ) ? thisCourse.getCourseID().substring(0, codeLength-1) : thisCourse.getCourseID();

        resultsArrayList = CourseSearch.searchCourses(toSearch, "", "", "", "", "");
        updateResultsList();
    }

    /*public GUIEditCourseView(GUICalendarView parent, SemesterSchedule semester, Course thisCourse) {
        //Set up nodes
        Text courseInfoText = new Text(formatCourseString(thisCourse));
        Button cancelButton = new Button("Back to Calendar");
        Button deleteButton = new Button("Remove Course from Semester");
        deleteButton.setTextFill(Color.RED);
        Text sectionSwitchInfo = new Text("Some courses are offered in several timeslots at once, as denoted by having an A, B, etc. in the course code.\nIf you would like to switch the timeslot for this course, click the button below to search, then select a timeslot.");

        //Set up search
        resultsList = FXCollections.observableArrayList();
        resultsArrayList = new ArrayList<>();
        searchResultsListView = new ListView<>(resultsList);
        searchResultsListView.setMaxHeight(60);
        int codeLength = thisCourse.getCourseID().length();
        final String toSearch = (
                Character.isAlphabetic(thisCourse.getCourseID().charAt(codeLength - 1))
        ) ? thisCourse.getCourseID().substring(0, codeLength-1) : thisCourse.getCourseID();
        Button startSearch = new Button("Search for other timeslots for " + toSearch);
        Button switchCourseButton = new Button("- No Course Selected -");
        Text extraInfoText = new Text("");
        Text errorText = new Text("");

        //Return to calendar view
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                //parent.showCalendar();
            }
        });
        //Attempts to remove the course from the semester
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                try {
                    semester.removeCourse(thisCourse);
                } catch (Exception e) {
                    System.err.println("GUI ERROR: semester.removeCourse threw an exception somehow." +
                            "\n - Tell Connor Felton about this error. It's probably his fault!" +
                            "\n - See GUIEditCourseView.java");
                }
                parent.redrawCalendar();
                //parent.showCalendar();
            }
        });
        //Searches for other sections of the course
        startSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                resultsArrayList = CourseSearch.searchCourses(toSearch, "", "", "", "", "");
                updateResultsList();
            }
        });
        //Checks which course is selected
        searchResultsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            //When a course is selected from the list, update courseToAdd and addCourseButton
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<Integer> selected = searchResultsListView.getSelectionModel().getSelectedIndices();

                if (selected.size() >= 1) {
                    courseToAdd = resultsArrayList.get(selected.get(0));
                    switchCourseButton.setText("Switch to " + courseToAdd.getCourseID());
                    extraInfoText.setText("Professor: " + courseToAdd.getProfName() + "; " + courseToAdd.getTotalSeats() + " Seats");
                } else {
                    courseToAdd = null;
                    switchCourseButton.setText("- No Course Selected -");
                    extraInfoText.setText("");
                }
            }
        });
        //Attempts to switch the course to the selected section.
        switchCourseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                errorText.setText("");
                if (courseToAdd == null) {
                    errorText.setText("You must select a course.");
                    return;
                }
                try {
                    semester.changeSection(thisCourse, courseToAdd);
                } catch (RuntimeException e) {
                    errorText.setText("This timeslot is taken by other courses in your semester.");
                    return;
                }

                parent.redrawCalendar();
                //parent.showCalendar();
            }
        });

        root = new VBox(cancelButton, courseInfoText, deleteButton, sectionSwitchInfo, startSearch, searchResultsListView, extraInfoText, errorText, switchCourseButton);
        root.setSpacing(10);
    }*/

    /**
     * Displays a textual representation of the given course.
     * @param i Course of information to display
     * @return String containing information about that course.
     */
    private String formatCourseString(Course i) {
        String courseString = "";
        if (i.getStartTime().equals(i.getEndTime())) {
            courseString += "[ONLINE]";
        } else {
            courseString += i.getDaysMet();
            courseString += " " + formatTimeFromCourse(i.getStartTime().getHours(), i.getStartTime().getMinutes());
            courseString += " - " + formatTimeFromCourse(i.getEndTime().getHours(), i.getEndTime().getMinutes());
        }
        courseString += " | " + i.getCourseID();
        courseString += ": " + i.getCourseName();
        courseString += " | " + i.getCredits() + " Credits";
        courseString += "\nProfessor: " + i.getProfName() + "; " + i.getTotalSeats() + " Seats";
        courseString += "\nPrerequisites: " + i.getPrerequisites();

        return courseString;
    }

    /**
     * Formats a time string with the given hour and minute.
     * @param hour The hour for the given time
     * @param min The minute for the given time
     * @return A nicely formatted String representing the time
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
            ret += " PM";
        } else {
            ret += " AM";
        }

        return ret;
    }

    /**
     * Updates the list of results in the search list.
     */
    private void updateResultsList() {
        resultsList.clear();

        for (Course i : resultsArrayList) {
            if (i.getStartTime().equals(i.getEndTime())) {
                resultsList.add(formatOnlineCourse(i));
            } else {
                String courseString = "";
                courseString += i.getDaysMet();
                courseString += " " + formatTimeFromCourse(i.getStartTime().getHours(), i.getStartTime().getMinutes());
                courseString += " - " + formatTimeFromCourse(i.getEndTime().getHours(), i.getEndTime().getMinutes());
                courseString += " | " + i.getCourseID();
                courseString += ": " + i.getCourseName();
                courseString += " | " + i.getCredits() + " Credits";
                resultsList.add(courseString);
            }
        }
    }

    private String formatOnlineCourse(Course i) {
        String courseString = "[ONLINE] ";
        courseString += i.getCourseID();
        courseString += ": " + i.getCourseName();
        courseString += " | " + i.getCredits() + " Credits";
        return courseString;
    }
}