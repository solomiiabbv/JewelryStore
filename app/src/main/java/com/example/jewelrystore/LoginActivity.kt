package com.example.jewelrystore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        val authPref = getSharedPreferences("auth", MODE_PRIVATE)

        // Якщо користувач вже авторизований, йдемо відразу на MainActivity
        if (authPref.getBoolean("isAuthorized", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            val userPref = getSharedPreferences("user", MODE_PRIVATE)

            // Логін за username, а не email
            val savedUsername = userPref.getString("username", "")
            val savedPassword = userPref.getString("password", "")

            if (username == savedUsername && password == savedPassword) {
                // Зберігаємо прапорець авторизації
                authPref.edit().putBoolean("isAuthorized", true).apply()

                val intent = Intent(this, MainActivity::class.java)
                // Передаємо ім'я користувача для привітання
                intent.putExtra("firstName", userPref.getString("firstName", "Адміністратор"))
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Невірний логін або пароль", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}