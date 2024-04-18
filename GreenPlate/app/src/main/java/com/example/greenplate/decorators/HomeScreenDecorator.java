package com.example.greenplate.decorators;

import com.example.greenplate.HomeScreenElement;

public abstract class HomeScreenDecorator implements HomeScreenElement {
    protected HomeScreenElement decoratedElement;

    public HomeScreenDecorator(HomeScreenElement decoratedElement) {
        this.decoratedElement = decoratedElement;
    }

    @Override
    public void display() {
        decoratedElement.display();
    }
}


