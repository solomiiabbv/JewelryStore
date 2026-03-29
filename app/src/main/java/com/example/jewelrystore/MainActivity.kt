package com.example.jewelrystore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

        loadUserData() // завантажуємо ім'я та фото

        btnManageProducts.setOnClickListener {
            startActivity(Intent(this, ProductsActivity::class.java))
        }

        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        btnLogout.setOnClickListener {
            getSharedPreferences("auth", MODE_PRIVATE).edit()
                .putBoolean("isAuthorized", false).apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        ivAdminPhoto.setOnClickListener {
            Toast.makeText(this, "Профіль адміністратора", Toast.LENGTH_SHORT).show()
        }
    }

    // Оновлюємо дані при поверненні на головний екран
    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {
        val prefs = getSharedPreferences("user", MODE_PRIVATE)
        val firstName = prefs.getString("firstName", "Адміністратор")
        tvGreeting.text = "Привіт, $firstName!"

        val photoUriString = prefs.getString("photoUri", null)
        if (photoUriString != null) {
            ivAdminPhoto.setImageURI(Uri.parse(photoUriString))
        } else {
            ivAdminPhoto.setImageResource(R.drawable.ic_admin_placeholder)
        }
    }
