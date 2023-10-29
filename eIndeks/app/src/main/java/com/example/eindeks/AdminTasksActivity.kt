package com.example.eindeks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminTasksActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_tasks)

        val btnAddStudents: Button = findViewById(R.id.btnAddStudents)
        btnAddStudents.setOnClickListener {
            val intent = Intent(this, AddingStudentsActivity::class.java)
            startActivity(intent)
        }
    }
}