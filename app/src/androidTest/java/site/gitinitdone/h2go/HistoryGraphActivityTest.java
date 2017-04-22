package site.gitinitdone.h2go;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import site.gitinitdone.h2go.controller.HistoryGraphActivity;
import site.gitinitdone.h2go.controller.LoginActivity;

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

/**
 * Created by Shourya on 4/22/2017.
 */

public class HistoryGraphActivityTest {
    public ActivityTestRule<HistoryGraphActivity> mActivityRule = new ActivityTestRule<>(HistoryGraphActivity.class);
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
        onView(withText("Purity Graph")).perform(click());

    }
    @Test
    public void validFieldsEntered() {
        // Type text and then press the button.
        onView(withId(R.id.locationLat)).perform(replaceText("-90"), closeSoftKeyboard());
        onView(withId(R.id.locationLong)).perform(replaceText("-180"), closeSoftKeyboard());
        onView(withText("Contaminant")).perform(click());
        onView(withId(R.id.historyGraphViewYearEntered)).perform(replaceText("2017"), closeSoftKeyboard());
        // Check that the text was changed.
        onView(withId(R.id.locationLat)).check(matches(withText("-90")));
        onView(withId(R.id.locationLong)).check(matches(withText("-180")));
        onView(withId(R.id.historyGraphViewYearEntered)).check(matches(withText("2017")));
        onView(withId(R.id.viewGraphButton)).perform(click());
        onView(withText("Purity Graph")).
                inRoot(withDecorView(not(is(mActivityRule2.getActivity().getWindow()
                        .getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void invalidLatEntered() {
        // Type text and then press the button.
        onView(withId(R.id.locationLat)).perform(replaceText("-100"), closeSoftKeyboard());
        onView(withId(R.id.locationLong)).perform(replaceText("-180"), closeSoftKeyboard());
        onView(withText("Contaminant")).perform(click());
        onView(withId(R.id.historyGraphViewYearEntered)).perform(replaceText("2017"), closeSoftKeyboard());
        // Check that the text was changed.
        onView(withId(R.id.locationLat)).check(matches(withText("-100")));
        onView(withId(R.id.locationLong)).check(matches(withText("-180")));
        onView(withId(R.id.historyGraphViewYearEntered)).check(matches(withText("2017")));
        onView(withId(R.id.viewGraphButton)).perform(click());
        onView(withId(R.id.locationLat)).check(matches(hasErrorText("Latitude must be in between -90 and 90 degrees")));
    }

    @Test
    public void invalidLongEntered() {
        // Type text and then press the button.
        onView(withId(R.id.locationLat)).perform(replaceText("-90"), closeSoftKeyboard());
        onView(withId(R.id.locationLong)).perform(replaceText("-200"), closeSoftKeyboard());
        onView(withText("Contaminant")).perform(click());
        onView(withId(R.id.historyGraphViewYearEntered)).perform(replaceText("2017"), closeSoftKeyboard());
        // Check that the text was changed.
        onView(withId(R.id.locationLat)).check(matches(withText("-90")));
        onView(withId(R.id.locationLong)).check(matches(withText("-200")));
        onView(withId(R.id.historyGraphViewYearEntered)).check(matches(withText("2017")));
        onView(withId(R.id.viewGraphButton)).perform(click());
        onView(withId(R.id.locationLong)).check(matches(hasErrorText("Longitude must be in between -180 and 180 degrees")));
    }

    @Test
    public void futureYearEntered() {
        // Type text and then press the button.
        onView(withId(R.id.locationLat)).perform(replaceText("-90"), closeSoftKeyboard());
        onView(withId(R.id.locationLong)).perform(replaceText("-180"), closeSoftKeyboard());
        onView(withText("Contaminant")).perform(click());
        onView(withId(R.id.historyGraphViewYearEntered)).perform(replaceText("2018"), closeSoftKeyboard());
        // Check that the text was changed.
        onView(withId(R.id.locationLat)).check(matches(withText("-90")));
        onView(withId(R.id.locationLong)).check(matches(withText("-180")));
        onView(withId(R.id.historyGraphViewYearEntered)).check(matches(withText("2018")));
        onView(withId(R.id.viewGraphButton)).perform(click());
        onView(withId(R.id.historyGraphViewYearEntered)).check(matches(hasErrorText("Year cannot be greater than 2017")));
    }

    @Test
    public void pastYearEntered() {
        // Type text and then press the button.
        onView(withId(R.id.locationLat)).perform(replaceText("-90"), closeSoftKeyboard());
        onView(withId(R.id.locationLong)).perform(replaceText("-180"), closeSoftKeyboard());
        onView(withText("Contaminant")).perform(click());
        onView(withId(R.id.historyGraphViewYearEntered)).perform(replaceText("1999"), closeSoftKeyboard());
        // Check that the text was changed.
        onView(withId(R.id.locationLat)).check(matches(withText("-90")));
        onView(withId(R.id.locationLong)).check(matches(withText("-180")));
        onView(withId(R.id.historyGraphViewYearEntered)).check(matches(withText("1999")));
        onView(withId(R.id.viewGraphButton)).perform(click());
        onView(withId(R.id.historyGraphViewYearEntered)).check(matches(hasErrorText("Year cannot be less than 2000")));
    }

    @Test
    public void noYearEntered() {
        // Type text and then press the button.
        onView(withId(R.id.locationLat)).perform(replaceText("-90"), closeSoftKeyboard());
        onView(withId(R.id.locationLong)).perform(replaceText("-180"), closeSoftKeyboard());
        onView(withText("Contaminant")).perform(click());
        onView(withId(R.id.historyGraphViewYearEntered)).perform(replaceText(""), closeSoftKeyboard());
        // Check that the text was changed.
        onView(withId(R.id.locationLat)).check(matches(withText("-90")));
        onView(withId(R.id.locationLong)).check(matches(withText("-180")));
        onView(withId(R.id.historyGraphViewYearEntered)).check(matches(withText("")));
        onView(withId(R.id.viewGraphButton)).perform(click());
        onView(withId(R.id.historyGraphViewYearEntered)).check(matches(hasErrorText("Year can't be empty")));
    }

    // No tests for blank latitude, longitude fields because blank automatically turns to zero
}


