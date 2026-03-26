package com.example.jewelrystore

import android.net.Uri
import java.io.Serializable

data class Product(
    val name: String,
    val price: String,
    val imageUri: Uri,
    val category: String = "-",
    val weight: String = "-",
    val metal: String = "-",
    val gender: String = "-",
    val size: String = "-"
) : Serializable