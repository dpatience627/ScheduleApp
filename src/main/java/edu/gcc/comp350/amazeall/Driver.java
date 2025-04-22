package edu.gcc.comp350.amazeall;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.filechooser.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class Driver {
    private ArrayList<AllSemesters> careers = new ArrayList<AllSemesters>();

    private final String METADATA_FILENAME = "Metadata.ser";
    private static Logger dlog = LogManager.getLogger("DriverLog");



    public Driver() {
        //Loads any previously saved careers
        loadCareers();

    }
    
    /**
     * Creates a new career and returns the career
     * @return A career/ALLSemesters
     */
    public AllSemesters newCareer(String scheduleName, ArrayList<Major> majors, ArrayList<Major> minors, ArrayList<String> prevTakenCourses) {
        AllSemesters career = new AllSemesters(scheduleName, majors, minors, prevTakenCourses);
        careers.add(career);
        return career;
    }

    /**
     * Removes a career from the carrers list
     * @param a career to be removed
     */
    public void deleteCareer(AllSemesters a) {
        careers.remove(a);
    }

    /**
     * Loads all the saved careers info
     */
    public void loadCareers() {
        try{
            FileInputStream file = new FileInputStream(METADATA_FILENAME);
            ObjectInputStream in = new ObjectInputStream(file);

            careers = (ArrayList<AllSemesters>) in.readObject();

            in.close();
            file.close();
            dlog.info("Loaded Careers");
        } catch(Exception e) {
            System.out.println("Failed to do something reading the file '" + METADATA_FILENAME + "'");
            dlog.warn(e.getMessage());
            System.out.println(e);
        }
    }

    /**
     * Saves all the careers info
     */
    public void saveCareers() {
        try {
            FileOutputStream file = new FileOutputStream(METADATA_FILENAME);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(careers);
            out.close();
            file.close();
            dlog.info("Saved Careers");
        } catch(Exception e) {
            System.out.println("Failed to do something writing to the file '" + METADATA_FILENAME + "'");
            dlog.warn(e.getMessage());
            System.out.println(e);
        }
    }

    /**
     *
     * @return Returns the file location of the selected file to be used for pdf scraping
     */
    public static String getFileImport(){
        JFileChooser JF = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileNameExtensionFilter FF = new FileNameExtensionFilter("PDFS", "pdf");
        JF.setFileFilter(FF);
        int returnValue = JF.showOpenDialog(null);
        if(returnValue == JFileChooser.APPROVE_OPTION){
            File selected = JF.getSelectedFile();
            return selected.getAbsolutePath();
        }
        else{
            return "No File Selected";
        }
    }

    public void copyCareer(AllSemesters a) {

    }

    public void viewSideBySide(AllSemesters a, AllSemesters b) {

    }

    public ArrayList<AllSemesters> getCareers() {
        return careers;
    }

    public AllSemesters getCareer(int i) {
        return careers.get(i);
    }
}
