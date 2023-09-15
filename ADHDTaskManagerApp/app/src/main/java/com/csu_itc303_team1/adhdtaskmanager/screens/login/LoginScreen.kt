package com.csu_itc303_team1.adhdtaskmanager.screens.login

import android.app.Activity.RESULT_OK
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.common.composable.BasicTextButton
import com.csu_itc303_team1.adhdtaskmanager.common.composable.GoogleSignInButton
import com.csu_itc303_team1.adhdtaskmanager.common.ext.textButton
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import com.csu_itc303_team1.adhdtaskmanager.R.string as AppText

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    viewModel.setOneTapClient(Identity.getSignInClient(context))
    val lifecycleScope = rememberCoroutineScope()


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    viewModel.signInWithIntent(
                        openAndPopUp,
                        intent = result.data ?: return@launch
                    )
                }
            }
        }
    )


    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoogleSignInButton(AppText.sign_in_with_google) {
            lifecycleScope.launch {
                val signInIntentSender =
                    viewModel.signIn()
                launcher.launch(
                    IntentSenderRequest.Builder(
                        signInIntentSender ?: return@launch
                    ).build()
                )
            }
        }

        BasicTextButton(AppText.sign_in_anonymously, Modifier.textButton()) {
            viewModel.viewModelScope.launch {
                viewModel.onSignInAnonymouslyClick(openAndPopUp)
            }
        }
    }
}



