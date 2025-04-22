package edu.gcc.comp350.amazeall.gui;

import edu.gcc.comp350.amazeall.AllSemesters;
import edu.gcc.comp350.amazeall.VisualMain;
import edu.gcc.comp350.amazeall.Driver;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GUILoadCareer extends BaseGUI {
    private ListView<String> careerList;
    private Driver driver;
    private AllSemesters toLoad;

    private ObservableList<String> dataList;

    public GUILoadCareer(VisualMain visualDriver, Driver driver) {
        //TODO
        /*this.driver = driver;
        toLoad = null;
        dataList = FXCollections.observableArrayList();

        //Set up nodes
        Text instructionText = new Text("Select a career to load");
        careerList = new ListView<>(dataList);
        Button loadButton = new Button("- No Career Selected -");
        Button backButton = new Button("Back");

        //Set up events
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                visualDriver.loadRoot("LandingPage");
            }
        });
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                if (toLoad != null) {
                    visualDriver.setViewCareer(toLoad);
                    visualDriver.loadRoot("CareerView");
                }
            }
        });
        careerList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            //When a career is selected, update toLoad and loadButton
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<Integer> selected = careerList.getSelectionModel().getSelectedIndices();

                if (selected.size() >= 1) {
                    toLoad = driver.getCareer(selected.get(0));
                    loadButton.setText("Load " + toLoad.getScheduleName());
                } else {
                    toLoad = null;
                    loadButton.setText("- No Career Selected -");
                }
            }
        });

        //Set up root
        root = new VBox();
        root.getChildren().add(backButton);
        root.getChildren().add(instructionText);
        root.getChildren().add(careerList);
        root.getChildren().add(loadButton);*/
    }

    public void updateList() {
        dataList.clear();

        for (AllSemesters i : driver.getCareers()) {
            dataList.add(i.getScheduleName());
        }
    }

    @Override
    public void refreshVisuals() {

    }

    @Override
    public void initializeData() {

    }
}
