package com.example.jewelrystore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val ivLogo = findViewById<ImageView>(R.id.ivLogo)

        // Відображаємо привітання
        tvGreeting.text = "Привіт, користувач!"

        // Перевірка авторизації
        val pref = getSharedPreferences("auth", MODE_PRIVATE)
        if(pref.getBoolean("isAuthorized", false)){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val savedUser = getSharedPreferences("user", MODE_PRIVATE)
            if(username == savedUser.getString("username", "") &&
                password == savedUser.getString("password", "")){
                pref.edit().putBoolean("isAuthorized", true).apply()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Невірний логін або пароль", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        ivLogo.setOnClickListener {
            Toast.makeText(this, "Ювелірний магазин", Toast.LENGTH_SHORT).show()
        }
    }
}