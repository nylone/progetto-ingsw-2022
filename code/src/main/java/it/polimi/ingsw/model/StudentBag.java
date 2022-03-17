package it.polimi.ingsw.model;

import java.util.ArrayList;

public class StudentBag {
    private ArrayList<Student> mixedStudents;
    public StudentBag(){
        mixedStudents = new ArrayList<>();
    }
    public ArrayList<Student> draw(int size) { //todo aggiungere eccezione che garantisca size <= mixedStudents.size()
        ArrayList<Student> extractedStudents = new ArrayList<Student>();
        int random_index = 0;
        for(int i=0; i<size; i++){
            random_index = (int) Math.random() * mixedStudents.size();
            extractedStudents.add(mixedStudents.get(random_index));
            mixedStudents.remove(random_index);
        }
        return extractedStudents;
    }

    public void addStudent(Student student){
        mixedStudents.add(student);
    }


}
