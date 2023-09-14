package com.csu_itc303_team1.adhdtaskmanager.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.csu_itc303_team1.adhdtaskmanager.R


@Composable
fun CompleteTaskAnimation(isVisible: Boolean) {
    if (isVisible) {
        val comositeResult: LottieCompositionResult = rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.animation_llc3c1bg)
        )
        val progressionAnimation by animateLottieCompositionAsState(
            comositeResult.value,
            isPlaying = true,
            iterations = 1,
            speed = 1.0f
        )
        LottieAnimation(
            composition = comositeResult.value,
            progress = progressionAnimation,
            modifier = Modifier
                .fillMaxSize() // This makes sure the animation occupies the entire screen
                .zIndex(Float.MAX_VALUE) // This gives it the highest possible z-index
        )
    }
}

@Composable
fun WelcomeAnimation(isVisible: Boolean) {
    if (isVisible) {
        val comositeResult: LottieCompositionResult = rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.animation_llc3c1bg)
        )
        val progressionAnimation by animateLottieCompositionAsState(
            comositeResult.value,
            isPlaying = true,
            iterations = 1,
            speed = 1.0f
        )
        LottieAnimation(
            composition = comositeResult.value,
            progress = progressionAnimation,
            modifier = Modifier
                .fillMaxSize() // This makes sure the animation occupies the entire screen
                .zIndex(Float.MAX_VALUE) // This gives it the highest possible z-index
        )
    }
}

@Composable
fun CompletePomodoroTimerTask(){

}

@Composable
fun LoginStreakAnimation() {

}

@Composable
fun WelcomeToast(isVisible: Boolean, username: String, @StringRes welcomeText: List<Int>){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize() // Fills the entire screen
            .background(Color.Transparent)
            .blur( // Blur the background
                radius = 4.dp,
                edgeTreatment = BlurredEdgeTreatment.Rectangle
            )
    ) {
        if (isVisible) {
            // Display the toast box
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.CenterHorizontally) // Adjust width based on content
                    .wrapContentHeight(align = Alignment.CenterVertically) // Adjust height based on content
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer, // Using the RGBA color you provided for the box background
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(
                        horizontal = 48.dp,
                        vertical = 24.dp
                    ) // Adjust the padding to make the box larger
                    .zIndex(1f) // Ensure the toast message is displayed below the Lottie animation
            ) {
                Text(
                    text = "${stringResource(id = welcomeText[0])} $username!  ${stringResource(id = welcomeText[1])}",
                    color = MaterialTheme.colorScheme.onTertiaryContainer, // Setting the text color to white
                    fontSize = 18.sp
                )
            }
        }
    }
}

// Custom Toast Composable
@Composable
fun CustomToastMessage(
    @StringRes message: Int,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize() // Fills the entire screen
            .background(Color.Transparent)
    ) {
        if (isVisible) {
            // Display the toast box
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .wrapContentWidth(align = Alignment.CenterHorizontally) // Adjust width based on content
                    .wrapContentHeight(align = Alignment.CenterVertically) // Adjust height based on content
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer, // Using the RGBA color you provided for the box background
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(
                        horizontal = 48.dp,
                        vertical = 24.dp
                    ) // Adjust the padding to make the box larger
                    .zIndex(1f) // Ensure the toast message is displayed below the Lottie animation
            ) {
                Text(
                    text = stringResource(id = message),
                    color = MaterialTheme.colorScheme.onTertiaryContainer, // Setting the text color to white
                    fontSize = 18.sp
                )
            }
        }
    }
}