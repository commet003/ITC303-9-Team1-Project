package com.csu_itc303_team1.adhdtaskmanager.ui.sign_in

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.csu_itc303_team1.adhdtaskmanager.R
import com.csu_itc303_team1.adhdtaskmanager.utils.database_dao.RewardDao
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.SignInState
import com.csu_itc303_team1.adhdtaskmanager.utils.local_database.Reward
import com.csu_itc303_team1.adhdtaskmanager.utils.local_database.RewardDatabase
import kotlinx.coroutines.coroutineScope
import kotlin.math.log

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
    onAnonymousSignIn: () -> Unit
) {

    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    Column {

        // TODO: Add a logo image here

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ElevatedButton(
                    modifier = Modifier.padding(bottom = 48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    onClick = onSignInClick


                ) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.ic_google_logo
                        ),
                        contentDescription = null
                    )

                    Text(
                        text = "Sign in with Google",
                        modifier = Modifier.padding(6.dp),
                        fontSize = 18.sp
                    )



                }

                ElevatedButton(
                    modifier = Modifier.padding(bottom = 48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    onClick = onAnonymousSignIn) {
                    Text(
                        text = "Sign in Anonymously",
                        modifier = Modifier.padding(6.dp),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}