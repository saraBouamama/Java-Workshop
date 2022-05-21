package fr.epita.assistant.mytinyepita;

/**
 * Notification callback.
 *
 * @param <EVENT_T> The event being sent.
 */
@FunctionalInterface
public interface Observer<EVENT_T> {

    void onEvent(final EVENT_T event);
}
