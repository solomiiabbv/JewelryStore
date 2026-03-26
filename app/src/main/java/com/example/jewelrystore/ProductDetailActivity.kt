package com.example.jewelrystore

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Кнопка закриття
        val btnClose = findViewById<ImageButton>(R.id.btnClose)
        btnClose.setOnClickListener {
            finish() // повертає на попередню активність
        }

        // Пошук елементів
        val ivProduct = findViewById<ImageView>(R.id.ivProductDetail)
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val tvWeight = findViewById<TextView>(R.id.tvWeight)
        val tvMetal = findViewById<TextView>(R.id.tvMetal)
        val tvGender = findViewById<TextView>(R.id.tvGender)
        val tvSize = findViewById<TextView>(R.id.tvSize)

        // Отримання даних з Intent
        ivProduct.setImageResource(intent.getIntExtra("imageResId", R.drawable.gold_ring))
        tvName.text = intent.getStringExtra("name") ?: ""
        tvPrice.text = intent.getStringExtra("price") ?: ""
        tvWeight.text = "Вага: " + (intent.getStringExtra("weight") ?: "-")
        tvMetal.text = "Метал: " + (intent.getStringExtra("metal") ?: "-")
        tvGender.text = "Стать: " + (intent.getStringExtra("gender") ?: "-")
        tvSize.text = "Розмір: " + (intent.getStringExtra("size") ?: "-")
    }
}