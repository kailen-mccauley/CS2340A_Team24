package com.example.greenplate.models;

public class ConsoumableItem  implements Item {
    private String id;
    private String name;

    public ConsoumableItem(String name, String id) {
        this.name = name;
        this.id = id;
    }
    @Override
    public String getID() {
        return id;
    }
    @Override
    public void setID(String id) {
        this.id = id;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }
}
