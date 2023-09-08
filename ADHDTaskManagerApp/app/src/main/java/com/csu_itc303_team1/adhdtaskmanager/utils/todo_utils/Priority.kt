package com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils

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

        fun getOptions(): List<String> {
            val options = mutableListOf<String>()
            values().forEach { priority ->
                options.add(priority.name) }
            return options
        }
    }
}