package com.example.jewelrystore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView
    private lateinit var ivAdminPhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvGreeting = findViewById(R.id.tvGreeting)
        ivAdminPhoto = findViewById(R.id.ivAdminPhoto)

        val btnManageProducts = findViewById<Button>(R.id.btnManageProducts)
        val btnProfile = findViewById<Button>(R.id.btnProfile)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Отримуємо ім’я з SharedPreferences
        updateGreeting()

        // Клік по фото адміністратора (можна відкрити профіль)
        ivAdminPhoto.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Відкриваємо список товарів
        btnManageProducts.setOnClickListener {
            startActivity(Intent(this, ProductsActivity::class.java))
        }

        // Відкриваємо профіль адміністратора
        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Вихід
        btnLogout.setOnClickListener {
            val pref = getSharedPreferences("auth", MODE_PRIVATE)
            pref.edit().putBoolean("isAuthorized", false).apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Оновлюємо привітання при поверненні з ProfileActivity
        updateGreeting()
    }

    private fun updateGreeting() {
        val userPref = getSharedPreferences("user", MODE_PRIVATE)
        val firstName = userPref.getString("firstName", "Адміністратор")
        tvGreeting.text = "Привіт, $firstName!"
    }
}g