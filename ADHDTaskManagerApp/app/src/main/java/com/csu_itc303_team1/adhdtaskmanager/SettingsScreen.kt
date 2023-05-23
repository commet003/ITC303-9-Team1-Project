package com.csu_itc303_team1.adhdtaskmanager

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys



// For now, this is just a placeholder code for a functional screen
//@Preview
@Composable
fun SettingsScreen(context: Context) {

    Row(
        modifier = Modifier
            .fillMaxSize(),

//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "   Notifications  :                                   ",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(top = 15.dp)

        )

        SwitchDemo(context)

    }

    Column(verticalArrangement = Arrangement.Center) {
        SwitchRow(


            title = "Example: Dark Mode",
            desc = "Changes the Application's Theme from Light to Dark.",
            checked = false,
            onCheckedChange = null,
            enabled = true
        )
        Divider(color = Color.Black, thickness = 1.dp)
        EDropDownRow()
        Divider(color = Color.Black, thickness = 1.dp)
        TextFieldEdittor()
        Divider(color = Color.Black, thickness = 1.dp)
    }

}

@Composable
fun SwitchDemo(context: Context) {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    // Initialize/open an instance of EncryptedSharedPreferences on below line.
    val sharedPreferences = EncryptedSharedPreferences.create(
        // passing a file name to share a preferences
        "preferences",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    val isNotify = sharedPreferences.getString("isNotify","none")
    var myBool = false
    if(isNotify.toString() == "1" || isNotify.toString() == "none"){
        myBool =true
    }
    //myBool = isNotify != "0"


    //val checkedState = remember { mutableStateOf(myBool) }
    var checkedState by remember { mutableStateOf(myBool) }

    val onChange by rememberUpdatedState { checked: Boolean ->
        checkedState = checked
        if(checked){
            var editor = sharedPreferences.edit()
            editor.putString("isNotify","1")
            editor.commit()

        }else{
            var editor = sharedPreferences.edit()
            editor.putString("isNotify","1")
            editor.commit()
        }

    }
    Switch(
        checked = checkedState,
//        onCheckedChange = { checkedState = it },
        onCheckedChange = { switchOn_ ->
            checkedState = switchOn_
            if(switchOn_){
                var editor = sharedPreferences.edit()
                editor.putString("isNotify","1")
                editor.commit()

            }else{
                var editor = sharedPreferences.edit()
                editor.putString("isNotify","0")
                editor.commit()
            }
        },
//        onCheckedChange = {onChange},
        modifier = Modifier.padding(top = 10.dp)

    )

}
//@Composable
//fun setValueSwitch(context: Context,switchValue:String){
//    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
//
//    // Initialize/open an instance of EncryptedSharedPreferences on below line.
//    val sharedPreferences = EncryptedSharedPreferences.create(
//        // passing a file name to share a preferences
//        "preferences",
//        masterKeyAlias,
//        context,
//        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//    )
//
//}

