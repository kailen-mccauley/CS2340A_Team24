package com.example.greenplate.models;

public class ShoppingDecorator implements Item {
    private Item item;
    private int quantity;

    public ShoppingDecorator(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
    @Override
    public String getID() {
        return null;
    }

    @Override
    public void setID(String id) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
