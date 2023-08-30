package com.csu_itc303_team1.adhdtaskmanager.data

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.csu_itc303_team1.adhdtaskmanager.data.Tag
import com.csu_itc303_team1.adhdtaskmanager.data.TodoStatus
import com.csu_itc303_team1.adhdtaskmanager.data.TodoTag
import com.csu_itc303_team1.adhdtaskmanager.data.User
import com.csu_itc303_team1.adhdtaskmanager.data.UserTodo
import java.time.Instant

@DatabaseView(
    """
        SELECT
            t.id, t.title, t.status, t.dueAt, t.orderInCategory, t.isArchived,
            o.id AS owner_id, o.username AS owner_username, o.avatar AS owner_avatar
        FROM todos AS t
        INNER JOIN users AS o ON o.id = t.creatorId
    """
)
data class TodoSummary(
    val id: Long,

    val title: String,

    val status: TodoStatus,

    val dueAt: Instant,

    val orderInCategory: Int,

    val isArchived: Boolean = false,

    @Embedded(prefix = "owner_")
    val owner: User,

    @Relation(
        parentColumn = "id",
        entity = Tag::class,
        entityColumn = "id",
        associateBy = Junction(
            value = TodoTag::class,
            parentColumn = "todoId",
            entityColumn = "tagId"
        )
    )
    val tags: List<Tag>,

    val starred: Boolean,
)

@DatabaseView(
    """
        SELECT
            t.id, t.title, t.description, t.status, t.createdAt, t.dueAt, t.orderInCategory,
            t.isArchived,
            c.id AS creator_id, c.username AS creator_username, c.avatar as creator_avatar
        FROM todos AS t
        INNER JOIN users AS c ON c.id = t.creatorId
    """
)
data class TodoDetail(

    val id: Long,

    val title: String,

    val description: String,

    val status: TodoStatus,

    val createdAt: Instant,

    val dueAt: Instant,

    val orderInCategory: Int,

    val isArchived: Boolean,

    @Embedded(prefix = "creator_")
    val creator: User,

    @Relation(
        parentColumn = "id",
        entity = Tag::class,
        entityColumn = "id",
        associateBy = Junction(
            value = TodoTag::class,
            parentColumn = "todoId",
            entityColumn = "tagId"
        )
    )
    val tags: List<Tag>,

    @Relation(
        parentColumn = "id",
        entity = User::class,
        entityColumn = "id",
        associateBy = Junction(
            value = UserTodo::class,
            parentColumn = "todoId",
            entityColumn = "userId"
        )
    )
    val starUsers: List<User>
)
