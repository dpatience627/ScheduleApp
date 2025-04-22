package edu.gcc.comp350.amazeall.scrappers;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class MinorScraper {
    private static HashMap<String, Integer> numbers = new HashMap<String, Integer>() {
        {
            put("one", 1);
            put("two", 2);
            put("three", 3);
            put("four", 4);
            put("five", 5);
            put("six", 6);
            put("seven", 7);
            put("eight", 8);
            put("nine", 9);
            put("ten", 10);
            put("eleven", 11);
            put("twelve", 12);
            put("thirteen", 13);
            put("fourteen", 14);
            put("fifteen", 15);
            put("sixteen", 16);
            put("seventeen", 17);
            put("eighteen", 18);
            put("nineteen", 19);
            put("twenty", 20);
        }
    };
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
    // Main driver method
    public static void main(String[] args) throws Exception
    {
        LinkedList<String> possibleMinors = splitIntoMinors(ReadPDF());
        for (String possibleMinor: possibleMinors) {
            if (isValidMinor(possibleMinor)) { //filter out hard to read minors
                List<String> minor = formatMinor(possibleMinor);
                String reqCourses = "";
                String optCourses = "";
                for (int i = 2; i < minor.size(); i++) {
                    if (minor.get(i).contains("or")) {
                        optCourses += formatOptional(minor.get(i));
                    } else if (minor.get(i).length() <= 8) {
                        reqCourses += minor.get(i) + ";";
                    } else {
                        System.out.println(minor.get(i));
                    }
                }
                if (minor.size() > 2) {
                    System.out.println(minor.get(0));
                    System.out.println(minor.get(1));
                    System.out.println(reqCourses);
                    System.out.println(optCourses);
                    System.out.println("\n");
                }
            }
        }
    }

    private static String ReadPDF() {


        // Create a content handler
        BodyContentHandler contenthandler
                = new BodyContentHandler();

        // Create a file in local directory
        File f = new File("Minors 2022-23.pdf");

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

            // Printing the contents of the pdf document
            // using toString() method in java
//        System.out.println("Extracting contents :"
//                + contenthandler.toString());
            return contenthandler.toString();
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

    private static LinkedList<String> splitIntoMinors(String s) {
        Scanner scanner = new Scanner(s);
        LinkedList<String> list = new LinkedList<String>();

        String currentMinor = "";
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if ((line.isEmpty() || line.isBlank()) && !currentMinor.isEmpty()) {
                list.add(currentMinor);
                currentMinor = "";
            } else if (!line.isEmpty() && !line.isBlank() && !line.contains("9/16/2022")) { //makes sure the string being appended is useful data
                currentMinor += line;
            }
        }
        scanner.close();

//        String[] returnArray = new String[list.size() - 2];
//        for (int i = 0; i < returnArray.length; i++) {
//            returnArray[i] = list.get(i + 2);
//        }

        //Removing Unwanted data
        list.pop();
        list.pop();

        return list;
    }

    private static boolean isValidMinor(String s) {
        Scanner scanner = new Scanner(s);

        int counter = 0;
        while(scanner.hasNext() && counter < 5) {
            String word = scanner.next();
            if (word.contains(".")) {
                return true;
            }
            counter++;
        }
        scanner.close();

        return false;
    }

    private static LinkedList<String> formatMinor(String s) {
        LinkedList<String> minorList = new LinkedList<String>();
        String[] words = s.split(" ");

        /**
         * Gets the name of the minor and the credits as well as track the index
         */
        boolean hasFoundMinorName = false;
        boolean hasFoundHours = false;
        int startingIndex = 0;
        while((!hasFoundMinorName || !hasFoundHours) && startingIndex < words.length) {
            String wordInQuestion = words[startingIndex];
            if (!hasFoundMinorName && wordInQuestion.contains(".")) { //handles minor name
                String minorName = "";
                for(int j = startingIndex; j >= 0; j--) {
                    minorName = words[j] + " " + minorName;
                }
                minorList.add(minorName.substring(0, minorName.length() - 2).strip());
                hasFoundMinorName = true;
            }
            if (!hasFoundHours && (wordInQuestion.contains(",") || wordInQuestion.contains("including"))) { //handles hours req.
                String requiredHours = "";
                for(int j = startingIndex; j >= minorList.peek().split(" ").length; j--) {
                    requiredHours = words[j] + " " + requiredHours;
                }
                minorList.add(requiredHours.substring(0, requiredHours.length() - 2).strip());
                hasFoundHours = true;
            }
            startingIndex++;
        }

        /**
         * Loops through the string trying to read the courses from it
         */
        for (int i = words.length - 1; i >= startingIndex; i--) {
            String wordInQuestion = words[i];
            try {
                if (wordInQuestion.length() > 3) {
                    wordInQuestion = wordInQuestion.substring(0, wordInQuestion.length() - 1);
                }
                int courseNum = Integer.valueOf(wordInQuestion).intValue();//if we find course code than continue
                String dept = "";
                boolean foundOr = false;
                boolean foundNum = false;
                int j = i - 1;
                while(j >= startingIndex) {
                    if (words[j].equals("and")) { //and is pretty COOL
                        //possibly indicates a break
                    }
                    if (words[j].equals("or")) { //the problem word
                        foundOr = true;
                    }
                    if (numbers.containsKey(words[j])) {
                        foundNum = true;
                    }
                    if (deptMap.containsKey(words[j])) {//found dept Name
                        dept = deptMap.get(words[j]);
                    }
                    if (words[j].contains(".") || words[j].contains(";") || words[j].contains("including")) { //if punctuation encountered than break
                        j++;
                        break;
                    }
                    if (numbers.containsKey(words[j])) {
                        break;
                    }
                    j--;
                }
                if (foundOr) {//shifts where we are searching in the line if there was an optional set of courses
                    String returnPossibleCourses = "";
                    Integer count = 1;
                    boolean isInHrs = false;
                    for (int k = j; k <= i; k++) {
                        if (numbers.containsKey(words[k])) {
                            count = numbers.get(words[k]);
                        } else if (words[k].contains("hours")) {
                            isInHrs = true;
                            returnPossibleCourses += " " + words[k];
                        } else {
                            returnPossibleCourses += " " + words[k];
                        }
                    }
                    i = j;
                    if (!isInHrs) {
                        for (int k = 0; k < count; k++) {
                            minorList.add(returnPossibleCourses.strip());
                        }
                    } else {
                        for (int k = 0; k < count/3; k++) { //NOTE: ASSUMPTION THAT ALL CLASSES ARE 3-HRS!
                            minorList.add(returnPossibleCourses.strip());
                        }
                    }
                } else if (foundNum) {
                    String returnPossibleCourses = "";
                    Integer count = 1;
                    for (int k = j; k <= i; k++) {
                        if (numbers.containsKey(words[k])) {
                            count = numbers.get(words[k]);
                        } else {
                            returnPossibleCourses += " " + words[k];
                        }
                    }
                    i = j;
                    minorList.add(count + returnPossibleCourses);
                } else {
                    minorList.add(dept + courseNum);
                }
            } catch (Exception e) {
//                System.out.println("Error Thrown!!!!!!!!!!!!");
//                System.out.println(e);
            }
        }

        return minorList;
    }

    private static String formatOptional(String s) {
        String returnString = "";
        String[] words = s.split(" ");

        String dept = "";
        String condition = "or";
        for (int i = 0; i < words.length; i++) {
            String wordInQuestion = words[i];
            if (deptMap.containsKey(wordInQuestion)) {
                dept = deptMap.get(wordInQuestion);
            } else if (wordInQuestion.contains("or")) {
                condition = "or";
            } else if (wordInQuestion.contains("and")) {
                condition = "and";
            } else {
                try {
                    if (wordInQuestion.length() > 3) {
                        wordInQuestion = wordInQuestion.substring(0, wordInQuestion.length() - 1);
                    }
                    int courseNum = Integer.valueOf(wordInQuestion).intValue();
                    returnString += dept + courseNum + condition;
                } catch (Exception e) {
//                    System.out.println(e);
//                    System.out.println("Not a number!");
                }
            }
        }
        returnString = returnString.substring(returnString.length(), returnString.length()).contains("and") ? returnString.substring(0, returnString.length() - 4) : returnString.substring(0, returnString.length() - 2);
        return returnString + ";";
    }
}
