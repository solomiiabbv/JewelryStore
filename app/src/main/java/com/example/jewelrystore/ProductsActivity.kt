package com.example.jewelrystore

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val rvProducts = findViewById<RecyclerView>(R.id.rvProducts)
        val btnClose = findViewById<ImageButton>(R.id.btnClose)

        val productList = listOf(
            Product("Золоте кільце", "1200 грн", R.drawable.gold_ring, "Кільця", "5г", "Золото", "Жіноча", "16"),
            Product("Срібний браслет", "800 грн", R.drawable.silver_bracelet, "Браслети", "10г", "Срібло", "Жіноча", "20см"),
            Product("Сережки з перлами", "950 грн", R.drawable.pearl_earrings, "Сережки", "3г", "Срібло", "Жіноча", "2см"),
            Product("Кулон з діамантом", "8000 грн", R.drawable.diamond_pendant, "Кулони", "4г", "Золото", "Жіноча", "3см"),
            Product("Брошка 'Роза'", "450 грн", R.drawable.rose_brooch, "Брошки", "2г", "Метал", "Жіноча", "4см"),
            Product("Чоловічий годинник Casio", "3500 грн", R.drawable.mens_watch, "Годинники", "50г", "Метал", "Чоловіча", "44мм"),
            Product("Жіночий годинник Casio", "3200 грн", R.drawable.womens_watch, "Годинники", "40г", "Метал", "Жіноча", "38мм"),
            Product("Кольє з каменем", "1800 грн", R.drawable.stone_necklace, "Кольє", "15г", "Метал", "Жіноча", "45см"),
            Product("Срібна каблучка", "700 грн", R.drawable.silver_ring, "Кільця", "4г", "Срібло", "Жіноча", "17"),
            Product("Браслет з намистин", "650 грн", R.drawable.beaded_bracelet, "Браслети", "6г", "Метал", "Жіноча", "21см")
        )

        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = ProductAdapter(productList) { product ->

            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("name", product.name)
            intent.putExtra("price", product.price)
            intent.putExtra("imageResId", product.imageResId)
            intent.putExtra("weight", product.weight)
            intent.putExtra("metal", product.metal)
            intent.putExtra("gender", product.gender)
            intent.putExtra("size", product.size)

            startActivity(intent)
        }

        btnClose.setOnClickListener {
            finish()
        }
    }
}