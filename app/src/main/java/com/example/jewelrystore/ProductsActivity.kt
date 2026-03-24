package com.example.jewelrystore

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

class ProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val rvProducts = findViewById<RecyclerView>(R.id.rvProducts)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        // Приклад товарів із локальними картинками
        val productList = listOf(
            Product("Золоте кільце", "1200 грн", R.drawable.gold_ring),
            Product("Срібний браслет", "800 грн", R.drawable.silver_bracelet),
            Product("Сережки з перлами", "950 грн", R.drawable.pearl_earrings),
            Product("Кулон з діамантом", "5000 грн", R.drawable.diamond_pendant),
            Product("Брошка 'Роза'", "450 грн", R.drawable.rose_brooch)
        )

        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = ProductAdapter(productList)

        btnAdd.setOnClickListener {
            Toast.makeText(this, "Додати новий товар", Toast.LENGTH_SHORT).show()
        }
    }
}
