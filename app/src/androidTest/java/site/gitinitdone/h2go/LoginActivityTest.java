package site.gitinitdone.h2go;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import site.gitinitdone.h2go.controller.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;


/**
 * Created by Shourya on 4/2/2017.
 * JUnit test for LoginActivity.
 */

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Test
    public void validFieldsEntered() {
        // Type text and then press the button.
        onView(withId(R.id.username)).perform(typeText("test2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("pass"), closeSoftKeyboard());
        // Check that the text was changed.
        onView(withId(R.id.username)).check(matches(withText("test2")));
        onView(withId(R.id.password)).check(matches(withText("pass")));
        onView(withId(R.id.email_sign_in_button)).perform(click());

        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void invalidFieldsEntered() {
        // Type text and then press the button.
        onView(withId(R.id.username)).perform(typeText("invalid"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("invalid"), closeSoftKeyboard());
        // Check that the text was changed.
        onView(withId(R.id.username)).check(matches(withText("invalid")));
        onView(withId(R.id.password)).check(matches(withText("invalid")));
        onView(withId(R.id.email_sign_in_button)).perform(click());

        onView(withId(R.id.password)).check(matches(hasErrorText("Authentication failed")));
    }

    @Test
    public void noPasswordEntered() {
        // Type text and then press the button.
        onView(withId(R.id.username)).perform(typeText("test2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(""), closeSoftKeyboard());
        // Check that the text was changed.
        onView(withId(R.id.username)).check(matches(withText("test2")));
        onView(withId(R.id.password)).check(matches(withText("")));
        onView(withId(R.id.email_sign_in_button)).perform(click());

        onView(withId(R.id.password)).check(matches(hasErrorText("This password is too short")));
    }

    @Test
    public void noUsernameEntered() {
        // Type text and then press the button.
        onView(withId(R.id.username)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password"), closeSoftKeyboard());
        // Check that the text was changed.
        onView(withId(R.id.username)).check(matches(withText("")));
        onView(withId(R.id.password)).check(matches(withText("password")));
        onView(withId(R.id.email_sign_in_button)).perform(click());

        onView(withId(R.id.username)).check(matches(hasErrorText("This field is required")));
    }

    @Test
    public void incorrectPasswordEntered() {
        // Type text and then press the button.
        onView(withId(R.id.username)).perform(typeText("test2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("pa"), closeSoftKeyboard());
        // Check that the text was changed.
        onView(withId(R.id.username)).check(matches(withText("test2")));
        onView(withId(R.id.password)).check(matches(withText("pa")));
        onView(withId(R.id.email_sign_in_button)).perform(click());

        onView(withId(R.id.password)).check(matches(hasErrorText("Authentication failed")));
    }

    @Test
    public void incorrectUsernameEntered() {
        // Type text and then press the button.
        onView(withId(R.id.username)).perform(typeText("invalid"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password"), closeSoftKeyboard());
        // Check that the text was changed.
        onView(withId(R.id.username)).check(matches(withText("invalid")));
        onView(withId(R.id.password)).check(matches(withText("password")));
        onView(withId(R.id.email_sign_in_button)).perform(click());

        onView(withId(R.id.password)).check(matches(hasErrorText("Authentication failed")));
    }
}

