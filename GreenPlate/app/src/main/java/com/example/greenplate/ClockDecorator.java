package com.example.greenplate;

public class ClockDecorator extends HomeScreenDecorator {
    public ClockDecorator(HomeScreenElement decoratedElement) {
        super(decoratedElement);
    }

    @Override
    public void display() {
        // Add clock component to the home screen before displaying the decorated element
        // For example:
        // drawClock();
        // Call the display method of the decorated element
        super.display();
    }
}

