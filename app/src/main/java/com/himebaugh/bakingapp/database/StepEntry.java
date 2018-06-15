package com.himebaugh.bakingapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "steps",
        foreignKeys = @ForeignKey(entity = RecipeEntry.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = CASCADE,
                onUpdate = CASCADE))

public class StepEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int stepNumber;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;
    private int recipeId;

    @Ignore
    public StepEntry(int stepNumber, String shortDescription, String description, String videoURL, String thumbnailURL, int recipeId) {
        this.stepNumber = stepNumber;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.recipeId = recipeId;
    }

    public StepEntry(int id, int stepNumber, String shortDescription, String description, String videoURL, String thumbnailURL, int recipeId) {
        this.id = id;
        this.stepNumber = stepNumber;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

}
