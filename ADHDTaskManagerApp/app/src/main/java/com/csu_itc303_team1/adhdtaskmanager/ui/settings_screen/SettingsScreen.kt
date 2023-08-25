package com.csu_itc303_team1.adhdtaskmanager.ui.settings_screen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import coil.compose.rememberImagePainter
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults


// For now, this is just a placeholder code for a functional screen

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    currentUser: AuthUiClient,
    context: Context,
    scope: CoroutineScope
) {
    var showImagePicker by remember { mutableStateOf(false) }
    val isDarkTheme by settingsViewModel.isDarkTheme.observeAsState(initial = false)
    val imageList by settingsViewModel.profileImages.observeAsState(initial = listOf())
    val currentProfileImage by settingsViewModel.currentUserProfileImage.observeAsState(initial = null)
    var workTimeInput by remember { mutableStateOf(settingsViewModel.getWorkTimerValue().toString()) }
    var breakTimeInput by remember { mutableStateOf(settingsViewModel.getBreakTimerValue().toString()) }
    var selectedCountry by remember { mutableStateOf("") }
    var showCountryDropdown by remember { mutableStateOf(false) }

    // Fetch the profile pictures once when the screen is created
    LaunchedEffect(key1 = Unit, key2 = currentUser.getSignedInUser()?.userId) {
        // Fetch all profile pictures.
        settingsViewModel.fetchProfilePictures()

        // Fetch current user's profile picture.
        val userId = currentUser.getSignedInUser()?.userId
        if (userId != null) {
            settingsViewModel.fetchCurrentUserProfileImage(userId)
        }
    }

    val countryList = listOf(
        "United States", "Canada", "United Kingdom", "Australia", "Germany",
        "India", "France", "Italy", "Spain", "Brazil", "Russia" //... add more countries
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Original Content
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            SwitchRow(
                title = "Dark Mode",
                titleColor = Color(4, 64, 165), // Made adjustments here
                titleFontWeight = FontWeight.Bold, // And here
                titleFontSize = 20.sp, // Add this line
                desc = "Changes the Application's Theme from Light to Dark.",
                descColor = Color(4, 94, 165, 255),
                checked = isDarkTheme,
                onCheckedChange = { settingsViewModel.toggleTheme(it) },
                enabled = true
            )

            Divider(color = Color.Black, thickness = 1.dp)

            Spacer(modifier = Modifier.height(10.dp))

            // Heading
            Text(
                text = "Update your user profile",
                color = Color(4, 64, 165),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(5.dp))

            var username by remember { mutableStateOf("") }
            TextField(value = username, onValueChange = {
                username = it
            })

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                scope.launch {
                    currentUser.updateUsername(username)
                    Toast.makeText(context, "Username Updated", Toast.LENGTH_LONG).show()
                    username = ""
                }
            }) {
                Text(text = "Update Username")
            }


            Spacer(modifier = Modifier.height(10.dp))

// Title for Country Selection
            Text(text = " ", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(5.dp))

// Dropdown for Country Selection
            Box(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp * 0.72f) // Set width to 1/3 of screen width
                    .border(width = 1.dp, color = Color.Gray) // To give a boundary similar to TextField
                    .clickable { showCountryDropdown = !showCountryDropdown }
                    .padding(12.dp)
            ) {
                Text(text = if (selectedCountry.isNotEmpty()) selectedCountry else "Select a Country")

                DropdownMenu(expanded = showCountryDropdown, onDismissRequest = { showCountryDropdown = false }) {
                    countryList.forEach { country ->
                        DropdownMenuItem(onClick = {
                            selectedCountry = country
                            showCountryDropdown = false
                        }) {
                            Text(text = country)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                scope.launch {
                    currentUser.getSignedInUser()?.userId?.let { userId ->
                        settingsViewModel.updateUserCountry(userId, selectedCountry)
                        Toast.makeText(context, "Country Updated", Toast.LENGTH_LONG).show()
                        selectedCountry = ""
                    }
                }
            }) {
                Text(text = "Update Country")
            }






            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { showImagePicker = true }) {
                    Text(text = "Update Profile Mascot")
                }

                Spacer(modifier = Modifier.width(10.dp))

                currentProfileImage?.let { imageUrl ->
                    Image(
                        painter = rememberImagePainter(data = imageUrl),
                        contentDescription = "Current Profile Image",
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            Divider(color = Color.Black, thickness = 1.dp)

            Spacer(modifier = Modifier.height(15.dp))

// This should be outside the AnimatedVisibility
// Heading for Pomodoro Timer Adjustment
            Text(
                text = "Pomodoro Timer Adjustment",
                color = Color(4, 64, 165),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Input in minutes how long you would like your timer to run for.",
                color = Color(4, 94, 165, 255),
            )

            Spacer(modifier = Modifier.height(5.dp))

            TextField(
                value = workTimeInput,
                onValueChange = {
                    workTimeInput = it
                },
                label = { Text("Work Timer (minutes)") }
            )

            Spacer(modifier = Modifier.height(5.dp))

            TextField(
                value = breakTimeInput,
                onValueChange = {
                    breakTimeInput = it
                },
                label = { Text("Break Timer (minutes)") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                val workTime = workTimeInput.toIntOrNull() ?: 0
                val breakTime = breakTimeInput.toIntOrNull() ?: 0
                settingsViewModel.saveTimerValues(workTime, breakTime)
            }) {
                Text(text = "Save Timer Settings")

            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Image Picker Overlay: Moved outside of the main Column, but still inside the Box
        AnimatedVisibility(
            visible = showImagePicker,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(targetAlpha = 0.0f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize() // this ensures the Box takes up the entire screen space
                    .background(Color(4, 94, 165, 255))
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(imageList.chunked(2)) { rowImages ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            rowImages.forEach { imageUrl ->
                                Image(
                                    painter = rememberImagePainter(data = imageUrl),
                                    contentDescription = "Image from Firebase Storage",
                                    modifier = Modifier
                                        .size(150.dp)
                                        .clickable {
                                            scope.launch {
                                                currentUser.getSignedInUser()?.userId?.let { userId ->
                                                    settingsViewModel.updateProfileImage(
                                                        userId,
                                                        imageUrl
                                                    )
                                                    Toast.makeText(
                                                        context,
                                                        "Profile Image Updated",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    showImagePicker = false
                                                }
                                            }
                                        }
                                )
                            }
                        }


                    }
                }
            }

        }
    }
}







