package edu.gcc.comp350.amazeall;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.xml.bind.v2.runtime.output.NamespaceContextImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.crypto.Data;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AllSemesters implements java.io.Serializable {
    private ArrayList<SemesterSchedule> semesterList = new ArrayList<SemesterSchedule>();;
    private String scheduleName;
    private ArrayList<Major> majors;
    private ArrayList<Major> minors;
    private ArrayList<String> takenCourses;
    private ArrayList<Section> suggestions;
    private int totalCredits;
    private int currentSemester;
    private int plannedSemesters;
    //private static Document result;

    public AllSemesters(String scheduleName, ArrayList<Major> majors, ArrayList<Major> minors,ArrayList<String> prevTakenCourses) {
        this.scheduleName = scheduleName;
        this.takenCourses = prevTakenCourses;
        this.majors = majors;
        this.minors = minors;
        HashSet<String> reqCourses = new HashSet<String>();
        for (Major major : majors) {
            reqCourses.addAll(List.of(DatabaseQuery.CourseCodesFromString(major.getMajorReq())));
        }
        for (Major minor : minors) {
            reqCourses.addAll(List.of(DatabaseQuery.CourseCodesFromString(minor.getMajorReq())));
        }
        ArrayList<Section> suggestions = new ArrayList<Section>();
        for (String courseCode : reqCourses) {
            suggestions.add(new Section(courseCode));
        }
        this.suggestions = suggestions;
    }

    public SemesterSchedule selectSemester(int semester) {
        this.currentSemester = semester;
        return semesterList.get(semester);
    }

    /**
     * Adds a Schedule to this career
     * @return A new SemesterSchedule
     */
    public SemesterSchedule addSemesterSchedule() {
        SemesterSchedule semesterSchedule = new SemesterSchedule(this);
        semesterList.add(semesterSchedule);
        return semesterSchedule;
    }

    public void addTakenCourse(Course course) {
        takenCourses.add(course.getCourseID().substring(0, course.getCourseID().length() - 1));
    }

    public void removeTakenCourse(Course course) {
        takenCourses.remove(course.getCourseID().substring(0, course.getCourseID().length() - 1));
    }
//    public void setCareerSuggestions(SuggestionBox toSet){
//        this.careerSuggestions = toSet;
//    }
    /**
     * Generates a PDF overview sheet of the current AllSemesters Schedule with the filename being "CareerName.pdf"
     * Sends the file to the users Downloads folder (assuming they haven't changed it)
     */
    public void GenerateOverviewSheet(){
        Document result = new Document();
        String pdfName;
        //result.setMargins(36, 72, 108, 180);
        if(this.scheduleName.contains(":") || this.scheduleName.contains("/")){
            pdfName = this.scheduleName.replace(":", "");
            pdfName = pdfName.replace("/", "");
        }

        else{
            pdfName = this.scheduleName;
        }
        try{
            String home = System.getProperty("user.home");
            PdfWriter writer = PdfWriter.getInstance(result, new FileOutputStream(home + "/Downloads/" + pdfName + ".pdf"));
            result.open();

            Rectangle outerBorder = new Rectangle(10, 830, 585, 20);
            outerBorder.enableBorderSide(1);
            outerBorder.enableBorderSide(2);
            outerBorder.enableBorderSide(4);
            outerBorder.enableBorderSide(8);
           // outerBorder.setBorder(Rectangle.BOX);
            outerBorder.setBorderWidth(2);
            outerBorder.setBorderColor(BaseColor.BLACK);
            result.add(outerBorder);
            Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, 4);
            Font semesterHeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, 4);
            Font courseFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8);
            Paragraph header = new Paragraph(this.scheduleName + " Credits: " + this.getTotalCredits(),headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20f);
            result.add(header);
            for(SemesterSchedule e : semesterList){
                Paragraph semToAdd = new Paragraph("Semester: " + (semesterList.indexOf(e)+1) + " Credits: " + e.getCredits() + "\n",semesterHeaderFont);
                semToAdd.setSpacingAfter(10f);
                result.add(semToAdd);
                for(Course c : e.getCourseList()){
                    String formattedString = String.format("%5s %5s %5s %10s" , c.getCourseID()+ " | ",c.getCourseName() + " | ",c.getDaysMet() +" | ",c.getCredits()+" Credits");
                    Paragraph courseToAdd = new Paragraph(formattedString, courseFont);
                    courseToAdd.setAlignment(Element.MULTI_COLUMN_TEXT);
                    result.add(courseToAdd);
                }
            }
            result.close();
            writer.close();
        }catch (DocumentException | FileNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    public void AddMajor(String majorToAdd){
        Major toAdd = DatabaseQuery.getMajor(majorToAdd);
        System.out.println("Major type: " + toAdd.getMajorType().length());
        char[] bugFix = toAdd.getMajorType().toCharArray();

        if("Major".equals(toAdd.getMajorType().replaceAll("\\s", ""))){
            this.majors.add(toAdd);
        }
        else{
            this.minors.add(toAdd);
        }
    }
    /**
     *
     * @param recipient - who the user wants to send an email to
     * Generates the schedules status sheet as is and then grabs and sends it to the email
     */
    public String emailCareer(String recipient){
        Pattern specialCheck = Pattern.compile("[@]");
        Matcher checkString = specialCheck.matcher(recipient);
        if(recipient.isEmpty() || !checkString.find()){
            return("Invalid Input");
        }
        else {
            this.GenerateOverviewSheet();
            String sender = "amazeall12@gmail.com";
            String host = "smtp.gmail.com";
            Properties emailProp = new Properties();
            emailProp.put("mail.smtp.host", host);
            emailProp.put("mail.smtp.port", "465");
            emailProp.put("mail.smtp.ssl.enable", "true");
            emailProp.put("mail.smtp.auth", "true");
            javax.mail.Session emailSession = javax.mail.Session.getInstance(emailProp, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("amazeall12@gmail.com", "ivktfucvmhubkduz");
                }
            });
            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(emailSession);
                // Set From: header field of the header.
                message.setFrom(new InternetAddress(sender));
                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                // Set Subject: header field
                message.setSubject(this.scheduleName + "- Schedule Email");
                // Now set the actual message

                String home = System.getProperty("user.home");
                MimeMultipart mmp = new MimeMultipart();
                MimeBodyPart emailContent = new MimeBodyPart();
                emailContent.setText("This is sent from Amaze-All college career program!");
                MimeBodyPart mbp = new MimeBodyPart();
                mbp.attachFile(new File(home + "/Downloads/" + this.scheduleName + ".pdf"));
                mmp.addBodyPart(mbp);
                mmp.addBodyPart(emailContent);
                message.setContent(mmp);
                System.out.println("Sending email...");
                // Send message
                Transport.send(message);
                System.out.println("Sent email successfully!");
                return "EmailSent";
            } catch (MessagingException mex) {
                mex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "valid";
    }

    public void deleteSemesterSchedule(SemesterSchedule a) {
        semesterList.remove(a);
    }

    public ArrayList<SemesterSchedule> getSemesterList() {
        return semesterList;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public ArrayList<Major> getMajors() {
        return majors;
    }

    public void setMajors(ArrayList<Major> majors) {
        this.majors = majors;
    }

    public ArrayList<Major> getMinors() {
        return minors;
    }

    public void setMinors(ArrayList<Major> minors) {
        this.minors = minors;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    public int getPlannedSemesters() {
        return plannedSemesters;
    }

    public void setPlannedSemesters(int plannedSemesters) {
        this.plannedSemesters = plannedSemesters;
    }

    public ArrayList<Section> getCareerSuggestions() {
        return SuggestionBox.getSuggestions(suggestions, takenCourses);
    }

    public ArrayList<String> getTakenCourses() {
        return takenCourses;
    }

    public int getTotalCredits() {
        int total = 0;

        for(SemesterSchedule semesterSchedule: semesterList) {
            total += semesterSchedule.getCredits();
        }

        return total;
    }
}
