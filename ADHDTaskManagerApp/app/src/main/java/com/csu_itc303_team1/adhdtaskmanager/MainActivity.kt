package com.csu_itc303_team1.adhdtaskmanager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme

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

    private lateinit var navController: NavHostController

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ADHDTaskManagerTheme {

                // The Navigation Bar and Drawer will appear on the Main Activity (Every Screen)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // variables for remembering the state of the Coroutine Scope and Scaffold
                    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
                    val scope = rememberCoroutineScope()
                    navController = rememberNavController()

                    Scaffold(
                        modifier = Modifier
                            .padding(0.dp)
                            .fillMaxWidth()
                        ,
                        scaffoldState = scaffoldState,
                        // Creating the Top Bar
                        topBar = { AppTopAppBar(scope = scope, scaffoldState = scaffoldState) },
                        // Drawer content is what is inside the navigation drawer when clicking the
                        // menu icon. This case, A header and all the menu options in the drawer body
                        drawerContent = {
                            DrawerHeader()
                            DrawerBody(scope = scope, scaffoldState = scaffoldState, navController = navController)
                        }
                    ) { // In this Section is contents of the actual screen. A padding value had to
                        // be added in the lambda form.
                            contentPadding ->
                        Box(modifier = Modifier.padding(contentPadding))

                        // The content itself is the navController's current state, or Home Screen
                        // on Default
                        val state by viewModel.state.collectAsState()
                        SetupNavGraph(
                            navController = navController,
                            state = state,
                            event = viewModel::onEvent
                            )
                        }
                    }
                }
            }
            //HelpScreen()
        }
    }




