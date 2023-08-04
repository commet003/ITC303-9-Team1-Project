package com.csu_itc303_team1.adhdtaskmanager.utils.states

import com.csu_itc303_team1.adhdtaskmanager.Priority
import com.csu_itc303_team1.adhdtaskmanager.SortType
import com.csu_itc303_team1.adhdtaskmanager.Todo

data class TodoState(
    var todos: List<Todo> = emptyList(),
    var title: String = "",
    var description: String = "",
    var priority: Priority = Priority.LOW,
    var dueDate: String = "",
    var dueTime: String = "",
    var userId: String = "",
    var isClicked: Boolean = false,
    var id: Int = 0,
    val sortType: SortType = SortType.BY_NOT_COMPLETED,
    val showDialog: Boolean = false,
    val showEditTodoDialog: Boolean = false,
    val showDateSelector: Boolean = false,
    val showTimeSelector: Boolean = false,
    val showEditDateSelector: Boolean = false,
    val showEditTimeSelector: Boolean = false,
    val completedTodos: List<Todo> = emptyList()
) {
}
