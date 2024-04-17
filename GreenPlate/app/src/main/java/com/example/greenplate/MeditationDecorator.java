package com.example.greenplate;

public class MeditationDecorator extends HomeScreenDecorator {
    public MeditationDecorator(HomeScreenElement decoratedElement) {
        super(decoratedElement);
    }

    @Override
    public void display() {
        /* Add mediation steak component to the home screen before displaying the decorated element
         should have a button users can click to indicate that they have met their
         meditation for the day and add to the streak
         For example:
         meditationDisplay();
         Call the display method of the decorated element
         */
        super.display();
    }
}
