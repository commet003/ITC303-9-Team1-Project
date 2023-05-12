package com.csu_itc303_team1.adhdtaskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo.db"
        ).build()
    }



    private val viewModel by viewModels<TodoViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TodoViewModel(db.todoDao) as T
                }
            }
        })

    private val todoViewModel by viewModels<TodoViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TodoViewModel(db.todoDao) as T
            }
        }
    }







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            ADHDTaskManagerTheme {
                val state by viewModel.state.collectAsState()

                var rowCount by remember(todoViewModel) { mutableStateOf(0) }


                //TodoScreen(state = state, onEvent = viewModel::onEvent)
                Column {

                    Button(onClick = {
                        lifecycleScope.launch {
                            rowCount = todoViewModel.getTotalRowCount()
                        }
                    }) {
                        Text(text = "Tap to see updated number of tasks.")
                    }
                    if (rowCount >= 0) {
                        Text(text = "Active tasks: $rowCount")
                    }

                    //else if (rowCount == 0){ Text(text = "You have no active task")}
                    TodoScreen(state = state, onEvent = viewModel::onEvent)
                }
            }
        }
        lifecycleScope.launch {
            val rowCount = todoViewModel.getTotalRowCount()
            // Use the rowCount value as needed. This was used for testing Logcat output.
            //println("Hello " + rowCount)

        }

    }
}


