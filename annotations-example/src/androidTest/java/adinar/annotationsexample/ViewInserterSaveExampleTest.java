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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ViewInserterSaveExampleTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void viewInserterSaveExampleTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("View Inserter examples"),
                        childAtPosition(
                                withId(R.id.example_list),
                                1),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("Save"),
                        childAtPosition(
                                withId(R.id.example_list),
                                1),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        pressBack();

        ViewInteraction editText = onView(
                allOf(withId(R.id.taste),
                        childAtPosition(
                                allOf(withId(R.id.dst),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                3)),
                                0),
                        isDisplayed()));
        editText.check(matches(withText("")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.size),
                        childAtPosition(
                                allOf(withId(R.id.dst),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                3)),
                                1),
                        isDisplayed()));
        editText2.check(matches(withText("")));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.save), withText("Save!")));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.taste), withText("orange"),
                        childAtPosition(
                                allOf(withId(R.id.dst),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                3)),
                                0),
                        isDisplayed()));
        editText4.check(matches(withText("orange")));

        ViewInteraction editText5 = onView(
                allOf(withId(R.id.size), withText("500"),
                        childAtPosition(
                                allOf(withId(R.id.dst),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                3)),
                                1),
                        isDisplayed()));
        editText5.check(matches(withText("500")));

        ViewInteraction textView = onView(
                allOf(withId(R.id.description), withText(">>>>>> Very good juice! <<<<<<"),
                        childAtPosition(
                                allOf(withId(R.id.dst),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                3)),
                                2),
                        isDisplayed()));
        textView.check(matches(withText(">>>>>> Very good juice! <<<<<<")));

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
