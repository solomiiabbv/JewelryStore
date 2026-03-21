package com.example.jewelrystore

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val ivLogo = findViewById<ImageView>(R.id.ivLogo)
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val etBirthday = findViewById<EditText>(R.id.etBirthday)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnGoLogin = findViewById<Button>(R.id.btnGoLogin)

        // Дата народження
        etBirthday.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dpd = DatePickerDialog(this,
                { _, y, m, d -> etBirthday.setText("$d/${m+1}/$y") },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            val birthday = etBirthday.text.toString()

            if(username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty() || birthday.isEmpty()){
                Toast.makeText(this, "Заповніть всі поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(password != confirmPassword){
                Toast.makeText(this, "Паролі не співпадають", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Зберігаємо користувача
            val userPref = getSharedPreferences("user", MODE_PRIVATE)
            userPref.edit()
                .putString("username", username)
                .putString("firstName", firstName)
                .putString("lastName", lastName)
                .putString("password", password)
                .putString("birthday", birthday)
                .apply()

            Toast.makeText(this, "Реєстрація успішна!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        ivLogo.setOnClickListener {
            Toast.makeText(this, "Ювелірний магазин", Toast.LENGTH_SHORT).show()
        }
    }
}