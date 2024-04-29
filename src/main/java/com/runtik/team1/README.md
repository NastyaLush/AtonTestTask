# Описание решения тестового задания Java-разработчик в команду корпоративной шины данных и микросервисов

Для начала предположим, что поле `account` является уникальным, другой случай будет рассмотрен позднее.

В качестве основной структуры данных была выбрана `Map` и ее имплементация `HashMap`, данная структура позволяет находить
объект за `O(1)` по ключу, где в нашем случае ключ - account.

```java
    private final Map<Long, Person> accountToPerson = new HashMap<>();
```

Для реализации возможности быстрого поиска по всем полям реализованы так называемые "индексы", которые представляют собой тоже Map, 
что позволяет получать запись(и) по любому полю за `O(1)`

```java
    private final Map<String, List<Person>> nameToPerson = new HashMap<>();
    private final Map<Double, List<Person>> valueToPerson = new HashMap<>();
```

**Вставка**: в HashMap происходит в среднем за O(1), как и в List в среднем за O(1) => `O(1)`

**Удаление**: поиск значения с заданным ключом за O(1) плюс поиск в самом листе данного объекта O(len(list)), таким
образом общая сложность будет O(len(list))+ O(1)=`O(len(list))`, где list - все объекты с текущим ключом

**Обновление**: имеет сложность равную сумме удаления и обновление `O(len(list))`

**Поиск**: за `O(1)`

---
Теперь рассмотрим случай, когда `account` неуникальное поле.

Имеет смысл использовать те же самые коллекции, которые были предложены ранее с небольшими изменениями

```java
    private final Map<Long, List<Person>> accountToPerson = new HashMap<>();
    private final Map<String, List<Person>> nameToPerson = new HashMap<>();
    private final Map<Double, List<Person>> valueToPerson = new HashMap<>();
```
Однако общая сложность никак не изменится. Единственные изменения будут в процессе 
удаления (и обновления тоже, так как внутри он будет использовать удаление), в этом случае у нас изменится сигнатура метода, 
нам надо будет передавать не только ключ, но и весь объект целиком и делать поиск по каждому полю соответственно, 
однако в задании не указано, как именно должна вести себя программа при повторах(допускать их или нет, 
обновлять и удалять все обьекты или только первый встречный), но это никак не повлияет на алгоритмическую сложность.

---
Что касается сложности по памяти. Как известно в java все объекты хранятся по ссылке, и по факту в 
листах хранятся ссылки на наш объект, размер которых не более long и 
зависит от используемой архитектуры и версии виртуальной машины Java (JVM).

Алгоритмическая сложность алгоритма по памяти O(n)

Код основных элементов ниже, полную реализацию можно найти [здесь](https://github.com/NastyaLush/AtonTestTask/tree/master/src/main/java/com/runtik/team1)
--------

InMemoryDb

```java
package com.runtik.team1;

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
```
Person
```java
package com.runtik.team1;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {
    private Long account;
    private String name;
    private Double value;
}

```
