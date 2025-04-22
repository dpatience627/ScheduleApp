package edu.gcc.comp350.amazeall.gui;

import edu.gcc.comp350.amazeall.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.HashMap;

public class GUISceneSingleton {
    private static GUISceneSingleton INSTANCE;
    private static VisualMain visualMainRef;
    private static Driver driverRef;

    private HashMap<String, BaseGUI> controllers;
    private AnchorPane root;

    private AllSemesters currentCareer;
    private SemesterSchedule currentSemester;
    private Course currentCourse;
    private String careerReqs;

    private GUISceneSingleton() {
        root = new AnchorPane();

        controllers = new HashMap<>();
        controllers.put("GUILandingPage", new GUILandingPage());
        controllers.put("GUICreateNewCareer", new GUICreateNewCareer());
        controllers.put("GUICareerView", new GUICareerView());
        controllers.put("GUISemesterView", new GUISemesterView());
        controllers.put("GUIEditCourseView", new GUIEditCourseView());
        controllers.put("GUIOverview", new GUIOverview());

        //switchScene("GUILandingPage");
    }

    /**
     * Returns the instance of GUISceneSingleton.
     * Creates one first if it doesn't exist.
     * @return The instance of GUISceneSingleton
     */
    public static GUISceneSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GUISceneSingleton();
        }

        return INSTANCE;
    }

    /**
     * Gives GSS references to VisualMan and Driver to store.
     * Other classes can then get these references.
     * @param vm Reference to VisualMain
     * @param d Reference to Driver
     */
    public void setRefs(VisualMain vm, Driver d) {
        visualMainRef = vm;
        driverRef = d;
    }

    /**
     * Returns the reference to the Driver.
     * @return The Driver stored in GSS
     */
    public Driver getDriver() {
        return driverRef;
    }

    /**
     * Returns the reference to the VisualMain.
     * @return The VisualMain stored in GSS
     */
    public VisualMain getVisualMain() {
        return visualMainRef;
    }

    /**
     * Sets the AllSemesters stored in GSS, which is used by various classes.
     * Also updates the required courses.
     * @param career The AllSemesters object to store
     */
    public void setCareer(AllSemesters career) {
        currentCareer = career;

        //Update the course requirements.
        //Get the majors and minors.
        ArrayList<Major> majors = career.getMajors();
        ArrayList<Major> minors = career.getMinors();

        //Get all the required courses for this career.
        StringBuilder allCodes = new StringBuilder("");
        for (int i = 0; i < majors.size(); i++) {
            allCodes.append(majors.get(i).getMajorReq());
            allCodes.append(';');
        }
        for (int i = 0; i < minors.size(); i++) {
            allCodes.append(minors.get(i).getMajorReq());
            allCodes.append(';');
        }

        careerReqs = allCodes.toString();
    }

    public String getCareerReqs() {
        return careerReqs;
    }

    /**
     * Returns the career that is currently stored in GSS.
     * @return The career stored in GSS
     */
    public AllSemesters getCareer() {
        return currentCareer;
    }

    /**
     * Returns a specific semester from the singleton's stored career.
     * @param index The number of the semester to get
     * @return The desired semester
     */
    public SemesterSchedule getSemester(int index) {
        return currentCareer.getSemesterList().get(index);
    }

    /**
     * Sets the current semester to the given semester.
     */
    public void setCurrentSemester(SemesterSchedule currentSemester) {
        this.currentSemester = currentSemester;
    }

    /**
     * Returns the semester in currentSemester.
     * @return The current semester
     */
    public SemesterSchedule getCurrentSemester() {
        return currentSemester;
    }

    /**
     * Sets the current course to the given course.
     */
    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
    }

    /**
     * Returns the course in currentCourse.
     * @return The current course
     */
    public Course getCurrentCourse() {
        return currentCourse;
    }

    /**
     * Returns the reference to the base root of the GUI, as stored in GSS.
     * @return The base AnchorPane of the GUI
     */
    public AnchorPane getRoot() { return root; }

    /**
     * Switches the screen to the one denoted by the given string.
     * A matching controller must exist in the HashMap of the singleton.
     * @param toSwitchTo The name of the scene to switch to. For example, "GUILandingPage" will load GUILandingPage.fxml with the GUILandingPage controller.
     */
    public void switchScene(String toSwitchTo) {
        BaseGUI cont = controllers.get(toSwitchTo);
        try {
            if (cont == null) { throw new Exception("cont is null"); }
            FXMLLoader loader = new FXMLLoader();
            String location = "/GUIfxml/" + toSwitchTo + ".fxml";
            loader.setController(cont);
            loader.setLocation(getClass().getResource(location));
            AnchorPane newRoot = loader.load();
            if (newRoot == null) { throw new Exception("newRoot is null"); }
            cont.setRoot(newRoot);
        } catch (Exception e) {
            System.err.println("GUI ERROR: Issue switching to scene \"" + toSwitchTo + "\"\n" + e.getMessage());
            return;
        }

        cont.initializeData();
        cont.refreshVisuals();
        root.getChildren().setAll(cont.getRoot());
    }

    /**
     * Loads the given fxml name with the given controller.
     * Since the caller provides the controller, the HashMap of GSS does not need to contain one for the fxml file.
     * @param controller The controller to switch to
     * @param fxmlName The name of the fxml file to use. For example, "GUILandingPage" will load GUILandingPage.fxml.
     */
    public void switchScene(BaseGUI controller, String fxmlName) {
        try {
            if (controller == null) { throw new Exception("cont is null"); }
            FXMLLoader loader = new FXMLLoader();
            String location = "/GUIfxml/" + fxmlName + ".fxml";
            loader.setController(controller);
            loader.setLocation(getClass().getResource(location));
            AnchorPane newRoot = loader.load();
            if (newRoot == null) { throw new Exception("newRoot is null"); }
            controller.setRoot(newRoot);
        } catch (Exception e) {
            System.err.println("GUI ERROR: Issue switching to scene \"" + fxmlName + "\"\n" + e.getMessage());
            return;
        }

        controller.initializeData();
        controller.refreshVisuals();
        root.getChildren().setAll(controller.getRoot());
    }
}
