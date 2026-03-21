package com.example.jewelrystore

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etBirthday = findViewById<EditText>(R.id.etBirthday)
        val etUsername = findViewById<EditText>(R.id.etUsernameRegister)
        val etPassword = findViewById<EditText>(R.id.etPasswordRegister)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        etBirthday.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, { _, y, m, d ->
                etBirthday.setText(String.format("%02d/%02d/%d", d, m+1, y))
            }, year, month, day)
            dpd.show()
        }

        btnRegister.setOnClickListener {
            val fName = etFirstName.text.toString()
            val lName = etLastName.text.toString()
            val email = etEmail.text.toString()
            val birthday = etBirthday.text.toString()
            val username = etUsername.text.toString()
            val pass = etPassword.text.toString()
            val confirm = etConfirmPassword.text.toString()

            if(fName.isEmpty() || lName.isEmpty() || email.isEmpty() ||
                birthday.isEmpty() || username.isEmpty() || pass.isEmpty() || confirm.isEmpty()){
                Toast.makeText(this,"Заповніть усі поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(pass != confirm || pass.length < 6){
                Toast.makeText(this,"Паролі не співпадають або менше 6 символів", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userPref = getSharedPreferences("user", MODE_PRIVATE)
            userPref.edit().apply {
                putString("firstName", fName)
                putString("lastName", lName)
                putString("email", email)
                putString("birthday", birthday)
                putString("username", username)
                putString("password", pass)
            }.apply()

            Toast.makeText(this,"Реєстрація успішна!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}