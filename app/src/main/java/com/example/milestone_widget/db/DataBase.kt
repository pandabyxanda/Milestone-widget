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
                $DATE_CREATED_COL TEXT
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
        return db.rawQuery("SELECT * FROM $ITEM_ACTIONS_TABLE_NAME WHERE $ITEM_ID_COL = ?", arrayOf(itemId.toString()))
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

        const val ITEM_ACTIONS_TABLE_NAME = "item_actions_table"
        const val ACTION_ID_COL = "action_id"
        const val DATE_COL = "date"
    }
}


//class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
//    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
//    override fun onCreate(db: SQLiteDatabase) {
//        val query = ("CREATE TABLE " + TABLE_NAME + " ("
//                + ID_COL + " INTEGER PRIMARY KEY, " +
//                NAME_COl + " TEXT," +
//                AGE_COL + " TEXT," +
//                TIMESTAMP + " TEXT" + ")")
//        db.execSQL(query)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
//        onCreate(db)
//    }
//
//    fun addName(name: String, age: String) {
//        val values = ContentValues()
//        values.put(NAME_COl, name)
//        values.put(AGE_COL, age)
//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//        val currentDate = sdf.format(Date())
//        values.put(TIMESTAMP, currentDate)
//
////        val currentTime = System.currentTimeMillis()
////        values.put(TIMESTAMP, currentTime)
////        values.put(TIMESTAMP, "datetime('now')")
//        val db = this.writableDatabase
//        db.insert(TABLE_NAME, null, values)
//        db.close()
//    }
//
//    fun getName(): Cursor? {
//        val db = this.readableDatabase
//        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
//
//    }
//    fun getLastRow(): Cursor? {
//        val db = this.readableDatabase
//        return db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $ID_COL DESC LIMIT 1", null)
//    }
//    fun getLastRows(x: Int): Cursor? {
//        val db = this.readableDatabase
//        return db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $ID_COL DESC LIMIT $x", null)
//    }
//
//    fun getCountByName(name: String): Int {
//        val db = this.readableDatabase
//        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME WHERE $NAME_COl = ?", arrayOf(name))
//        var count = 0
//        if (cursor.moveToFirst()) {
//            count = cursor.getInt(0)
//        }
//        cursor.close()
//        return count
//    }
//
//    companion object {
//        // here we have defined variables for our database
//        private val DATABASE_NAME = "milestone_widget_db"
//        private val DATABASE_VERSION = 1
//        val TABLE_NAME = "main_table"
//        val ID_COL = "id"
//        val NAME_COl = "name"
//        val AGE_COL = "age"
//        val TIMESTAMP = "timestamp"
//    }
//}


//class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
//    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
//
//    // below is the method for creating a database by a sqlite query
//    override fun onCreate(db: SQLiteDatabase) {
//        // below is a sqlite query, where column names
//        // along with their data types is given
//        val query = ("CREATE TABLE " + TABLE_NAME + " ("
//                + ID_COL + " INTEGER PRIMARY KEY, " +
//                NAME_COl + " TEXT," +
//                AGE_COL + " TEXT" + ")")
//
//        // we are calling sqlite
//        // method for executing our query
//        db.execSQL(query)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
//        // this method is to check if table already exists
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
//        onCreate(db)
//    }
//
//    // This method is for adding data in our database
//    fun addName(name : String, age : String ){
//
//        // below we are creating
//        // a content values variable
//        val values = ContentValues()
//
//        // we are inserting our values
//        // in the form of key-value pair
//        values.put(NAME_COl, name)
//        values.put(AGE_COL, age)
//
//        // here we are creating a
//        // writable variable of
//        // our database as we want to
//        // insert value in our database
//        val db = this.writableDatabase
//
//        // all values are inserted into database
//        db.insert(TABLE_NAME, null, values)
//
//        // at last we are
//        // closing our database
//        db.close()
//    }
//
//    // below method is to get
//    // all data from our database
//    fun getName(): Cursor? {
//
//        // here we are creating a readable
//        // variable of our database
//        // as we want to read value from it
//        val db = this.readableDatabase
//
//        // below code returns a cursor to
//        // read data from the database
//        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
//
//    }
//
//    companion object{
//        // here we have defined variables for our database
//
//        // below is variable for database name
//        private val DATABASE_NAME = "GEEKS_FOR_GEEKS"
//
//        // below is the variable for database version
//        private val DATABASE_VERSION = 1
//
//        // below is the variable for table name
//        val TABLE_NAME = "gfg_table"
//
//        // below is the variable for id column
//        val ID_COL = "id"
//
//        // below is the variable for name column
//        val NAME_COl = "name"
//
//        // below is the variable for age column
//        val AGE_COL = "age"
//    }
//}
