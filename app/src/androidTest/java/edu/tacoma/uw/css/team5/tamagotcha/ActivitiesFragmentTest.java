package edu.tacoma.uw.css.team5.tamagotcha;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Instrumentation test class which runs through some of the functionality
 * of the app by testing some of the buttons and looking for strings that match
 * in the fragments.
 *
 * @author Ryan Hansen
 */
@RunWith(AndroidJUnit4.class)
public class ActivitiesFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new
            ActivityTestRule<>(MainActivity.class);


    @Test
    public void TestFragments() {
        Espresso.onView(withId(R.id.menu_button)).perform(click());
        Espresso.onView(withId(R.id.confirm_logout_button)).check(matches(isDisplayed()));
        Espresso.pressBack();

        Espresso.onView(withId(R.id.activities_button)).perform(click());
        Espresso.onView(withId(R.id.confirm_button)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.runButton)).perform(click());
        Espresso.onView(withId(R.id.discoButton)).perform(click());
        Espresso.onView(withId(R.id.dumbbellButton)).perform(click());
        Espresso.onView(withId(R.id.popcornButton)).perform(click());
        Espresso.pressBack();

        Espresso.onView(withId(R.id.fridge_button)).perform(click());
        Espresso.pressBack();


    }
}
