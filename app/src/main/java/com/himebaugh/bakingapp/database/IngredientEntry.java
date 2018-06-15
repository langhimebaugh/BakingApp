package com.himebaugh.bakingapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

// https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
// https://developer.android.com/reference/android/arch/persistence/room/ForeignKey
// A "CASCADE" action propagates the delete or update operation on the parent key to each dependent child key.
// For onDelete() action, this means that each row in the child entity that was associated with the deleted parent row is also deleted.
// For an onUpdate() action, it means that the values stored in each dependent child key are modified to match the new parent key values.

@Entity(tableName = "ingredients",
        foreignKeys = @ForeignKey(entity = RecipeEntry.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = CASCADE,
                onUpdate = CASCADE))

public class IngredientEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private float quantity;
    private String measure;
    private String ingredient;
    private int recipeId;


    @Ignore
    public IngredientEntry(float quantity, String measure, String ingredient, int recipeId) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
    }

    public IngredientEntry(int id, float quantity, String measure, String ingredient, int recipeId) {
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

}
