package edu.gcc.comp350.amazeall;

public class Major implements java.io.Serializable {
    private String majorId;
    private String majorName;
    private String majorReq;
    private String optionalCourses;
    private String majorType;

    //DO NOT DELETE NEEDED FOR DB
    public Major(){

    }

    public String getMajorId() {
        return majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    public String getMajorReq() {
        return majorReq;
    }

    public String getOptionalCourses() {
        return optionalCourses;
    }

    public java.lang.String getMajorType() {
        return majorType;
    }
}
