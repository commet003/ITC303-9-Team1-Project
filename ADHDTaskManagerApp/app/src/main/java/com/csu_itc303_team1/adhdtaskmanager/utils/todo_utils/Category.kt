package com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils

import androidx.compose.ui.graphics.Color

enum class Category(var color: Color?) {
    School(color = Color(0xFFE57373)),
    Work(color = Color(0xFF81C784)),
    Home(color = Color(0xFF64B5F6)),
    Other(color = Color(0xFFBA68C8));

    companion object {


        fun getCategoryByName(name: String?): Category {
            values().forEach { category -> if (name == category.name) return category }

            return Other
        }

        fun getCategories(): List<String> {
            val categories = mutableListOf<String>()
            values().forEach { category -> categories.add(category.name) }
            return categories
        }
    }
}