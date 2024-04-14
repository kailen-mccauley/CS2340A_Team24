package com.example.greenplate;

public class FitnessDecorator extends HomeScreenDecorator {
    public FitnessDecorator(HomeScreenElement decoratedElement) {
        super(decoratedElement);
    }

    @Override
    public void display() {
        // Add fitness streak counter component to the home screen before displaying the decorated element
        // should have a button users can click to indicate that they have met their fitness for the day
                //and add to the streak
        // For example:
        // fitnessDisplay();
        // Call the display method of the decorated element
        super.display();
    }
}
