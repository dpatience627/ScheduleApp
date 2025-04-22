package edu.gcc.comp350.amazeall.gui;

import edu.gcc.comp350.amazeall.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class GUISemesterView extends BaseGUI {
    @FXML private Pane calendarPane; //Put the calendar here, or text saying that a schedule goes here.
    @FXML private Text creditsText;
    @FXML private Text semesterName;
    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextField daysField;
    @FXML private TextField startHourField;
    @FXML private TextField startMinuteField;
    @FXML private TextField endHourField;
    @FXML private TextField endMinuteField;
    @FXML private Button startMerButton;
    @FXML private Button endMerButton;
    @FXML private ListView<String> searchResultsView;
    @FXML private Text courseSearchInfoText;
    @FXML private Button addCourseButton;

    @FXML private Text onlineText;
    @FXML private ScrollPane onlineScroll;
    @FXML private VBox onlineBox;

    //Suggestion nodes
    @FXML private Button sugBack;
    @FXML private ListView<String> sugView;
    @FXML private ListView<String> sugCourseView;
    @FXML private Text sugText;
    @FXML private Button sugAdd;
    private ObservableList<String> sugObserveList;
    private ObservableList<String> sugCourseObserveList;
    private ArrayList<Course> sugCourseAL;
    private ArrayList<Section> sugSectionAL;
    private Course sugCourse;
    private Section sugSection;

    private boolean startIsAM;
    private boolean endIsAM;
    private SemesterSchedule thisSemester;
    private ObservableList<String> resultsList;
    private ArrayList<Course> resultsArrayList;
    private GUICalendarView calendar;
    private Course courseToAdd;

    private GUISceneSingleton gss;
    private Driver driver;

    public GUISemesterView() {
        startIsAM = true;
        endIsAM = true;
        resultsList = FXCollections.observableArrayList();
        sugObserveList = FXCollections.observableArrayList();
        sugCourseObserveList = FXCollections.observableArrayList();

        calendar = new GUICalendarView();
    }

    @Override
    public void refreshVisuals() {
        if (thisSemester == null) { return; }

        calendarPane.getChildren().clear();

        if (thisSemester.getCourseList().isEmpty()) {
            Text noCoursesText = new Text("Courses you add will show up here.");
            noCoursesText.setLayoutX(10);
            noCoursesText.setLayoutY(20);
            calendarPane.getChildren().add(noCoursesText);
        } else {
            //Display a calendar.
            calendar.setSemester(thisSemester);
            calendar.redrawCalendar();
            calendarPane.getChildren().add(calendar.getRoot());
        }

        updateCredits();

        //NOTE: Everything past here requires gss to be initialized.
        if (gss == null) { return; }

        //Check if there are online courses to display. Display them if so.
        onlineBox.getChildren().clear();
        ArrayList<Course> onlineCourses = getOnlineCourses();
        if (onlineCourses.size() > 0) {
            onlineText.setVisible(true);
            onlineScroll.setVisible(true);

            for (Course i : onlineCourses) {
                Button btn = new Button(formatOnlineCourse(i));

                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        gss.setCurrentCourse(i);
                        gss.switchScene("GUIEditCourseView");
                    }
                });

                onlineBox.getChildren().add(btn);
            }
        } else {
            onlineText.setVisible(false);
            onlineScroll.setVisible(false);
        }
    }

    /**
     * Returns a list of online courses in the current semester.
     * @return ArrayList containing courses that are online
     */
    private ArrayList<Course> getOnlineCourses() {
        ArrayList<Course> ret = new ArrayList<>();
        for (Course i : thisSemester.getCourseList()) {
            if (i.getStartTime().equals(i.getEndTime())) {
                ret.add(i);
            }
        }
        return ret;
    }

    @Override
    public void initializeData() {
        gss = GUISceneSingleton.getInstance();
        driver = gss.getDriver();
        thisSemester = gss.getCurrentSemester();
        courseSearchInfoText.setText("\n\n");
        sugText.setText("\n\n");
        sugTopLevel();

        searchResultsView.setItems(resultsList);
        sugView.setItems(sugObserveList);
        sugCourseView.setItems(sugCourseObserveList);

        //Set up the functions of the manual search results list
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
                        //Check if they're taking that course in another section.
                        if (!thisSemester.isSameCourse(courseToAdd)) {
                            courseToAdd = resultsArrayList.get(selected.get(0));
                            addCourseButton.setText("Add " + courseToAdd.getCourseID());
                            addCourseButton.setDisable(false);
                        } else {
                            addCourseButton.setText("You're already taking another section of this course.");
                            courseToAdd = null;
                            addCourseButton.setDisable(true);
                        }
                    } else {
                        addCourseButton.setText("This course's time is not available.");
                        courseToAdd = null;
                        addCourseButton.setDisable(true);
                    }
                } else {
                    courseToAdd = null;
                    addCourseButton.setText("~ Select a Course ~");
                    courseSearchInfoText.setText("\n\n");
                    addCourseButton.setDisable(true);
                }
            }
        });
        //Set up the functions of the suggestion box results list
        sugView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<Integer> selected = sugView.getSelectionModel().getSelectedIndices();

                boolean keepGoing = true;

                //If nothing is selected, stop.
                if (selected.size() < 1) {
                    sugCourse = null;
                    sugAdd.setText("~ Select a Course ~");
                    sugText.setText("\n\n");
                    sugAdd.setDisable(true);
                    keepGoing = false;
                }

                System.out.println("FLAG 1");

                //If sugSection is null, then sections are being shown, so that's what the user must have selected.
                if (sugSection == null && keepGoing) {
                    sugAdd.setVisible(true);
                    sugBack.setVisible(true);
                    sugText.setVisible(true);
                    sugCourseView.setVisible(true);

                    System.out.println("FLAG 2");
                    sugSection = sugSectionAL.get(selected.get(0));
                    System.out.println("FLAG 3");

                    sugCourseAL = sugSection.getCourseSections();

                    System.out.println("FLAG 4");

                    //Diplay the timeslots available for this section.
                    //This much nesting is a bad thing, but it's kind of hard to avoid in this case.
                    sugCourseObserveList.clear();
                    for (Course i : sugCourseAL) {
                        if (i.getStartTime().equals(i.getEndTime())) {
                            sugCourseObserveList.add(formatOnlineCourse(i));
                        } else {
                            String courseString = "";
                            courseString += i.getDaysMet();
                            courseString += " " + formatTimeFromCourse(i.getStartTime().getHours(), i.getStartTime().getMinutes());
                            courseString += " - " + formatTimeFromCourse(i.getEndTime().getHours(), i.getEndTime().getMinutes());
                            courseString += " | " + i.getCourseID();
                            courseString += ": " + i.getCourseName();
                            courseString += " | " + i.getCredits() + " Credits";
                            sugCourseObserveList.add(courseString);
                        }
                    }

                    System.out.println("FLAG 5");

                    //keepGoing = false;
                    //return;
                }

                /*if (keepGoing) {
                    System.out.println("FLAG 6");

                    //At this point, the user must have selected a course.
                    sugCourse = sugCourseAL.get(selected.get(0));

                    sugText.setText("Professor: " + sugCourse.getProfName() + "; " + sugCourse.getTotalSeats() + " Seats" +
                            "\nPrerequisites: " + sugCourse.getPrerequisites());

                    //Check if that time slot is free
                    if (thisSemester.isFreeTimeslot(sugCourse)) {
                        //Check if they're taking that course in another section.
                        if (!thisSemester.isSameCourse(sugCourse)) {
                            sugCourse = sugCourseAL.get(selected.get(0));
                            sugAdd.setText("Add " + sugCourse.getCourseID());
                            sugAdd.setDisable(false);
                        } else {
                            sugAdd.setText("You're already taking another section of this course.");
                            sugCourse = null;
                            sugAdd.setDisable(true);
                        }
                    } else {
                        sugAdd.setText("This course's time is not available.");
                        sugCourse = null;
                        sugAdd.setDisable(true);
                    }
                }*/

            }
        });
        sugCourseView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<Integer> selected = sugCourseView.getSelectionModel().getSelectedIndices();

                boolean keepGoing = true;

                //If nothing is selected, stop.
                if (selected.size() < 1) {
                    sugCourse = null;
                    sugAdd.setText("~ Select a Course ~");
                    sugText.setText("\n\n");
                    sugAdd.setDisable(true);
                    keepGoing = false;
                }

                if (keepGoing) {
                    sugCourse = sugCourseAL.get(selected.get(0));

                    sugText.setText("Professor: " + sugCourse.getProfName() + "; " + sugCourse.getTotalSeats() + " Seats" +
                            "\nPrerequisites: " + sugCourse.getPrerequisites());

                    //Check if that time slot is free
                    if (thisSemester.isFreeTimeslot(sugCourse)) {
                        //Check if they're taking that course in another section.
                        if (!thisSemester.isSameCourse(sugCourse)) {
                            sugCourse = sugCourseAL.get(selected.get(0));
                            sugAdd.setText("Add " + sugCourse.getCourseID());
                            sugAdd.setDisable(false);
                        } else {
                            sugAdd.setText("You're already taking another section of this course.");
                            sugCourse = null;
                            sugAdd.setDisable(true);
                        }
                    } else {
                        sugAdd.setText("This course's time is not available.");
                        sugCourse = null;
                        sugAdd.setDisable(true);
                    }
                }

            }
        });

        //Creating the enter button event handler
        EventHandler<KeyEvent> keyPressedEventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    doSearch();
                }
            }
        };
        //Adding event Filter
        codeField.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        nameField.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        daysField.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        startHourField.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        startMinuteField.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        endHourField.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        endMinuteField.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
    }

    @FXML
    private void goBack() {
        gss.switchScene("GUICareerView");
    }

    @FXML
    private void undoAction() {
        thisSemester.undoAction();
        driver.saveCareers();
        refreshVisuals();
    }

    @FXML
    private void doSearch() {
        //If an hour is given but not a minute, fill in the minute.
        if (startMinuteField.getText().isEmpty() && !startHourField.getText().isEmpty()) { startMinuteField.setText("00"); }
        if (endMinuteField.getText().isEmpty() && !endHourField.getText().isEmpty()) { endMinuteField.setText("50"); }

        //Format the start and end times.
        String startTime = formatStartTime();
        String endTime = formatEndTime();

        //If the times were not formatted successfully, erase the problematic fields.
        if (startTime.equals("BAD")) {
            startTime = "";
            startHourField.setText("");
            startMinuteField.setText("");
        }
        if (endTime.equals("BAD")) {
            endTime = "";
            endHourField.setText("");
            endMinuteField.setText("");
        }
        daysField.setText(formatDays(daysField.getText()));

        resultsArrayList = CourseSearch.searchCourses(codeField.getText(), nameField.getText(), startTime, endTime, daysField.getText(), "");

        updateResultsList();
    }

    @FXML
    private void addToSemester() {
        if (courseToAdd == null) {
            System.err.println("GUI Semester View error: Tried to add a course without selecting one.");
            return;
        }
        try {
            thisSemester.addCourse(courseToAdd);
        } catch (RuntimeException e) {
            System.err.println("GUI Semester View error: Tried to add course that is already taken in another section or does not fit.");
            return;
        }

        addCourseButton.setText("Added!");
        addCourseButton.setDisable(true);

        driver.saveCareers();

        refreshVisuals();
    }

    @FXML
    private void switchStartMer() {
        startIsAM = !startIsAM;
        startMerButton.setText(startIsAM ? "AM" : "PM");
    }

    @FXML
    private void switchEndMer() {
        endIsAM = !endIsAM;
        endMerButton.setText(endIsAM ? "AM" : "PM");
    }

    @FXML
    private void sugTopLevel() {
        refreshCourseSuggestions();

        sugSection = null;
        sugBack.setVisible(false);
        sugAdd.setVisible(false);
        sugText.setVisible(false);
        sugCourseView.setVisible(false);

        //Fill the list with the sections.
        sugObserveList.clear();
        for (Section i : sugSectionAL) {
            sugObserveList.add(i.getCourseName());
        }
    }

    @FXML
    private void sugAddFromSug() {
        try {
            thisSemester.addCourse(sugCourse);
        } catch (RuntimeException e) {
            System.err.println("GUI Semester View error: Issue adding course from suggestions.");
            return;
        }

        sugAdd.setText("Added!");
        sugAdd.setDisable(true);

        driver.saveCareers();

        refreshVisuals();

        sugTopLevel();
    }

    private void refreshCourseSuggestions() {
        //Find the course requirements and taken courses.
        String[] reqs = gss.getCareerReqs().split(";");
        ArrayList<String> takenAL = gss.getCareer().getTakenCourses();
        String[] taken = takenAL.toArray(new String[takenAL.size()]);
        sugSectionAL = gss.getCareer().getCareerSuggestions();
//        if(gss.getCareer().getCareerSuggestions() == null){
//            sugSectionAL = new ArrayList<Section>();
//        }
//        else{
//            sugSectionAL = gss.getCareer().getCareerSuggestions();
//        }


    }

    /**
     * Updates the list of search results.
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

    /**
     * Formats the course to a String of text that is denoted as an online course.
     * @param i A Course to format as a string
     * @return String representing information about a course, specifying that it is online
     */
    private String formatOnlineCourse(Course i) {
        String courseString = "[ONLINE] ";
        courseString += i.getCourseID();
        courseString += ": " + i.getCourseName();
        courseString += " | " + i.getCredits() + " Credits";
        return courseString;
    }

    /**
     * Formats a given string into days so that the user can worry less about formatting.
     * @param input A string to format into dates
     * @return A string formatted as "MTWRF", containing letters found in the input string.
     */
    private String formatDays(String input) {
        String ret = "";
        if (input.contains("M") || input.contains("m")) { ret += 'M'; }
        if (input.contains("T") || input.contains("t")) { ret += 'T'; }
        if (input.contains("W") || input.contains("w")) { ret += 'W'; }
        if (input.contains("R") || input.contains("r")) { ret += 'R'; }
        if (input.contains("F") || input.contains("f")) { ret += 'F'; }
        return ret;
    }

    /**
     * Formats the searched for start time for the purpose of searching.
     * @return A String that works with course search.
     */
    private String formatStartTime() {
        //Get the start hour.
        int hour = 0;
        String hourString = "";
        if (!startHourField.getText().equals("")) {
            try {
                hour = Integer.parseInt(startHourField.getText());
            } catch (NumberFormatException e) {
                return "BAD";
            }
            if (hour < 1 || hour > 12) {
                return "BAD";
            }
            hourString = "" + hour;
        }

        //Get the start minute.
        int minute = 0;
        String minuteString = "";
        if (!startMinuteField.getText().equals("")) {
            try {
                minute = Integer.parseInt(startMinuteField.getText());
            } catch (NumberFormatException e) {
                return "BAD";
            }
            if (minute < 0 || minute > 59) {
                return "BAD";
            }
            //Format the minute properly.
            minuteString = ":";
            if (minute < 10) {
                minuteString += '0';
            }
            minuteString += minute;
        }

        //Account for it being PM
        if (!hourString.equals("")) {
            if (!startIsAM && hour != 12) {
                hour += 12;
                hourString = "" + hour;
            }
            //Make it possible to enter midnight.
            //No courses start or end at midnight, but the possibility should be there for correct results.
            if (startIsAM && hour == 12) {
                hour = 0;
                hourString = "" + hour;
            }
        }

        if (minuteString.isEmpty() && !hourString.isEmpty()) {
            hourString += ":";
        }

        return hourString + minuteString;
    }

    /**
     * Formats the searched for end time for the purpose of searching.
     * @return A String that works with course search.
     */
    private String formatEndTime() {
        //Get the start hour.
        int hour = 0;
        String hourString = "";
        if (!endHourField.getText().equals("")) {
            try {
                hour = Integer.parseInt(endHourField.getText());
            } catch (NumberFormatException e) {
                return "BAD";
            }
            if (hour < 1 || hour > 12) {
                return "BAD";
            }
            hourString = "" + hour;
        }

        //Get the start minute.
        int minute = 0;
        String minuteString = "";
        if (!endMinuteField.getText().equals("")) {
            try {
                minute = Integer.parseInt(endMinuteField.getText());
            } catch (NumberFormatException e) {
                return "BAD";
            }
            if (minute < 0 || minute > 59) {
                return "BAD";
            }
            //Format the minute properly.
            minuteString = ":";
            if (minute < 10) {
                minuteString += '0';
            }
            minuteString += minute;
        }

        //Account for it being PM
        if (!hourString.equals("")) {
            if (!endIsAM && hour != 12) {
                hour += 12;
                hourString = "" + hour;
            }
            //Make it possible to enter midnight.
            //No courses start or end at midnight, but the possibility should be there for correct results.
            if (endIsAM && hour == 12) {
                hour = 0;
                hourString = "" + hour;
            }
        }

        if (minuteString.isEmpty() && !hourString.isEmpty()) {
            hourString += ":";
        }

        return hourString + minuteString;
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
            ret += " PM";
        } else {
            ret += " AM";
        }

        return ret;
    }

    /**
     * Allows external classes to force the credits count to update.
     */
    public void updateCredits() {
        creditsText.setText("Credits: " + thisSemester.getCredits());
    }
}
