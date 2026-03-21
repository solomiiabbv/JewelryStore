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

        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val ivLogo = findViewById<ImageView>(R.id.ivLogo)

        val btnManageProducts = findViewById<Button>(R.id.btnManageProducts)
        val btnInfoList = findViewById<Button>(R.id.btnInfoList)
        val btnProfile = findViewById<Button>(R.id.btnProfile)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Отримуємо ім’я з реєстрації
        val userPref = getSharedPreferences("user", MODE_PRIVATE)
        val firstName = userPref.getString("firstName", "Адміністратор")
        tvGreeting.text = "Привіт, $firstName!"

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
            val pref = getSharedPreferences("auth", MODE_PRIVATE)
            pref.edit().putBoolean("isAuthorized", false).apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        ivLogo.setOnClickListener {
            Toast.makeText(this, "Ювелірний магазин", Toast.LENGTH_SHORT).show()
        }
    }
}