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
    private final Map<String, String> allergens;

    FoodList(final List<FoodItem> foodItems) {
        this.foodItems = Collections.unmodifiableList(foodItems);
        this.allergens = getAllergenIngredients();
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
        // Don't think this can be streamed, we are finding the intersection of all the ingredient lists.
        for (FoodItem foodItem: foodItems)
            common.retainAll(foodItem.ingredients);
        return new ArrayList<>(common);
    }

    private Map<String, List<String>> findAllergenCandidates() {
        Map<String, List<String>> candidates = new HashMap<>();
        // Extract a list of all the unique allergen names.
        List<String> allergens = foodItems.stream()
                .flatMap(i -> i.allergens.stream())
                .distinct()
                .collect(Collectors.toList());
        // For each allergen find a list of candidate ingredients it might be in.
        for (String allergen : allergens) {
            List<FoodItem> itemsWithAllergen = findFoodItemsWithAllergen(allergen);
            List<String> commonIngredients = findCommonIngredients(itemsWithAllergen);
            candidates.put(allergen, commonIngredients);
        }
        return candidates;
    }

    private Map<String, String> getAllergenIngredients() {
        Map<String, String> allergens = new HashMap<>();
        Map<String, List<String>> allergenCandidates = findAllergenCandidates();
        do {
            // Find the known allergens (only one candidate).
            allergenCandidates.entrySet().stream()
                    .filter(e -> e.getValue().size() == 1)
                    .forEach(e -> allergens.put(e.getKey(), e.getValue().get(0)));
            // Remove the known allergens from the candidates list.
            allergens.keySet().forEach(allergenCandidates::remove);
            allergenCandidates.values().forEach(c -> c.removeAll(allergens.values()));
        } while (allergenCandidates.size() > 0);
        return allergens;
    }

    long countNonAllergenIngredients() {
        return this.foodItems.stream()
                .flatMap(i -> i.ingredients.stream())
                .filter(i -> !this.allergens.containsValue(i))
                .count();
    }

    String listAllergenIngredients() {
        return this.allergens.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.joining(","));
    }
}


public class Advent2020Day21 {

    private static void testCountNonAllergenIngredients(final FoodList foodList) {
        long expectedCount = 5;
        long actualCount = foodList.countNonAllergenIngredients();
        assert actualCount == expectedCount :
                String.format("Expected non-allergen ingredient count to be %d not %d!%n", expectedCount, actualCount);
    }

    private static void testListAllergenIngredients(final FoodList foodList) {
        String expectedIngredients = "mxmxvkd,sqjhc,fvjkl";
        String actualIngredients = foodList.listAllergenIngredients();
        assert actualIngredients.equals(expectedIngredients) :
                String.format("Expected allergen ingredient list to be \"%s\" not \"%s\"!%n", expectedIngredients, actualIngredients);
    }

    public static void main(final String[] args) {
        FoodList testFood = FoodList.fromFile("2020/day21/test21a.txt");
        testCountNonAllergenIngredients(testFood);
        testListAllergenIngredients(testFood);
        FoodList food = FoodList.fromFile("2020/day21/input21.txt");
        System.out.printf("Day 21, part 1, non-allergen ingredient count is %d.%n", food.countNonAllergenIngredients());
        System.out.printf("Day 21, part 2, allergen ingredient list is \"%s\".%n", food.listAllergenIngredients());
    }
}
