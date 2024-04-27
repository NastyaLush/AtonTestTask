package com.runtik.task1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDb {
    private final Map<Long, Person> accountToPerson = new HashMap<>();
    private final Map<String, List<Person>> nameToPerson = new HashMap<>();
    private final Map<Double, List<Person>> valueToPerson = new HashMap<>();

    public void add(Person person) {
        if (accountToPerson.get(person.getAccount()) != null) {
            throw new IllegalArgumentException("Account already exists");
        }
        accountToPerson.put(person.getAccount(), person);
        nameToPerson.computeIfAbsent(person.getName(), k -> new ArrayList<>())
                    .add(person);
        valueToPerson.computeIfAbsent(person.getValue(), k -> new ArrayList<>())
                     .add(person);
    }

    public void delete(Long account) {
        Person remove = accountToPerson.remove(account);
        nameToPerson.computeIfAbsent(remove.getName(), k -> new ArrayList<>())
                    .remove(remove);
        valueToPerson.computeIfAbsent(remove.getValue(), k -> new ArrayList<>())
                     .remove(remove);
    }

    public void update(Person person) {
        delete(person.getAccount());
        add(person);
    }

    public Person getPersonsByAccount(Long account) {
        return accountToPerson.get(account);
    }

    public List<Person> getPersonsByName(String name) {
        return nameToPerson.computeIfAbsent(name, k -> new ArrayList<>());
    }

    public List<Person> getPersonsByValue(Double value) {
        return valueToPerson.computeIfAbsent(value, k -> new ArrayList<>());
    }


}
