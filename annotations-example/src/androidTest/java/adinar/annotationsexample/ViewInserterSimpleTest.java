package adinar.annotationsexample;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import adinar.annotationsexample.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ViewInserterSimpleTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void viewInserterSimpleTest() {
        ViewInteraction appCompatTextView = onView(
allOf(withId(android.R.id.text1), withText("View Inserter examples"),
childAtPosition(
withId(R.id.example_list),
1),
isDisplayed()));
        appCompatTextView.perform(click());
        
        ViewInteraction appCompatTextView2 = onView(
allOf(withId(android.R.id.text1), withText("Simple"),
childAtPosition(
withId(R.id.example_list),
0),
isDisplayed()));
        appCompatTextView2.perform(click());
        
        ViewInteraction textView = onView(
allOf(withId(R.id.name), withText("John"),
childAtPosition(
allOf(withId(R.id.person_layout),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0)),
0),
isDisplayed()));
        textView.check(matches(withText("John")));
        
        ViewInteraction textView2 = onView(
allOf(withId(R.id.surname), withText("Johnatansky"),
childAtPosition(
allOf(withId(R.id.person_layout),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0)),
1),
isDisplayed()));
        textView2.check(matches(withText("Johnatansky")));
        
        ViewInteraction textView3 = onView(
allOf(withId(R.id.full_name), withText("Full name: John Johnatansky"),
childAtPosition(
allOf(withId(R.id.person_layout),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0)),
2),
isDisplayed()));
        textView3.check(matches(withText("Full name: John Johnatansky")));
        
        ViewInteraction textView4 = onView(
allOf(withId(R.id.age), withText("20"),
childAtPosition(
allOf(withId(R.id.person_layout),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0)),
3),
isDisplayed()));
        textView4.check(matches(withText("20")));
        
        ViewInteraction checkBox = onView(
allOf(withId(R.id.checkBox),
childAtPosition(
allOf(withId(R.id.data_layout),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
2)),
0),
isDisplayed()));
        checkBox.check(matches(isDisplayed()));
        
        ViewInteraction seekBar = onView(
allOf(withId(R.id.seekBar),
childAtPosition(
allOf(withId(R.id.data_layout),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
2)),
1),
isDisplayed()));
        seekBar.check(matches(isDisplayed()));
        
        ViewInteraction editText = onView(
allOf(withId(R.id.editText), withText("Nice hint!"),
childAtPosition(
allOf(withId(R.id.data_layout),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
2)),
2),
isDisplayed()));
        editText.check(matches(withText("Nice hint!")));
        
        ViewInteraction editText2 = onView(
allOf(withId(R.id.editText), withText("Nice hint!"),
childAtPosition(
allOf(withId(R.id.data_layout),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
2)),
2),
isDisplayed()));
        editText2.check(matches(withText("Nice hint!")));
        
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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
