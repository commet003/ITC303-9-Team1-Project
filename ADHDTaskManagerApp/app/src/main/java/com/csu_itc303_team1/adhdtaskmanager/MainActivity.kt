package com.csu_itc303_team1.adhdtaskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo.db"
        ).build()
    }



//    private val viewModel by viewModels<TodoViewModel>(
//        factoryProducer = {
//            object : ViewModelProvider.Factory {
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    return TodoViewModel(db.todoDao) as T
//                }
//            }
//        })

    private val todoViewModel by viewModels<TodoViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TodoViewModel(db.todoDao) as T
            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch{
            val x = todoViewModel.getCountOfCompletedTodos()
            println("x is " + x)
        }
        setContent {
            ADHDTaskManagerTheme {
                //val state by viewModel.state.collectAsState()
                val state by todoViewModel.state.collectAsState()

                //initialised activeTasks and points to 0
                var activeTasks by remember(todoViewModel) { mutableStateOf(0) }

                var points:Int by remember (todoViewModel){ mutableStateOf(0)}


                //TodoScreen(state = state, onEvent = viewModel::onEvent)
                /**
                 * This column contains the two buttons that when pressed shows
                 * active number of tasks and points respectively.
                 */
                Column {
                    Row {

                        Button(onClick = {
                            lifecycleScope.launch {
                                activeTasks =
                                    todoViewModel.getTotalRowCount() - todoViewModel.getCountOfCompletedTodos()
                            }
                        }) {
                            Text(text = "Tap to see active number of tasks.")
                        }
                        if (activeTasks >= 0) {
                            Text(text = "  $activeTasks", Modifier.
                                padding(15.dp),
                                color = Color(20,30,4),
                                fontWeight = FontWeight(400)

                            )
                        }
                    }

                    Row {

                        Button(onClick = {
                            lifecycleScope.launch {
                                points =
                                    (todoViewModel.getCountOfCompletedTodos()) * 5
                            }
                        }) {
                            Text(text = "Tap to see points.")
                        }
                        if (activeTasks >= 0) {
                            Text(text = "  $points", Modifier.
                            padding(15.dp),
                                color = Color(20,30,4),
                                fontWeight = FontWeight(400)

                            )
                        }
                    }

                    //else if (rowCount == 0){ Text(text = "You have no active task")}
                    //TodoScreen(state = state, onEvent = viewModel::onEvent)
                    TodoScreen(state = state, onEvent = todoViewModel::onEvent)
                }
            }
        }


        lifecycleScope.launch{
            val x = todoViewModel.getCountOfCompletedTodos()
            println("x is " + x)
        }

        lifecycleScope.launch {
            val rowCount = todoViewModel.getTotalRowCount() - todoViewModel.getCountOfCompletedTodos()
            // Use the rowCount value as needed. This was used for testing Logcat output.
            println("Hello " + rowCount)

        }

    }
}


