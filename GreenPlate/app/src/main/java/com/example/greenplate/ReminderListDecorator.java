package com.example.greenplate;

public class ReminderListDecorator extends HomeScreenDecorator {
    public ReminderListDecorator(HomeScreenElement decoratedElement) {
        super(decoratedElement);
    }

    @Override
    public void display() {
        // Add goal's list component to the home screen before displaying the decorated element
        // For example:
        // goalsListDisplay();
        // Call the display method of the decorated element
        super.display();
    }
}
