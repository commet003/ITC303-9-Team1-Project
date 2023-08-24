package com.csu_itc303_team1.adhdtaskmanager.ui.userScreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.Final
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.Response
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.Users
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.UsersRepo
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.UsersViewModel
import com.google.android.gms.auth.api.identity.Identity

@Composable
fun userScreen(

    client: AuthUiClient,

) {
    val context = LocalContext.current
    var viewManualClicked by remember { mutableStateOf(false) }
    var viewArchitectureClicked by remember { mutableStateOf(false) }
    var viewVideoClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val usersList = Final.finalDataList


    val signedInUser = client.getSignedInUser()






    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Add vertical spacing between items
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(5.dp))


            if (signedInUser != null) {
                Text(

                    text = "Welcome to your profile page  ${signedInUser.username}" ,
                    modifier = Modifier.weight(1f),
                    color = Color.Black,
                    fontSize = 22.sp
                )
            }
            Spacer(modifier = Modifier.width(60.dp))
        }

        // Add spacing between rows using Spacer or Modifier.height
        Spacer(modifier = Modifier.height(26.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFADD8E6)) // Custom LightBlue color (hex value)
        ) {
            Text(
                text = "Display Name:",
                modifier = Modifier.weight(1f),
                color = Color.Black,
                fontSize = 18.sp
            )
            if (signedInUser != null) {
                Text(
                    //text = user.displayName.toString(),
                    text = signedInUser.username.toString(),
                    color = Color.Black,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp)) // Add spacing between rows using Spacer or Modifier.height

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF90EE90)) // Custom LightGreen color (hex value)
        ) {
            Text(
                text = "Current Points:",
                modifier = Modifier.weight(1f),
                color = Color.Black,
                fontSize = 18.sp
            )
            Text(
                //text = user.points.toString(),
                //text = user.points.toString(),
                //text = user.points.toString(),
                //text = currentUser.value?.points.toString(),
                text = "SSS",
                color = Color.Black,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(26.dp)) // Add spacing between rows using Spacer or Modifier.height

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF90EE90)) // Custom LightGreen color (hex value)
        ) {
            Text(
                text = "Rewards:",
                modifier = Modifier.weight(1f),
                color = Color.Black,
                fontSize = 18.sp
            )
            Text(
                text = "user.displayName.toString()",
                color = Color.Black,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(26.dp)) // Add spacing between rows using Spacer or Modifier.height

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Country:",
                modifier = Modifier.weight(1f),
                color = Color.Black,
                fontSize = 18.sp
            )
            Text(
                text = "user.country.toString()",
                color = Color.Black,
                fontSize = 18.sp
            )
        }
    }
}


@Preview
@Composable
fun uScreenPreview() {

    val context = LocalContext.current
    val oneTapClient = Identity.getSignInClient(context)
     val usersRepo= UsersRepo()

    val authUiClient = AuthUiClient(context, oneTapClient)

    var t =  Users()
    var f = UsersRepo()
    var k = Response()

    val uvm  = UsersViewModel()


    userScreen(authUiClient)
}

