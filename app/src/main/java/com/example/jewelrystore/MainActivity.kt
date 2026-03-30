package com.example.jewelrystore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView
    private lateinit var ivLogo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvGreeting = findViewById(R.id.tvGreeting)
        ivLogo = findViewById(R.id.ivLogo)

        val btnManageProducts = findViewById<Button>(R.id.btnManageProducts)
        val btnProfile = findViewById<Button>(R.id.btnProfile)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Відкриваємо список товарів
        btnManageProducts.setOnClickListener {
            startActivity(Intent(this, ProductsActivity::class.java))
        }

        // Профіль адміністратора
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

        // Завантаження імені адміністратора
        loadUserData()
    }

    private fun loadUserData() {
        val userPref = getSharedPreferences("user", MODE_PRIVATE)
        val firstName = userPref.getString("firstName", "Адміністратор")
        tvGreeting.text = "Привіт, $firstName!"
        // Фото на головній сторінці залишаємо фіксованим логотипом
        ivLogo.setImageResource(R.drawable.ic_store)
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }
}