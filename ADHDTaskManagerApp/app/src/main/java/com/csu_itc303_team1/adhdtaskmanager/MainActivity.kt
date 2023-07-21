package com.csu_itc303_team1.adhdtaskmanager

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.csu_itc303_team1.adhdtaskmanager.ui.sign_in.SignInScreen
import com.csu_itc303_team1.adhdtaskmanager.ui.sign_in.SignInViewModel
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.launch
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import com.google.firebase.ktx.Firebase



@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {


    private val passPhrase = "passPhrase"
    private val factory = SupportFactory(SQLiteDatabase.getBytes(passPhrase.toCharArray()))
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo.db"
        )/*.openHelperFactory(factory)*/.build()
    }


//    private val rewardDB by lazy {
//        Room.databaseBuilder(
//            applicationContext,
//            RewardDatabase::class.java, "reward_database.db"
//        ).createFromAsset("reward.db").build()}

    private val rewardViewModel by viewModels<RewardViewModel>(
        factoryProducer = {
            object: ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RewardViewModel(application) as T
                }
            }
        }
    )





    private lateinit var navController: NavHostController
    private lateinit var leadViewModel: LeaderboardViewModel
    //private lateinit var rewardViewModel: RewardViewModel

    private val googleAuthUiClient by lazy {
        AuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    // Boolean value to check if the user is signed in or not
    private val isSignedIn by lazy {
        mutableStateOf(false)
    }




    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<TodoViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return TodoViewModel(db.todoDao) as T
                    }
                }
            }
        )

        googleAuthUiClient.addAuthStateListener {
            isSignedIn.value = googleAuthUiClient.getSignedIn()
        }


        setContent {
            // Retrieve's Leaderboard data onCreate
            leadViewModel = ViewModelProvider(this)[LeaderboardViewModel::class.java]
            // Puts it into a readable format
            getResponseUsingCallback()
            //initialRewards(rewardViewModel)
            //rewardViewModel = ViewModelProvider(this) [RewardViewModel::class.java]


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
                        topBar = {
                            if (isSignedIn.value){
                                AppTopAppBar(scope = scope, scaffoldState = scaffoldState)
                            }else {
                                SignInTopAppBar()
                            }
                                 },
                        // Drawer content is what is inside the navigation drawer when clicking the
                        // menu icon. This case, A header and all the menu options in the drawer body
                        // If the user is signed in, show the drawer

                        drawerContent = {
                            if (isSignedIn.value) {
                                DrawerHeader()
                                DrawerBody(
                                    context = applicationContext,
                                    scope = scope,
                                    scaffoldState = scaffoldState,
                                    navController = navController,
                                    currentUser = googleAuthUiClient
                                )
                            }
                            else null
                        }
                    ) { // In this Section is contents of the actual screen. A padding value had to
                        // be added in the lambda form.
                            contentPadding ->
                        Box(modifier = Modifier.padding(contentPadding))

                        val notificationPermission = rememberPermissionState(
                            android.Manifest.permission.POST_NOTIFICATIONS
                        )

                        if (!notificationPermission.hasPermission) {
                            PermissionDialog(
                                onEvent = viewModel::onEvent,
                                notificationPermission = notificationPermission
                            )
                        }

//                        val owner = LocalViewModelStoreOwner.current
//
//                        owner?.let{
//                            val rewardViewModel: RewardViewModel = viewModel(
//                                it,
//                                "RewardViewModel",
//                                RewardViewModelFactory(LocalContext.current.applicationContext as Application))
//                            RewardSetup(rewardViewModel)
//                            TodoRewardSetup(rewardViewModel)
//                        }


                        // The content itself is the navController's current state, or Home Screen
                        // on Default
                        val state by viewModel.state.collectAsState()
                        if (isSignedIn.value){
                            state.userId = googleAuthUiClient.getSignedInUser()?.userId ?: ""
                        }
                        //val rewardState by rewardViewModel.collectAsState()
                        val todoEvent = viewModel::onEvent
                        val signInViewModel = viewModel<SignInViewModel>()
                        val signInState by signInViewModel.state.collectAsState()

                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = { result ->
                                if(result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthUiClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        signInViewModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )

                        // NavHost for controlling the pages.
                        NavHost(
                            navController = navController,
                            startDestination = Screen.SignInScreen.route   // Screen that displays when app is first opened
                        ){
                            // Home screen/to-do screen
                            composable(
                                route = Screen.TodoScreen.route
                            ) {
                                TodoScreen(
                                    state = state,
                                    onEvent = todoEvent,
                                    rewardViewModel = rewardViewModel)
                            }

                            // Settings Screen
                            composable(
                                route = Screen.SettingsScreen.route
                            ) {
                                SettingsScreen()
                            }

                            // Leaderboard Screen
                            composable(
                                route = Screen.LeaderboardScreen.route
                            ) {
                                LeaderboardScreen()
                            }

                            // Rewards Screen
                            composable(
                                route = Screen.RewardsScreen.route
                            ) {
                                RewardsScreen(rewardViewModel)
                            }

                            // Sign In Screen
                            composable(
                                route = Screen.SignInScreen.route
                            ) {


                                LaunchedEffect(key1 = Unit) {
                                    if(googleAuthUiClient.getSignedInUser() != null) {
                                        navController.navigate("todo_screen")
                                        // Update top bar and drawer
                                    }
                                }


                                LaunchedEffect(key1 = signInState.isSignInSuccessful) {
                                    if(signInState.isSignInSuccessful) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Sign in successful",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.navigate("todo_screen")
                                        signInViewModel.resetState()
                                    }
                                }

                                SignInScreen(
                                    state = signInState,
                                    onSignInClick = {
                                        lifecycleScope.launch {
                                            val signInIntentSender = googleAuthUiClient.signIn()
                                            launcher.launch(
                                                IntentSenderRequest.Builder(
                                                    signInIntentSender ?: return@launch
                                                ).build()
                                            )
                                        }
                                    },
                                    onAnonymousSignIn = {
                                        lifecycleScope.launch {
                                            googleAuthUiClient.signInAnonymously()
                                            Toast.makeText(
                                                applicationContext,
                                                "Signed in anonymously",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            navController.navigate("todo_screen")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    //Shows the notification
    // TODO - Make this show the notification when a task is due
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "channel1")
            .setContentTitle("Task Reminder")
            .setContentText("You have a task due soon!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        notificationManager.notify(1, notification)
    }


    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun PermissionDialog(
        modifier: Modifier = Modifier,
        notificationPermission: PermissionState,
        onEvent: (TodoEvent) -> Unit
    ){
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {onEvent(TodoEvent.hideDialog)},
            title = {Text(text = "Notification Permission Required")},
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "This app requires notification permission to work")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        notificationPermission.launchPermissionRequest()
                    }
                ) {
                    Text(text = "Give Permission")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onEvent(TodoEvent.hideDialog)
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    // creates Arraylist of users from the firestore database
    private fun getResponseUsingCallback() {
        leadViewModel.getResponseUsingCallback((object : FirebaseCallback {
            override fun onResponse(response: Response) {
                usersList(response)
            }
        }))
    }

//    class RewardViewModelFactory(val application: Application):
//            ViewModelProvider.Factory{
//                override fun <T: ViewModel> create(modelClass: Class<T>): T {
//                    return RewardViewModel(application) as T
//                }
//            }

}