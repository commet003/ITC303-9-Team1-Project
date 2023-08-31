package com.csu_itc303_team1.adhdtaskmanager.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import com.csu_itc303_team1.adhdtaskmanager.R
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    private val DATE_TIME_FORMATTER_PATTERN = DateTimeFormatter.ofPattern("MMM d, yyyy")
    private const val MAX_NUM_DAYS_FOR_CUSTOM_MESSAGE = 5

    /**
     * Returns `dueDate` in human readable format.
     */
    @SuppressLint("StringFormatInvalid")
    fun formattedDate(resources: Resources, dueDate: Instant, clock: Clock): String {
        return resources.getString(
            R.string.due_date_generic,
            ZonedDateTime
                .ofInstant(dueDate, clock.zone)
                .format(DATE_TIME_FORMATTER_PATTERN)
        )
    }

    /**
     * Returns either a message about the time till the due date (example: "Due today", or
     * "Due 3 days ago") or returns a human readable version of the due date (Example:
     * "October 10, 2021")
     */
    fun durationMessageOrDueDate(
        resources: Resources,
        dueDate: Instant,
        clock: Clock
    ): String {
        val message = durationMessage(resources, dueDate, clock)
        return message.ifEmpty {
            formattedDate(resources, dueDate, clock)
        }
    }

    /**
     * Returns a message about the time till the due date (example: "Due today", or
     * "Due 3 days ago"). Used to draw attention to items which are overdue or about to become due.
     */
    private fun durationMessage(
        resources: Resources,
        dueDate: Instant,
        clock: Clock
    ): String {
        return when (val daysTillDue =
            Duration.between(Instant.now(clock), dueDate).toDays().toInt()) {
            in Int.MIN_VALUE..-1 -> {
                val daysOverdue = daysTillDue.times(-1)
                resources.getQuantityString(
                    R.plurals.due_date_overdue_x_days,
                    daysOverdue,
                    daysOverdue
                )
            }
            0 -> {
                resources.getString(R.string.due_date_today)
            }
            in 1 until MAX_NUM_DAYS_FOR_CUSTOM_MESSAGE ->
                resources.getQuantityString(
                    R.plurals.due_date_days,
                    daysTillDue,
                    daysTillDue
                )
            else ->
                ""
        }
    }
}