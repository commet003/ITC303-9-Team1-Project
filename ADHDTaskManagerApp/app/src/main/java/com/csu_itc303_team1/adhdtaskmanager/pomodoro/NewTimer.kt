package com.csu_itc303_team1.adhdtaskmanager.pomodoro

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.csu_itc303_team1.adhdtaskmanager.TodoEvent
import com.csu_itc303_team1.adhdtaskmanager.TodoViewModel
import com.csu_itc303_team1.adhdtaskmanager.database.local.TodoDatabase
import com.csu_itc303_team1.adhdtaskmanager.pomodoro.circle.TimerVm
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.compose.rememberNavController
import com.csu_itc303_team1.adhdtaskmanager.MainActivity
import com.csu_itc303_team1.adhdtaskmanager.R
import com.csu_itc303_team1.adhdtaskmanager.pomodoro.circle.TimerDisplay
import com.csu_itc303_team1.adhdtaskmanager.pomodoro.circle.theme.FlowTimerTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.PI
import kotlin.math.cos
import kotlin.math.sin

var globalValue = mutableMapOf<String, Long>()

class NewTimer : AppCompatActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo.db"
        ).fallbackToDestructiveMigration().build()
    }

    private val todoViewModel by viewModels<TodoViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TodoViewModel(db.todoDao) as T
                }
            }
        })

    override fun onCreate(savedInstanceState: Bundle?,) {
        super.onCreate(savedInstanceState,)

        fun stringToLong(str: String): Long {
            val splits = str.split(":")
            return splits[0].toLong() * 60 + splits[1].toLong()
        }

        val tastTitle: String = intent.getStringExtra("TASK_NAME") ?: ""
        val pomodoroTime: String = intent.getStringExtra("POMORO_TIME") ?: "0"
        val breakTime: String = intent.getStringExtra("BRAEK_TIME") ?: "0"

        val breakTimeNumber: Long = stringToLong(breakTime)
        var pomodoroTimeNumber: Long = stringToLong(pomodoroTime)
        var flag = false

        if (globalValue[tastTitle] != null && globalValue[tastTitle] != 0L ) {
            pomodoroTimeNumber = globalValue[tastTitle]!!
            flag = true
        }


        setContent {
            NewTimerScreen(onEvent = todoViewModel::onEvent, tastTitle, pomodoroTimeNumber, breakTimeNumber, flag)
        }

    }

    override fun onBackPressed() {
        // Handle the navigation back to the original Compose UI (Z)
        super.onBackPressed()
    }

    @Composable
    fun NewTimerScreen(onEvent: (TodoEvent) -> Unit, taskTitle: String, pomodoroTimeNumber: Long, breakTimeNumber: Long, flag: Boolean) {
        Column(
            modifier = Modifier
                .padding(20.dp, 20.dp, 0.dp, 0.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Text(text = "This is ${taskTitle}", color = Color.White, fontSize = 20.sp)
            Text(text = "PomodoroTime is ${pomodoroTimeNumber}s", color = Color.White, fontSize = 20.sp)
            Text(text = "BreakTime is ${breakTimeNumber}s", color = Color.White, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(height = 70.dp))

//            FlowTimerTheme {
//                val vm = viewModel<TimerVm>()
//                val timerState = vm.timerStateFlow.collectAsState()
//                TimerDisplay(timerState.value, vm::toggleStart)
//            }

            Box(
                modifier = Modifier
                    .fillMaxSize() // Fill the entire screen
                    .clickable(onClick = {
                        Log.e("clickevent", "Timer: clickente",)
                        onBackPressed()
                    }),
                contentAlignment = Alignment.Center

            ) {
                // call the function Timer
                // and pass the values
                // it is defined below.
                Timer(
                    totalTime = pomodoroTimeNumber * 1000L,
                    breakTime = breakTimeNumber * 1000L,
                    handleColor = Color(0xFF0013B9),
                    inactiveBarColor = Color.DarkGray,
                    activeBarColor = Color(0xFF0013B9),
                    modifier = Modifier.size(200.dp),
                    taskName = taskTitle,
                    flag = flag,
                )
            }
        }
    }

    class PomodoroCountdownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {

        override fun onFinish() {
            Log.e("NewTimer", "onFinished")
        }

        override fun onTick(millisUntilFinished: Long) {
            Log.e("NewTimer", "millisUntilFinished: ${millisUntilFinished / 1000}")
        }
    }


    @Composable
    fun Timer(
        // total time of the timer
        totalTime: Long,

        breakTime: Long,

        // circular handle color
        handleColor: Color,

        // color of inactive bar / progress bar
        inactiveBarColor: Color,

        // color of active bar
        activeBarColor: Color,
        modifier: Modifier = Modifier,

        // set initial value to 1
        initialValue: Float = 1f,
        strokeWidth: Dp = 5.dp,
        taskName: String,
        flag: Boolean
    ) {
        var countDownTimer: CountDownTimer? by remember { mutableStateOf(null) }

        // create variable for
        // size of the composable
        var size by remember {
            mutableStateOf(IntSize.Zero)
        }
        // create variable for value
        var value by remember {
            mutableStateOf(initialValue)
        }
        // create variable for current time
        var currentTime by remember {
            mutableStateOf(totalTime)
        }

        var myBreakTime by remember {
            mutableStateOf(breakTime)
        }
        // create variable for isTimerRunning
        var isTimerRunning by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
            if(currentTime > 0 && isTimerRunning) {
                delay(100L)
                currentTime -= 100L
                value = currentTime / totalTime.toFloat()
            }
        }

        val context = LocalContext.current
        val channelId = "MyTestChannel"
        val notificationId = 0

        LaunchedEffect(Unit) {
            createNotificationChannel(channelId, context)
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .onSizeChanged {
                    size = it
                }
        ) {
            // draw the timer
            Canvas(modifier = modifier) {
                // draw the inactive arc with following parameters
                drawArc(
                    color = inactiveBarColor, // assign the color
                    startAngle = -215f, // assign the start angle
                    sweepAngle = 250f, // arc angles
                    useCenter = false, // prevents our arc to connect at te ends
                    size = Size(size.width.toFloat(), size.height.toFloat()),

                    // to make ends of arc round
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
                // draw the active arc with following parameters
                drawArc(
                    color = activeBarColor, // assign the color
                    startAngle = -215f,  // assign the start angle
                    sweepAngle = 250f * value, // reduce the sweep angle
                    // with the current value
                    useCenter = false, // prevents our arc to connect at te ends
                    size = Size(size.width.toFloat(), size.height.toFloat()),

                    // to make ends of arc round
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
                // calculate the value from arc pointer position
                val center = Offset(size.width / 2f, size.height / 2f)
                val beta = (250f * value + 145f) * (PI / 180f).toFloat()
                val r = size.width / 2f
                val a = cos(beta) * r
                val b = sin(beta) * r
                // draw the circular pointer/ cap
                drawPoints(
                    listOf(Offset(center.x + a, center.y + b)),
                    pointMode = PointMode.Points,
                    color = handleColor,
                    strokeWidth = (strokeWidth * 3f).toPx(),
                    cap = StrokeCap.Round  // make the pointer round
                )
            }
            // add value of the timer
            Row(
                Modifier.width(100.dp)
            ) {
                Text(
                    text = if ((currentTime / 1000L / 60L).toString().length == 1) "0${(currentTime / 1000L / 60L)}" else "${(currentTime / 1000L / 60L)}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = " : ",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = if (((currentTime / 1000L) % 60L).toString().length == 1) "0${((currentTime / 1000L) % 60L)}" else "${((currentTime / 1000L) % 60L)}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            val countDownTimer2 = object : CountDownTimer(myBreakTime, 1000) {
                override fun onTick(millisUntilFinished2: Long) {
                    // Code to be executed every second until the countdown is finished
                    currentTime = millisUntilFinished2
                    isTimerRunning = true
                    Log.d("TAG", "Seconds remaining: " + millisUntilFinished2 / 1000)
                    if ((currentTime / 1000) == 10L) {
                        showSimpleNotification(
                            context,
                            channelId,
                            notificationId,
                            "Simple notification",
                            "Time is less than 10 seconds!"
                        )
                    }
                }

                override fun onFinish() {
                    // Code to be executed when the countdown finishes
                    Log.d("TAG", "Countdown timer has finished!")
                    isTimerRunning = false
                }
            }

            // create button to start or stop the timer
            Button(
                onClick = {
//                if(currentTime <= 0L) {
//                    currentTime = totalTime
//                    isTimerRunning = true
//                } else {
//                    isTimerRunning = !isTimerRunning
//                }

                    if(currentTime <= 0L) {
                        currentTime = totalTime
                    }

                    if (!isTimerRunning && currentTime >= 0L) {
                        // Initialize the countdown timer with the remaining time
                        countDownTimer = object : CountDownTimer(currentTime, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                // Code to be executed every second until the countdown is finished
                                currentTime = millisUntilFinished
                                globalValue[taskName] = millisUntilFinished / 1000
                                Log.d("TAG", "Seconds remaining: " + millisUntilFinished / 1000)
                                if ((currentTime / 1000) == 10L) {
                                    showSimpleNotification(
                                        context,
                                        channelId,
                                        notificationId,
                                        "Simple notification",
                                        "Timer has less than 10 seconds!"
                                    )
                                }
                            }

                            override fun onFinish() {
                                // Code to be executed when the countdown finishes
                                Log.d("TAG", "Countdown timer has finished!")

                                countDownTimer2.start()
                            }
                        }

                        // Start the countdown timer
                        countDownTimer?.start()
                        isTimerRunning = true
                    }
                    else if (isTimerRunning && currentTime >= 0L) {
                        countDownTimer?.cancel()
                        currentTime = if (currentTime > 0) currentTime else 0
                        isTimerRunning = false
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter),
                // change button color
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isTimerRunning || currentTime <= 0L) {
                        Color(0xFF0013B9)
                    } else {
                        Color.Red
                    }
                )
            ) {
                Text(
                    // change the text of button based on values
                    text = if (isTimerRunning && currentTime >= 0L) "Stop"
                    else if (!isTimerRunning && currentTime >= 0L) "Start"
                    else "Restart"
                )
            }
        }
    }

    fun createNotificationChannel(channelId: String, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MyTestChannel"
            val descriptionText = "My important test channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    fun showSimpleNotification(
        context: Context,
        channelId: String,
        notificationId: Int,
        textTitle: String,
        textContent: String,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_edit_location)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(priority)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(notificationId, builder.build())
        }
    }

}





