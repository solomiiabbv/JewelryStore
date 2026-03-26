package com.example.jewelrystore

import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val btnClose = findViewById<ImageButton>(R.id.btnClose)
        val ivProduct = findViewById<ImageView>(R.id.ivProductDetail)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val tvWeight = findViewById<TextView>(R.id.tvWeight)
        val tvMetal = findViewById<TextView>(R.id.tvMetal)
        val tvGender = findViewById<TextView>(R.id.tvGender)
        val tvSize = findViewById<TextView>(R.id.tvSize)
        val tvCategory = findViewById<TextView>(R.id.tvCategory)

        // Отримуємо дані з Intent
        val name = intent.getStringExtra("name") ?: "-"
        val price = intent.getStringExtra("price") ?: "-"
        val weight = intent.getStringExtra("weight") ?: "-"
        val metal = intent.getStringExtra("metal") ?: "-"
        val gender = intent.getStringExtra("gender") ?: "-"
        val size = intent.getStringExtra("size") ?: "-"
        val category = intent.getStringExtra("category") ?: "-"
        val imageUriString = intent.getStringExtra("imageUri")

        // Встановлюємо текст з підписами
        tvName.text = name
        tvPrice.text = price
        tvWeight.text = "Weight: $weight"
        tvMetal.text = "Metal: $metal"
        tvGender.text = "Gender: $gender"
        tvSize.text = "Size: $size"
        tvCategory.text = "Category: $category"

        // Показуємо картинку
        if (!imageUriString.isNullOrEmpty()) {
            ivProduct.setImageURI(Uri.parse(imageUriString))
        } else {
            ivProduct.setImageResource(R.drawable.ic_launcher_foreground)
        }

        // Кнопка назад
        btnClose.setOnClickListener { finish() }
    }
}