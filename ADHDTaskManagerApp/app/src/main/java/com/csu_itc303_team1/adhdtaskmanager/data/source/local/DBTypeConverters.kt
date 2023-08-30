package com.csu_itc303_team1.adhdtaskmanager.data.source.local

import androidx.room.TypeConverter
import com.csu_itc303_team1.adhdtaskmanager.data.Avatar
import com.csu_itc303_team1.adhdtaskmanager.data.TagColor
import com.csu_itc303_team1.adhdtaskmanager.data.TodoStatus
import java.time.Instant

class DBTypeConverters {

    @TypeConverter
    fun instantToLong(value: Instant): Long {
        return value.toEpochMilli()
    }

    @TypeConverter
    fun longToInstant(value: Long): Instant {
        return Instant.ofEpochMilli(value)
    }

    @TypeConverter
    fun todoStatusToInt(todoStatus: TodoStatus?): Int? {
        return todoStatus?.key
    }

    @TypeConverter
    fun intToTodoStatus(int: Int): TodoStatus? {
        return TodoStatus.fromKey(int)
    }

    @TypeConverter
    fun tagColorToString(color: TagColor?): String? {
        return color?.name
    }

    @TypeConverter
    fun stringToTagColor(string: String): TagColor {
        return TagColor.valueOf(string)
    }

    @TypeConverter
    fun avatarToString(avatar: Avatar): String {
        return avatar.name
    }

    @TypeConverter
    fun stringToAvatar(name: String): Avatar? {
        return Avatar.values().firstOrNull{ it.name == name}
    }
}