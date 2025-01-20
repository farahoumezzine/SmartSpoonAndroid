package com.example.smartspoon.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.smartspoon.model.GroceryItem

class GroceryDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "grocery_db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "grocery_items"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_IS_FAVORITE = "is_favorite"
        private const val COLUMN_IS_CHECKED = "is_checked"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_IS_FAVORITE INTEGER,
                $COLUMN_IS_CHECKED INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addItem(item: GroceryItem): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, item.name)
            put(COLUMN_IS_FAVORITE, if (item.isFavorite) 1 else 0)
            put(COLUMN_IS_CHECKED, if (item.isChecked) 1 else 0)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllItems(): List<GroceryItem> {
        val items = mutableListOf<GroceryItem>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val item = GroceryItem(
                    id = getLong(getColumnIndexOrThrow(COLUMN_ID)),
                    name = getString(getColumnIndexOrThrow(COLUMN_NAME)),
                    isFavorite = getInt(getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1,
                    isChecked = getInt(getColumnIndexOrThrow(COLUMN_IS_CHECKED)) == 1
                )
                items.add(item)
            }
        }
        cursor.close()
        return items
    }

    fun updateItem(item: GroceryItem): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, item.name)
            put(COLUMN_IS_FAVORITE, if (item.isFavorite) 1 else 0)
            put(COLUMN_IS_CHECKED, if (item.isChecked) 1 else 0)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(item.id.toString()))
    }

    fun deleteItem(itemId: Long): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(itemId.toString()))
    }
} 