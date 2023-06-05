package com.csu_itc303_team1.adhdtaskmanager.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.view.Gravity
import android.widget.Toast

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "TASK_TIMER"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "timers"
        const val ID_COL = "id"
        const val TASK_COL = "task"
        const val TIME_COL = "time"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " (" + ID_COL + " INTEGER PRIMARY KEY, " +
                TASK_COL + " TEXT," +
                TIME_COL + " TEXT" + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTimer(task: String?, timer: String?) {
        val values = ContentValues()
        values.put(TASK_COL, task)
        values.put(TIME_COL, timer)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()

    }

    fun updateTimer(task: String?, timer: String?) {
        val values = ContentValues()
        values.put(TASK_COL, task)
        values.put(TIME_COL, timer)

        val db = this.writableDatabase
        db.update(TABLE_NAME, values, "$TIME_COL = ?", arrayOf(task))
        db.close()
    }

    fun deleteTimer(task: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$TASK_COL = ?", arrayOf(task))
        db.close()
    }

    fun getTimer(task: String?): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $TASK_COL = '$task'", null)
    }

}