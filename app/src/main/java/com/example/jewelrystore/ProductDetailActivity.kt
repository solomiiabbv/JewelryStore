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
        val tvCategory = findViewById<TextView>(R.id.tvCategory)
        val tvWeight = findViewById<TextView>(R.id.tvWeight)
        val tvMetal = findViewById<TextView>(R.id.tvMetal)
        val tvGender = findViewById<TextView>(R.id.tvGender)
        val tvSize = findViewById<TextView>(R.id.tvSize)

        tvName.text = intent.getStringExtra("name")
        tvPrice.text = intent.getStringExtra("price")
        tvCategory.text = intent.getStringExtra("category")
        tvWeight.text = intent.getStringExtra("weight")
        tvMetal.text = intent.getStringExtra("metal")
        tvGender.text = intent.getStringExtra("gender")
        tvSize.text = intent.getStringExtra("size")

        val imageUriString = intent.getStringExtra("imageUri")
        if (imageUriString != null) ivProduct.setImageURI(Uri.parse(imageUriString))
        else ivProduct.setImageResource(R.drawable.ic_launcher_foreground)

        btnClose.setOnClickListener { finish() }
    }
}