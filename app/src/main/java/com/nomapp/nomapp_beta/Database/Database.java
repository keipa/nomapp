package com.nomapp.nomapp_beta.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by antonid on 12.07.2015.
 */
public class Database {
    private static Database database;

    private SQLiteDatabase generalDb;

    private static final String DATABASE_NAME = "Database.db";

    private static final String INGREDIENTS_TABLE_NAME = "Ingredients";
    private static final String INGREDIENT_ID = "_id";
    private static final String INGREDIENT_NAME = "name";
    private static final String IS_CHECKED = "checked";
    private static final String INGREDIENTS_FOR_WHAT_RECIPES = "forWhatRecipes";

    private static final String RECIPES_TABLE_NAME = "Recipes";
    private static final String RECIPES_ID = "_id";
    private static final String RECIPES_NAME = "Name";
    private static final String RECIPES_INGREDIENTS = "ingredients";
    private static final String RECIPES_HOW_TO_COOK = "howToCook";
    private static final String RECIPES_IS_AVAILABLE = "isAvailable";
    private static final String RECIPES_NUMBER_OF_STEPS = "numberOfSteps";
    private static final String RECIPES_TIME_FOR_COOKING = "timeForCooking";
    private static final String RECIPES_DESCRIPTION = "description";
    private static final String RECIPES_NUMBER_OF_PERSONS = "numberOfPersons";
    private static final String RECIPES_NUMBER_OF_EVERY_ING = "numberOfEveryIng";
    private static final String RECIPES_NUMBER_OF_INGREDIENTS = "numberOfIngredients";

    private static final String RECIPES_CATEGORIES_TABLE_NAME = "CategoriesOfRecipes";
    private static final String RECIPES_CATEGORIES_ID = "_id";
    private static final String RECIPES_CATEGORIES_NAME = "name";
    private static final String RECIPES_CATEGORIES_RECIPES = "recipes";

    private static final String CATEGORIES_TABLE_NAME = "Categories";
    private static final String CATEGORIES_ID = "_id";
    private static final String CATEGORY_NAME= "name";
    private static final String CATEGORY_INGREDIENTS = "ingredients";
    private static final String CATEGORY_NUMBER_OF_INGREDIENTS = "numberOfIngredients";
    private static final String CATEGORY_EXAMPLE = "example";


    public static void initDatabase(Context context) {
        if (database == null) {
            database = new Database(context);
        }
    }

    public static Database getDatabase() {
        return database;
    }

    private Database(Context context) {
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(context, DATABASE_NAME);
        generalDb = dbOpenHelper.openDataBase();
    }

    public SQLiteDatabase getGeneralDb() {
        return generalDb;
    }

    public static String getRecipesId() {
        return RECIPES_ID;
    }
    public static String getRecipesName() {
        return RECIPES_NAME;
    }
    public static String getRecipesIngredients() {
        return RECIPES_INGREDIENTS;
    }
    public static String getRecipesHowToCook() {
        return RECIPES_HOW_TO_COOK;
    }
    public static String getRecipesIsAvailable() {
        return RECIPES_IS_AVAILABLE;
    }
    public static String getRecipesNumberOfSteps() {
        return RECIPES_NUMBER_OF_STEPS;
    }
    public static String getRecipesTableName() { return RECIPES_TABLE_NAME; }
    public static String getRecipesTimeForCooking() { return RECIPES_TIME_FOR_COOKING; }
    public static String getRecipesDescription() { return RECIPES_DESCRIPTION; }
    public static String getRecipesNumberOfPersons() { return RECIPES_NUMBER_OF_PERSONS; }
    public static String getRecipesNumberOfEveryIng() { return  RECIPES_NUMBER_OF_EVERY_ING; }
    public static String getRecipesNumberOfIngredients() { return RECIPES_NUMBER_OF_INGREDIENTS; }

    public static String getIngredientsTableName() {return INGREDIENTS_TABLE_NAME; };
    public static String getIngredientId() {
        return INGREDIENT_ID;
    }
    public static String getIngredientName() {
        return INGREDIENT_NAME;
    }
    public static String getIngredientIsChecked() {
        return IS_CHECKED;
    }
    public static String getIngredientsForWhatRecipes() { return  INGREDIENTS_FOR_WHAT_RECIPES; }

    public static String getCategoriesId() {
        return CATEGORIES_ID;
    }
    public static String getCategoryName() {
        return CATEGORY_NAME;
    }
    public static String getCategoryIngredients() { return CATEGORY_INGREDIENTS; }
    public static String getCategoryNumberOfIngredients() { return CATEGORY_NUMBER_OF_INGREDIENTS;}
    public static String getCategoryExample() { return CATEGORY_EXAMPLE;}
    public static String getCategoriesTableName() { return CATEGORIES_TABLE_NAME; }

    public static String getRecipesCategoriesTableName() { return RECIPES_CATEGORIES_TABLE_NAME; }
    public static String getRecipesCategoriesId() { return RECIPES_ID; }
    public static String getRecipesCategoriesName() { return RECIPES_CATEGORIES_NAME; }
    public static String getRecipesCategoriesRecipes() { return RECIPES_CATEGORIES_RECIPES; }



}
