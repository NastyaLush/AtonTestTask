# Тестовое задание для стажера Java-разработчик в команду технологий фонт-офиса

### Задание 1

Описание решения:

Релизован многопоточный вывод ситкома.

Переданный файл считывается в однопоточном режиме, 
где создаются объекты типа `Person` с соответствующими им репликами.
Далее в многопоточном режиме все объекты печатают фразы. 

Для сохранения порядка используется Phaser.

 Код основных элементов, полную реализацию можно найти [здесь](https://github.com/NastyaLush/AtonTestTask/tree/master/src/main/java/com/runtik/team2/task1)
 ---
FileManager (чтение файла)
```java
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
```

Person - класс типа Thread, реализующий логику вывода ситкома
```java
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
```
Main - основной класс, где происходит чтение и вывод ситкома
```java
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

```