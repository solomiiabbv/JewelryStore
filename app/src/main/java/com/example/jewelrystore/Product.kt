package com.example.jewelrystore

import android.net.Uri

data class Product(
    val name: String,
    val price: String,
    val image: Any, // Uri або Int drawable
    val category: String,
    val weight: String,
    val metal: String,
    val gender: String,
    val size: String
) {
    // Конвертуємо в Uri для передачі в ProductDetailActivity
    val imageUri: String?
        get() = when (image) {
            is Uri -> image.toString()
            is Int -> "android.resource://com.example.jewelrystore/$image"
            else -> null
        }
}