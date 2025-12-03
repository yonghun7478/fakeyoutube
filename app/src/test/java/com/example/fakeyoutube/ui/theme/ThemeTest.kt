package com.example.fakeyoutube.ui.theme

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Local Unit Test for Theme Logic.
 * Verifies that the core brand colors are defined correctly according to the spec.
 */
class ThemeTest {

    @Test
    fun youtubeRedColor_isCorrect() {
        // Spec 2: YouTube Brand Color Red #FF0000
        val expectedArgb = 0xFFFF0000
        
        // Note: Color.value is a ULong in Compose
        assertEquals(expectedArgb.toULong(), YouTubeRed.value)
    }
}