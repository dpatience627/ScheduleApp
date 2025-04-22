package edu.gcc.comp350.amazeall.gui;

import edu.gcc.comp350.amazeall.AllSemesters;
import edu.gcc.comp350.amazeall.SemesterSchedule;
import edu.gcc.comp350.amazeall.VisualMain;
import edu.gcc.comp350.amazeall.Driver;
import javafx.scene.control.TextField;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GUICareerView extends BaseGUI {
    /*private AllSemesters currentCareer;
    private Text name;
    private GridPane grid;
    private ArrayList<SemesterSchedule> semesters;
    private VisualMain visualDriver;

    private int credits;*/

    @FXML private HBox semesterBox;
    @FXML private Text nameText;
    @FXML private Text creditsText;

    @FXML private TextField emailField;
    @FXML private Button emailButton;
    @FXML private Text emailError;

    @FXML private Button downloadButton;
    @FXML private Text downloadText;
    private GUISceneSingleton gss;
    private Driver driver;
    private AllSemesters career;

    public GUICareerView() {

    }

    @FXML
    private void goBack() {
        GUISceneSingleton.getInstance().switchScene("GUILandingPage");
    }

    @FXML
    private void sendEmail(){
        String test = career.emailCareer(emailField.getText());
        if(test.equals("Invalid Input")){
            emailError.setText("Invalid Email");
        }
        else{
            emailError.setText("Email sent!");
        }
    }

    @FXML
    private void downloadAction(){
        career.GenerateOverviewSheet();
        downloadText.setText("Schedule Downloaded to: " + System.getProperty("user.home") + "/Downloads");
    }


    @Override
    public void initializeData() {
        //Get references to various objects.
        gss = GUISceneSingleton.getInstance();
        driver = gss.getDriver();
        career = gss.getCareer();
        downloadText.setText("");
        emailError.setText("");


    }

    @Override
    public void refreshVisuals() {
        nameText.setText(career.getScheduleName());
        creditsText.setText("Credits: " + career.getTotalCredits());

        //Clear out the box of old data.
        semesterBox.getChildren().clear();

        //TODO: Instead of buttons, use something more visually appealing that displays more info.
        //Create buttons for viewing the semesters.
        ArrayList<SemesterSchedule> semesters = career.getSemesterList();
        for (int i = 0; i < semesters.size(); i++) {
            String semesterCourses = "";
            for (int j = 0; j < semesters.get(i).getCourseList().size(); j++) {
                semesterCourses += "\n" + semesters.get(i).getCourseList().get(j).getCourseName();
            }
            final int num = i;
            Button btn = new Button("Sem " + (i+1) + semesterCourses);
            btn.setMinHeight(125);
            btn.setMinWidth(150);
            /*final GUISemesterView sem = new GUISemesterView(visualDriver, semesters.get(i));
            final Pane rootToReturn = sem.getRoot();*/

            //Assign an action to that button.
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    //sem.showCalendar();
                    //visualDriver.loadRoot(rootToReturn);
                    //System.out.println("Semester " + num);
                    gss.setCurrentSemester(semesters.get(num));
                    career.setCurrentSemester(num);
                    gss.switchScene("GUISemesterView");
                }
            });

            semesterBox.getChildren().add(btn);
        }

    }

    @FXML
    private void seeOverview() {
        gss.switchScene("GUIOverview");
    }

    /**
     * Sets this view to the specified career, which rebuilds the view.
     * @param career The career to display
     */
    public void setCurrentCareer(AllSemesters career) {
        /*currentCareer = career;

        //Set up the view.
        name.setText(currentCareer.getScheduleName());
        grid.getChildren().removeAll(grid.getChildren()); //Wipe everything in the grid. Should I just make a new GridPane?
        semesters = career.getSemesterList();

        //Create buttons for viewing the semesters.
        for (int i = 0; i < semesters.size(); i++) {
            //final int num = i + 1;
            Button btn = new Button("Sem " + (i+1));
            final GUISemesterView sem = new GUISemesterView(visualDriver, semesters.get(i));
            final Pane rootToReturn = sem.getRoot();

            //Assign an action to that button.
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    sem.showCalendar();
                    //visualDriver.loadRoot(rootToReturn); //TODO
                }
            });

            grid.add(btn, i%2, i/2);
        }*/

    }

    //public GUICareerView() {
        //this.visualDriver = visualDriver;

        //Set up nodes
        /*Button backButton = new Button("Save and Exit");
        name = new Text("");
        grid = new GridPane();*/

        /*backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                driver.saveCareers();
                //visualDriver.loadRoot("LandingPage"); //TODO
            }
        });*/

        //Set up root //TODO
        /*root = new VBox();
        root.getChildren().add(backButton);
        root.getChildren().add(name);
        root.getChildren().add(grid);*/
    //}
}
