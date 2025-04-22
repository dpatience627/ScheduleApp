package edu.gcc.comp350.amazeall;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import java.util.ArrayList;

public class CourseSearch {
    //These are the categories to search by
    private String nameToSearch;
    private String codeToSearch;
    private String startTimeToSearch;
    private String endTimeToSearch;
    private String daysToSearch;
    //This arraylist contains the results that are received from the database
    private ArrayList<Course> currentCourseList;
    //Initializing the logger for this class
    private static Logger log = LogManager.getLogger("searchCourses");

    public CourseSearch() {
    }
    /**
     *
     * @param codeToSearch - The course code the user is trying to find
     * @param nameToSearch - The course name the user is trying to find
     * @param startTimeToSearch - The starting time of the course the user is trying to find
     * @param endTimeToSearch - The ending time of the course the user is trying to find
     * @param daysToSearch - The days the course meets that the user is trying to find
     * @return - Returns an ArrayList of Course objects that come from the database based on the search parameters
     */
    public static ArrayList<Course> searchCourses(String codeToSearch, String nameToSearch, String startTimeToSearch, String endTimeToSearch, String daysToSearch, String creditsToSearch) {
        //get the time at the beginning of the function for debugging
        double startTime = System.currentTimeMillis();
            log.info("Starting searchCourses");
        //Parameters are modified so that they match "%%" format used in the sql LIKE statement
        //LIKE statement with the format "%%" looks for entries containing the contents in the middle
        nameToSearch = "\"" + "%" + nameToSearch + "%\"";
        codeToSearch = "\"" + "%" + codeToSearch + "%\"";
        startTimeToSearch = "\"" + "%" + startTimeToSearch + "%\"";
        endTimeToSearch = "\"" + "%" + endTimeToSearch + "%\"";
        daysToSearch = "\"" + "%" + daysToSearch + "%\"";
        creditsToSearch = "\"" + "%" + creditsToSearch + "%\"";
        /*
         * SessionFactory is the object from the Hibernate library
         * that runs the connection to the database, sending queries, etc.
         */
        SessionFactory factory;
        try{
            /*Configuration object initialization, calling the configure() function will load the root config for
            the project from the resource folder*/
            Configuration config = new Configuration();
            config.configure();
            //Builds the sessionfactory using the configuration object
            factory = config.buildSessionFactory();
        } catch (Throwable e){
                log.warn(e);
            System.err.println("Failed to create session factory");
            throw new ExceptionInInitializerError(e);
        }
        //Logs the parameters put into the function when it is called
            log.info("Inputted results: " + codeToSearch +" | " + nameToSearch+ " | " + startTimeToSearch+ " | " + endTimeToSearch+ " | " + daysToSearch + " | " + creditsToSearch);
        //Starts a connection to the database to complete a transaction (interaction with the database)
        Session sess = factory.getCurrentSession();
        sess.beginTransaction();
        //This string is the SQL statement to be send to the database
        String toSearch = "SELECT a from Course a where courseID like " + codeToSearch + " AND courseName like " + nameToSearch + " AND startTime like " + startTimeToSearch + " AND endTime like " + endTimeToSearch + " AND daysMet like " + daysToSearch + "AND credits like " + creditsToSearch;
        Query query = sess.createQuery(toSearch);
        query.setMaxResults(50);
        //Sets the results equal to the results from that given query
        ArrayList<Course> results = new ArrayList<Course>(query.getResultList());
        //Logging to get query execution time and when the query was sent.
            log.info("Query start time: " + factory.getStatistics().getStart());
            log.info("Time to execute query " + factory.getStatistics().getQueryExecutionMaxTime() + " milliseconds");
            log.info("Ending searchCourses");
        double endTime = System.currentTimeMillis();
        //The sessionfactory and session are closed at the end of the function because they are not safe to be kept open
        sess.close();
        factory.close();
            log.info("Total function time: " + (endTime - startTime)/1000 + " Seconds");
        return results;
    }

    /**
     *
     * @param courseCode - course code to search by
     * @return ArrayList of the course sections for that courseCode
     */
    public static ArrayList<Course> simplifiedSearch(String courseCode){
        String preparedCode = "\"" + "%" + courseCode + "%\"";
        SessionFactory simpleFact = null;
        try{
            Configuration config = new Configuration();
            config.configure();
            simpleFact = config.buildSessionFactory();

        }catch(Throwable e){
            log.warn(e.getMessage());
        }
        Session sess = simpleFact.getCurrentSession();
        sess.beginTransaction();
        String preparedStatement = "SELECT a from Course a where courseID like " + preparedCode;
        Query query = sess.createQuery(preparedStatement);
        query.setMaxResults(50);
        ArrayList<Course> results = new ArrayList<Course>(query.getResultList());
        System.out.println(results.get(0).getCourseID());

        sess.close();
        simpleFact.close();
        return results;
    }
    public static Course directSearch(String courseCode){
        SessionFactory simpleFact = null;
        double startTime = System.currentTimeMillis();
        try{
            Configuration config = new Configuration();
            config.configure();
            simpleFact = config.buildSessionFactory();

        }catch(Throwable e){
            log.warn(e.getMessage());
        }
        Session sess = simpleFact.getCurrentSession();
        sess.beginTransaction();
        String preparedStatement = "From Course a where courseID = " + "'"+courseCode+"'";
        Query query = sess.createQuery(preparedStatement);
        query.setMaxResults(1);
        Course result = (Course) query.getResultList().get(0);
        double endTime = System.currentTimeMillis();
        System.out.println("Total function time: " + (endTime - startTime)/1000 + " Seconds");
        sess.close();
        simpleFact.close();
        return result;
    }
}