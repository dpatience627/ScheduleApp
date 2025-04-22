package edu.gcc.comp350.amazeall;
import java.io.File;
import java.sql.Array;
import java.sql.SQLOutput;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class ConsoleMain {
   // static private HashMap<String, Course> courseList = new HashMap<String, Course>();
    static private SemesterSchedule semester = new SemesterSchedule(new AllSemesters("",new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
    static private CourseSearch courseSearch = new CourseSearch();

    public static void main(String[] args) {
        //Creates the driver
        Driver drive = new Driver();
        if (drive.getCareers().size() < 1) { //makes it so we only have 1 semester for now
            drive.newCareer("My First Planner",new ArrayList<>(), new ArrayList<>(), new ArrayList<>()).addSemesterSchedule();
        }

        //Start the terminalIO and sets it equal to the same schedule everytime
        semester = drive.getCareer(0).getSemesterList().get(0);
        terminalIO();

        //save the schedule
        drive.saveCareers();
    }

    /**
     * Opens the 2020-2021EDITED.csv and adds the data to a course map named courseList
     */
    private static void readCoursesCSV() {
        try {
            Scanner scanner = new Scanner(new File("2020-2021EDITED.csv"));
            scanner.nextLine();

            while(scanner.hasNextLine()) {
                Scanner courseScanner = new Scanner(scanner.nextLine());
                courseScanner.useDelimiter(",");

                String courseID = courseScanner.next();
                String courseName = courseScanner.next();
                int credits = 0;
                if (courseScanner.hasNextInt()) {
                    credits = courseScanner.nextInt();
                }
                int totalSeats = 0;
                if (courseScanner.hasNextInt()) {
                    totalSeats = courseScanner.nextInt();
                }
                String daysMet = courseScanner.next();
                String sTime = courseScanner.next();
                Time startTime = new Time(0);
                if (sTime.contains(":")) {
                    startTime = Time.valueOf(sTime);
                }
                String eTime = courseScanner.next();
                Time endTime = new Time(0);
                if (eTime.contains(":")) {
                    endTime = Time.valueOf(eTime);
                }
                String profName = (courseScanner.next() + courseScanner.next()).replace('"', ' ').strip();

                courseScanner.reset();
                String prerequisites = courseScanner.next();

                Course course = new Course(courseID, courseName, credits, totalSeats, daysMet, startTime, endTime, profName, prerequisites);

                courseScanner.close();
            }

            scanner.close();
        } catch(Exception e) {
            System.out.println("Failed to do something with the file 2020-2021EDITED.csv");
            System.out.println(e);
        }

    }

    /**
     * Starts a scanner that the user can interact with to do course scheduling
     */
    private static void terminalIO() {
        Scanner terminalScanner = new Scanner(System.in);
        String s = "";
        ArrayList<Course> results = new ArrayList<Course>();

        do {
            System.out.println("\nEnter a command:");
            s = terminalScanner.next().strip().toLowerCase();
            switch (s) {
                case "list": // lists courses user has added
                    list();
                    break;
                case "add":
                    String string = terminalScanner.next();
                    // checks if course is in the search results
                    for (int i = 0; i < results.size(); i++) {
                        if (string.equals(results.get(i).getCourseID())) {
                            // if so, add it
                            semester.addCourse(results.get(i));
                        }
                    }
                    break;
                case "remove":
                    String string1 = terminalScanner.next();
                    // checks if course is a valid course in the list of all courses
                    for (Iterator<Course> iterator = semester.getCourseList().iterator(); iterator.hasNext();) {
                        Course course = iterator.next();
                        if (string1.equals(course.getCourseID())) {
                            // if so, remove it
                            iterator.remove();

                        }
                    }
                    break;
                case "switch":
                    String currentlyTaking = terminalScanner.next();
                    String toSwitchToo = terminalScanner.next();
                    // searching through added courses
                    for (Course course: semester.getCourseList()) {
                        // if course 1 is a valid course
                        if (currentlyTaking.equals(course.getCourseID())) {
                            // searching through searched courses
                            for (Course course1: results) {
                                // if course 2 is a valid course
                                if (toSwitchToo.equals(course1.getCourseID())) {
                                    // switch courses
                                    semester.changeSection(course, course1);
                                }
                            }
                        }
                    }

                    break;
                case "coursedetails":
                    String string2 = terminalScanner.next();
                    // checks if course is in the search results
                    for (int i = 0; i < results.size(); i++) {
                        if (string2.equals(results.get(i).getCourseID())) {
                            // print course details
                            System.out.println("Course Name: " + results.get(i).getCourseName());
                            System.out.println("Course ID: " + results.get(i).getCourseID());
                            System.out.println("Professor Name: " + results.get(i).getProfName());
                            System.out.println("Course Credits: " + results.get(i).getCredits());
                            System.out.println("Total Seats: " + results.get(i).getTotalSeats());
                            System.out.print("Days Met: ");
                            for (char c: results.get(i).getDaysMet().toCharArray()) {
                                System.out.print(c);
                            }
                            System.out.println("\nStart Time: " + results.get(i).getStartTime());
                            System.out.println("End Time: " + results.get(i).getEndTime());
                            System.out.println("Prerequisites: " + results.get(i).getPrerequisites());
                        }
                    }

                    break;
                case "viewsemestercredits":
                    System.out.println("Credits: " + semester.getCredits());
                    break;
                case "coursesearch":
                    String searchString = terminalScanner.nextLine();
                    // changing delimiter to comma
                    String[] stringList = searchString.split(",", 6);

                    for (int i = 0; i < stringList.length; i++) {
                        // adding user input that is seperated by commas to stringList
                        stringList[i] = stringList[i].strip();
                    }
                    if (stringList.length != 6) {
                        System.out.println("Invalid search query entered");
                        break;
                    }
                    // formatting
                    results = (courseSearch.searchCourses(stringList[0], stringList[1], stringList[2], stringList[3], stringList[4], stringList[5]));
                    String formattedTop = String.format("%s | %s | %s | %s | %s | %s", "Course Code", "Course Name", "Start Time", "End Time", "Meeting Days", "Credits");
                    System.out.println(formattedTop);
                    for (int i = 0; i < results.size(); i++) {
                        String formattedRet = String.format("%s | %s | %s | %s | %s | %s\n", results.get(i).getCourseID(), results.get(i).getCourseName(),results.get(i).getStartTime(),results.get(i).getEndTime(),results.get(i).getDaysMet(),results.get(i).getCredits());
                        System.out.printf(formattedRet);
                    }
                    break;
                case "help":
                    System.out.println("Command Names: (not case sensitive)");
                    System.out.println("1. list");
                    System.out.println("2. add _courseID_");
                    System.out.println("3. remove _courseID");
                    System.out.println("4. switch _currentlyTakingCourseID_ _courseToSwitchToID_");
                    System.out.println("5. coursedetails _courseID_");
                    System.out.println("6. viewsemestercredits");
                    System.out.println("7. coursesearch _codeToSearch_, _nameToSearch_, _startTimeToSearch_, _endTimeToSearch_, _daysToSearch_, _creditsToSearch_");
                    break;
                case "calendar":
                    ArrayList<String[]> calendar = new ArrayList<String[]>();
                    Time maxTime = new Time(12, 00, 00);
                    // first line of calendar
                    String[] topline = new String[6];
                    topline[0] = "Time";
                    topline[1] = "Monday";
                    topline[2] = "Tuesday";
                    topline[3] = "Wednesday";
                    topline[4] = "Thursday";
                    topline[5] = "Friday";
                    calendar.add(topline);
                    //System.out.printf("%s | %s | %s | %s | %s | %s", calendar.get(0)[0], calendar.get(0)[1], calendar.get(0)[2], calendar.get(0)[3], calendar.get(0)[4], calendar.get(0)[5]);
                    Time timeToTest = new Time(8, 00, 00);
                    for(Course c : semester.getCourseList()){
                        timeToTest = new Time(8, 00, 00);
                        maxTime = new Time(12, 00, 00);
                        String[] toAdd = new String[6];
                        while(timeToTest.before(maxTime)) {

                            //if its at the start time then move on
                            if (c.getStartTime().equals(timeToTest)) {
                                //yes I see the irony in me changing days met to a string
                                char[] days = c.getDaysMet().toCharArray();
                               // System.out.println("Days Length: " + (int)days[0] + " " + (int)days[1] + " " + (int)days[2]);
                                for (char ch : days) {


                                    switch (ch) {
                                        case 'M':
                                            toAdd[1] = c.getCourseID();
                                            break;
                                        case 'T':
                                            toAdd[2] = c.getCourseID();
                                            break;
                                        case 'W':
                                            toAdd[3] = c.getCourseID();
                                            break;
                                        case 'R':
                                            toAdd[4] = c.getCourseID();
                                            break;
                                        case 'F':
                                            toAdd[5] = c.getCourseID();
                                            break;
                                        default:
                                            System.out.println("Character: " + "|" +(int)ch + "|");
                                            break;
                                    }

                                }
                                //add the time to the front
                                toAdd[0] = timeToTest.toString();
                                maxTime = timeToTest;
                                calendar.add(toAdd);
                            } else {
                                timeToTest.setTime(timeToTest.getTime() + 900000L);
                            }
                        }
                    }
                    for(String[] entry : calendar) {
                        String formattedResult = String.format("%s | %s | %s | %s | %s | %s\n", entry[0], entry[1], entry[2], entry[3], entry[4], entry[5]);
                        System.out.printf(formattedResult);
                    }
                    break;
                default:
                    System.out.println("Entered an invalid command");
            }
        } while (!s.equals("q"));

        System.out.println("Terminated terminalIO");
        terminalScanner.close();
    }

    private static void list() {
        ArrayList<Course> courseList = semester.getCourseList();
        for (Course course: courseList) {
            // for each course print it
            System.out.println(course.getCourseID() + " | " + course.getCourseName() + " | " + course.getStartTime() + " | " + course.getEndTime() + " | " + course.getDaysMet() + " | " + course.getCredits());
        }
    }

}
