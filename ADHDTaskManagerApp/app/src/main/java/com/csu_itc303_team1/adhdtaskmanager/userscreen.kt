package com.csu_itc303_team1.adhdtaskmanager

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient

@Composable
fun userScreen(user: Users, userRepo:UsersRepo, response: Response, client: AuthUiClient) {

    val usersList = Final.finalDataList
    response.users

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
                text = user.points.toString(),
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
                text = user.displayName.toString(),
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
                text = user.country.toString(),
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

    val authUiClient = AuthUiClient(context, oneTapClient)

    var t =  Users()
    var f = UsersRepo()
    var k = Response()

    userScreen(t, f, k, authUiClient)
}


