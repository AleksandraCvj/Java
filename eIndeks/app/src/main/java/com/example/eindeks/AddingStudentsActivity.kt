package com.example.eindeks


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class AddingStudentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_students)

        val databaseHelper = DatabaseHelper(applicationContext)

        val etName: EditText = findViewById(R.id.etName)
        val etSurname: EditText = findViewById(R.id.etSurname)
        val etIndeks: EditText = findViewById(R.id.etIndeks)
        val etJMBG: EditText = findViewById(R.id.etJMBG)
        val etStudentUsername: EditText = findViewById(R.id.etStudentUsername)
        val etStudentPassword: EditText = findViewById(R.id.etStudentPassword)

        val btnAdd: Button = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val surname = etSurname.text.toString()
            val indeks = etIndeks.text.toString()
            val jmbg = etJMBG.text.toString()
            val username = etStudentUsername.text.toString()
            val password = etStudentPassword.text.toString()

            if(name.equals("") || surname.equals("") || indeks.equals("") || jmbg.equals("") || username.equals("") || password.equals("")){
                Toast.makeText(this@AddingStudentsActivity, "You have to fill all fields.", Toast.LENGTH_SHORT).show()
            }else{
                if(jmbg.length == 13) {
                    val student = Student()
                    student.setName(name)
                    student.setSurname(surname)
                    student.setIndeks(indeks)
                    student.setJmbg(jmbg)
                    student.setUsername(username)
                    student.setPassword(password)

                    databaseHelper.createStudent(student)

                    etName.setText("")
                    etSurname.setText("")
                    etIndeks.setText("")
                    etJMBG.setText("")
                    etStudentUsername.setText("")
                    etStudentPassword.setText("")

                }else{
                    Toast.makeText(this@AddingStudentsActivity, "Wrong length of JMBG field.", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


}



