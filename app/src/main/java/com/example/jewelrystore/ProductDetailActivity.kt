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

        val btnClose = findViewById<ImageButton>(R.id.btnClose)

        val ivProduct = findViewById<ImageView>(R.id.ivProduct)
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val tvWeight = findViewById<TextView>(R.id.tvWeight)
        val tvMetal = findViewById<TextView>(R.id.tvMetal)
        val tvGender = findViewById<TextView>(R.id.tvGender)
        val tvSize = findViewById<TextView>(R.id.tvSize)

        ivProduct.setImageResource(intent.getIntExtra("imageResId", 0))
        tvName.text = intent.getStringExtra("name")
        tvPrice.text = intent.getStringExtra("price")
        tvWeight.text = "Вага: " + intent.getStringExtra("weight")
        tvMetal.text = "Метал: " + intent.getStringExtra("metal")
        tvGender.text = "Стать: " + intent.getStringExtra("gender")
        tvSize.text = "Розмір: " + intent.getStringExtra("size")

        btnClose.setOnClickListener {
            finish()
        }
    }
}