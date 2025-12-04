package com.example.fakeyoutube.ui.navigation

import org.junit.Test
import org.junit.Assert.assertEquals

class BottomBarItemTest {
    @Test
    fun `verify bottom navigation items count and routes`() {
        val items = listOf(
            Screen.Home,
            Screen.Shorts,
            Screen.Upload,
            Screen.Subscription,
            Screen.Library
        )
        assertEquals(5, items.size)
        assertEquals("home", items[0].route)
        assertEquals("shorts", items[1].route)
        assertEquals("upload", items[2].route)
        assertEquals("subscription", items[3].route)
        assertEquals("library", items[4].route)
    }
}