package day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        List<String> allergens = new ArrayList<>(Arrays.asList(groups[1].split(", ")));
        return new FoodItem(ingredients, allergens);
    }

    boolean containsIngredient(final String ingredient) {
        return this.ingredients.contains(ingredient);
    }

    boolean containsAnyIngredients(final List<String> anyIngredients) {
        for (String ingredient: this.ingredients) {
            if (anyIngredients.contains(ingredient))
                return true;
        }
        return false;
    }

    boolean containsAllergen(final String allergen) {
        return this.allergens.contains(allergen);
    }

    boolean allAllergens() {
        return this.ingredients.size() == this.allergens.size();
    }

    List<String> commonAllergens(final FoodItem foodItem) {
        List<String> common = new ArrayList<>();
        for (String allergen: this.allergens) {
            if (foodItem.containsAllergen(allergen)) {
                common.add(allergen);
            }
        }
        return common;
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

    private List<String> findUniqueIngredients() {
        List<String> uniqueIngredients = new ArrayList<>();
        for (FoodItem foodItem: this.food) {
            for (String ingredient : foodItem.ingredients) {
                if (uniqueIngredient(foodItem, ingredient)) {
                    uniqueIngredients.add(ingredient);
                }
            }
        }
        return uniqueIngredients;
    }

    private Map<String, String> findAllergens(final FoodItem allAllergens) {
        Map<String, String> allergens = new HashMap<>();
        List<String> allergyIngredients = new ArrayList<>(allAllergens.ingredients);
        while (allergyIngredients.size() > 0) {
            String allergyIngredient = allergyIngredients.remove(0);
            for (FoodItem foodItem: this.food) {
                if (foodItem != allAllergens && foodItem.containsIngredient(allergyIngredient) && !foodItem.containsAnyIngredients(allergyIngredients)) {
                    List<String> commonAllergens = foodItem.commonAllergens(allAllergens);
                    if (commonAllergens.size() == 1) {
                        allergens.put(commonAllergens.get(0), allergyIngredient);
                    }
                }
            }
        }
        return allergens;
    }

    int countNonAllergenIngredients() {
        List<String> uniqueIngredients = findUniqueIngredients();
        for (FoodItem foodItem: this.food) {
            foodItem.ingredients.removeAll(uniqueIngredients);
        }
        Map<String, String> allergens = new HashMap<>();
        for (FoodItem foodItem: this.food) {
            if (foodItem.allAllergens()) {
                allergens.putAll(findAllergens(foodItem));
            }
        }
        for (FoodItem foodItem: this.food) {
            foodItem.ingredients.removeAll(allergens.values());
            foodItem.allergens.removeAll(allergens.keySet());
        }
        return uniqueIngredients.size();
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
