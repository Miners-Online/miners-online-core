package uk.minersonline.minestom.lobby.stuff.people;

import java.util.UUID;

public class Person {
    public UUID uuid;
    public String name;
    public int age;
    public String address;

    public Person(String name, int age, String address) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String speak(String phrase) {
        return this.name + " said " + phrase;
    }
}
