package edu.gcc.comp350.amazeall;

import java.util.ArrayList;

public class CoursePath {
    private String pathName;
    private ArrayList<ArrayList<Course>> options;

    public CoursePath(String pathName, ArrayList<ArrayList<Course>> options) {
        this.pathName = pathName;
        this.options = options;
    }

    public String getPathName() {
        return pathName;
    }

    public ArrayList<ArrayList<Course>> getOptions() {
        return options;
    }
}
