package uk.minersonline.minestom.lobby.stuff.people;

import java.util.UUID;

public class Person {
    private final UUID uuid;
    private String name;
    private int age;
    private String address;

    public Person(String name, int age, String address) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String speak(String phrase) {
        return this.name + " said " + phrase;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }
}
