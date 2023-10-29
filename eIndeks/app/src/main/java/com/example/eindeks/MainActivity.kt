package com.example.eindeks

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private var etUsername: TextInputEditText? = null
    private var etPassword: TextInputEditText? = null


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        var spinnerRole: Spinner = findViewById(R.id.spinnerRole)

        val roles = arrayOf("admin", "student")
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, roles)
        spinnerRole.adapter = adapter


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            if(etUsername!!.text.toString().equals("admin") && etPassword!!.text.toString().equals("admin") && spinnerRole.selectedItem.toString().equals("admin")){
                val intent = Intent(this, AdminTasksActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Wrong credentials", Toast.LENGTH_SHORT).show();

            }
        }
    }


}