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
public class ObjectDialogExampleSimpleTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void objectDialogExampleSimpleTest() {
        ViewInteraction appCompatTextView = onView(
allOf(withId(android.R.id.text1), withText("Dialog examples"),
childAtPosition(
withId(R.id.example_list),
0),
isDisplayed()));
        appCompatTextView.perform(click());
        
        ViewInteraction appCompatTextView2 = onView(
allOf(withId(android.R.id.text1), withText("Simple"),
childAtPosition(
withId(R.id.example_list),
0),
isDisplayed()));
        appCompatTextView2.perform(click());
        
        ViewInteraction floatingActionButton = onView(
allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());
        
        ViewInteraction appCompatEditText = onView(
allOf(withId(R.id.value), isDisplayed()));
        appCompatEditText.perform(replaceText("test"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText2 = onView(
allOf(withId(R.id.value), isDisplayed()));
        appCompatEditText2.perform(click());
        
        ViewInteraction appCompatEditText3 = onView(
allOf(withId(R.id.value), isDisplayed()));
        appCompatEditText3.perform(replaceText("123"), closeSoftKeyboard());
        
        ViewInteraction appCompatButton = onView(
allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton.perform(scrollTo(), click());
        
        ViewInteraction textView = onView(
allOf(withId(R.id.name), withText("test"),
childAtPosition(
childAtPosition(
withId(R.id.example_list),
0),
0),
isDisplayed()));
        textView.check(matches(withText("test")));
        
        ViewInteraction textView2 = onView(
allOf(withId(R.id.contact_phone), withText("123"),
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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
