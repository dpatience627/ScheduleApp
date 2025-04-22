package edu.gcc.comp350.amazeall.gui;

import javafx.scene.layout.AnchorPane;

public abstract class BaseGUI {
    private AnchorPane screenRoot;

    /**
     * Returns the root of this screen.
     * @return The root of the screen
     */
    public AnchorPane getRoot() {
        return screenRoot;
    }

    /**
     * Sets the root of this screen.
     * @param root The root to set this screen to
     */
    public void setRoot(AnchorPane root) { screenRoot = root; }

    /**
     * Updates the visuals of the screen to reflect its current data.
     */
    public abstract void refreshVisuals();

    /**
     * Has the class setup its information after creation.
     * How the class gets its information is determined by the class's implementation.
     */
    public abstract void initializeData();

}