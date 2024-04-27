package com.runtik.team1;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        InMemoryDb inMemoryDb = new InMemoryDb();
        inMemoryDb.add(new Person(12l, "name", 12d));
        inMemoryDb.add(new Person(13l, "name", 12d));
        inMemoryDb.add(new Person(14l, "name", 12d));
        inMemoryDb.update(new Person(13l, "name1", 12d));
    }
}
