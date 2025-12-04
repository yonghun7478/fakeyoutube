package com.example.fakeyoutube

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.fakeyoutube.ui.screens.MainScreen
import com.example.fakeyoutube.ui.theme.FakeYouTubeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            FakeYouTubeTheme {
                MainScreen()
            }
        }
    }
}