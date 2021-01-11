package xyz.cintiawan.footballapp

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.cintiawan.footballapp.R.id.*
import android.widget.EditText
import xyz.cintiawan.footballapp.tests.IdlingResourceProvider
import android.support.test.espresso.Espresso.onData
import org.hamcrest.Matchers.*


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField var activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(IdlingResourceProvider.idler)
    }

    @Test
    fun testAppBehaviour() {
        onView(withId(viewpager)).check(matches(isDisplayed()))
        onView(withId(tabs)).check(matches(isDisplayed()))
        onView(allOf(withId(spinner), isDisplayed())).check(matches(isDisplayed()))
        onView(withId(action_search)).check(matches(isDisplayed()))
        onView(allOf(withId(spinner), isDisplayed())).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Spanish La Liga"))).perform(click())
        onView(allOf(withId(spinner), isDisplayed())).check(matches(withSpinnerText(containsString("Spanish La Liga"))))
        onView(withId(viewpager)).perform(swipeLeft())
        onView(withText("NEXT")).perform(click())
        onView(allOf(withId(list_match), isDisplayed())).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        onView(allOf(withId(list_match), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))

        onView(withId(add_to_favorite)).check(matches(isDisplayed()))
        onView(withId(add_to_favorite)).perform(click())
        onView(withText("Added to favorite")).check(matches(isDisplayed()))

        pressBack()

        onView(withId(action_search)).perform(click())

        onView(withId(search_view)).check(matches(isDisplayed()))
        onView(withId(search_view)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("barcelona"), closeSoftKeyboard())
        onView(withId(list_match)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(add_to_favorite)).perform(click())
        onView(withText("Added to favorite")).check(matches(isDisplayed()))

        pressBack()
        pressBack()
        pressBack()

        onView(withId(teams)).perform(click())

        onView(allOf(withId(spinner), isDisplayed())).check(matches(isDisplayed()))
        onView(withId(action_search)).check(matches(isDisplayed()))
        onView(allOf(withId(spinner), isDisplayed())).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Spanish La Liga"))).perform(click())
        onView(allOf(withId(spinner), isDisplayed())).check(matches(withSpinnerText(containsString("Spanish La Liga"))))
        onView(allOf(withId(list_team), isDisplayed())).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(13))
        onView(allOf(withId(list_team), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(13, click()))

        onView(withId(viewpager)).check(matches(isDisplayed()))
        onView(withId(tabs)).check(matches(isDisplayed()))
        onView((withText("PLAYER"))).perform(click())
        onView(allOf(withId(list_player), isDisplayed())).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        onView(allOf(withId(list_player), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))

        onView(withId(player_banner)).check(matches(isDisplayed()))
        onView(withId(player_description)).check(matches(isDisplayed()))

        pressBack()

        onView(withId(add_to_favorite)).perform(click())
        onView(withText("Added to favorite")).check(matches(isDisplayed()))

        pressBack()

        onView(withId(action_search)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("barcelona"), closeSoftKeyboard())
        onView(allOf(withId(list_team), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(add_to_favorite)).perform(click())
        onView(withText("Added to favorite")).check(matches(isDisplayed()))

        pressBack()
        closeSoftKeyboard()

        onView(withId(favorites)).perform(click())

        onView(withId(viewpager)).check(matches(isDisplayed()))
        onView(withId(tabs)).check(matches(isDisplayed()))
        onView((withText("TEAMS"))).perform(click())
        onView(allOf(withId(list_team), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(add_to_favorite)).perform(click())
        onView(withText("Removed from favorite")).check(matches(isDisplayed()))

        pressBack()

        onView((withText("MATCHES"))).perform(click())
        onView(allOf(withId(list_match), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(add_to_favorite)).perform(click())
        onView(withText("Removed from favorite")).check(matches(isDisplayed()))
    }
}