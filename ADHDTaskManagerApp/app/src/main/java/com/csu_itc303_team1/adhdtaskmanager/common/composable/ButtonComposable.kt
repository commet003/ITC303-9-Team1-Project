package com.csu_itc303_team1.adhdtaskmanager.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.R

@Composable
fun BasicTextButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    TextButton(onClick = action, modifier = modifier) { Text(text = stringResource(text)) }
}

@Composable
fun BasicButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    Button(
        onClick = action,
        modifier = modifier,
        colors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text = stringResource(text), fontSize = 16.sp)
    }
}

@Composable
fun DialogConfirmButton(@StringRes text: Int, action: () -> Unit) {
    Button(
        onClick = action,
        colors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text = stringResource(text))
    }
}

@Composable
fun DialogCancelButton(@StringRes text: Int, action: () -> Unit) {
    Button(
        onClick = action,
        colors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = stringResource(text))
    }
}

@Composable
fun GoogleSignInButton(@StringRes label: Int, onClick: () -> Unit) {
    ElevatedButton(
        modifier = Modifier.padding(bottom = 48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = onClick
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.ic_google_logo
            ),
            contentDescription = null
        )

        Text(
            text = stringResource(label),
            modifier = Modifier.padding(6.dp),
            fontSize = 18.sp
        )
    }
}


@Composable
fun ControlButton(
    modifier: Modifier = Modifier,
    running: Boolean,
    onResumeClicked: () -> Unit,
    onRestartClicked: () -> Unit,
    onPauseClicked: () -> Unit,
) {
    if (running) {
        Row {
            Icon(
                imageVector = Icons.Filled.Pause,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(id = R.string.stop_timer_description),
                modifier = modifier
                    .size(50.dp)
                    .clickable { onPauseClicked.invoke() },
            )
            Spacer(modifier = Modifier.width(width = 16.dp))
            Icon(
                imageVector = Icons.Outlined.Refresh,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(id = R.string.restart_timer_description),
                modifier = modifier
                    .size(50.dp)
                    .clickable { onRestartClicked.invoke() },
            )
        }
    } else {
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(id = R.string.resume_timer_description),
            modifier = modifier
                .size(50.dp)
                .clickable { onResumeClicked.invoke() },
        )
    }
}
