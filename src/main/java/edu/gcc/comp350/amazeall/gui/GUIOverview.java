package edu.gcc.comp350.amazeall.gui;

import edu.gcc.comp350.amazeall.AllSemesters;
import edu.gcc.comp350.amazeall.Major;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;

public class GUIOverview extends BaseGUI {
    @FXML private TextFlow infoTextFlow;
    @FXML private Text checkSymbol;
    @FXML private Text crossSymbol;

    private GUISceneSingleton gss;
    private AllSemesters career;

    //private final String takenChar = "&#x2705";
    //private final String notTakenChar = "&#x274C";

    //private StringBuilder display;

    @Override
    public void refreshVisuals() {
        infoTextFlow.getChildren().clear();

        //Header
        addln(career.getScheduleName());
        //Display the majors
        add("Majors: ");
        ArrayList<Major> majors = career.getMajors();
        for (int i = 0; i < majors.size(); i++) {
            add(majors.get(i).getMajorName());
            if (i != majors.size() - 1) {
                add(", ");
            }
        }
        addln("");
        //Display the minors
        add("Minors: ");
        ArrayList<Major> minors = career.getMinors();
        for (int i = 0; i < minors.size(); i++) {
            add(minors.get(i).getMajorName());
            if (i != minors.size() - 1) {
                add(", ");
            }
        }
        addln("");

        ArrayList<String> taken = career.getTakenCourses();
        //DEBUG
        for (int i = 0; i < taken.size(); i++) { System.out.println(taken.get(i)); }

        //For each major, display its courses and whether or not they've been taken.
        for (Major m : majors) {
            addln("\n" + m.getMajorName() + ":");

            String[] needed = m.getMajorReq().split(";");
            for (String s : needed) {
                //addSymbol(taken.contains(s));
                addSymbol(hastaken(taken, s));
                addln(": " + s);
            }
        }

        //For each minor, display its courses and whether or not they've been taken.
        for (Major m : minors) {
            addln("\n" + m.getMajorName() + ":");

            String[] needed = m.getMajorReq().split(";");
            for (String s : needed) {
                //addSymbol(taken.contains(s));
                addSymbol(hastaken(taken, s));
                addln(": " + s);
            }
        }
    }

    private boolean hastaken(ArrayList<String> allTaken, String courseCode) {
        for (String s : allTaken) {
            if (s.equals(courseCode)) { return true; }
        }
        return false;
    }

    private void addln(String toAppend) {
        infoTextFlow.getChildren().add(new Text(toAppend + "\n"));
    }

    private void add(String toAppend) {
        infoTextFlow.getChildren().add(new Text(toAppend));
    }

    private void addSymbol(boolean taken) {
        Text symbol = new Text(taken ? checkSymbol.getText() : crossSymbol.getText());
        symbol.setFill(taken ? checkSymbol.getFill() : crossSymbol.getFill());
        infoTextFlow.getChildren().add(symbol);
    }

    @Override
    public void initializeData() {
        gss = GUISceneSingleton.getInstance();
        career = gss.getCareer();
    }

    @FXML
    private void goBack() {
        gss.switchScene("GUICareerView");
    }
}
