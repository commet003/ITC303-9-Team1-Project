package com.csu_itc303_team1.adhdtaskmanager.screens.settings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csu_itc303_team1.adhdtaskmanager.common.composable.DangerousCardEditor
import com.csu_itc303_team1.adhdtaskmanager.common.composable.DialogCancelButton
import com.csu_itc303_team1.adhdtaskmanager.common.composable.DialogConfirmButton
import com.csu_itc303_team1.adhdtaskmanager.common.composable.RegularCardEditor
import com.csu_itc303_team1.adhdtaskmanager.common.ext.card
import com.csu_itc303_team1.adhdtaskmanager.data.UserPreferences
import kotlinx.coroutines.launch
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
    val userPreferences by viewModel.getPreferences().collectAsState(initial = UserPreferences())
    val scope = rememberCoroutineScope()
    var roundsSliderPosition by remember { mutableFloatStateOf(0f) }
    var focusSliderPosition by remember { mutableFloatStateOf(0f) }
    var shortBreakSliderPosition by remember { mutableFloatStateOf(0f) }
    var longBreakSliderPosition by remember { mutableFloatStateOf(0f) }

    roundsSliderPosition = userPreferences.pomodoroTimerRounds.toFloat()
    focusSliderPosition = userPreferences.pomodoroTimerFocusDuration.toFloat()
    shortBreakSliderPosition = userPreferences.pomodoroTimerShortBreakDuration.toFloat()
    longBreakSliderPosition = userPreferences.pomodoroTimerLongBreakDuration.toFloat()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .height(90.dp)
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Dark Mode:",
                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.weight(1f))
            FilledIconToggleButton(
                checked = !userPreferences.darkMode,
                onCheckedChange = {
                    scope.launch {
                        Log.d("Dark Mode", "Dark Mode: ${userPreferences.darkMode}")
                        viewModel.updateDarkMode(!userPreferences.darkMode)
                    }
                },
                enabled = true,
                colors = IconToggleButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                    checkedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    checkedContentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (userPreferences.darkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                    contentDescription = "Dark Mode"
                )
            }
            Spacer(Modifier.width(16.dp))
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Timer Rounds:",
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = roundsSliderPosition.toInt().toString(),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                )
            }

            Spacer(Modifier.weight(1f))

            Slider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .semantics { contentDescription = "Timer Rounds" },
                value = roundsSliderPosition,
                onValueChange = {roundsSlider ->
                    roundsSliderPosition = roundsSlider
                                },
                onValueChangeFinished = {
                    scope.launch {
                        viewModel.updateTimerRounds(roundsSliderPosition.toInt())
                    }
                },
                steps = 4,
                valueRange = 1f..6f,
            )
            Spacer(Modifier.width(16.dp))
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Focus Time:",
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = focusSliderPosition.toInt().toString(),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                )
            }

            Spacer(Modifier.weight(1f))

            Slider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .semantics { contentDescription = "Focus Time" },
                value = focusSliderPosition,
                onValueChange = {focusSlider ->
                    focusSliderPosition = focusSlider
                },
                onValueChangeFinished = {
                    scope.launch {
                        viewModel.updateFocusTimerDuration(focusSliderPosition.toLong())
                    }
                },
                steps = 4,
                valueRange = 5f..30f,
            )
            Spacer(Modifier.width(16.dp))
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Short Break Time:",
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = shortBreakSliderPosition.toInt().toString(),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                )
            }

            Spacer(Modifier.weight(1f))

            Slider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .semantics { contentDescription = "Short Break Time" },
                value = shortBreakSliderPosition,
                onValueChange = {shortBreakSlider ->
                    shortBreakSliderPosition = shortBreakSlider
                },
                onValueChangeFinished = {
                    scope.launch {
                        viewModel.updateShortBreakTimerDuration(shortBreakSliderPosition.toLong())
                    }
                },
                steps = 4,
                valueRange = 1f..10f,
            )
            Spacer(Modifier.width(16.dp))
        }

        HorizontalDivider()


        Row(
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Long Break Time:",
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = longBreakSliderPosition.toInt().toString(),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                )
            }

            Spacer(Modifier.weight(1f))

            Slider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .semantics { contentDescription = "Short Break Time" },
                value = longBreakSliderPosition,
                onValueChange = {longBreakSlider ->
                    longBreakSliderPosition = longBreakSlider
                },
                onValueChangeFinished = {
                    scope.launch {
                        viewModel.updateLongBreakTimerDuration(longBreakSliderPosition.toLong())
                    }
                },
                steps = 4,
                valueRange = 1f..20f,
            )
            Spacer(Modifier.width(16.dp))
        }

        HorizontalDivider()

        Spacer(modifier = Modifier.height(40.dp))

        if (uiState.isSignedIn && uiState.isAnonymousAccount) {
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
            title = { Text(
                stringResource(AppText.delete_account_title),
                color = MaterialTheme.colorScheme.error
                ) },
            text = { Text(
                stringResource(AppText.delete_account_description),
                color = MaterialTheme.colorScheme.error
                ) },
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