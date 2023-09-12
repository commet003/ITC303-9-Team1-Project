package com.csu_itc303_team1.adhdtaskmanager.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.csu_itc303_team1.adhdtaskmanager.common.composable.DangerousCardEditor
import com.csu_itc303_team1.adhdtaskmanager.common.composable.DialogCancelButton
import com.csu_itc303_team1.adhdtaskmanager.common.composable.DialogConfirmButton
import com.csu_itc303_team1.adhdtaskmanager.common.composable.RegularCardEditor
import com.csu_itc303_team1.adhdtaskmanager.common.ext.card
import com.csu_itc303_team1.adhdtaskmanager.common.ext.spacer
import com.csu_itc303_team1.adhdtaskmanager.R.drawable as AppIcon
import com.csu_itc303_team1.adhdtaskmanager.R.string as AppText

@ExperimentalMaterialApi
@Composable
fun SettingsScreen(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(
        initial = SettingsUiState(false, true))

    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.spacer())

        if (uiState.isSignedIn.not()) {
            RegularCardEditor(AppText.login_title, AppIcon.ic_sign_in, "", Modifier.card()) {
                viewModel.onLoginClick(openScreen)
            }

            RegularCardEditor(AppText.link_account, AppIcon.ic_create_account, "", Modifier.card()) {
                viewModel.onLinkAccountClick(openScreen)
            }
        } else if (uiState.isSignedIn) {
            DeleteMyAccountCard { viewModel.onDeleteMyAccountClick(restartApp) }
        }
    }
}



@ExperimentalMaterialApi
@Composable
private fun DeleteMyAccountCard(deleteMyAccount: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    DangerousCardEditor(
        AppText.delete_my_account,
        AppIcon.ic_delete_my_account,
        "",
        Modifier.card()
    ) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.delete_account_title)) },
            text = { Text(stringResource(AppText.delete_account_description)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.delete_my_account) {
                    deleteMyAccount()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}