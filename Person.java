package com.peter.foward;

public class Person {
    private int id;
    private String name;
    private String nickname;
    // Add other fields as needed

    public Person(int id, String name, String nickname) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        // Initialize other fields
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    // Add getter methods for other fields

    // You can also add setter methods if needed
}
