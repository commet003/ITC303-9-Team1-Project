package com.csu_itc303_team1.adhdtaskmanager.utils.states

import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.SortOrder
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Todo

data class TodoState(
    var todos: List<Todo> = emptyList(),
    var title: String = "",
    var description: String = "",
    var priority: Int = 0,
    var category: String = "",
    var dueDate: String = "",
    var dueTime: String = "",
    var userId: String = "",
    var isClicked: Boolean = false,
    var id: Int = 0,
    val sortOrder: SortOrder = SortOrder.BY_DEADLINE,
    val titleError: Boolean = false,
    val descriptionError: Boolean = false,
    val showDialog: Boolean = false,
    val showEditTodoDialog: Boolean = false,
    val showDateSelector: Boolean = false,
    val showTimeSelector: Boolean = false,
    val showEditDateSelector: Boolean = false,
    val showEditTimeSelector: Boolean = false,
    val completedTodos: List<Todo> = emptyList(),
    var showLottieAnimation: Boolean = false

)
