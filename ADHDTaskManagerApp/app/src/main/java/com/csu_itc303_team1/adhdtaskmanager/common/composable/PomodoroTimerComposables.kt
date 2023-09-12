package com.csu_itc303_team1.adhdtaskmanager.common.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
//consists of #pomodoros that indicates session count
fun PomodoroCount(
    numberOfPoms: Int,
    pomsCompleted: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .wrapContentSize()
            .padding(32.dp),
    ) {
        for (index in 0 until pomsCompleted) {
            Icon(imageVector = Icons.Filled.Circle,
                contentDescription = null,
                modifier = Modifier
                    .padding(32.dp))
        }
        for (index in pomsCompleted until numberOfPoms) {
            Icon(imageVector = Icons.Filled.RadioButtonUnchecked,
                contentDescription = null,
                modifier = Modifier.padding(32.dp))
        }
    }
}


@Composable
//a clock that goes through by time
fun PomodoroTimer(
    text: String,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    PomodoroProgress(percentage = progress, modifier = modifier.padding(50.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .padding(4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                modifier = modifier
                    .padding(horizontal = 20.dp)
                    .wrapContentSize(),
                color = Color.Black
            )
        }
    }
}

@Composable
fun PomodoroProgress(
    percentage: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        content()
        ProgressIndicator(percentage)
    }
}


@Composable
fun ProgressIndicator(percentage: Float, modifier: Modifier = Modifier) {
    val stroke = with(LocalDensity.current) { Stroke(4.dp.toPx(), cap = StrokeCap.Round) }
    val color = MaterialTheme.colorScheme.primary
    val bac = MaterialTheme.colorScheme.primaryContainer
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f)
    ) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.height - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        val sweepAngle = percentage * 360f
        drawArc(
            color = bac,
            startAngle = 0f,
            sweepAngle = 360f,
            topLeft = topLeft,
            size = size,
            useCenter = false,
            style = stroke
        )
        drawArc(
            color = color,
            startAngle = 270f,
            sweepAngle = sweepAngle,
            topLeft = topLeft,
            size = size,
            useCenter = false,
            style = stroke
        )

        val angleInDegrees = sweepAngle + 180
        val x = -(innerRadius * kotlin.math.sin(
            Math.toRadians(
                angleInDegrees.toDouble()
            )
        )).toFloat() + ((size.width + 4.dp.toPx()) / 2)
        val y = (innerRadius * kotlin.math.cos(
            Math.toRadians(angleInDegrees.toDouble())
        )).toFloat() + ((size.height + 4.dp.toPx()) / 2)

        if (percentage != 0f) {
            drawCircle(
                color = color,
                radius = 10f,
                center = Offset(x, y)
            )
        }
    }
}