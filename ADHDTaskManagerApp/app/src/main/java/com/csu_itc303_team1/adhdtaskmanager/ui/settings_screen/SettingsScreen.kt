package com.csu_itc303_team1.adhdtaskmanager.ui.settings_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.FirestoreViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// For now, this is just a placeholder code for a functional screen

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    currentUser: AuthUiClient,
    firestoreViewModel: FirestoreViewModel,
    scope: CoroutineScope
) {


    val isDarkTheme by settingsViewModel.isDarkTheme.observeAsState(initial = false)



    Column(verticalArrangement = Arrangement.Center) {
        SwitchRow(
            title = "Dark Mode",
            desc = "Changes the Application's Theme from Light to Dark.",
            checked = isDarkTheme,
            onCheckedChange = { settingsViewModel.toggleTheme(it) },
            enabled = true
        )
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        EDropDownRow()
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        TextFieldEdittor()
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        Text("Update Username")
        var username by remember { mutableStateOf("") }
        TextField(value = username, onValueChange = {
            username = it
        })

        Button(
            onClick = {
                scope.launch {
                    firestoreViewModel.updateUsername(currentUser.getSignedInUser()?.userID.toString(), username)

                    // reset the username
                    username = ""
                }
            }) {
            Text(text = "Update Username")
        }
    }
}