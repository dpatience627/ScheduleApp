package edu.gcc.comp350.amazeall.gui;
import edu.gcc.comp350.amazeall.AllSemesters;
import edu.gcc.comp350.amazeall.SemesterSchedule;
import edu.gcc.comp350.amazeall.VisualMain;

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

import java.io.FileNotFoundException;

public class GUILandingPage extends BaseGUI {
    @FXML ListView<String> loadableView;
    @FXML Button loadButton;
    @FXML Button deleteButton;
    @FXML Button restoreButton;
    @FXML Text undoDeleteText;
    @FXML Text doneRestoreText;

    private AllSemesters toLoad;
    private ObservableList<String> dataList;

    private GUISceneSingleton gss;

    public GUILandingPage() {

    }

    @FXML
    private void createNew() {
        gss.switchScene("GUICreateNewCareer");
    }

    @FXML
    private void loadCareer() {
        if (toLoad != null) {
            gss.setCareer(toLoad);
            gss.switchScene("GUICareerView");
        }
    }

    @FXML void deleteCareer() {
        if (toLoad != null) {
            gss.getDriver().deleteCareer(toLoad);
            undoDeleteText.setVisible(true);
            refreshVisuals();
        }
    }

    @FXML
    private void restoreCoursesAction() throws FileNotFoundException {
        if (toLoad != null) {
            // for each semester in toLoad, restoreCourses
            for (SemesterSchedule schedule : toLoad.getSemesterList()) {
                try {
                    schedule.restoreCourses();
                }
                catch (Exception e) {
                    System.out.println("Couldn't restore a semester");
                }
            }
            doneRestoreText.setVisible(true);
        }
    }

    @Override
    public void refreshVisuals() {
        toLoad = null;
        dataList.clear();
        loadButton.setText("~ Select a Career ~");
        loadButton.setDisable(true);
        deleteButton.setDisable(true);
        restoreButton.setDisable(true);

        for (AllSemesters i : gss.getDriver().getCareers()) {
            dataList.add(i.getScheduleName());
        }
    }

    @Override
    public void initializeData() {
        toLoad = null;
        dataList = FXCollections.observableArrayList();
        gss = GUISceneSingleton.getInstance();
        loadableView.setItems(dataList);

        loadableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            //When a career is selected, update toLoad and loadButton
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<Integer> selected = loadableView.getSelectionModel().getSelectedIndices();

                if (selected.size() >= 1) {
                    toLoad = gss.getDriver().getCareer(selected.get(0));
                    loadButton.setText("Load " + toLoad.getScheduleName());
                    //restoreButton.setText("Load " + toLoad.getScheduleName());
                    loadButton.setDisable(false);
                    deleteButton.setDisable(false);
                    restoreButton.setDisable(false);
                } else {
                    toLoad = null;
                    loadButton.setText("~ Select a Career ~");
                    loadButton.setDisable(true);
                    deleteButton.setDisable(true);
                    restoreButton.setDisable(true);
                }
            }
        });
    }
}
