package com.csu_itc303_team1.adhdtaskmanager.model

enum class Priority(val priorityName: String?, val value: Int) {
    None(priorityName = "None" , value = 0),
    Low(priorityName = "Low" ,value = 1),
    Medium(priorityName = "Medium" , value = 2),
    High(priorityName = "High" ,value = 3);

    companion object {
        fun getByName(name: String?): Priority {
            values().forEach { priority -> if (name == priority.name) return priority }

            return None
        }

        fun getOptions(): List<String> {
            val options = mutableListOf<String>()
            values().forEach { priority -> options.add(priority.name) }
            return options
        }
    }
}
