package com.example.fakeyoutube.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class ScreenTest {
    @Test
    fun verifyScreenRoutes() {
        assertEquals("home", Screen.Home.route)
        assertEquals("shorts", Screen.Shorts.route)
        assertEquals("upload", Screen.Upload.route)
        assertEquals("subscription", Screen.Subscription.route)
        assertEquals("library", Screen.Library.route)
    }

    @Test
    fun verifyScreenTitles() {
        assertEquals("Home", Screen.Home.title)
        assertEquals("Shorts", Screen.Shorts.title)
        assertEquals("Upload", Screen.Upload.title)
        assertEquals("Subscription", Screen.Subscription.title)
        assertEquals("You", Screen.Library.title)
    }
}