package fr.epita.assistant.mytinyepita;

public abstract class Person {
    protected boolean available = true;

    public String getLogin() {
        return login;
    }

    protected String login;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public abstract void postNews(final Newsgroup newsgroup, final String message);
}

