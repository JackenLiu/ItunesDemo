package com.example.itunesdemo

import android.R
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ActivityTest {

    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testLoginSuccess() {
//        onView(withId(R.id.ed)).perform(typeText("user123"))
//        onView(withId(R.id.passwordEditText)).perform(typeText("password123"))
//        onView(withId(R.id.loginButton)).perform(click())
//        onView(withId(R.id.welcomeMessageTextView)).check(matches(withText("Welcome, user123!")))
    }
}


