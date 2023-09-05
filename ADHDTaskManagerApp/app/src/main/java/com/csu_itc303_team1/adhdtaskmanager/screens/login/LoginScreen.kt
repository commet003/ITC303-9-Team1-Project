package com.csu_itc303_team1.adhdtaskmanager.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.csu_itc303_team1.adhdtaskmanager.common.composable.BasicButton
import com.csu_itc303_team1.adhdtaskmanager.common.composable.BasicTextButton
import com.csu_itc303_team1.adhdtaskmanager.common.composable.BasicToolbar
import com.csu_itc303_team1.adhdtaskmanager.common.ext.basicButton
import com.csu_itc303_team1.adhdtaskmanager.common.ext.textButton
import com.csu_itc303_team1.adhdtaskmanager.R.string as AppText

@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {

    BasicToolbar(AppText.login_title)

    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicButton(AppText.sign_in_with_google, Modifier.basicButton()) { viewModel.onSignInClick(openAndPopUp) }

        BasicTextButton(AppText.sign_in_anonymously, Modifier.textButton()) {
            viewModel.onSignInAnonymouslyClick(openAndPopUp)
        }
    }
}