package com.example.jewelrystore

import android.net.Uri

data class Product(
    val name: String,
    val price: String,
    val imageResId: Int? = null, // для стартових товарів з drawable
    val imageUri: Uri? = null,   // для фото з камери/галереї
    val category: String,
    val weight: String,
    val metal: String,
    val gender: String,
    val size: String
)