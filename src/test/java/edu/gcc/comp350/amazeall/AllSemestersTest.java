package edu.gcc.comp350.amazeall;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AllSemestersTest {

    @Test
    void selectSemester() {
    }

    @Test
    void TestingPDFGenerator(){
        AllSemesters test = new AllSemesters("Test Planner",new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        ArrayList<Course> testRes = CourseSearch.searchCourses("","","","","","");
        test.addSemesterSchedule();
        test.addSemesterSchedule();
        test.addSemesterSchedule();
        test.addSemesterSchedule();
        test.addSemesterSchedule();
        test.addSemesterSchedule();
        test.addSemesterSchedule();
        test.addSemesterSchedule();
        test.selectSemester(0).addCourse(testRes.get(0));
        test.selectSemester(0).addCourse(testRes.get(10));
        test.GenerateOverviewSheet();

    }

    @Test
    void TestingSendingEmail(){
        AllSemesters newSem = new AllSemesters("Test",new ArrayList<>(), new ArrayList<>(), new ArrayList<String>());
        newSem.GenerateOverviewSheet();
        newSem.emailCareer("homajg19@gcc.edu");
    }
}