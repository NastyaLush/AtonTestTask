package com.runtik.team2.task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileManager {
    private FileManager() {
    }

    public static List<Person> readFile(String filePath, Printer p) throws IOException {
        Path path1 = Path.of(filePath);
        List<Person> persons = new ArrayList<>();
        InputStream inputStream = Files.newInputStream(path1);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            reader.lines()
                  .forEach(line -> {
                      String[] split = line.split(":");

                      Optional<Person> first = persons.stream()
                                                      .filter(person -> person.getName()
                                                                              .equalsIgnoreCase(split[0]))
                                                      .findFirst();
                      first.ifPresentOrElse(person -> person.addPhrase(split[1]), () -> persons.add(new Person(split[0], p)));
                  });
        }
        return persons;
    }
}
