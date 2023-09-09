package com.csu_itc303_team1.adhdtaskmanager.common.composable

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.model.Category
import com.csu_itc303_team1.adhdtaskmanager.model.Priority
import com.csu_itc303_team1.adhdtaskmanager.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalMaterialApi
fun DropdownContextMenu(
    options: List<String>,
    modifier: Modifier,
    onActionClick: (String) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        modifier = modifier,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        Icon(
            modifier = Modifier.padding(8.dp, 0.dp),
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More"
        )

        ExposedDropdownMenu(
            modifier = Modifier.width(180.dp),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        isExpanded = false
                        onActionClick(selectionOption)
                    },
                    text = { Text(text = selectionOption) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalMaterialApi
fun DropdownSelector(
    @StringRes label: Int,
    options: List<String>,
    selection: String,
    modifier: Modifier,
    onNewValue: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Log.d("Dropdown Options", "Options: $options")

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        modifier = modifier,
        onExpandedChange = {
            isExpanded.let {
                isExpanded = !isExpanded
            }
            Log.d("Dropdown Change", "isExpanded: $isExpanded")
        },
        content = {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                value = selection,
                onValueChange = {},
                label = { Text(stringResource(label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded) },
                colors = dropdownColors()
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            onNewValue(selectionOption)
                            isExpanded = false
                        },
                        text = { Text(text = selectionOption) }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityDropdown(
    task: Task,
    onNewValue: (Int) -> Unit
){
    var prioritySelection by remember { mutableIntStateOf(task.priority) }
    var priorityExpandedMenu by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth(0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ExposedDropdownMenuBox(
            expanded = priorityExpandedMenu,
            onExpandedChange = { priorityExpandedMenu = !priorityExpandedMenu }
        )
        {
            Button(
                modifier = Modifier
                    .menuAnchor(),
                shape = MaterialTheme.shapes.small,
                onClick = { }) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (prioritySelection == 0) "Priority" else Priority.values()[prioritySelection].name,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimary,
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Priority"
                )
            }

            ExposedDropdownMenu(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary),
                expanded = priorityExpandedMenu,
                onDismissRequest = { priorityExpandedMenu = false }) {
                Priority.values().forEach { priorityLevel ->
                    DropdownMenuItem(
                        modifier = Modifier.clip(MaterialTheme.shapes.medium),
                        onClick = {
                            prioritySelection = priorityLevel.value
                            onNewValue(priorityLevel.value)
                            priorityExpandedMenu = false
                        },
                        text = {
                            Text(
                                text = priorityLevel.name,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    task: Task,
    onNewValue: (String) -> Unit
){
    var categorySelection by remember { mutableStateOf(task.category) }
    var categoryExpandedMenu by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxWidth(0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ExposedDropdownMenuBox(
            expanded = categoryExpandedMenu,
            onExpandedChange = { categoryExpandedMenu = !categoryExpandedMenu }
        )
        {
            Button(
                modifier = Modifier
                    .menuAnchor(),
                shape = MaterialTheme.shapes.small,
                onClick = { }) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (categorySelection.isBlank()) "Category" else Category.getCategoryByName(
                        categorySelection
                    ).name,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(0.9f))
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimary,
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Category"
                )
            }

            ExposedDropdownMenu(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary),
                expanded = categoryExpandedMenu,
                onDismissRequest = { categoryExpandedMenu = false }) {
                Category.values().forEach { categoryLevel ->
                    DropdownMenuItem(
                        modifier = Modifier.clip(MaterialTheme.shapes.medium),
                        onClick = {
                            categorySelection = categoryLevel.name
                            onNewValue(categoryLevel.name)
                            categoryExpandedMenu = false
                        },
                        text = {
                            Text(
                                text = categoryLevel.name,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalMaterialApi
private fun dropdownColors(): TextFieldColors {
    return ExposedDropdownMenuDefaults.textFieldColors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        disabledContainerColor = MaterialTheme.colorScheme.surface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
        focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
    )
}