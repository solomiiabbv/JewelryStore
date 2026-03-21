package com.example.jewelrystore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SharedPreferences для авторизації та користувача
        val authPref = getSharedPreferences("auth", MODE_PRIVATE)
        val userPref = getSharedPreferences("user", MODE_PRIVATE)

        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val ivLogo = findViewById<ImageView>(R.id.ivLogo)
        val btnManageProducts = findViewById<Button>(R.id.btnManageProducts)
        val btnInfoList = findViewById<Button>(R.id.btnInfoList)
        val btnProfile = findViewById<Button>(R.id.btnProfile)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Вітання з ім’ям адміністратора
        val username = userPref.getString("username", "Адміністратор")
        tvGreeting.text = "Привіт, $username!"

        // Обробники кнопок
        btnManageProducts.setOnClickListener {
            Toast.makeText(this, "Керування товарами", Toast.LENGTH_SHORT).show()
        }
        btnInfoList.setOnClickListener {
            Toast.makeText(this, "Інформаційний список", Toast.LENGTH_SHORT).show()
        }
        btnProfile.setOnClickListener {
            Toast.makeText(this, "Профіль адміністратора", Toast.LENGTH_SHORT).show()
        }
        btnLogout.setOnClickListener {
            // Очищення прапорця авторизації
            authPref.edit().clear().apply() // Очищає всі дані авторизації
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Логотип
        ivLogo.setOnClickListener {
            Toast.makeText(this, "Ювелірний магазин", Toast.LENGTH_SHORT).show()
        }
    }
}