package com.runtik.team2.task1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Phaser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
public class Person extends Thread {
    @Getter
    private final String personName;
    private final List<String> phrases = new ArrayList<>();
    private final Printer printer;
    private final Phaser phaser;
    private final List<Person> order;

    @Override
    public void run() {
        for (String phrase : phrases) {
            waitForTurn();
            printer.print(personName + ": " + phrase);
            phaser.arriveAndAwaitAdvance();
        }
        phaser.arriveAndDeregister();
    }

    private void waitForTurn() {
        while (!isMyTurn()) {
            phaser.arriveAndAwaitAdvance();
        }
    }

    private boolean isMyTurn() {
        return order.get(phaser.getPhase()).getPersonName().equals(personName);
    }

    public void addPhrase(String phrase) {
        phrases.add(phrase);
    }
}
