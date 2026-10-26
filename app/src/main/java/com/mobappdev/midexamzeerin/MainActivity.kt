package com.mobappdev.midexamzeerin // Sesuaikan dengan nama package kamu

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val etName = findViewById<EditText>(R.id.etStudentName)
        val etID = findViewById<EditText>(R.id.etStudentID)
        val cbEnrolled = findViewById<CheckBox>(R.id.cbHasEnrolled)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnView = findViewById<Button>(R.id.btnView)
        val tvDisplay = findViewById<TextView>(R.id.tvDisplay)

        // 1. ADD DATA
        btnAdd.setOnClickListener {
            val name = etName.text.toString().trim()
            val studentId = etID.text.toString().trim()
            val isEnrolled = cbEnrolled.isChecked

            if (name.isNotEmpty() && studentId.isNotEmpty()) {
                val student = Student(studentId = studentId, name = name, isEnrolled = isEnrolled)
                val status = dbHelper.addStudent(student)
                if (status > -1) {
                    Toast.makeText(this, "Data added successfully!", Toast.LENGTH_SHORT).show()
                    clearInputs(etName, etID, cbEnrolled)
                } else {
                    Toast.makeText(this, "Failed to add data!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter Student Name and Student ID!", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. VIEW DATA
        btnView.setOnClickListener {
            val studentList = dbHelper.getAllStudents()
            if (studentList.isEmpty()) {
                tvDisplay.text = "No data found."
            } else {
                val builder = StringBuilder()
                for (s in studentList) {
                    val statusText = if (s.isEnrolled) "Enrolled" else "Not Enrolled"
                    builder.append("ID: ${s.studentId} | Name: ${s.name} | Status: $statusText\n")
                }
                tvDisplay.text = builder.toString()
            }
        }

        // 3. EDIT DATA
        btnEdit.setOnClickListener {
            val name = etName.text.toString().trim()
            val studentId = etID.text.toString().trim()
            val isEnrolled = cbEnrolled.isChecked

            if (studentId.isNotEmpty()) {
                val student = Student(studentId = studentId, name = name, isEnrolled = isEnrolled)
                val rows = dbHelper.updateStudent(student)
                if (rows > 0) {
                    Toast.makeText(this, "Data updated successfully!", Toast.LENGTH_SHORT).show()
                    clearInputs(etName, etID, cbEnrolled)
                } else {
                    Toast.makeText(this, "Student ID not found!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter Student ID to edit!", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. DELETE DATA
        btnDelete.setOnClickListener {
            val studentId = etID.text.toString().trim()
            if (studentId.isNotEmpty()) {
                val rows = dbHelper.deleteStudent(studentId)
                if (rows > 0) {
                    Toast.makeText(this, "Data deleted successfully!", Toast.LENGTH_SHORT).show()
                    clearInputs(etName, etID, cbEnrolled)
                } else {
                    Toast.makeText(this, "Student ID not found!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter Student ID to delete!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearInputs(etName: EditText, etID: EditText, cbEnrolled: CheckBox) {
        etName.text.clear()
        etID.text.clear()
        cbEnrolled.isChecked = false
    }
}