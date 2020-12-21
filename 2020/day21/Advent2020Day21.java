package day21;


class FoodList {

    FoodList() {}

    static FoodList fromFile(final String filename) {
        return new FoodList();
    }

    int countNonAllergenIngredients() {
        return 0;
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
