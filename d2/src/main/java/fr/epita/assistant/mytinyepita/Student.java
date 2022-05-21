package fr.epita.assistant.mytinyepita;

import java.util.HashSet;
import java.util.Set;

public class Student extends Person implements Observable<Student>, Observer<Newsgroup> {

    private Set<Observer<Student>> observers;
    private int energy;
    private Status status;

    public Student(final String login, final int energy) {
        this.login = login;
        this.energy = energy;
        status = Status.OK;
        observers = new HashSet<Observer<Student>>();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Set<Observer<Student>> getObservers() {
        return observers;
    }

    @Override
    public void register(Observer<Student>... observers) {
        for (Observer<Student> observer : observers) {
            this.observers.add(observer);
        }
    }

    @Override
    public void unregister(Observer<Student> observer) {
        this.observers.remove(observer);
    }

    @Override
    public void fire(Student event) {
        for (Observer<Student> observer : observers) {
            observer.onEvent(event);
        }
    }

    @Override
    public void postNews(Newsgroup newsgroup, String message) throws IllegalArgumentException {
        if (!newsgroup.getObservers().contains(this)) {
            throw new IllegalArgumentException("Student cannot post in a newsgroup he is not subscribed to.");
        }
        newsgroup.postNews(this, message);
    }

    @Override
    public void onEvent(Newsgroup event) {
        System.out.println(login + " reads the news.");
    }
    public void subscribeToNewsgroup(Newsgroup newsgroup){
        newsgroup.register(this);
    }

    public void unsubscribeToNewsgroup(Newsgroup newsgroup) throws IllegalArgumentException{
        if (!newsgroup.getObservers().contains(this)){
            throw new IllegalArgumentException("Student is not subscribed to this newsgroup.");
        }
        newsgroup.unregister(this);
    }

    public void work() throws IllegalStateException{
        if (this.isAvailable()){
            throw new IllegalStateException("Student must be in a room to work.");
        }
        if (status.equals(Status.TIRED)){
            System.out.println(login + " is too tired to work.");
        }
        else{
            System.out.println(login + " is working.");
            energy--;
            if (energy == 0){
                status = Status.TIRED;
            }
        }
    }

    public void goToSleep() throws IllegalStateException{
        if (!this.isAvailable()){
            throw new IllegalStateException("Student must leave the room before going to sleep.");
        }
        energy = 5;
        status = Status.OK;
    }

    public void askForHelp(){
        if (observers.isEmpty()){
            System.out.println(login + " wishes he could be helped...");
        }
        else{
            if (!status.equals(Status.TIRED)) {
                status = Status.ASKING_FOR_HELP;
            }
            System.out.println(login + " is asking for help.");
            for (Observer<Student> observer : observers){
                observer.onEvent(this);
            }
        }
    }
}