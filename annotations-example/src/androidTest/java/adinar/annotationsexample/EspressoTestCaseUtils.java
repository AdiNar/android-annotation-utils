package adinar.annotationsexample;


import android.annotation.SuppressLint;
import android.support.test.espresso.ViewInteraction;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class EspressoTestCaseUtils {
    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex;
            int viewObjHash;

            @SuppressLint("DefaultLocale") @Override
            public void describeTo(Description description) {
                description.appendText(String.format("with index: %d ", index));
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (matcher.matches(view) && currentIndex++ == index) {
                    viewObjHash = view.hashCode();
                }
                return view.hashCode() == viewObjHash;
            }
        };
    }

    public static void clickChooseListElementViewWith(String text) {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText(text),
                        childAtPosition(
                                withId(R.id.example_list),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());
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
