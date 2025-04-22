package edu.gcc.comp350.amazeall;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SuggestionBox implements java.io.Serializable {
    private ArrayList<Section> suggestions;
    private ArrayList<String> taken;

    public SuggestionBox(ArrayList<Section> sugg, ArrayList<String> take){
        this.suggestions = sugg;
        this.taken  = take;
    }

    public void setSuggestions(ArrayList<Section> toSet){
        this.suggestions = toSet;
    }
    public ArrayList<Section> getSuggestions(){
        return this.suggestions;
    }

    public static ArrayList<Section> getSuggestions(ArrayList<Section> requiredCourses, ArrayList<String> takenCourses) {
//        System.out.println("Starting size: " + requiredCourses.size());
        ArrayList<Section> filter1 = filterOutTakenCourses(requiredCourses, takenCourses);
//        System.out.println("Filter 1 size: " + filter1.size());
        ArrayList<Section> filter2 = filterOutUnfulfilledPreReqs(filter1, takenCourses);
//        System.out.println("Filter 2 size: " + filter2.size());
        return filter2;
    }

    private static ArrayList<Section> filterOutTakenCourses(ArrayList<Section> required, ArrayList<String> taken){
        ArrayList<String> requiredCourses = new ArrayList<>();
        for (Section section : required) {
            requiredCourses.add(section.getCourseName());
        }
//        List<String> union = new ArrayList<String>(List.of(required));
        requiredCourses.removeAll(taken);

        ArrayList<Section> requiredSections = new ArrayList<>();
        for (Section section : required) {
            if (requiredCourses.contains(section.getCourseName())) {
                requiredSections.add(section);
            }
        }

        return requiredSections;
    }

    private static ArrayList<Section> filterOutUnfulfilledPreReqs(ArrayList<Section> required, ArrayList<String> takenCourses) {
//        List<String> union = new ArrayList<String>(List.of(takenCourses));
        ArrayList<Section> toBeRemoved = new ArrayList<>();
        for (Section section : required) {
            if (section.getCourseSections().size() > 0) {
//                System.out.println(section.getCourseSections().get(0).getPrerequisites());
                String[] courseReqs = DatabaseQuery.CourseCodesFromString(section.getCourseSections().get(0).getPrerequisites());
                ArrayList<String> preReqs = new ArrayList<>();
                for (String s : courseReqs) {
                    preReqs.add(s.replace(",","").strip());
                }

//                System.out.println(preReqs.size());
//                for (String s: preReqs) {
//                    System.out.print(s + ", ");
//                }
//                System.out.println();
                preReqs.removeAll(takenCourses);
//                System.out.println(preReqs.size());
//                for (String s: preReqs) {
//                    System.out.print(s + ", ");
//                }
//                System.out.println();
                if (preReqs.size() > 0 && !preReqs.get(0).isBlank()) {
                    toBeRemoved.add(section);
                }
            }
        }

        required.removeAll(toBeRemoved);

        return required;

//        for (int i = 0; i < required.size(); i++) {
//            ArrayList<Course> sections = required.get(i).getCourseSections();
//            if (sections.size() > 0) {
//                String[] courseReqs = DatabaseQuery.CourseCodesFromString(sections.get(0).getPrerequisites());
//                ArrayList<String> preReqs = new ArrayList<String >(List.of(courseReqs));
//
//                preReqs.removeAll(required); //THIS MAY BE DOING SOMETHING WIERD
//                if (preReqs.size() > 0) {
//                    required.remove(required.get(i));
//                }
//            }
//        }
    }

    private void generateCourseSections(){
        /**
         * Generates the majorReq ArrayList and optionalcourse
         * Arraylist using a simplified search function and thr strings from the
         * major object from the DB
         */
        // String toBreak = major.getMajorReq();
        String toTest = "ACCT201;MNGT203;ECON101;WROT101;PHYE100;ACCT202;ECON102;MATH141;HUMA102;ACCT301;MNGT201;FNCE301;HUMA200;ACCT302;MNGT214;ACCT303;ACCT401;HUMA202;ACCT321;ACCT402;MARK204;HUMA301;ACCT403;ACCT405;MNGT303;HUMA303;MNGT486";
        String[] courseCodes = DatabaseQuery.CourseCodesFromString(toTest);
        for(String cc : courseCodes){
            ArrayList<Course> sections = CourseSearch.simplifiedSearch(cc);
            // Section toAdd = new Section(cc, sections);
            //   this.majorReqs.add(toAdd);
        }


    }

//    public void selectCourse(int toChoose){
//        suggestions.get(toChoose).displaySections();
//    }

}
