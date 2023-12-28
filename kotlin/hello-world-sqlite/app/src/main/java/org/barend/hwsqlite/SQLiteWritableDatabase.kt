package org.barend.hwsqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteWritableDatabase(context: Context, databaseName : String, schema : Array<String>) :
    SQLiteOpenHelper(context, databaseName, null, 1)
{
    private val schema : Array<String>

    init
    {
        this.schema = schema
    }

    override fun onCreate(db: SQLiteDatabase) {
        for (sql in schema)
        {
            db.execSQL(sql)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // Here a database upgrade might be implemented
    }

    fun insert(tableName : String, row : ContentValues)
    {
        val db = this.writableDatabase
        db.insert(tableName, null, row)
        db.close()
    }

    // Demonstrates the usage of factory with binding with LONG
    // (for generic bindings more work would be necessary)
    fun select(sql : String, values : Array<Long>, processRow : (cursor : Cursor) -> Unit)
    {
        val db = this.readableDatabase
        val cursor = db.rawQueryWithFactory({ _, masterQuery, editTable, query ->
            for ((index, value) in values.withIndex())
            {
                query.bindLong(index + 1, value)
            }
            SQLiteCursor(masterQuery, editTable, query)
        }, sql, null, "")

        while (cursor.moveToNext())
        {
            processRow(cursor)
        }
        cursor.close()
    }

    fun select(sql : String, processRow : (cursor : Cursor) -> Unit)
    {
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)

        while (cursor.moveToNext())
        {
            processRow(cursor)
        }
        cursor.close()
    }
}
