package com.example.milestone_widget.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DataBase(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createItemTableQuery = """
            CREATE TABLE $ITEM_TABLE_NAME (
                $ITEM_ID_COL INTEGER PRIMARY KEY AUTOINCREMENT,
                $NAME_COL TEXT NOT NULL,
                $SHORT_NAME_COL TEXT,
                $DESCRIPTION_COL TEXT,
                $DATE_CREATED_COL TEXT,
                $ACTIVE_COL INTEGER DEFAULT 1
            )
        """.trimIndent()

        val createItemActionsTableQuery = """
            CREATE TABLE $ITEM_ACTIONS_TABLE_NAME (
                $ACTION_ID_COL INTEGER PRIMARY KEY AUTOINCREMENT,
                $ITEM_ID_COL INTEGER,
                $DATE_COL TEXT,
                FOREIGN KEY($ITEM_ID_COL) REFERENCES $ITEM_TABLE_NAME($ITEM_ID_COL) ON DELETE CASCADE
            )
        """.trimIndent()

        db.execSQL(createItemTableQuery)
        db.execSQL(createItemActionsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $ITEM_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $ITEM_ACTIONS_TABLE_NAME")
        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    fun addItem(name: String, shortName: String?, description: String?) {
        val values = ContentValues().apply {
            put(NAME_COL, name)
            put(SHORT_NAME_COL, shortName)
            put(DESCRIPTION_COL, description)
            val currentDate = getCurrentDate()
            put(DATE_CREATED_COL, currentDate)
            put(ACTIVE_COL, 1)
        }

        val db = this.writableDatabase
        db.use { database ->
            database.insert(ITEM_TABLE_NAME, null, values)
        }
    }

    fun addItemAction(itemId: Int) {
        val values = ContentValues().apply {
            put(ITEM_ID_COL, itemId)
            val currentDate = getCurrentDate()
            put(DATE_COL, currentDate)
        }

        val db = this.writableDatabase
        db.use { database ->
            database.insert(ITEM_ACTIONS_TABLE_NAME, null, values)
        }
    }

    fun getAllItems(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $ITEM_TABLE_NAME", null)
    }


    fun getActionsByItemIdAndDate(itemId: Int, date: String, startHour: String = "06"): Cursor? {
        val db = this.readableDatabase
        val startDateStr = "$date $startHour:00:00"
        val dateStart = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateStr)
        if (dateStart != null) {
            val calendar = Calendar.getInstance().apply {
                time = dateStart
                add(Calendar.DAY_OF_MONTH, 1)
                add(Calendar.SECOND, -1)
            }
            val dateEnd = calendar.time
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val endDateStr = formatter.format(dateEnd)
            return db.rawQuery(
                "SELECT * FROM $ITEM_ACTIONS_TABLE_NAME WHERE $ITEM_ID_COL = ? AND $DATE_COL BETWEEN ? AND ?",
                arrayOf(itemId.toString(), startDateStr, endDateStr)
            )
        } else {
            return null
        }
    }

    fun getActionCountByItemIdAndDate(itemId: Int, date: String, startHour: String = "06"): Int {
        val db = this.readableDatabase
        val startDateStr = "$date $startHour:00:00"
        val dateStart = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateStr)

        var count = 0
        if (dateStart != null) {
            val calendar = Calendar.getInstance().apply {
                time = dateStart
                add(Calendar.DAY_OF_MONTH, 1)
                add(Calendar.SECOND, -1)
            }
            val dateEnd = calendar.time
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val endDateStr = formatter.format(dateEnd)
            val cursor = db.rawQuery(
                "SELECT COUNT(*) FROM $ITEM_ACTIONS_TABLE_NAME WHERE $ITEM_ID_COL = ? AND $DATE_COL BETWEEN ? AND ?",
                arrayOf(itemId.toString(), startDateStr, endDateStr)
            )
            cursor.use {
                if (it.moveToFirst()) {
                    count = it.getInt(0)
                }
            }
        }
        return count
    }

    fun updateItem(id: Int, name: String, shortName: String, description: String, isActive: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(NAME_COL, name)
            put(SHORT_NAME_COL, shortName)
            put(DESCRIPTION_COL, description)
            put(ACTIVE_COL, isActive)
        }
        db.use { database ->
            database.update(ITEM_TABLE_NAME, values, "$ITEM_ID_COL = ?", arrayOf(id.toString()))
        }
    }

    fun deleteItem(itemId: Int) {
        val db = this.writableDatabase
        db.use { database ->
            database.delete(ITEM_TABLE_NAME, "$ITEM_ID_COL = ?", arrayOf(itemId.toString()))
        }
    }

    fun deleteAction(actionId: Int) {
        val db = this.writableDatabase
        db.use { database ->
            database.delete(
                ITEM_ACTIONS_TABLE_NAME,
                "$ACTION_ID_COL = ?",
                arrayOf(actionId.toString())
            )
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return sdf.format(Date())
    }

    companion object {
        private const val DATABASE_NAME = "milestone_widget_db"
        private const val DATABASE_VERSION = 1

        const val ITEM_TABLE_NAME = "item_table"
        const val ITEM_ID_COL = "item_id"
        const val NAME_COL = "name"
        const val SHORT_NAME_COL = "short_name"
        const val DESCRIPTION_COL = "description"
        const val DATE_CREATED_COL = "date_created"
        const val ACTIVE_COL = "active"

        const val ITEM_ACTIONS_TABLE_NAME = "item_actions_table"
        const val ACTION_ID_COL = "action_id"
        const val DATE_COL = "date"

        private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }
}