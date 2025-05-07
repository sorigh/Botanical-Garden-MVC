package org.example.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    private final List<Observer> observers = new ArrayList<>();

    protected void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}
