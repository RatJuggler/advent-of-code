package day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class FoodItem {

    final List<String> ingredients;
    final List<String> allergens;

    FoodItem(final List<String> ingredients, final List<String> allergens) {
        this.ingredients = ingredients;
        this.allergens = allergens;
    }

    static FoodItem fromString(final String item) {
        String[] groups = item.substring(0, item.length() - 1).split(" \\(contains ");
        List<String> ingredients = new ArrayList<>(Arrays.asList(groups[0].split(" ")));
        List<String> allergens = new ArrayList<>(Arrays.asList(groups[1].split(" ")));
        return new FoodItem(ingredients, allergens);
    }

    boolean containsIngredient(final String ingredient) {
        return this.ingredients.contains(ingredient);
    }

    boolean containsAllergen(final String allergen) {
        return this.allergens.contains(allergen);
    }
}


class FoodList {

    private final List<FoodItem> food;

    FoodList(final List<FoodItem> food) {
        this.food = food;
    }

    static FoodList fromFile(final String filename) {
        List<FoodItem> food;
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            food = stream.map(FoodItem::fromString)
                    .collect(Collectors.toList());
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Problem reading food ingredients file!", ioe);
        }
        return new FoodList(food);
    }

    private boolean uniqueIngredient(final FoodItem infoodItem, final String ingredient) {
        for (FoodItem foodItem: this.food) {
            if (foodItem != infoodItem && foodItem.containsIngredient(ingredient))
                return false;
        }
        return true;
    }

    int countNonAllergenIngredients() {
        int nonAllergenIngredients = 0;
        for (FoodItem foodItem: this.food) {
            for (Iterator<String> it = foodItem.ingredients.iterator(); it.hasNext(); ) {
                String ingredient = it.next();
                if (uniqueIngredient(foodItem, ingredient)) {
                    System.out.println(ingredient);
                    it.remove();
                    nonAllergenIngredients++;
                }
            }
        }
        return nonAllergenIngredients;
    }
}


public class Advent2020Day21 {

    private static void testCountNonAllergenIngredients() {
        int expectedCount = 5;
        FoodList food = FoodList.fromFile("2020/day21/test21a.txt");
        int actualCount = food.countNonAllergenIngredients();
        assert actualCount == expectedCount :
                String.format("Expected non-allergen ingredient count to be %d not %d!%n", expectedCount, actualCount);

    }

    public static void main(final String[] args) {
        testCountNonAllergenIngredients();
        FoodList food = FoodList.fromFile("2020/day21/input21.txt");
        System.out.printf("Day 21, part 1, non-allergen ingredient count is %d.%n", food.countNonAllergenIngredients());
    }
}
