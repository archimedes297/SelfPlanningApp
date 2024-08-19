package com.peter.foward;

public class Procedure {
    private int id;
    private String name;
    private String steps;

    // Constructors

    public Procedure() {
        // Default constructor
    }

    public Procedure(int id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }

    // Getter methods

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSteps() {
        return steps;
    }

    // Setter methods

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }
}


