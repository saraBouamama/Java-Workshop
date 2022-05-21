package fr.epita.assistant.mytinyepita;

import java.util.ArrayList;
import java.util.List;

public class ComputerRoom {
    protected String name;
    protected int capacity;
    protected List<Student> students;
    protected List<Assistant> assistants;
    protected int size;


    public String getName() {
        return name;
    }

    public int getSize() {
        return students.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Assistant> getAssistants() {
        return assistants;
    }

    public ComputerRoom(final String name, int capacity) {
        this.capacity = capacity;
        this.name = name;
        this.size = capacity;
        this.students = new ArrayList<Student>();
        this.assistants = new ArrayList<Assistant>();
    }

    public void addStudent(final Student student) throws InsertionException{
        if (this.getSize() == this.getCapacity()){
            throw new InsertionException("Room is full.");
        }
        if (students.contains(student)){
            throw new InsertionException("Student already inside the room.");
        }
        if (!student.isAvailable()){
            throw new InsertionException("Student already inside another room.");
        }
        students.add(student);
        student.setAvailable(false);
        for (Assistant assistant : assistants){
            student.register(assistant);
        }
    }

    public void addStudents(final Student... students) throws InsertionException{
        for (Student student : students){
            try{
                this.addStudent(student);
            }
            catch (InsertionException e){
                throw new InsertionException(e.getMessage());
            }
        }
    }

    public void addAssistant(final Assistant assistant) throws InsertionException {
        if (this.assistants.contains(assistant)) {
            throw new InsertionException("Assistant already inside the room.");
        }
        if (!assistant.isAvailable()) {
            throw new InsertionException("Assistant already inside another room.");
        }
        assistants.add(assistant);
        for (Student s : this.students) {
            s.getObservers().add(assistant);
        }
        assistant.setAvailable(false);
    }

    public void addAssistants(final Assistant... assistants) throws InsertionException {
        for (Assistant a : assistants) {
            try {
                this.addAssistant(a);
            } catch(Exception e) {
                throw e;
            }
        }
    }

    public void removeStudent(final Student student) throws IllegalArgumentException{
        if (!students.contains(student)){
            throw new IllegalArgumentException("Student is not inside the room.");
        }
        students.remove(student);
        student.setAvailable(true);
        for (Assistant assistant : assistants){
            student.unregister(assistant);
        }
    }

    public void removeStudents(final Student... students) throws IllegalArgumentException{
        for (Student student : students){
            try{
                this.removeStudent(student);
            }
            catch (IllegalArgumentException e){
                throw e;
            }
        }
    }

    public void removeAssistant(final Assistant assistant) throws IllegalArgumentException{
        if (!assistants.contains(assistant)){
            throw new IllegalArgumentException("Assistant is not inside the room.");
        }
        assistants.remove(assistant);
        assistant.setAvailable(true);
        for (Student student : students){
            student.unregister(assistant);
        }
    }

    public void removeAssistants(final Assistant... assistants) throws IllegalArgumentException{
        for (Assistant assistant : assistants){
            try{
                this.removeAssistant(assistant);
            }
            catch (IllegalArgumentException e){
                throw e;
            }
        }
    }
}