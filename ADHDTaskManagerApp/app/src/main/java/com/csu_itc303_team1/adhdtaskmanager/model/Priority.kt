package com.csu_itc303_team1.adhdtaskmanager.model

import android.util.Log

enum class Priority(val value: Int) {
    None(value = 0),
    Low(value = 1),
    Medium(value = 2),
    High(value = 3);

    companion object {
        fun getByName(name: String?): Priority {
            values().forEach { priority -> if (name == priority.name) return priority }

            return None
        }

        fun getByValue(value: Int): Priority {
            values().forEach { priority -> if (value == priority.value) return priority }

            return None
        }

        fun getOptions(): List<String> {
            val options = mutableListOf<String>()
            values().forEach { priority ->
                Log.d("Priority", priority.name)
                options.add(priority.name)
            }
            return options
        }
    }
}