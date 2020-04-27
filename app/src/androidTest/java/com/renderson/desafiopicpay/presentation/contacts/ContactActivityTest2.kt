package com.renderson.desafiopicpay.presentation.contacts


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.renderson.desafiopicpay.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ContactActivityTest2 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ContactActivity::class.java)

    @Test
    fun contactActivityTest2() {
        val searchAutoComplete = onView(
            allOf(
                withId(R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(replaceText("e"), closeSoftKeyboard())

        val appCompatImageView = onView(
            allOf(
                withId(R.id.search_close_btn), withContentDescription("Limpar consulta"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val recyclerView = onView(
            allOf(
                withId(R.id.recycler),
                childAtPosition(
                    withId(R.id.constraintLayout),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val appCompatEditText = onView(
            allOf(
                withId(R.id.transaction_value), withText("0,00"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            2
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("0,009"))

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.transaction_value), withText("0,009"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            2
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        //appCompatEditText2.perform(closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.transaction_btn_payment), withText("Pagar"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                        1
                    ),
                    1
                )
            )
        )
        appCompatButton.perform(scrollTo(), click())

        val relativeLayout = onView(
            allOf(
                withId(R.id.actionArrow),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    0
                )
            )
        )
        relativeLayout.perform(scrollTo(), click())

        val searchAutoComplete2 = onView(
            allOf(
                withId(R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete2.perform(replaceText("paulo"), closeSoftKeyboard())

        val recyclerView2 = onView(
            allOf(
                withId(R.id.recycler),
                childAtPosition(
                    withId(R.id.constraintLayout),
                    1
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val relativeLayout2 = onView(
            allOf(
                withId(R.id.actionArrow),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    0
                )
            )
        )
        relativeLayout2.perform(scrollTo(), click())

        val relativeLayout3 = onView(
            allOf(
                withId(R.id.actionArrow),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    0
                )
            )
        )
        relativeLayout3.perform(scrollTo(), click())

        val appCompatImageView2 = onView(
            allOf(
                withId(R.id.search_close_btn), withContentDescription("Limpar consulta"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
