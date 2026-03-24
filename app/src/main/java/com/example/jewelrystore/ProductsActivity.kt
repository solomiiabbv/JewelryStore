package com.example.jewelrystore

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductsActivity : AppCompatActivity() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var btnAdd: Button
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        rvProducts = findViewById(R.id.rvProducts)
        btnAdd = findViewById(R.id.btnAdd)

        // Заповнимо початкові товари (10 штук)
        for(i in 1..10){
            productList.add(Product("Товар $i", "Ціна: ${i*100} грн", R.drawable.ic_store))
        }

        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = ProductAdapter(productList)

        btnAdd.setOnClickListener {
            Toast.makeText(this, "Додати новий товар", Toast.LENGTH_SHORT).show()
        }
    }
}
