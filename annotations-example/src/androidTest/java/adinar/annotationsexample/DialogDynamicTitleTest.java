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
public class DialogDynamicTitleTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void dialogDynamicTitleTest() {
        clickChooseListElementViewWith("Dialog examples", 0);
        clickChooseListElementViewWith("Dynamic title", 3);

        // fab
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        // check if proper strings are displayed
        ViewInteraction textView = onView(withText("A title made of two strings."));
        textView.check(matches(withText("A title made of two strings.")));

        ViewInteraction editText = onView(withIndex(withId(R.id.value), 0));
        editText.check(matches(withText("A title")));

        ViewInteraction editText2 = onView(withIndex(withId(R.id.value), 1));
        editText2.check(matches(withText(" made of two strings.")));

        // replace content
        editText.perform(replaceText("A title rr"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(withIndex(withId(R.id.value), 1));
        appCompatEditText4.perform(replaceText(" made of two strings. rr"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Add")));
        appCompatButton.perform(scrollTo(), click());

        // check if elements on list are changed
        ViewInteraction textView2 = onView(withIndex(withId(R.id.field1), 0));
        textView2.check(matches(withText("A title rr")));

        ViewInteraction textView3 = onView(withIndex(withId(R.id.field2), 0));
        textView3.check(matches(withText(" made of two strings. rr")));

        // click on list
        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        withId(R.id.example_list),
                        0),
                        isDisplayed()));
        linearLayout.perform(click());

        // check if title was properly set and values are stored
        ViewInteraction textView4 = onView(withText("A title rr made of two strings. rr"));
        textView4.check(matches(withText("A title rr made of two strings. rr")));

        ViewInteraction editText3 = onView(withText("A title rr"));
        editText3.check(matches(withText("A title rr")));

        ViewInteraction editText4 = onView(withText(" made of two strings. rr"));
        editText4.check(matches(withText(" made of two strings. rr")));

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
