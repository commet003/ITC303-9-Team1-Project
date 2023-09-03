package com.csu_itc303_team1.adhdtaskmanager.model

enum class Category {
    School,
    Work,
    Home,
    Other;

    companion object {
        fun getByName(name: String?): Category {
            values().forEach { category -> if (name == category.name) return category }

            return Other
        }

        fun getOptions(): List<String> {
            val options = mutableListOf<String>()
            values().forEach { category -> options.add(category.name) }
            return options
        }
    }
}
