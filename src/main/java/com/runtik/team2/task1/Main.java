package com.runtik.team2.task1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Printer printer = System.out::println;
        List<Person> peoples = FileManager.readFile("src/main/java/com/runtik/task2/task1/sitcom.txt", printer);
        List<Thread> list = new ArrayList<>();
        peoples.forEach(person -> list.add(new Thread(person)));
        for (Thread t : list) {
            t.start();
        }
        for (Thread t : list) {
            t.join();
        }

    }
}
