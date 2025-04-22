package edu.gcc.comp350.amazeall.scrappers;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class TakenCoursesScraper {
    private static HashMap<String, String> deptMap = new HashMap<String, String>(){
        {
            put("Accounting", "ACCT");
            put("Art", "ART");
            put("Astronomy", "ASTR");
            put("Biology", "BIOL");
            put("Chemistry", "CHEM");
            put("Ministries", "CMIN"); //christian ministries
            put("Communication", "COMM");
            put("Computer", "COMP"); //computer science
            put("Data", "DSCI");
            put("Design", "DESI");
            put("Economics", "ECON");
            put("Education", "EDUC");
            put("Electrical", "ELEE"); //electrical engineering
            put("English", "ENGL");
            put("Engineering", "ENGR");
            put("Entrepreneurship", "ENTR");
            put("Exercise", "EXER"); //exercise science
            put("Finance", "FNCE");
            put("French", "FREN");
            put("Geology", "GEOL");
            put("Greek", "GREK");
            put("Hebrew", "HEBR");
            put("History", "HIST");
            put("Humanities", "HUMA");
            put("Business", "INBS");
            put("Latin", "LATN");
            put("Marketing", "MARK");
            put("Mathematics", "MATH");
            put("Mechanical", "MECE");
            put("Management", "MNGT");
            put("Music", "MUSI");
            put("Nursing", "NURS");
            put("Philosophy", "PHIL");
            put("Physical", "PHYE"); //physical education
            put("Physics", "PHYS");
            put("Political", "POLS"); //political science
            put("Psychology", "PSYC");
            put("Religion", "RELI");
            put("Robotics", "ROBO");
//                put("Science", "SCIC");
//                put("Special", "SEDU");
            put("Statistics", "STAT");
            put("Sociology", "SOCI");
            put("Social", "SOCW"); //social work
            put("Spanish", "SPAN");
            put("Technology", "SSFT"); //science faith & technology
            put("Theatre", "THEA");
            put("Writing", "WRIT");
        }
    };

    public TakenCoursesScraper() {

    }
//    public static void main(String[] args) throws Exception
//    {
////        String s = readTranscriptPDF("GCC Transcript.pdf");
////        System.out.println(s);
////        System.out.println("--------------------------");
////        System.out.println(formatToTakenCourses(s));
//    }

    public static ArrayList<String> readTranscriptPDF(String s) {
        String pdfData = readPDF(s);
        return formatToTakenCourses(pdfData);
    }

    private static String readPDF(String s) {
        // Create a content handler
        BodyContentHandler contenthandler
                = new BodyContentHandler();

        // Create a file in local directory
        File f = new File(s);

        // Create a file input stream
        // on specified path with the created file
        try {
            FileInputStream fstream = new FileInputStream(f);

            // Create an object of type Metadata to use
            Metadata data = new Metadata();

            // Create a context parser for the pdf document
            ParseContext context = new ParseContext();

            // PDF document can be parsed using the PDFparser
            // class
            PDFParser pdfparser = new PDFParser();

            // Method parse invoked on PDFParser class
            pdfparser.parse(fstream, contenthandler, data,
                    context);

            //return the pdf as a string
            return contenthandler.toString();
        } catch (Exception e) {
//            System.out.println(e);
            return "";
        }
    }

    private static ArrayList<String> formatToTakenCourses(String s) {
        ArrayList<String> takenCourses = new ArrayList<>();
        String[] words = s.split(" |\n|\t");

        for (int i = 0; i < words.length; i++) {
            if (deptMap.containsValue(words[i].strip())) {
                try {
                    int courseNum = Integer.valueOf(words[i+1].substring(0, 3)).intValue();//if we find course code than continue
                    takenCourses.add(words[i] + courseNum);
                } catch (Exception e) {
//                    System.out.println(e);
                }
            }
        }

        return takenCourses;
    }
}
