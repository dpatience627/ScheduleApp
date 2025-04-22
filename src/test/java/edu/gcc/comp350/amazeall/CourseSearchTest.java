package edu.gcc.comp350.amazeall;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseSearchTest {

    @Test
    void directSearchTest(){
        Course test = CourseSearch.directSearch("COMP141A");
        Course test2 = CourseSearch.directSearch("COMP141A");
        Course test3 = CourseSearch.directSearch("COMP141A");
        Course test4 = CourseSearch.directSearch("COMP141A");
        System.out.println(test.getCourseID());
    }
    @Test
    void searchCourses1() {
        assertEquals(50, CourseSearch.searchCourses("","","","","", "").size());
    }
    @Test
    void searchCourses2() {
        assertEquals(0, CourseSearch.searchCourses("ZZZZZZZ","","","","", "").size());
    }
    @Test
    void searchCourses3() {
        assertEquals(0, CourseSearch.searchCourses("","XXXXX","","","", "").size());
    }
    @Test
    void searchCourses4() {
        assertEquals(0, CourseSearch.searchCourses("","","","","XXXXX", "").size());
    }
    @Test
    void searchCourses5() {
        assertEquals(0, CourseSearch.searchCourses("","","","XXXX","", "").size());
    }
    @Test
    void searchCourses6() {
        assertEquals(0, CourseSearch.searchCourses("","","XXX","","", "").size());
    }
    @Test
    void searchCourses7() {
        assertEquals(24, CourseSearch.searchCourses("ACCT","","","","","").size());
    }
    @Test
    void searchCourses8() {
        assertEquals(19, CourseSearch.searchCourses("","Accounting","","","","").size());
    }
    @Test
    void searchCourses9() {
        assertEquals(50, CourseSearch.searchCourses("","","3:00:00","","","").size());
    }
    @Test
    void searchCourses10() {
        assertEquals(43, CourseSearch.searchCourses("","","","8:50:00","","").size());
    }
    @Test
    void searchCourses11() {
        assertEquals(11, CourseSearch.searchCourses("","","","","RF","").size());
    }
    @Test
    void searchCourses12() {
        assertEquals(50, CourseSearch.searchCourses("","","","","","4").size());
    }
    @Test
    void searchCourses13() { assertEquals(1, CourseSearch.simplifiedSearch("ACCT403B").size()); }

    /*@Test
    void searchCourseNames(){
        assertEquals("INTRODUCTION TO SOLID MODELING", CourseSearch.searchCourses("","MECE", "", "", "").get(4).getCourseName());
    }
    @Test
    void searchCourseNames2(){
        assertEquals("SCREENWRITING", CourseSearch.searchCourses("","COMM", "", "", "").get(17).getCourseName());
    }
    @Test
    void searchCourseNames3(){
        assertEquals("PIANO LESSON (1HR)", CourseSearch.searchCourses("","MUSI", "", "", "").get(27).getCourseName());
    }
    @Test
    void searchCourseCode(){
        assertEquals("SOCI101B", CourseSearch.searchCourses("FOUNDATIONS OF APPLIED SOCIOLOGY","", "", "", "").get(1).getCourseID());
    }
    @Test
    void searchCourseCode2(){
        assertEquals("MATH303A", CourseSearch.searchCourses("COLLEGE GEOMETRY","", "", "", "").get(0).getCourseID());
    }
    @Test
    void searchCourseCode3(){
        assertEquals("EXER215O", CourseSearch.searchCourses("MEDICAL TERMINOLOGY","", "", "", "").get(0).getCourseID());
    }
    */

}