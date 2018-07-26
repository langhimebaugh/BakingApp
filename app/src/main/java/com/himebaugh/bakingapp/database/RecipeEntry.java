package com.himebaugh.bakingapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = AppDatabaseContract.RecipeEntry.TABLE_NAME)
public class RecipeEntry {

    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String name  ;
    private int servings;
    private String image;

    @Ignore
    public RecipeEntry(String name, int servings, String image) {
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public RecipeEntry(int id, String name, int servings, String image) {
        this._id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
