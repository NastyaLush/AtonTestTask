package com.runtik.team2.task1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Person implements Runnable {
    @Getter
    protected String name;
    protected final List<String> phrases = new ArrayList<>();
    protected final Printer printer;

    @Override
    public void run() {
        Iterator<String> iterator = phrases.iterator();
        while (iterator.hasNext()) {
            String phrase = iterator.next();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (printer) {
                printer.print(name + ": " + phrase);
            }
        }

    }

    public void addPhrase(String phrase) {
        phrases.add(phrase);
    }
}
