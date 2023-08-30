package com.csu_itc303_team1.adhdtaskmanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.csu_itc303_team1.adhdtaskmanager.R
import java.time.Duration
import java.time.Instant

@Entity(
    tableName = "todos",
    foreignKeys = [
        ForeignKey(
            childColumns = ["creatorId"],
            entity = User::class,
            parentColumns = ["id"]
        ),

    ],
    indices = [
        Index("creatorId"),
    ]
)
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val description: String = "",
    val status: TodoStatus = TodoStatus.NOT_STARTED,
    val creatorId: Long,
    val createdAt: Instant = Instant.now(),
    val dueAt: Instant = Instant.now() + Duration.ofDays(7),
    val orderInCategory: Int,

    @ColumnInfo(defaultValue = "0")
    val isArchived: Boolean = false
)

enum class TodoStatus(val key: Int, val stringResId: Int) {
    NOT_STARTED(1, R.string.not_started),
    IN_PROGRESS(2, R.string.in_progress),
    COMPLETED(3, R.string.completed);

    companion object {
        private val map = values().associateBy(TodoStatus::key)
        fun fromKey(key: Int) = map[key]
    }
}

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey
    val id: Long,
    val label: String,
    val color: TagColor
)

enum class TagColor(val textColor: Int, val backgroundColor: Int) {
    BLUE(R.attr.blueTagTextColor, R.attr.blueTagBackgroundColor),
    GREEN(R.attr.greenTagTextColor, R.attr.greenTagBackgroundColor),
    PURPLE(R.attr.purpleTagTextColor, R.attr.purpleTagBackgroundColor),
    RED(R.attr.redTagTextColor, R.attr.redTagBackgroundColor),
    TEAL(R.attr.tealTagTextColor, R.attr.tealTagBackgroundColor),
    YELLOW(R.attr.yellowTagTextColor, R.attr.yellowTagBackgroundColor),
}

enum class Avatar(val drawableResId: Int) {
    USER_PROFILE_PICTURE(R.drawable.ic_user_profile_picture)
}

@Entity(
    tableName = "todo_tags",
    foreignKeys = [
        ForeignKey(
            childColumns = ["todoId"],
            entity = Todo::class,
            parentColumns = ["id"]
        ),
        ForeignKey(
            childColumns = ["tagId"],
            entity = Tag::class,
            parentColumns = ["id"]
        )
    ],
    indices = [
        Index(value = ["todoId", "tagId"], unique = true),
        Index("todoId"),
        Index("tagId")
    ]
)
data class TodoTag(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val todoId: Long,
    val tagId: Long
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Long,
    val username: String,
    val avatar: Avatar
)

@Entity(
    tableName = "user_todos",
    foreignKeys = [
        ForeignKey(
            childColumns = ["userId"],
            entity = User::class,
            parentColumns = ["id"]
        ),
        ForeignKey(
            childColumns = ["todoId"],
            entity = Todo::class,
            parentColumns = ["id"]
        )
    ],
    indices = [
        Index(value = ["userId", "todoId"], unique = true),
        Index("userId"),
        Index("todoId")
    ]
)
data class UserTodo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val todoId: Long
)