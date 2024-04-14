package com.example.greenplate;

public class ReminderListDecorator extends HomeScreenDecorator {
    public ReminderListDecorator(HomeScreenElement decoratedElement) {
        super(decoratedElement);
    }

    @Override
    public void display() {
        // Add reminder's list component to the home screen before displaying the decorated element
        // For example:
        // reminderListDisplay();
        // Call the display method of the decorated element
        super.display();
    }
}
