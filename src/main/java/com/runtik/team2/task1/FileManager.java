package com.runtik.team2.task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class FileManager {
    private FileManager() {
    }

    public static Scenario readScenario(String filePath, Printer printer, Phaser phaser) throws IOException {
        Path path1 = Path.of(filePath);
        List<Person> persons = new ArrayList<>();
        List<Person> order = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path1)))) {
            reader.lines()
                    .forEach(line -> {
                        String[] split = line.split(":");
                        String personName = split[0];
                        String phrase = split[1];

                        Person person = persons.stream()
                                .filter(p -> p.getPersonName().equalsIgnoreCase(personName))
                                .findFirst()
                                .orElseGet(() -> {
                                    Person newPerson = new Person(personName, printer, phaser, order);
                                    persons.add(newPerson);
                                    return newPerson;
                                });
                        person.addPhrase(phrase);
                        order.add(person);
                    });
        }
        return new Scenario(persons, order);
    }
}
