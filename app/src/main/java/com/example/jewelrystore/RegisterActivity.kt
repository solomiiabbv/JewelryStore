package com.example.jewelrystore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        val userPref = getSharedPreferences("user", MODE_PRIVATE)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val email = etEmail.text.toString()

            if(username.isNotEmpty() && password.isNotEmpty()){
                // Зберігаємо дані користувача
                userPref.edit()
                    .putString("username", username)
                    .putString("password", password)
                    .putString("firstName", firstName)
                    .putString("lastName", lastName)
                    .putString("email", email)
                    .apply()

                Toast.makeText(this, "Реєстрація пройшла успішно", Toast.LENGTH_SHORT).show()

                // Переходимо на екран входу
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Введіть логін та пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }
}