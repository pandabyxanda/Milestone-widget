package com.example.milestone_widget.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
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
                FOREIGN KEY($ITEM_ID_COL) REFERENCES $ITEM_TABLE_NAME($ITEM_ID_COL)
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

    fun addItem(name: String, shortName: String?, description: String?) {
        val values = ContentValues().apply {
            put(NAME_COL, name)
            put(SHORT_NAME_COL, shortName)
            put(DESCRIPTION_COL, description)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDate = sdf.format(Date())
            put(DATE_CREATED_COL, currentDate)
            put(ACTIVE_COL, 1) // Set Active to True by default
        }

        val db = this.writableDatabase
        db.insert(ITEM_TABLE_NAME, null, values)
        db.close()
    }

    fun addItemAction(itemId: Int) {
        val values = ContentValues().apply {
            put(ITEM_ID_COL, itemId)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDate = sdf.format(Date())
            put(DATE_COL, currentDate)
        }

        val db = this.writableDatabase
        db.insert(ITEM_ACTIONS_TABLE_NAME, null, values)
        db.close()
    }

    fun getAllItems(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $ITEM_TABLE_NAME", null)
    }

    fun getActionsByItemId(itemId: Int): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM $ITEM_ACTIONS_TABLE_NAME WHERE $ITEM_ID_COL = ?",
            arrayOf(itemId.toString())
        )
    }

    fun getActionCountByItemId(itemId: Int): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM $ITEM_ACTIONS_TABLE_NAME WHERE $ITEM_ID_COL = ?",
            arrayOf(itemId.toString())
        )
        var count = 0
        cursor?.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
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
        db.update(ITEM_TABLE_NAME, values, "$ITEM_ID_COL = ?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteItem(itemId: Int) {
        val db = this.writableDatabase
        db.delete(ITEM_TABLE_NAME, "$ITEM_ID_COL = ?", arrayOf(itemId.toString()))
        db.close()
    }

    fun deleteAction(actionId: Int) {
        val db = this.writableDatabase
        db.delete(ITEM_ACTIONS_TABLE_NAME, "$ACTION_ID_COL = ?", arrayOf(actionId.toString()))
        db.close()
    }

    companion object {
        private const val DATABASE_NAME = "milestone_widget_db"
        private const val DATABASE_VERSION = 2

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
    }
}
