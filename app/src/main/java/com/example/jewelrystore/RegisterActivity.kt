package com.example.jewelrystore

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPref = getSharedPreferences("jewelry_store_pref", MODE_PRIVATE)

        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etBirthday = findViewById<EditText>(R.id.etBirthday)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        etBirthday.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this, { _, y, m, d ->
                etBirthday.setText("$d/${m+1}/$y")
            }, year, month, day)
            dpd.show()
        }

        btnRegister.setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val email = etEmail.text.toString()
            val birthday = etBirthday.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if(firstName.isBlank() || lastName.isBlank() || email.isBlank() ||
                birthday.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                Toast.makeText(this, "Заповніть всі поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(password != confirmPassword) {
                Toast.makeText(this, "Паролі не співпадають", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(password.length < 6) {
                Toast.makeText(this, "Пароль має бути мінімум 6 символів", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sharedPref.edit().apply {
                putString("firstName", firstName)
                putString("lastName", lastName)
                putString("email", email)
                putString("birthday", birthday)
                putString("username", username)
                putString("password", password)
                apply()
            }

            Toast.makeText(this, "Реєстрація успішна", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}