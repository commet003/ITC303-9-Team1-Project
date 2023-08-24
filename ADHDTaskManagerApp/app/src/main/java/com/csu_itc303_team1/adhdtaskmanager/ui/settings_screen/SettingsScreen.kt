package com.csu_itc303_team1.adhdtaskmanager.ui.settings_screen

import android.content.Context
import android.content.IntentSender
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.lifecycle.lifecycleScope
import com.csu_itc303_team1.adhdtaskmanager.ui.sign_in.SignInViewModel
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// For now, this is just a placeholder code for a functional screen

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    signInViewModel: SignInViewModel,
    currentUser: AuthUiClient,
    context: Context,
    scope: CoroutineScope
) {


    val isDarkTheme by settingsViewModel.isDarkTheme.observeAsState(initial = false)
    var convertUserAuthIntentSender: IntentSender?
    var convertedUser: AuthCredential?

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                scope.launch {
                    val convertedSignInResult =
                        currentUser.getUsersGoogleCredentials(
                            intent = result.data ?: return@launch
                        )
                    currentUser.convertAnonymousUserToGoogleUser(convertedSignInResult)
                }
            }
        }
    )



    Column(verticalArrangement = Arrangement.Center) {
        SwitchRow(
            title = "Dark Mode",
            desc = "Changes the Application's Theme from Light to Dark.",
            checked = isDarkTheme,
            onCheckedChange = { settingsViewModel.toggleTheme(it) },
            enabled = true
        )
        Divider(color = Color.Black, thickness = 1.dp)
        EDropDownRow()
        Divider(color = Color.Black, thickness = 1.dp)
        TextFieldEdittor()
        Divider(color = Color.Black, thickness = 1.dp)
        Text("Update Username")
        var username by remember { mutableStateOf("") }
        TextField(value = username, onValueChange = {
            username = it
        })

        Button(
            onClick = {
                scope.launch {
                    currentUser.updateUsername(username)
                    Toast.makeText(
                        context,
                        "Username Updated",
                        Toast.LENGTH_LONG
                    ).show()

                    // reset the username
                    username = ""
                }
            }) {
            Text(text = "Update Username")
        }

        Row {
            Button(onClick = {
                scope.launch{
                    val convertUserAuthIntentSender =
                        currentUser.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            convertUserAuthIntentSender ?: return@launch
                        ).build()
                    )
                }
            }) {
                Text(text = "Sign Out")
            }
        }
    }
}