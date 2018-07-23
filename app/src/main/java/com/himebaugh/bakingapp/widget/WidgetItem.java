package com.himebaugh.bakingapp.widget;

import com.himebaugh.bakingapp.model.Ingredients;

import java.util.ArrayList;

public class WidgetItem {
    private int recipeID;
    private String recipeName;
    private ArrayList<Ingredients> ingredientList;

    public WidgetItem(int recipeID, String recipeName, ArrayList<Ingredients> ingredientList) {
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.ingredientList = ingredientList;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public ArrayList<Ingredients> getIngredientList() {
        return ingredientList;
    }

}
