package com.example.pizzaapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Toast
import com.example.pizzaapp.model.MenuModel
import com.google.gson.internal.bind.DateTypeAdapter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class DatabaseHelper(var context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object{
        private val DATABASE_NAME = "pizza"
        private val DATABASE_VERSION = 1
        //table name
        private val TABLE_ACCOUNT = "account"
        //column account table
        private val COLUMN_EMAIL = "account"
        private val COLUMN_NAME = "name"
        private val COLUMN_LEVEL = "level"
        private val COLUMN_PASSWORD = "password"

        //table menu
        private val TABLE_MENU = "menu"
        //column menu table
        private val  COLUMN_ID_MENU = "idMenu"
        private val  COLUMN_NAMA_MENU = "menuName"
        private val  COLUMN_PRICE_MENU = "price"
        private val  COLUMN_IMAGE = "photo"
    }

    //create table account sql query
    private val CREATE_ACCOUNT_TABLE = ("CREATE TABLE " + TABLE_ACCOUNT + "(" + COLUMN_EMAIL + " TEXT PRIMARY KEY, "+ COLUMN_NAME +" TEXT, "+ COLUMN_LEVEL + " TEXT, "+ COLUMN_PASSWORD + " TEXT)")

    //DROP TABLE ACCOUNT SQL QUERY
    private val DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS $TABLE_ACCOUNT"

    //create table menu
    private val CREATE_MENU_TABLE = ("CREATE TABLE " + TABLE_MENU + "(" + COLUMN_ID_MENU + " TEXT PRIMARY KEY, "+ COLUMN_NAMA_MENU +" TEXT, "+ COLUMN_PRICE_MENU + " INT, "+ COLUMN_IMAGE + " BLOB)")

    //DROP TABLE ACCOUNT SQL QUERY
    private val DROP_MENU_TABLE = "DROP TABLE IF EXISTS $TABLE_MENU"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_ACCOUNT_TABLE)
        p0?.execSQL(CREATE_MENU_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_ACCOUNT_TABLE)
        p0?.execSQL(DROP_MENU_TABLE)
        onCreate(p0)
    }

    //login check
    fun checkLogin(email:String, password:String):Boolean{
        val columns = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase
        //selection criteria
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        //selection argument
        val selectionArgs = arrayOf(email,password)

        val cursor = db.query(TABLE_ACCOUNT, //tabble query
        columns, //columns returns
        selection, //columns for where clause
        selectionArgs, //the values for the where clause
        null,
            null,
            null
        )

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        //check data available or not
        if (cursorCount > 0)
            return true
        else
            return false
    }

    fun addAccount (email:String, name:String, level:String, password:String) {
        val db = this.readableDatabase

        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_LEVEL, level)
        values.put(COLUMN_PASSWORD, password)

       val result = db.insert(TABLE_ACCOUNT, null, values)
        //show massage
        if (result == (0).toLong()) {
            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Register Success, " + "Please login using your new account", Toast.LENGTH_SHORT).show()
        }
       db.insert(TABLE_ACCOUNT, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun checkData(email:String):String {
        val columns = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase
        //selection criteria
        val selection = "$COLUMN_EMAIL = ?"
        //selection argument
        val selectionArgs = arrayOf(email)
        var name: String = ""

        val cursor = db.query(
            TABLE_ACCOUNT, //tabble query
            columns, //columns returns
            selection, //columns for where clause
            selectionArgs, //the values for the where clause
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        }

        cursor.close()
        db.close()
        return name

    }

    fun addMenu(menu: MenuModel){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID_MENU, menu.id)
        values.put(COLUMN_NAMA_MENU, menu.name)
        values.put(COLUMN_PRICE_MENU, menu.price)
        //prepare image
        val byteOutputStream = ByteArrayOutputStream()
        val imageInByte:ByteArray
        menu.image.compress(Bitmap.CompressFormat.JPEG, 100,byteOutputStream)
        imageInByte = byteOutputStream.toByteArray()
        values.put(COLUMN_IMAGE, imageInByte)

        val result = db.insert(TABLE_MENU, null, values)
        //show message
        if(result==(0).toLong()){
            Toast.makeText(context, "Add Menu Failed", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Add Menu Success", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun editMenu(menu: MenuModel){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID_MENU, menu.id)
        values.put(COLUMN_NAMA_MENU, menu.name)
        values.put(COLUMN_PRICE_MENU, menu.price)
        //prepare image
        val byteOutputStream = ByteArrayOutputStream()
        val imageInByte:ByteArray
        menu.image.compress(Bitmap.CompressFormat.JPEG, 100,byteOutputStream)
        imageInByte = byteOutputStream.toByteArray()
        values.put(COLUMN_IMAGE, imageInByte)

        val result = db.update(TABLE_MENU,values, COLUMN_ID_MENU +" = ? ", arrayOf(menu.id.toString())).toLong()
        //show message
        if(result==(0).toLong()){
            Toast.makeText(context, "Add Menu Failed", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Add Menu Success", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun deleteMenu (id:Int) {
        val db = this.writableDatabase
        val result = db.delete(TABLE_MENU, COLUMN_ID_MENU + " = ?", arrayOf(id.toString())).toLong()
        //show message
        if(result==(0).toLong()){
            Toast.makeText(context, "Add Menu Failed", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Add Menu Success", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    @SuppressLint("Range")
    fun showMenu() : ArrayList<MenuModel> {
        val listModel = ArrayList<MenuModel>()
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_MENU", null)
        } catch (se: SQLiteException) {
            db.execSQL(CREATE_MENU_TABLE)
            return ArrayList()
        }

        var id:Int
        var name: String
        var price: Int
        var imageArray: ByteArray
        var image: Bitmap

        if(cursor.moveToFirst()) {
            do {
                //get data text
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_MENU))
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_MENU))
                price = cursor.getInt(cursor.getColumnIndex(COLUMN_PRICE_MENU))
                //get data image
                imageArray = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE))
                //convert bytearray to bitmap
                val byteInputStream = ByteArrayInputStream(imageArray)
                val imageBmp = BitmapFactory.decodeStream(byteInputStream)
                val model = MenuModel(id = id, name = name, price = price, image = imageBmp)
                listModel.add(model)
            }while (cursor.moveToNext())
        }
        return listModel
    }

}