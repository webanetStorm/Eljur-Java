package com.example.eljur.ui.schedule;


import com.example.eljur.model.Grade;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class FirebaseSyncCache {
    private static DataSnapshot holidaysSnapshot;
    private static DataSnapshot academicYearSnapshot;
    private static DataSnapshot scheduleSnapshot;
    private static DataSnapshot termsSnapshot;
    private static DataSnapshot lessonsSnapshot;
    private static DataSnapshot homeworksSnapshot;
    private static DataSnapshot gradesSnapshot;

    public static void loadAllData( DatabaseReference dbRef, String classId, String userId) {
        dbRef.child("holidays").get().addOnSuccessListener(snapshot -> holidaysSnapshot = snapshot);
        dbRef.child("academic_year").get().addOnSuccessListener(snapshot -> academicYearSnapshot = snapshot);
        dbRef.child("schedule").get().addOnSuccessListener(snapshot -> scheduleSnapshot = snapshot);
        dbRef.child("terms").get().addOnSuccessListener(snapshot -> termsSnapshot = snapshot);
        dbRef.child("lessons").get().addOnSuccessListener(snapshot -> lessonsSnapshot = snapshot);
        dbRef.child("homeworks").child(classId).get().addOnSuccessListener(snapshot -> homeworksSnapshot = snapshot);
        dbRef.child("grades").child(userId).get().addOnSuccessListener(snapshot -> gradesSnapshot = snapshot);
    }

    public static DataSnapshot getHolidaysSnapshot() { return holidaysSnapshot; }
    public static DataSnapshot getAcademicYearSnapshot() { return academicYearSnapshot; }
    public static DataSnapshot getScheduleSnapshot() { return scheduleSnapshot; }
    public static DataSnapshot getTermsSnapshot() { return termsSnapshot; }

    public static String getLessonTime(int lessonNumber, boolean isStart) {
        if (lessonsSnapshot != null && lessonsSnapshot.hasChild(String.valueOf(lessonNumber))) {
            return lessonsSnapshot.child(String.valueOf(lessonNumber))
                    .child(isStart ? "start_time" : "end_time").getValue(String.class);
        }
        return "--:--";
    }

    public static String getHomework(String classId, String subject, LocalDate date) {
        if (homeworksSnapshot != null && homeworksSnapshot.hasChild(date.toString())) {
            for (DataSnapshot hw : homeworksSnapshot.child(date.toString()).getChildren()) {
                if (subject.equals(hw.child("subject").getValue(String.class))) {
                    return hw.child("description").getValue(String.class);
                }
            }
        }
        return null;
    }

    public static List<Grade> getGrades( String userId, String subject, LocalDate date) {
        List<Grade> grades = new ArrayList<>();
        if (gradesSnapshot != null && gradesSnapshot.hasChild(date.toString())) {
            for (DataSnapshot g : gradesSnapshot.child(date.toString()).getChildren()) {
                if (subject.equals(g.child("subject").getValue(String.class))) {
                    grades.add(new Grade("-", g.child("value").getValue(Integer.class), g.child("weight").getValue(Integer.class)));
                }
            }
        }
        return grades;
    }
}
