package com.himebaugh.bakingapp.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.himebaugh.bakingapp.model.Recipe;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtil {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static boolean isWifiConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Builds the URL used to query Videos Endpoint.
     *
     * @param stringUrl String to convert to URL.
     * @return The URL to return.
     */
    public static URL buildUrl(Context context, String stringUrl) {

        Uri builtUri = Uri.parse(stringUrl).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     */
    public static String getJsonFromHttpUrl(URL url) {

        String jsonString = null;

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                jsonString = scanner.next();
            }

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public static ArrayList<Recipe> getRecipeList(URL url) throws IOException {

        ArrayList<Recipe> returnRecipeList;

        // Can use either one... getJsonFromHttpUrl() -OR- getJsonFromOkHttpClient()
        String recipeJsonResults = getJsonFromHttpUrl(url);
        // String popularRecipesJsonResults = getJsonFromOkHttpClient(queryUrl);  // This Works Fine

        if (recipeJsonResults == null) {

            // ***********************************************************
            // INTERNET IS DOWN, so get RecipeList from database
            // ***********************************************************
            // returnRecipeList = getRecipeListFromCursor(context, filter);
            returnRecipeList = null;

        } else {

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Recipe>>() {
            }.getType();
            returnRecipeList = gson.fromJson(recipeJsonResults, listType);

            // ***********************************************************
            // The latest RecipeList results are saved to the database
            // Also, RecipeUtils.FILTER_POPULAR is saved to COLUMN_CATEGORY
            // ***********************************************************
            // saveRecipeListToCursor(context, returnRecipeList, RecipeUtils.FILTER_POPULAR);
        }

        return returnRecipeList;

    }

}