package adinar.annotationsexample;


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
import static adinar.annotationsexample.EspressoTestCaseUtils.withIndex;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ObjectDialogExampleSimpleTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void objectDialogExampleSimpleTest() throws InterruptedException {
        clickChooseListElementViewWith("Dialog examples", 0);
        clickChooseListElementViewWith("Simple", 0);

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        onView(withIndex(withId(R.id.value), 0))
                .perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                withIndex(withId(R.id.value), 1));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                withIndex(withId(R.id.value), 1));
        appCompatEditText3.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton.perform(scrollTo(), click());

        Thread.sleep(500);

        ViewInteraction textView = onView(
                allOf(withId(R.id.field1), withText("test"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.example_list),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("test")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.field2), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.example_list),
                                        0),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("123")));
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
