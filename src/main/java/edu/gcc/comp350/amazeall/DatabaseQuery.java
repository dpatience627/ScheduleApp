package edu.gcc.comp350.amazeall;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.ArrayList;

public class DatabaseQuery {

    public static Major getMajor(String name){
        //calls DB to get the major based on input string
        String preparedCode = "\"" + "%" + name + "%\"";
        SessionFactory simpleFact = null;
        try{
            Configuration config = new Configuration();
            config.configure();
            simpleFact = config.buildSessionFactory();

        }catch(Throwable e){
            System.out.println(e.getMessage());
        }
        Session sess = simpleFact.getCurrentSession();
        sess.beginTransaction();
        String preparedStatement = "SELECT a from Major a where majorId like " + preparedCode;
        Query query = sess.createQuery(preparedStatement);
        query.setMaxResults(10);
        ArrayList<Major> results = new ArrayList<Major>(query.getResultList());
        sess.close();
        simpleFact.close();
        return results.get(0);
    }
    public static String[] CourseCodesFromString(String input) {
        /**
         * breaks the input into an array of course codes to be used in generateCourses
         */
        String[] toRet = input.split(";");
        return toRet;
    }
}
