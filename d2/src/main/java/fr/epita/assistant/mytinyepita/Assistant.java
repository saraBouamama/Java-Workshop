package fr.epita.assistant.mytinyepita;



public class Assistant extends Person implements Observer<Student>{

    public  Assistant(String login) {
        this.login = login;
        this.available = true;
    }

    @Override
    public void onEvent(Student event) {
        if (event.getStatus() == Status.ASKING_FOR_HELP) {
            System.out.println(this.login + " helps him.");
            event.setStatus(Status.OK);
        }
        if (event.getStatus() == Status.TIRED) {
            System.out.println(this.login + " suggests " + event.login + " to go to sleep.");
        }
    }

    @Override
    public void postNews(Newsgroup newsgroup, String message) {
        newsgroup.postNews(this, message);
    }
}