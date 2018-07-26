package com.himebaugh.bakingapp.widget;

import com.himebaugh.bakingapp.model.Ingredients;

import java.util.List;

public class StackWidgetItem {
    private int recipeID;
    private String recipeName;
    private List<Ingredients> ingredientList;

    public StackWidgetItem(int recipeID, String recipeName, List<Ingredients> ingredientList) {
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

    public List<Ingredients> getIngredientList() {
        return ingredientList;
    }

}
