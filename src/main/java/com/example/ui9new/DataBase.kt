package com.example.ui9new

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context : Context) : SQLiteOpenHelper(context,"PC_DB",null,5) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE PC_Table (id INT PRIMARY KEY, name TEXT, prix TEXT, image TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS PC_Table")
        onCreate(db)
    }

    fun Id_Exist(id: Int): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT name FROM PC_Table WHERE id = ?", arrayOf(id.toString()))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun updateItem(pc: Pc): Int {
        val values = ContentValues()
        values.put("name", pc.name)
        values.put("prix", pc.prix)
        values.put("image", pc.image)

        val db = this.writableDatabase
        return db.update("PC_Table", values, "id=?", arrayOf(pc.id.toString()))
    }

    fun deleteItem(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("PC_Table", "id=?", arrayOf(id.toString()))
    }

    fun Additem(pc: Pc): Long {
        val values = ContentValues()
        val db = this.writableDatabase
        // Check if the id already exists
        if (!Id_Exist(pc.id)) {
            values.put("id", pc.id)
            values.put("name", pc.name)
            values.put("prix", pc.prix)
            values.put("image", pc.image)

            // Insert the new record
            val inserted = db.insert("PC_Table", null, values)

            return inserted
        } else {
            // Handle the case where the id already exists (you can update the existing record or take other actions)
            db.close()
            return -1
        }
    }

    fun getItem(id: Int): Pc {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM PC_Table WHERE id = ?", arrayOf(id.toString()))

        val pc: Pc

        if (cursor.moveToFirst()) {
            val itemId = cursor.getInt(0)
            val name = cursor.getString(1)
            val prix = cursor.getString(2)
            val image = cursor.getString(3)

            pc = Pc(itemId, name, prix, image)
        } else {
            // Handle the case where the item with the specified ID is not found
            pc = Pc(-1, "", "", "")
        }

        cursor.close()
        db.close()

        return pc
    }


    fun getAll():ArrayList<Pc> {

        var system_List = ArrayList<Pc>()

        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM PC_Table", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val prix = cursor.getString(2)
                val image = cursor.getString(3)

                val app = Pc( id, name, prix, image)

                system_List.add(app)

            } while (cursor.moveToNext())
        }

        db.close()
        return system_List
    }

}