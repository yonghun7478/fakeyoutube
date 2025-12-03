package com.example.fakeyoutube.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class ScreenTest {

    @Test
    fun screen_route_definitions_match_spec() {
        // Verify Home route
        assertEquals("home", Screen.Home.route)
        assertEquals("Home", Screen.Home.title)

        // Verify Shorts route
        assertEquals("shorts", Screen.Shorts.route)
        assertEquals("Shorts", Screen.Shorts.title)

        // Verify Upload route
        assertEquals("upload", Screen.Upload.route)
        assertEquals("Upload", Screen.Upload.title)

        // Verify Subscription route
        assertEquals("subscription", Screen.Subscription.route)
        assertEquals("Subscription", Screen.Subscription.title)

        // Verify Library route
        assertEquals("library", Screen.Library.route)
        assertEquals("You", Screen.Library.title)
    }
}
