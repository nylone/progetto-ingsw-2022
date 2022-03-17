package it.polimi.ingsw.model;

import java.util.Arrays;

public class Cloud {
    Student[] students = new Student[4];
    StudentBag studentBag = new StudentBag();

    public void fillCloud(){
        //todo devo sapere quanti giocatori ci sono
    }
    public Student[] getStudents(){ //get cloud's students without removing them

        return students.clone();
    }
    public Student[] extractStudents(){ //get cloud's students removing them
        Student[] extractedStudents = students.clone();
        Arrays.fill(students,null);
        return extractedStudents;
    }

}
