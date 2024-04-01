package com.example.greenplate;
import static org.junit.Assert.assertEquals;

import com.example.greenplate.models.Meal;

import org.junit.Before;
import org.junit.Test;

public class MealModelUnitTest {
    Meal meal;
    @Before
    public void start() {
        meal = new Meal("pasta", "500", "userId", "sampleDate");
    }

    @Test
    public void testGetMealName() {
        assertEquals("pasta", meal.getMealName());
    }

    @Test
    public void testSetMealName() {
        meal.setMealName("omelette");
        assertEquals("omelette", meal.getMealName());
    }

    @Test
    public void testGetCalories() {
        assertEquals("500", meal.getCalories());
    }

    @Test
    public void testSetCalories() {
        meal.setCalories("200");
        assertEquals("200", meal.getCalories());
    }

    @Test
    public void testGetUserId() {
        assertEquals("userId", meal.getUserId());
    }

    @Test
    public void testSetUserId() {
        meal.setUserId("userId2");
        assertEquals("userId2", meal.getUserId());
    }

    @Test
    public void testGetDate() {
        assertEquals("sampleDate", meal.getDate());
    }

    @Test
    public void testSetDate() {
        meal.setDate("sampleDate2");
        assertEquals("sampleDate2", meal.getDate());
    }
}