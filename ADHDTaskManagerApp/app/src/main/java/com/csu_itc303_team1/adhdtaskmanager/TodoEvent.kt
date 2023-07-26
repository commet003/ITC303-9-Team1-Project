package com.csu_itc303_team1.adhdtaskmanager

sealed interface TodoEvent {

    // Data class to update todo
    object updateTodo: TodoEvent

    // Get individual todo
    data class getTodoById(val id: Int): TodoEvent

    // Toggle isClicked
    data class toggleIsClicked(val todo: Todo): TodoEvent

    object saveTodo: TodoEvent
    data class setTitle(val title: String): TodoEvent
    data class setDescription(val description: String): TodoEvent
    data class setPriority(val priority: Priority): TodoEvent
    data class setDueDate(val dueDate: String): TodoEvent
    data class setDueTime(val dueTime: String): TodoEvent
    data class deleteTodo(val todo: Todo): TodoEvent
    data class setCompletedDate(val todo: Todo): TodoEvent

    // setUserId
    data class setUserId(val userId: String): TodoEvent

    data class toggleCompleted(val todo: Todo): TodoEvent
    // Data class to sort by sort type
    data class sortBy(val sortType: SortType): TodoEvent

    object showDialog: TodoEvent
    object hideDialog: TodoEvent

    object showEditTodoDialog: TodoEvent
    object hideEditTodoDialog: TodoEvent

    object showDateSelector: TodoEvent
    object hideDateSelector: TodoEvent

    object showTimeSelector: TodoEvent
    object hideTimeSelector: TodoEvent
    object ShowCompletedTasks: TodoEvent

}