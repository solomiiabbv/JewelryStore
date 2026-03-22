package com.example.jewelrystore

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: Button

    private val productsList = mutableListOf<Product>(
        Product("Кільце Золоте", "Класичне золоте кільце", 1200.0),
        Product("Срібний браслет", "Браслет зі срібла", 800.0),
        Product("Підвіска з каменем", "Камінь: Сапфір", 1500.0),
        Product("Сережки", "Золоті сережки", 900.0),
        Product("Чоловічий годинник", "Сталь + шкіра", 5000.0),
        Product("Кулон", "Срібло з гравіюванням", 700.0),
        Product("Брошка", "Кристали Swarovski", 1200.0),
        Product("Кільце з діамантом", "Діамант 0.5 карат", 10000.0),
        Product("Браслет з камінням", "Каміння: топаз", 1300.0),
        Product("Сережки-кільця", "Золото + перли", 1600.0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        recyclerView = findViewById(R.id.recyclerViewProducts)
        btnAdd = findViewById(R.id.btnAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ProductAdapter(productsList) { product ->
            // Клік на товар - показати деталі
            AlertDialog.Builder(this)
                .setTitle(product.name)
                .setMessage("Опис: ${product.description}\nЦіна: ${product.price} грн")
                .setPositiveButton("ОК", null)
                .show()
        }
        recyclerView.adapter = adapter

        btnAdd.setOnClickListener {
            Toast.makeText(this, "Додати новий товар", Toast.LENGTH_SHORT).show()
        }
    }
}