package com.runtik.team2.task1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class Main {
    public static void main(String[] args) throws IOException {
        Phaser phaser = new Phaser();
        Printer printer = System.out::println;
        Scenario scenario = FileManager.readScenario("src/main/java/com/runtik/team2/task1/sitcom.txt", printer, phaser);
        List<Person> peoples = scenario.persons();
        for (int i = 0; i < peoples.size(); i++) {
            phaser.register();
        }
        for (Person person : peoples) {
            person.start();
        }
    }
}
