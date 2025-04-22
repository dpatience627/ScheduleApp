package edu.gcc.comp350.amazeall;

import com.mysql.cj.log.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import java.util.ArrayList;

public class LogEntry {

    private String schedName;
    private String action;
    private String courseID;
    private int semesterNumber;


    public LogEntry() {

    }

    public static void sendToDatabase(String name, String action, String courseID, int semeNum) {

        SessionFactory simpleFact = null;
        String testName = "'" + name + "'";
        try {
            Configuration config = new Configuration();
            config.configure();
            simpleFact = config.buildSessionFactory();

        } catch(Throwable e) {
            System.out.println((e.getMessage()));
        }
        Session sess = simpleFact.getCurrentSession();
        sess.beginTransaction();
        System.out.println("Inside send function " + name + " " + action + " " + courseID + " " + semeNum);
        String preparedStatement = "INSERT into LogEntry(schedName, action, courseID, semesterNumber) values ('" + name + "', '" + action + "', '" + courseID + "',:semNum)";

        System.out.println(preparedStatement);
        Query test = sess.createQuery(preparedStatement);
        test.setParameter("semNum", semeNum);
        test.executeUpdate();

        sess.close();
        simpleFact.close();
    }

    public static ArrayList<LogEntry> getFromDatabase(String scheduleName) {

        String preparedCode = "\"" + "%" + scheduleName + "%\"";
        SessionFactory simpleFacto = null;
        try{
            
            Configuration config = new Configuration();
            config.configure();
            simpleFacto = config.buildSessionFactory();

        }catch(Throwable e){
            System.out.println((e.getMessage()));
        }
        Session sess = simpleFacto.getCurrentSession();
        sess.beginTransaction();
        String preparedStatement = "SELECT a from LogEntry a where schedName like " + preparedCode;
        System.out.println("Prepared statement: " + preparedStatement);
        Query query = sess.createQuery(preparedStatement);
        query.setMaxResults(50);
        ArrayList<LogEntry> list = new ArrayList<LogEntry>(query.getResultList());
        for (int i = 0; i < list.size(); i++) {
            System.out.println("Entry " + i + ": " + list.get(i).getSchedName() + " " + list.get(i).getAction() + " " + list.get(i).getCourseID());
        }

        sess.close();
        simpleFacto.close();

        return list;
    }

    public String getSchedName() { return schedName; }

    public void setSchedName(String schedName) { this.schedName = schedName; }

    public String getAction() { return action; }

    public void setAction(String action) { this.action = action; }

    public String getCourseID() { return courseID; }

    public void setCourseID(String courseID) { this.courseID = courseID; }

    public int getSemesterNumber() { return semesterNumber; }

    public void setSemesterNumber(int semesterNumber) { this.semesterNumber = semesterNumber; }
}
