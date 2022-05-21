package fr.epita.assistant.mytinyepita;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class School {

    private List<ComputerRoom> listRooms;

    private List<Student> studentList;

    public School() {
        this.listRooms = new ArrayList<>();
    }

    public List<ComputerRoom> getComputerRooms() {
        return this.listRooms;
    }

    public List<ComputerRoom> getAvailableRooms() {
        List<ComputerRoom> list = new ArrayList<>();
        for (ComputerRoom room : listRooms)
        {
            if (room.getSize() != room.getCapacity()) {
                list.add(room);
            }
        }
        return list;
    }

    public void addComputerRooms(final ComputerRoom... computerRooms) throws InsertionException {
        for (ComputerRoom room : computerRooms) {
            for (ComputerRoom thisroom : this.listRooms){
                if (Objects.equals(room.getName(), thisroom.getName())) {
                    throw new InsertionException("Another room with the same name already exists.");
                }
            }
            this.listRooms.add(room);
        }
    }

    public Optional<ComputerRoom> findStudent(final String login){
        for (ComputerRoom room : this.listRooms) {
            for (Student stu : room.getStudents()) {
                if (stu.login == login){
                    return Optional.of(room);
                }
            }

        }
        return Optional.empty();
    }

    public int numberOfStudents(){
        int c = 0;
        for (ComputerRoom room : listRooms) {
            c += room.getSize();
        }
        return c;
    }
    public void printState(){
        for (int p = 0; p < this.listRooms.size(); p = p + 1) {
            ComputerRoom room = listRooms.get(p);
            System.out.println(room.getName() + " (" + room.getSize() + "/" + room.getCapacity() + ")");
            System.out.println("Assistants:");
            for (Assistant ass : room.getAssistants() ) {
                System.out.println(ass.login);
            }
            System.out.println("Students:");
            for (Student stu : room.getStudents())
            {
                System.out.println(stu.login);
            }
            if (p != listRooms.size() - 1)
                System.out.println("---");
        }
    }
}
