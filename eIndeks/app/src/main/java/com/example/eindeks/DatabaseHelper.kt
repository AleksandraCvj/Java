package com.example.eindeks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    lateinit var db: SQLiteDatabase

//    //this is good praxis for code organization
//    //it is similar as creating static variables in Java
//    //you can acces them like this val databaseName = DatabaseHelper.DATABASE_NAME
    companion object {
        private const val DATABASE_NAME = "eIndeks.db"
        private const val DATABASE_VERSION = 1

    }

    private val LOG = "DatabaseHelper"


    //TABLE NAMES
    object TableNames {
        const val TABLE_STUDENTS = "students"
        // Add more table names as needed
    }

    //Common column names
    private val KEY_ID = "id"
    private val KEY_NAME = "name"

    //TABLE_STUDENTS - column names
    private val KEY_SURNAME = "surname"
    private val KEY_INDEKS = "indeks"
    private val KEY_JMBG= "jmbg"
    private val KEY_USERNAME= "username"
    private val KEY_PASSWORD = "password"

    //Student object
    private val KEY_STUDENT_ID = "student_id"

    //create tables
    private val CREATE_TABLE_STUDENTS = "CREATE TABLE IF NOT EXISTS " + TableNames.TABLE_STUDENTS +
            "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT, " +
            KEY_SURNAME + " TEXT, " +
            KEY_INDEKS + " TEXT, " +
            KEY_JMBG + " TEXT, " +
            KEY_USERNAME + " TEXT, " +
            KEY_PASSWORD + " TEXT" +
            ")"


    override fun onCreate(db: SQLiteDatabase?) {
        //creating tables
        db?.execSQL(CREATE_TABLE_STUDENTS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
          // on upgrade drop older tables
        db!!.execSQL("DROP TABLE IF EXISTS " + TableNames.TABLE_STUDENTS)
        onCreate(db)
    }

    //create tables from scratch (if they are deleted)
    fun createTables(){
        if(db == null)
            db = writableDatabase;

        db?.execSQL(CREATE_TABLE_STUDENTS)
    }

    //delete tables
    fun dropTables(){
        db?.execSQL("DROP TABLE IF EXISTS" + TableNames.TABLE_STUDENTS)
    }

    fun createStudent(student: Student): Long{
        val values = ContentValues()
        val db = this.writableDatabase
        //key and column name must be same
        values.put(KEY_NAME, student.getName())
        values.put(KEY_SURNAME, student.getSurname())
        values.put(KEY_INDEKS, student.getIndeks())
        values.put(KEY_JMBG, student.getJmbg())
        values.put(KEY_USERNAME, student.getUsername())
        values.put(KEY_PASSWORD, student.getPassword())

        val student_id: Long = db?.insert(TableNames.TABLE_STUDENTS, null, values) ?: -1

        if (student_id != -1L) {
            Log.d("Student", "Added successfully")
            student.setId(student_id)
        } else {
            Log.d("Student", "Failed to add")
        }

        return student_id
    }

}