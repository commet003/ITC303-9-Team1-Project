package com.csu_itc303_team1.adhdtaskmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class TodoViewModel(
    private val todoDao: TodoDao
): ViewModel() {
    private val _state = MutableStateFlow(TodoState())
    private val _sortType = MutableStateFlow(SortType.BY_DATE)

    //val UserID : Int = 5;

    @OptIn(ExperimentalCoroutinesApi::class)


    private val _todos = _sortType
        .flatMapLatest { sortType ->
        when(sortType){
            SortType.BY_DATE -> todoDao.sortByDueDate()
            SortType.BY_PRIORITY -> todoDao.sortByPriority()
            SortType.BY_TIME -> todoDao.sortByDueTime()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _sortType, _todos){ state, sortType, todos ->
        state.copy(
            todos = todos,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodoState())

    fun onEvent(event: TodoEvent){

        when(event){

            is TodoEvent.markTodoAsCompleted -> {
                val todo = state.value.todos.find { it.id == event.todo.id}
                todo?.let {
                    val updatedTodo = it.copy(isCompleted = true)
                    println("Com")
                    viewModelScope.launch {
                        todoDao.updateTodoIsCompleted(updatedTodo)
                    }}}


            is TodoEvent.deleteTodo -> {
                viewModelScope.launch {
                    todoDao.deleteTodo(event.todo)
                }

            }
            TodoEvent.hideDialog -> {
                _state.update {
                    it.copy(showDialog = false)
                }
            }
            TodoEvent.saveTodo -> {

                val title = state.value.title
                val description = state.value.description
                val priority = state.value.priority
                val dueDate = state.value.dueDate
                val dueTime = state.value.dueTime

                if (title.isBlank() || description.isBlank()){
                    return
                }

                val todo = Todo(
                    title = title,
                    description = description,
                    priority = priority,
                    dueDate = dueDate,
                    dueTime = dueTime,


                )

                viewModelScope.launch {
                    todoDao.insertTodo(todo)
                }

                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        priority = Priority.LOW,
                        dueDate = "",
                        dueTime = "",
                        showDialog = false
                    )
                }
            }
            is TodoEvent.setDescription -> {
                _state.update {
                    it.copy(description = event.description)
                }
            }
            is TodoEvent.setDueDate -> {
                _state.update {
                    it.copy(dueDate = event.dueDate)
                }
            }
            is TodoEvent.setDueTime -> {
                _state.update {
                    it.copy(dueTime = event.dueTime)
                }
            }
            is TodoEvent.setPriority -> {
                _state.update {
                    it.copy(priority = event.priority)
                }
            }
            is TodoEvent.setTitle -> {
                _state.update {
                    it.copy(title = event.title)
                }
            }
            TodoEvent.showDialog -> {
                _state.update {
                    it.copy(showDialog = true)
                }
            }
            is TodoEvent.sortBy -> {
                _sortType.value = event.sortType
            }
            TodoEvent.hideEditTodoDialog -> {
                _state.update {
                    it.copy(showEditTodoDialog = false)
                }
            }
            TodoEvent.showEditTodoDialog -> {
                _state.update {
                    it.copy(showEditTodoDialog = true)
                }
            }
            TodoEvent.hideDateSelector -> {
                _state.update {
                    it.copy(showDateSelector = false)
                }
            }
            TodoEvent.showDateSelector -> {
                _state.update {
                    it.copy(showDateSelector = true)
                }
            }
            TodoEvent.hideTimeSelector -> {
                _state.update {
                    it.copy(showTimeSelector = false)
                }
            }
            TodoEvent.showTimeSelector -> {
                _state.update {
                    it.copy(showTimeSelector = true)
                }
            }
            else -> {}
        }
    }


// Gets called in MainActivity
    /**
     * getCountOfCompletedTodos() returns the number of tasks assigned as completed
     * getTotalRowCount() returns the total number of rows
     */
    suspend fun getCountOfCompletedTodos(): Int {
        val num = todoDao.getCountOfCompletedTodos()
        return num
    }
    suspend fun getTotalRowCount(): Int {
        var todos  = todoDao.getAllTodos() - getCountOfCompletedTodos()
        return todos.size
    }


}