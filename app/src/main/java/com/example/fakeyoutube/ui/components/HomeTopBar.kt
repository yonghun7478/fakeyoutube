package com.example.fakeyoutube.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.height
import androidx.compose.ui.layout.ContentScale
import com.example.fakeyoutube.R
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // YouTube Premium Logo
                Icon(
                    painter = painterResource(id = R.drawable.ic_youtube_logo),
                    contentDescription = "YouTube Logo",
                    modifier = Modifier.height(24.dp),
                    tint = Color.Unspecified // Keep original Red color
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "YouTube",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold, // YouTube is Bold
                    letterSpacing = (-1).sp
                )
                Text(
                    text = "Premium",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold, // Premium is also Bold but slightly different in real app, keeping Bold for now but adjusting spacing
                    letterSpacing = (-1).sp,
                    modifier = Modifier.padding(start = 2.dp) // Slight gap
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: Cast Action */ }) {
                Icon(
                    imageVector = Icons.Default.Cast,
                    contentDescription = "Cast"
                )
            }
            IconButton(onClick = { /* TODO: Notifications Action */ }) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = Color(0xFFCC0000),
                            contentColor = Color.White
                        ) {
                            Text("9+")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications"
                    )
                }
            }
            IconButton(onClick = { /* TODO: Search Action */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background, // Use background color (White/Dark)
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        scrollBehavior = scrollBehavior
    )
}