package com.mobappdev.midexamzeerin // Adjust package name if needed

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Student(
    val id: Int = 0,
    val studentId: String,
    val name: String,
    val isEnrolled: Boolean
)

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "StudentDB.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "students"
        private const val COLUMN_ID = "id"
        private const val COLUMN_STUDENT_ID = "student_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_IS_ENROLLED = "is_enrolled"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_STUDENT_ID TEXT, "
                + "$COLUMN_NAME TEXT, "
                + "$COLUMN_IS_ENROLLED INTEGER)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addStudent(student: Student): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_STUDENT_ID, student.studentId)
            put(COLUMN_NAME, student.name)
            put(COLUMN_IS_ENROLLED, if (student.isEnrolled) 1 else 0)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    fun getAllStudents(): List<Student> {
        val list = mutableListOf<Student>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val studentId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val isEnrolled = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ENROLLED)) == 1
                list.add(Student(id, studentId, name, isEnrolled))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun updateStudent(student: Student): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_IS_ENROLLED, if (student.isEnrolled) 1 else 0)
        }
        val result = db.update(TABLE_NAME, values, "$COLUMN_STUDENT_ID = ?", arrayOf(student.studentId))
        db.close()
        return result
    }

    fun deleteStudent(studentId: String): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_STUDENT_ID = ?", arrayOf(studentId))
        db.close()
        return result
    }
}