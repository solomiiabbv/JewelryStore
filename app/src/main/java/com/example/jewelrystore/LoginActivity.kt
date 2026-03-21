package com.example.jewelrystore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)

        tvGreeting.text = "Вітаємо! Будь ласка, увійдіть або зареєструйтесь."

        // Перевірка авторизації
        val pref = getSharedPreferences("auth", MODE_PRIVATE)
        if (pref.getBoolean("isAuthorized", false)) {
            // Якщо користувач вже авторизований, переходимо в MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            val savedUser = getSharedPreferences("user", MODE_PRIVATE)
            if (username == savedUser.getString("username", "") &&
                password == savedUser.getString("password", "")
            ) {
                pref.edit().putBoolean("isAuthorized", true).commit() // commit() гарантує збереження
                startActivity(Intent(this, MainActivity::class.java))
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