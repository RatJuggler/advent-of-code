package day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
}


class FoodList {

    private final List<FoodItem> foodItems;
    private final Set<String> allergens;

    FoodList(final List<FoodItem> foodItems) {
        this.foodItems = Collections.unmodifiableList(foodItems);
        this.allergens = foodItems.stream()
                .flatMap(i -> i.allergens.stream())
                .collect(Collectors.toSet());
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

    private List<FoodItem> findFoodItemsWithAllergen(final String allergen) {
        return this.foodItems.stream()
                .filter(i -> i.allergens.contains(allergen))
                .collect(Collectors.toList());
    }

    private List<String> findCommonIngredients(final List<FoodItem> foodItems) {
        Set<String> common = new HashSet<>(foodItems.get(0).ingredients);
        for (FoodItem foodItem: foodItems) {
            common.retainAll(foodItem.ingredients);
        }
        return new ArrayList<>(common);
    }

    private Map<String, List<String>> findAllergenCandidates() {
        Map<String, List<String>> candidates = new HashMap<>();
        for (String allergen : this.allergens) {
            List<FoodItem> itemsWithAllergen = findFoodItemsWithAllergen(allergen);
            List<String> commonIngredients = findCommonIngredients(itemsWithAllergen);
            candidates.put(allergen, commonIngredients);
        }
        return candidates;
    }

    Map<String, String> getAllergenIngredients() {
        Map<String, List<String>> allergenCandidates = findAllergenCandidates();
        Map<String, String> allergens = new HashMap<>();
        do {
            for (String allergen : allergenCandidates.keySet()) {
                if (allergenCandidates.get(allergen).size() == 1) {
                    allergens.put(allergen, allergenCandidates.get(allergen).get(0));
                }
            }
            for (String allergen : allergens.keySet()) {
                allergenCandidates.remove(allergen);
            }
            for (String ingredient : allergens.values()) {
                for (List<String> candidates : allergenCandidates.values()) {
                    candidates.remove(ingredient);
                }
            }
        } while (allergenCandidates.size() > 0);
        return allergens;
    }

    int countNonAllergenIngredients() {
        Map<String, String> allergens = this.getAllergenIngredients();
        List<String> ingredients = this.foodItems.stream().flatMap(i -> i.ingredients.stream()).collect(Collectors.toList());
        ingredients.removeAll(allergens.values());
        return ingredients.size();
    }

    String listAllergenIngredients() {
        return this.getAllergenIngredients()
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.joining(","));
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

    private static void testListAllergenIngredients() {
        String expectedIngredients = "mxmxvkd,sqjhc,fvjkl";
        FoodList food = FoodList.fromFile("2020/day21/test21a.txt");
        String actualIngredients = food.listAllergenIngredients();
        assert actualIngredients.equals(expectedIngredients) :
                String.format("Expected allergen ingredient list to be \"%s\" not \"%s\"!%n", expectedIngredients, actualIngredients);
    }

    public static void main(final String[] args) {
        testCountNonAllergenIngredients();
        FoodList food1 = FoodList.fromFile("2020/day21/input21.txt");
        System.out.printf("Day 21, part 1, non-allergen ingredient count is %d.%n", food1.countNonAllergenIngredients());
        testListAllergenIngredients();
        FoodList food2 = FoodList.fromFile("2020/day21/input21.txt");
        System.out.printf("Day 21, part 2, allergen ingredient list is \"%s\".%n", food2.listAllergenIngredients());
    }
}
