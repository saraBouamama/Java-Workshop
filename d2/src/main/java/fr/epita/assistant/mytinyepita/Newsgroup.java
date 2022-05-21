package fr.epita.assistant.mytinyepita;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Newsgroup implements Observable<Newsgroup> {
    protected Set<Observer<Newsgroup>> observers;
    @Override
    public Set<Observer<Newsgroup>> getObservers(){
        return observers;
    }

    @Override
    public void register(Observer<Newsgroup>... observers){
        for (Observer<Newsgroup> observer : observers){
            this.observers.add(observer);
        }
    }

    @Override
    public void unregister(Observer<Newsgroup> observer){
        this.observers.remove(observer);
    }

    @Override
    public void fire(Newsgroup event) {
        for (Observer<Newsgroup> obs : this.getObservers()) {
            obs.onEvent(event);
        }

    }

    private String name;
    public Newsgroup(final String name){
        this.name = name;
        this.observers = new HashSet<>();
    }

    public String getName() {
        return name;
    }
    public void postNews(final Person person, final String message){
        System.out.println(person.getLogin()+" sends a news in " + this.name+".");
        System.out.println("Message: "+ message);
        fire(this);
    }
}