package com.csu_itc303_team1.adhdtaskmanager

data class TodoState(
    val todos: List<Todo> = emptyList(),
    var title: String = "",
    var description: String = "",
    var priority: Priority = Priority.LOW,
    var dueDate: String = "",
    var dueTime: String = "",
    var userId: String = "",
    var isClicked: Boolean = false,
    var id: Int = 0,
    val sortType: SortType = SortType.BY_DATE,
    val showDialog: Boolean = false,
    val showEditTodoDialog: Boolean = false,
    val showDateSelector: Boolean = false,
    val showTimeSelector: Boolean = false,
    val showEditDateSelector: Boolean = false,
    val showEditTimeSelector: Boolean = false,
    val completedTodos: List<Todo> = emptyList()
) {
}
