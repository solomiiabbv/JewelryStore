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

        tvName.text = intent.getStringExtra("name")
        tvPrice.text = intent.getStringExtra("price")
        tvWeight.text = "Weight: " + intent.getStringExtra("weight")
        tvMetal.text = "Metal: " + intent.getStringExtra("metal")
        tvGender.text = "Gender: " + intent.getStringExtra("gender")
        tvSize.text = "Size: " + intent.getStringExtra("size")

        // Показуємо картинку
        val imageUriString = intent.getStringExtra("imageUri")
        val imageResId = intent.getIntExtra("imageResId", 0)

        if (imageUriString != null) {
            ivProduct.setImageURI(Uri.parse(imageUriString))
        } else if (imageResId != 0) {
            ivProduct.setImageResource(imageResId)
        } else {
            ivProduct.setImageResource(R.drawable.ic_launcher_foreground)
        }

        btnClose.setOnClickListener { finish() }
    }
}
