package edu.gcc.comp350.amazeall;

import java.awt.geom.Area;
import java.lang.reflect.Array;
import java.util.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class StatusSheet implements java.io.Serializable{
    private String name;
    private ArrayList<Section> majorReqs;
    private ArrayList<Course> optionalCourses;
    private Major major;

    public StatusSheet() {
        majorReqs = new ArrayList<Section>();
    }

    public String getName() {
        return name;
    }
}
