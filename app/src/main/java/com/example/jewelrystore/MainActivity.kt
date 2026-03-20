package com.example.jewelrystore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnManageProducts: Button
    private lateinit var btnInfoList: Button
    private lateinit var btnProfile: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // знаходимо елементи через findViewById
        tvWelcome = findViewById(R.id.tvWelcome)
        btnManageProducts = findViewById(R.id.btnManageProducts)
        btnInfoList = findViewById(R.id.btnInfoList)
        btnProfile = findViewById(R.id.btnProfile)
        btnLogout = findViewById(R.id.btnLogout)

        // Текст привітання
        tvWelcome.text = "Ласкаво просимо в наш ювелірний магазин!"

        // обробка кнопок
        btnManageProducts.setOnClickListener {
            Toast.makeText(this, "Відкрито керування товарами", Toast.LENGTH_SHORT).show()
        }

        btnInfoList.setOnClickListener {
            Toast.makeText(this, "Відкрито інформаційний список", Toast.LENGTH_SHORT).show()
        }

        btnProfile.setOnClickListener {
            Toast.makeText(this, "Відкрито профіль адміністратора", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "Вихід з системи", Toast.LENGTH_SHORT).show()
        }
    }
}