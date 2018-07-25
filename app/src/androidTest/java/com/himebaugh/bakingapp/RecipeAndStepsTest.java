package com.himebaugh.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.allOf;
/**
 * This test demos a user clicking the decrement button and verifying that it properly decrease
 * the quantity the total cost.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeAndStepsTest {

    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Checks the MainActivity to see if recipes have loaded.
     */
    @Test
    public void areRecipesLoaded() {
        onView(withId(R.id.main_recyclerView)).perform(RecyclerViewActions.scrollToPosition(0));

        onView(withText(startsWith("Nu"))).check(matches(isDisplayed()));

        onView(withId(R.id.main_recyclerView)).perform(RecyclerViewActions.scrollToPosition(1));

        onView(withText("Brownies")).check(matches(isDisplayed()));
    }

    /**
     * Clicks on Yellow Cake (3rd item) and Starting Prep (2nd step item).
     * Should display Step1 of RecipeStepDetailActivity
     */
    @Test
    public void clickRecipeAndSteps() {

        // Click the third Recipe Card
        onView(withId(R.id.main_recyclerView)).perform(RecyclerViewActions.scrollToPosition(3)).perform(click());

                                            // this works too...
                                            // .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        // Check that the RecipeStepListActivity is displayed
        onView(withId(R.id.ingredients_label)).check(matches(withText("Ingredients:")));

        // Check that the RecipeStepListActivity is displaying the correct Recipe
        // by verifying the first ingredient is sifted cake flour
        onView(withId(R.id.ingredients_recyclerView)).perform(RecyclerViewActions.scrollToPosition(0));

        // android.support.test.espresso.AmbiguousViewMatcherException: 'with id: com.himebaugh.bakingapp:id/tv_ingredient' matches multiple views in the hierarchy.
        // onView(withId(R.id.tv_ingredient)).check(matches(withText(startsWith("sifted"))));
        // so do it like this...
        onView(allOf(isCompletelyDisplayed(), withId(R.id.tv_ingredient))).check(matches(withText(startsWith("sifted cake flour"))));

        // Click Step 1
        onView(withId(R.id.steps_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

                // Error performing 'single click' on view 'with id: com.himebaugh.bakingapp:id/steps_recyclerView'.
                // .perform(RecyclerViewActions.scrollToPosition(1)).perform(click());

        // Checks that the RecipeStepDetailActivity opens to Step 1
        onView(allOf(isCompletelyDisplayed(), withId(R.id.tv_recipe_step_detail))).check(matches(withText(startsWith("1. Preheat the oven"))));
    }

}
