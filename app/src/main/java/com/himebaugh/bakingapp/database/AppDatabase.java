package com.himebaugh.bakingapp.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.himebaugh.bakingapp.utils.NetworkUtil;

import java.net.URL;

@Database(entities = {RecipeEntry.class, IngredientEntry.class, StepEntry.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    public static final String DATABASE_NAME = "baked16";    // .db ??
    private static AppDatabase sInstance;                   // volatile ??

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)

                        // To Populate database
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);

                                URL url = NetworkUtil.buildUrl(context.getApplicationContext(), "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");

                                // ALL 3 Options below will work!

                                // #1
                                DatabaseInitializer.populateAsync(url, sInstance);

                                // #2
                                //DatabaseInitializer.populateSync(url, sInstance);

                                // #3
                                // new InitDatabaseTask(sInstance).execute(context.getApplicationContext());
                            }
                        })

                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract RecipeDao getRecipeDao();

    public abstract IngredientDao getIngredientDao();

    public abstract StepDao getStepDao();

}
