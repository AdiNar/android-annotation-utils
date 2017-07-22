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
public class DialogDynamicTitleTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void dialogDynamicTitleTest() {
        ViewInteraction appCompatTextView = onView(
allOf(withId(android.R.id.text1), withText("Dialog examples"),
childAtPosition(
withId(R.id.example_list),
0),
isDisplayed()));
        appCompatTextView.perform(click());
        
        ViewInteraction appCompatTextView2 = onView(
allOf(withId(android.R.id.text1), withText("Dynamic title"),
childAtPosition(
withId(R.id.example_list),
3),
isDisplayed()));
        appCompatTextView2.perform(click());
        
        ViewInteraction floatingActionButton = onView(
allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());
        
        ViewInteraction textView = onView(
allOf(IsInstanceOf.<View>instanceOf(android.widget.TextView.class), withText("A title made of two strings."),
childAtPosition(
allOf(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0)),
0),
isDisplayed()));
        textView.check(matches(withText("A title made of two strings.")));
        
        ViewInteraction editText = onView(
allOf(withId(R.id.value), withText("A title"),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0),
0),
isDisplayed()));
        editText.check(matches(withText("A title")));
        
        ViewInteraction editText2 = onView(
allOf(withId(R.id.value), withText(" made of two strings."),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
1),
0),
isDisplayed()));
        editText2.check(matches(withText(" made of two strings.")));
        
        ViewInteraction appCompatButton = onView(
allOf(withId(android.R.id.button1), withText("Add")));
        appCompatButton.perform(scrollTo(), click());
        
        ViewInteraction linearLayout = onView(
allOf(childAtPosition(
withId(R.id.example_list),
0),
isDisplayed()));
        linearLayout.perform(click());
        
        ViewInteraction appCompatEditText = onView(
allOf(withId(R.id.value), withText("A title"), isDisplayed()));
        appCompatEditText.perform(click());
        
        ViewInteraction appCompatEditText2 = onView(
allOf(withId(R.id.value), withText("A title"), isDisplayed()));
        appCompatEditText2.perform(replaceText("A title blablab"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText3 = onView(
allOf(withId(R.id.value), withText(" made of two strings."), isDisplayed()));
        appCompatEditText3.perform(replaceText(" made of t strings."), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText4 = onView(
allOf(withId(R.id.value), withText(" made of t strings."), isDisplayed()));
        appCompatEditText4.perform(click());
        
        ViewInteraction appCompatEditText5 = onView(
allOf(withId(R.id.value), withText(" made of t strings."), isDisplayed()));
        appCompatEditText5.perform(replaceText(" made of."), closeSoftKeyboard());
        
        ViewInteraction appCompatButton2 = onView(
allOf(withId(android.R.id.button1), withText("Add")));
        appCompatButton2.perform(scrollTo(), click());
        
        ViewInteraction linearLayout2 = onView(
allOf(childAtPosition(
withId(R.id.example_list),
0),
isDisplayed()));
        linearLayout2.perform(click());
        
        ViewInteraction textView2 = onView(
allOf(IsInstanceOf.<View>instanceOf(android.widget.TextView.class), withText("A title blablab made of."),
childAtPosition(
allOf(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0)),
0),
isDisplayed()));
        textView2.check(matches(withText("A title blablab made of.")));
        
        ViewInteraction editText3 = onView(
allOf(withId(R.id.value), withText("A title blablab"),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0),
0),
isDisplayed()));
        editText3.check(matches(withText("A title blablab")));
        
        ViewInteraction editText4 = onView(
allOf(withId(R.id.value), withText(" made of."),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
1),
0),
isDisplayed()));
        editText4.check(matches(withText(" made of.")));
        
        ViewInteraction appCompatButton3 = onView(
allOf(withId(android.R.id.button1), withText("Add")));
        appCompatButton3.perform(scrollTo(), click());
        
        ViewInteraction textView3 = onView(
allOf(withId(R.id.name),
childAtPosition(
childAtPosition(
withId(R.id.example_list),
0),
0),
isDisplayed()));
        textView3.check(matches(withText("")));
        
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
