package com.csu_itc303_team1.adhdtaskmanager.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.R
import com.csu_itc303_team1.adhdtaskmanager.TodoDestinations
import com.csu_itc303_team1.adhdtaskmanager.TodoNavigationActions
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppModalDrawer(
    drawerState: DrawerState,
    currentRoute: String,
    navigationActions: TodoNavigationActions,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit
) {
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                navigateToTodos = { navigationActions.navigateToTodos() },
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        content()
    }
}

@Composable
private fun AppDrawer(
    currentRoute: String,
    navigateToTodos: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        DrawerHeader()
        DrawerButton(
//            painter = painterResource(id = R.drawable.ic_list),
            label = stringResource(id = R.string.list_title),
            isSelected = currentRoute == TodoDestinations.TODOS_ROUTE,
            action = {
                navigateToTodos()
                closeDrawer()
            }
        )
    }
}

@Composable
private fun DrawerHeader(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .height(dimensionResource(id = R.dimen.header_height))
            .padding(dimensionResource(id = R.dimen.header_padding))
    ) {
        /*Image(
            painter = painterResource(id = R.drawable.logo_no_fill),
            contentDescription =
            stringResource(id = R.string.todos_header_image_content_description),
            modifier = Modifier.width(dimensionResource(id = R.dimen.header_image_width))
        )*/
        Text(
            text = stringResource(id = R.string.navigation_view_header_title),
            color = MaterialTheme.colors.surface
        )
    }
}

@Composable
private fun DrawerButton(
   // painter: Painter,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tintColor = if (isSelected) {
        MaterialTheme.colors.secondary
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
    }

    TextButton(
        onClick = action,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            /*Icon(
                painter = painter,
                contentDescription = null, // decorative
                tint = tintColor
            )*/
            Spacer(Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.body2,
                color = tintColor
            )
        }
    }
}

@Preview("Drawer contents")
@Composable
fun PreviewAppDrawer() {
    ADHDTaskManagerTheme {
        Surface {
            AppDrawer(
                currentRoute = TodoDestinations.TODOS_ROUTE,
                navigateToTodos = {},
                closeDrawer = {}
            )
        }
    }
}