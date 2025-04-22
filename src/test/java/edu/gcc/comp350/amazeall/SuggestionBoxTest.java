package edu.gcc.comp350.amazeall;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SuggestionBoxTest {
    @Test
    void TestingSuggestionsDisplay(){
        String[] reqs = "ACCT201;MNGT203;ECON101;WRIT101;PHYE100;ACCT202;ECON102;MATH141;HUMA102;ACCT301;MNGT201;FNCE301;HUMA200;ACCT302;MNGT214;ACCT303;ACCT401;HUMA202;ACCT321;ACCT402;MARK204;HUMA301;ACCT403;ACCT405;MNGT303;HUMA303;MNGT486".split(";");
        ArrayList<Section> reqCourses =  new ArrayList<>();
        for (String s : reqs) {
            reqCourses.add(new Section(s));
        }
        ArrayList<String> takenCourses = new ArrayList<>(List.of("ACCT201;MNGT203;ECON101;WRIT101;PHYE100;ACCT202;ECON102;MATH141;HUMA102;ACCT301;MNGT201;FNCE301;HUMA200;ACCT302;MNGT214;ACCT303;ACCT401;HUMA202;ACCT321;ACCT402;MARK204;HUMA301;ACCT403;".split(";")));
        ArrayList<Section> testSuggestion = SuggestionBox.getSuggestions(reqCourses,takenCourses);
        for (Section section : testSuggestion) {
            section.displaySections();
        }
    }

    @Test
    void TestingSuggestionBox1() {
        String[] reqs =  "ACCT201;MNGT203;ECON101;WRIT101;PHYE100;ACCT202;ECON102;MATH141;HUMA102;ACCT301;MNGT201;FNCE301;HUMA200;ACCT302;MNGT214;ACCT303;ACCT401;HUMA202;ACCT321;ACCT402;MARK204;HUMA301;ACCT403;ACCT405;MNGT303;HUMA303;MNGT486".split(";");
        ArrayList<Section> reqCourses =  new ArrayList<>();
        for (String s : reqs) {
            reqCourses.add(new Section(s));
        }
        ArrayList<String> takenCourses = new ArrayList<>(List.of("ACCT201;MNGT203;ECON101;WRIT101;PHYE100;ACCT202;ECON102;MATH141;HUMA102;ACCT301;MNGT201;FNCE301;HUMA200;ACCT302;MNGT214;ACCT303;ACCT401;HUMA202;ACCT321;ACCT402;MARK204;HUMA301;ACCT403;".split(";")));
        ArrayList<Section> testSuggestion = SuggestionBox.getSuggestions(reqCourses,takenCourses);

        assertEquals(2, testSuggestion.size());
    }

    @Test
    void TestingSuggestionBox2() {
        String[] reqs =  "ACCT201;MNGT203;ECON101;WRIT101;PHYE100;ACCT202;ECON102;MATH141;HUMA102;ACCT301;MNGT201;FNCE301;HUMA200;ACCT302;MNGT214;ACCT303;ACCT401;HUMA202;ACCT321;ACCT402;MARK204;HUMA301;ACCT403;ACCT405;MNGT303;HUMA303;MNGT486".split(";");
        ArrayList<Section> reqCourses =  new ArrayList<>();
        for (String s : reqs) {
            reqCourses.add(new Section(s));
        }
        ArrayList<String> takenCourses = new ArrayList<String>(List.of("ACCT201;MNGT203;".split(";")));
        ArrayList<Section> testSuggestion = SuggestionBox.getSuggestions(reqCourses,takenCourses);

        assertEquals(12, testSuggestion.size());
    }
}
