package com.csu_itc303_team1.adhdtaskmanager

import com.csu_itc303_team1.adhdtaskmanager.database.local.Todo

data class TodoState(
    val todos: List<Todo> = emptyList(),
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.LOW,
    val dueDate: String = "",
    val dueTime: String = "",
    val sortType: SortType = SortType.BY_DATE,
    val showDialog: Boolean = false,
    val showEditTodoDialog: Boolean = false,
    val showDateSelector: Boolean = false,
    val showTimeSelector: Boolean = false,
    val pomodoroTime: String = "",
    val breakTime: String = "",
)
