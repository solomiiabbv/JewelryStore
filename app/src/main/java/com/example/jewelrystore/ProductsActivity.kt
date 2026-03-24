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

        val productList = listOf(
            Product("Золоте кільце", "1200 грн", R.drawable.gold_ring, "Кільця"),
            Product("Срібний браслет", "800 грн", R.drawable.silver_bracelet, "Браслети"),
            Product("Сережки з перлами", "950 грн", R.drawable.pearl_earrings, "Сережки"),
            Product("Кулон з діамантом", "5000 грн", R.drawable.diamond_pendant, "Кулони"),
            Product("Брошка 'Роза'", "450 грн", R.drawable.rose_brooch, "Брошки"),
            Product("Чоловічий годинник", "2500 грн", R.drawable.mens_watch, "Годинники"),
            Product("Жіночий годинник", "2200 грн", R.drawable.womens_watch, "Годинники"),
            Product("Кольє з каменем", "1800 грн", R.drawable.stone_necklace, "Кольє"),
            Product("Срібна каблучка", "700 грн", R.drawable.silver_ring, "Кільця"),
            Product("Браслет з намистин", "650 грн", R.drawable.beaded_bracelet, "Браслети")
        )

        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = ProductAdapter(productList)

        btnAdd.setOnClickListener {
            Toast.makeText(this, "Додати новий товар", Toast.LENGTH_SHORT).show()
        }
    }
}
