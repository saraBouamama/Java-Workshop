package fr.epita.assistant.mytinyepita;

import java.util.Set;

/**
 * Observable interface
 *
 * @param <EVENT_T> Type that observed events/
 */
public interface Observable<EVENT_T> {

    /**
     * Set of observers.
     *
     * @return the set of observers.
     */
    Set<Observer<EVENT_T>> getObservers();

    /**
     * Register all the arguments as observers.
     *
     * @param observers Observers to register.
     */
    void register(final Observer<EVENT_T>... observers);

    /**
     * Unregister the argument from the observers.
     *
     * @param observer Observer to unregister.
     */
    void unregister(final Observer<EVENT_T> observer);

    /**
     * Notify all registered observers of the given event.
     *
     * @param event The event to notify observers with.
     */
    void fire(final EVENT_T event);
}
