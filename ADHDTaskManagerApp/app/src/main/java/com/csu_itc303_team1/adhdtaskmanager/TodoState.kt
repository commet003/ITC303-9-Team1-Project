package com.csu_itc303_team1.adhdtaskmanager

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
    var isCompleted: Boolean = false,

)
