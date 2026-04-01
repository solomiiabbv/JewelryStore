package com.example.jewelrystore

import android.net.Uri
import java.io.Serializable

data class Product(
    var name: String,
    var price: String,
    val imageResId: Int,
    var category: String = "-",
    var weight: String = "-",
    var metal: String = "-",
    var gender: String = "-",
    var size: String = "-"
) : Serializable