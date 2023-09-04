package com.csu_itc303_team1.adhdtaskmanager.model

import androidx.compose.ui.graphics.Color


enum class Category(val categoryName: String?, var color: Color?) {
    School(categoryName = "School", color = Color(0xFFE57373)),
    Work(categoryName = "Work", color = Color(0xFF81C784)),
    Home(categoryName = "Home", color = Color(0xFF64B5F6)),
    Other(categoryName = "Other", color = Color(0xFFBA68C8));

    companion object {


        fun getCategoryByName(name: String?): Category {
            values().forEach { category -> if (name == category.categoryName) return category }

            return Other
        }

        fun getCategory(): List<String> {
            val categories = mutableListOf<String>()
            values().forEach { category -> categories.add(category.categoryName!!) }
            return categories
        }
    }
}
