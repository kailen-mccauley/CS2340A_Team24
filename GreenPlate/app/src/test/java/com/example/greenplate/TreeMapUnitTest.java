package com.example.greenplate;
import static org.junit.Assert.assertEquals;
import com.example.greenplate.models.Ingredient;
import org.junit.Test;
import java.util.Map;
import java.util.TreeMap;

public class TreeMapUnitTest {
    @Test
    public void testAddIngredients() {
        Map<String, Ingredient> map = new TreeMap<>();
        Ingredient ing1 = new Ingredient("flour", 60, 10, "id");
        Ingredient ing2 = new Ingredient("egg", 10, 5, "id");
        Ingredient ing3 = new Ingredient("wheat", 30, 70, "id");
        map.put("flour", ing1);
        map.put("egg", ing2);
        map.put("wheat", ing3);
        assertEquals("[egg, flour, wheat]", map.keySet().toString());
    }

    @Test
    public void testRemoveIngredients() {
        Map<String, Ingredient> map = new TreeMap<>();
        Ingredient ing1 = new Ingredient("flour", 60, 10, "id");
        Ingredient ing2 = new Ingredient("egg", 10, 5, "id");
        Ingredient ing3 = new Ingredient("wheat", 30, 70, "id");
        map.put("flour", ing1);
        map.put("egg", ing2);
        map.put("wheat", ing3);
        map.remove("flour");
        assertEquals("[egg, wheat]", map.keySet().toString());
    }
}
