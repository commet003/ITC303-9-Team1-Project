package com.csu_itc303_team1.adhdtaskmanager.ui.settings_screen

<<<<<<< HEAD
=======
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
>>>>>>> parent of 15a1b0c (Rewards System working)
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
<<<<<<< HEAD
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.FirestoreViewModel
=======
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
>>>>>>> parent of 15a1b0c (Rewards System working)
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// For now, this is just a placeholder code for a functional screen

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
<<<<<<< HEAD
    currentUser: UserData,
    firestoreViewModel: FirestoreViewModel,
=======
    currentUser: AuthUiClient,
    context: Context,
>>>>>>> parent of 15a1b0c (Rewards System working)
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
<<<<<<< HEAD
                    firestoreViewModel.updateUsername(currentUser.userID.toString(), username)
=======
                    currentUser.updateUsername(username)
                    Toast.makeText(
                        context,
                        "Username Updated",
                        Toast.LENGTH_LONG
                    ).show()
>>>>>>> parent of 15a1b0c (Rewards System working)

                    // reset the username
                    username = ""
                }
            }) {
            Text(text = "Update Username")
        }
    }
}