package com.csu_itc303_team1.adhdtaskmanager.ui.edit_todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.data.Tag
import com.csu_itc303_team1.adhdtaskmanager.data.TodoDetail
import com.csu_itc303_team1.adhdtaskmanager.data.TodoStatus
import com.csu_itc303_team1.adhdtaskmanager.data.User
import com.csu_itc303_team1.adhdtaskmanager.usecase.LoadTagsUseCase
import com.csu_itc303_team1.adhdtaskmanager.usecase.LoadTodoDetailUseCase
import com.csu_itc303_team1.adhdtaskmanager.usecase.LoadUsersUseCase
import com.csu_itc303_team1.adhdtaskmanager.usecase.SaveTodoDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class TodoEditViewModel @Inject constructor(
    private val loadTodoDetailUseCase: LoadTodoDetailUseCase,
    private val loadUsersUseCase: LoadUsersUseCase,
    private val loadTagsUseCase: LoadTagsUseCase,
    private val saveTodoDetailUseCase: SaveTodoDetailUseCase,
    private val currentUser: User
) : ViewModel() {

    var todoId: Long = 0L
        set(value) {
            field = value
            loadInitialData(value)
        }

    val title = MutableStateFlow("")
    private var initialTitle = ""

    val description = MutableStateFlow("")
    private var initialDescription = ""

    private val _status = MutableStateFlow(TodoStatus.NOT_STARTED)
    val status: StateFlow<TodoStatus> = _status

    private val _creator = MutableStateFlow(currentUser)
    val creator: StateFlow<User> = _creator

    val _dueAt = MutableStateFlow(Instant.now() + Duration.ofDays(7))
    val dueAt: StateFlow<Instant> = _dueAt

    val _createdAt = MutableStateFlow(Instant.now())
    val createdAt: StateFlow<Instant> = _createdAt

    private var orderInCategory = 0

    // Whether this task should be placed on top of the status when it is saved.
    // Creating a new task puts it at the top of the status.
    private var topInCategory = true

    // This is not editable, but needed for when we save.
    private var isArchived = false

    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags: StateFlow<List<Tag>> = _tags

    var starUsers = mutableListOf<User>()

    lateinit var users: List<User>
        private set

    lateinit var allTags: List<Tag>
        private set

    val _discarded = Channel<Unit>(capacity = Channel.CONFLATED)
    val discarded = _discarded.receiveAsFlow()

    /**
     * Whether any of the content is modified or not.
     */
    private val _modified = MutableStateFlow(false)
    val modified: StateFlow<Boolean> = _modified

    private var modifiedTitleDescriptionJob: Job = viewModelScope.launch {
        combine(title, description) { title, description ->
            title != initialTitle || description != initialDescription
        }.collect { modified ->
            // Other fields affect modification, so we never go from true to false.
            if (modified) {
                _modified.value = true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        modifiedTitleDescriptionJob.cancel()
    }

    private fun loadInitialData(todoId: Long) {
        viewModelScope.launch {
            users = loadUsersUseCase()
            allTags = loadTagsUseCase()
            if (todoId != 0L) {
                val detail = loadTodoDetailUseCase(todoId)
                if (detail != null) {
                    initialTitle = detail.title
                    initialDescription = detail.description

                    title.value = initialTitle
                    description.value = initialDescription
                    _status.value = detail.status
                    _creator.value = detail.creator
                    _dueAt.value = detail.dueAt
                    _createdAt.value = detail.createdAt
                    orderInCategory = detail.orderInCategory
                    isArchived = detail.isArchived
                    topInCategory = false
                    _tags.value = detail.tags
                    starUsers.clear()
                    starUsers.addAll(detail.starUsers)
                    _modified.value = false
                }
            }
        }
    }

    fun updateState(status: TodoStatus) {
        if (_status.value != status) {
            _status.value = status
            _modified.value = true
            // The task is placed at the top of the target status.
            topInCategory = true
        }
    }


    fun updateDueAt(instant: Instant) {
        _dueAt.value = instant
        _modified.value = true
    }

    fun addTag(tag: Tag) {
        _tags.value.let { currentTags ->
            if (!currentTags.contains(tag)) {
                _tags.value = currentTags + tag
                _modified.value = true
            }
        }
    }

    fun removeTag(tag: Tag) {
        _tags.value.let { currentTags ->
            if (currentTags.contains(tag)) {
                val tags = currentTags.toMutableList()
                tags.remove(tag)
                _tags.value = tags
                _modified.value = true
            }
        }
    }

    fun save(onSaveFinished: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                saveTodoDetailUseCase(
                    TodoDetail(
                        id = todoId,
                        title = title.value,
                        description = description.value,
                        status = _status.value,
                        createdAt = _createdAt.value,
                        dueAt = _dueAt.value,
                        orderInCategory = orderInCategory,
                        isArchived = isArchived,
                        creator = _creator.value,
                        tags = _tags.value,
                        starUsers = starUsers
                    ),
                    topInCategory
                )
                onSaveFinished(true)
            } catch (e: RuntimeException) {
                e.printStackTrace()
                onSaveFinished(false)
            }
        }
    }

    fun discardChanges() {
        _discarded.trySend(Unit)
    }
}