package edu.gcc.comp350.amazeall.gui;

import edu.gcc.comp350.amazeall.*;

import edu.gcc.comp350.amazeall.scrappers.TakenCoursesScraper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GUICreateNewCareer extends BaseGUI {
    @FXML private TextField nameField;
    @FXML private TextField numberField;
    @FXML private Text errorText;
    @FXML private Text importPdfText;

    //Majors
    @FXML private CheckBox acctBox;
    @FXML private CheckBox compBox;
    @FXML private CheckBox dataBox;
    @FXML private CheckBox elecBox;
    @FXML private CheckBox mathBox;

    //Minors
    @FXML private CheckBox mobyBox;
    @FXML private CheckBox musiBox;
    @FXML private CheckBox nssBox;
    @FXML private CheckBox mngBox;
    @FXML private CheckBox cybBox;
    @FXML private CheckBox chemBox;

    private ArrayList<String> takenCourses = new ArrayList<String>();

    public GUICreateNewCareer() {

    }

    @Override
    public void refreshVisuals() {
        //Nothing to do
    }

    @Override
    public void initializeData() {
        errorText.setText("");
        nameField.setText("");
        numberField.setText("");
        importPdfText.setText("");
    }

    @FXML
    private void goBack() {
        GUISceneSingleton.getInstance().switchScene("GUILandingPage");
    }

    @FXML
    private void attemptToCreate() {
        //If the name field is empty, tell the user to fill it.
        String careerName = nameField.getText();
        if (careerName.equals("")) {
            errorText.setText("You must enter a name.");
            return;
        }

        //If the length field is empty or invalid, inform the user.
        String enteredLength = numberField.getText();
        if (enteredLength.equals("")) {
            errorText.setText("You must enter a number of semesters.");
            return;
        }
        int givenSemesters = 0;
        try {
            givenSemesters = Integer.parseInt(enteredLength);
        } catch (NumberFormatException e) {
            errorText.setText("Number of semesters must be an integer.");
            return;
        }
        if (givenSemesters <= 0) {
            errorText.setText("You must have at least one semester.");
            return;
        }

        //At this point, all the input is valid.

        GUISceneSingleton gss = GUISceneSingleton.getInstance();

        //Create the AllSemesters object.
        AllSemesters career = gss.getDriver().newCareer(careerName, addMajorsFromBoxes(), addMinorsFromBoxes(), takenCourses);
        for (int i = 0; i < givenSemesters; i++) {
            career.addSemesterSchedule();
        }
//        addMajorsMinorsFromBoxes(career);
        if(career.getMajors().size() != 0){
            String[] majorReq = DatabaseQuery.CourseCodesFromString(career.getMajors().get(0).getMajorReq());
        }
        System.out.println("Majors: " +career.getMajors().size() + "; Minors: " + career.getMinors().size() + "\nMajors: " + career.getMajors() + "\nMinors: " + career.getMinors());
        gss.getDriver().saveCareers();
        gss.setCareer(career);

        gss.switchScene("GUICareerView");
    }
    private ArrayList<Major> addMajorsFromBoxes() {
        ArrayList<Major> majors = new ArrayList<>();

        //Majors
        if (acctBox.isSelected()) { majors.add(DatabaseQuery.getMajor("Accounting")); }
        if (compBox.isSelected()) { majors.add(DatabaseQuery.getMajor("ComputerScience")); }
        if (dataBox.isSelected()) { majors.add(DatabaseQuery.getMajor("DataScience")); }
        if (elecBox.isSelected()) { majors.add(DatabaseQuery.getMajor("ElectricalEngineering")); }
        if (mathBox.isSelected()) { majors.add(DatabaseQuery.getMajor("Mathematics")); }

        return majors;
    }

    private ArrayList<Major> addMinorsFromBoxes() {
        ArrayList<Major> minors = new ArrayList<>();

        //Minors
        if (mobyBox.isSelected()) { minors.add(DatabaseQuery.getMajor("MobileDevelopment")); }
        if (musiBox.isSelected()) { minors.add(DatabaseQuery.getMajor("Music")); }
        if (nssBox.isSelected()) { minors.add(DatabaseQuery.getMajor("NationalSecurityStudies")); }
        if (mngBox.isSelected()) { minors.add(DatabaseQuery.getMajor("Management"));; }
        if (cybBox.isSelected()) { minors.add(DatabaseQuery.getMajor("CyberSecurity")); }
        if (chemBox.isSelected()) { minors.add(DatabaseQuery.getMajor("Chemistry")); }

        return minors;
    }
//    private void addMajorsMinorsFromBoxes(AllSemesters career) {
//        //Majors
//        if (acctBox.isSelected()) { career.AddMajor("Accounting"); }
//        if (compBox.isSelected()) { career.AddMajor("ComputerScience"); }
//        if (dataBox.isSelected()) { career.AddMajor("DataScience"); }
//        if (elecBox.isSelected()) { career.AddMajor("ElectricalEngineering"); }
//        if (mathBox.isSelected()) { career.AddMajor("Mathematics"); }
//
//        //Minors
//        if (mobyBox.isSelected()) { career.AddMajor("MobileDevelopment"); }
//        if (musiBox.isSelected()) { career.AddMajor("Music"); }
//        if (nssBox.isSelected()) { career.AddMajor("NationalSecurityStudies"); }
//        if (mngBox.isSelected()) { career.AddMajor("Management"); }
//        if (cybBox.isSelected()) { career.AddMajor("CyberSecurity"); }
//        if (chemBox.isSelected()) { career.AddMajor("Chemistry"); }
//    }

    @FXML
    private void addUnoficalTranscript() {
        String path = GUISceneSingleton.getInstance().getDriver().getFileImport();
        if (!path.equals("No File Selected")) {
            takenCourses = TakenCoursesScraper.readTranscriptPDF(path);
        } else {
            takenCourses = new ArrayList<String>();
        }
        importPdfText.setText(takenCourses.size() + " taken courses added");
    }

    /*public GUICreateNewCareer(VisualMain visualDriver, Driver driver) {
        //Set up nodes
        Button backButton = new Button("Back");
        Text contextText = new Text("Create a new college career: A set of semesters representing your years at GCC.");
        Text nameCareerText = new Text("Name this career: ");
        TextField nameField = new TextField();
        Text lengthCareerText = new Text("How many semesters: ");
        TextField lengthField = new TextField();
        Text errorText = new Text("");
        Button createButton = new Button("Create");

        HBox nameBox = new HBox();
        nameBox.getChildren().add(nameCareerText);
        nameBox.getChildren().add(nameField);

        HBox lengthBox = new HBox();
        lengthBox.getChildren().add(lengthCareerText);
        lengthBox.getChildren().add(lengthField);

        //Set up root
        root = new VBox();
        root.getChildren().add(backButton);
        root.getChildren().add(contextText);
        root.getChildren().add(nameBox);
        root.getChildren().add(lengthBox);
        root.getChildren().add(errorText);
        root.getChildren().add(createButton);

        //Set up events
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                errorText.setText("");
                nameField.setText("");
                lengthField.setText("");
                visualDriver.loadRoot("LandingPage");
            }
        });
        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                //If the name field is empty, tell the user to fill it.
                String careerName = nameField.getText();
                if (careerName.equals("")) {
                    errorText.setText("You must enter a name.");
                    return;
                }

                //If the length field is empty or invalid, inform the user.
                String enteredLength = lengthField.getText();
                if (enteredLength.equals("")) {
                    errorText.setText("You must enter a number of semesters.");
                    return;
                }
                int givenSemesters = 0;
                try {
                    givenSemesters = Integer.parseInt(enteredLength);
                } catch (NumberFormatException e) {
                    errorText.setText("Number of semesters must be an integer.");
                    return;
                }
                if (givenSemesters <= 0) {
                    errorText.setText("You must have at least one semester.");
                    return;
                }

                //At this point, all the input is valid.
                //Create the AllSemesters object.
                AllSemesters career = driver.newCareer();
                career.setScheduleName(careerName);
                for (int i = 0; i < givenSemesters; i++) {
                    career.addSemesterSchedule();
                }
                driver.saveCareers();
                visualDriver.setViewCareer(career);

                errorText.setText("");
                nameField.setText("");
                lengthField.setText("");
                visualDriver.loadRoot("CareerView");
            }
        });
    }*/
}
