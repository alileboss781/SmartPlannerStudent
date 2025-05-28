package fr.eseo.e3e.smartplanner.model;

import java.util.List;

public class Student extends  Person {
    public Student(String surname, List<Crenau> crenaus, Profession profession, String firsname) {
        super(surname, crenaus, profession, firsname);
    }
}
