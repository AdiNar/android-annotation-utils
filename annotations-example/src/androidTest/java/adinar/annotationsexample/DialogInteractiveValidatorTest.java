package adinar.annotationsexample;


import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static adinar.annotationsexample.EspressoTestCaseUtils.clickChooseListElementViewWith;
import static adinar.annotationsexample.EspressoTestCaseUtils.hasErrorText;
import static adinar.annotationsexample.EspressoTestCaseUtils.withIndex;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DialogInteractiveValidatorTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void dialogInteractiveValidatorTest() {
        clickChooseListElementViewWith("Dialog examples", 0);
        clickChooseListElementViewWith("Validators and primitives", 1);

        // fab
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        // text: s, ok
        ViewInteraction appCompatEditText = onView(
                getFieldMatcher());
        appCompatEditText.perform(replaceText("s"), closeSoftKeyboard());

        // no errors
        assertNoErrors();

        // text: sa, should raise error
        appCompatEditText.perform(replaceText("sa"), closeSoftKeyboard());

        assertErrors();

        // text: w, error should disappear
        appCompatEditText.perform(replaceText("w"), closeSoftKeyboard());

        assertNoErrors();

        // text: a, check if error is displayed again
        appCompatEditText.perform(replaceText("a"), closeSoftKeyboard());

        assertErrors();
    }

    @NonNull
    private Matcher<View> getFieldMatcher() {
        return withIndex(withId(R.id.value), 3);
    }

    private void assertNoErrors() {
        onView(getFieldMatcher())
                .check(matches(not(hasErrorText("abc characters are forbidden!"))));
    }

    private void assertErrors() {
        onView(getFieldMatcher())
                .check(matches(hasErrorText("abc characters are forbidden!")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
