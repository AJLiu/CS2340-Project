package site.gitinitdone.h2go;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import site.gitinitdone.h2go.controller.LoginActivity;
import site.gitinitdone.h2go.controller.SubmitSourceReportActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static site.gitinitdone.h2go.R.string.soure_report_success;

/**
 * Created by Shourya on 4/22/2017.
 */

public class SubmitSourceReportActivityTest {
    public ActivityTestRule<SubmitSourceReportActivity> mActivityRule = new ActivityTestRule<>(
        SubmitSourceReportActivity.class);
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule2 = new ActivityTestRule<>(
            LoginActivity.class);


    @Before
    public void setUp() throws Exception {
        // Type text and then press the button.
        onView(withId(R.id.username)).perform(replaceText("test2"));
        onView(withId(R.id.password)).perform(replaceText("pass"));
        // Check that the text was changed.
        onView(withId(R.id.username)).check(matches(withText("test2")));
        onView(withId(R.id.password)).check(matches(withText("pass")));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withText("Submit a Report")).perform(click());
        onView(withText("Submit a Water Source Report")).perform(click());

    }
    @Test
    public void validFieldsEntered() {
        // Type text and then press the button.
        onView(withId(R.id.locationLat)).perform(replaceText("-90"), closeSoftKeyboard());
        onView(withId(R.id.locationLong)).perform(replaceText("-180"), closeSoftKeyboard());
        onView(withId(R.id.waterTypeSpinner)).perform(click());
        onView(withText("Bottled")).perform(click());
        onView(withId(R.id.waterConditionSpinner)).perform(click());
        onView(withText("Potable")).perform(click());
        // Check that the text was changed.
        onView(withId(R.id.locationLat)).check(matches(withText("-90")));
        onView(withId(R.id.locationLong)).check(matches(withText("-180")));
        onView(withId(R.id.submitButtonWaterSourceReport)).perform(click());
        onView(withText(soure_report_success)).
                inRoot(withDecorView(not(is(mActivityRule2.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void invalidLatEntered() {
        // Type text and then press the button.
        onView(withId(R.id.locationLat)).perform(replaceText("-100"), closeSoftKeyboard());
        onView(withId(R.id.locationLong)).perform(replaceText("-180"), closeSoftKeyboard());
        onView(withId(R.id.waterTypeSpinner)).perform(click());
        onView(withText("Well")).perform(click());
        onView(withId(R.id.waterConditionSpinner)).perform(click());
        onView(withText("Potable")).perform(click());
        // Check that the text was changed.
        onView(withId(R.id.locationLat)).check(matches(withText("-100")));
        onView(withId(R.id.locationLong)).check(matches(withText("-180")));
        onView(withId(R.id.submitButtonWaterSourceReport)).perform(click());
        onView(withId(R.id.locationLat)).check(matches(hasErrorText("Latitude must be in between -90 and 90 degrees")));
    }

    @Test
    public void invalidLongEntered() {
        // Type text and then press the button.
        onView(withId(R.id.locationLat)).perform(replaceText("-90"), closeSoftKeyboard());
        onView(withId(R.id.locationLong)).perform(replaceText("-200"), closeSoftKeyboard());
        onView(withId(R.id.waterTypeSpinner)).perform(click());
        onView(withText("Well")).perform(click());
        onView(withId(R.id.waterConditionSpinner)).perform(click());
        onView(withText("Potable")).perform(click());
        // Check that the text was changed.
        onView(withId(R.id.locationLat)).check(matches(withText("-90")));
        onView(withId(R.id.locationLong)).check(matches(withText("-200")));
        onView(withId(R.id.submitButtonWaterSourceReport)).perform(click());
        onView(withId(R.id.locationLong)).check(matches(hasErrorText("Longitude must be in between -180 and 180 degrees")));
    }

    // No tests for blank latitude, longitude fields because blank automatically turns to zero
}

