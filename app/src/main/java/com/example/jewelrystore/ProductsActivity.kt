package com.example.jewelrystore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView

class ProductsActivity : AppCompatActivity() {

    private val productList = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter
    private var selectedImageResId = R.drawable.gold_ring // дефолтна картинка

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val rvProducts = findViewById<RecyclerView>(R.id.rvProducts)
        val btnClose = findViewById<ImageButton>(R.id.btnClose)
        val btnAdd = findViewById<Button>(R.id.btnAdd) // кнопка додати товар

        // Додаємо стартові товари
        productList.addAll(
            listOf(
                Product("Золоте кільце", "1200 грн", R.drawable.gold_ring, "Кільця", "5 г", "Золото", "Жіноча", "16"),
                Product("Срібний браслет", "800 грн", R.drawable.silver_bracelet, "Браслети", "7 г", "Срібло", "Жіноча", "-"),
                Product("Сережки з перлами", "950 грн", R.drawable.pearl_earrings, "Сережки", "3 г", "Срібло", "Жіноча", "-"),
                Product("Кулон з діамантом", "8000 грн", R.drawable.diamond_pendant, "Кулони", "10 г", "Золото", "Жіноча", "-")
            )
        )

        adapter = ProductAdapter(productList) { product ->
            // Відкриття детальної інформації
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

        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = adapter

        // Закриття активності
        btnClose.setOnClickListener { finish() }

        // Додавання нового товару
        btnAdd.setOnClickListener { showAddProductDialog() }
    }

    private fun showAddProductDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_product_full, null)

        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val etWeight = dialogView.findViewById<EditText>(R.id.etWeight)
        val etMetal = dialogView.findViewById<EditText>(R.id.etMetal)
        val etGender = dialogView.findViewById<EditText>(R.id.etGender)
        val etSize = dialogView.findViewById<EditText>(R.id.etSize)
        val etCategory = dialogView.findViewById<EditText>(R.id.etCategory)
        val ivSelectImage = dialogView.findViewById<ImageView>(R.id.ivSelectImage)

        // Клік на картинку – залишимо дефолтну для початку
        ivSelectImage.setOnClickListener {
            // тут можна додати вибір картинки з галереї, якщо потрібно
        }

        AlertDialog.Builder(this)
            .setTitle("Додати товар")
            .setView(dialogView)
            .setPositiveButton("Додати") { _, _ ->
                val newProduct = Product(
                    name = etName.text.toString(),
                    price = etPrice.text.toString(),
                    imageResId = selectedImageResId,
                    category = etCategory.text.toString(),
                    weight = etWeight.text.toString(),
                    metal = etMetal.text.toString(),
                    gender = etGender.text.toString(),
                    size = etSize.text.toString()
                )
                productList.add(newProduct)
                adapter.notifyItemInserted(productList.size - 1)
            }
            .setNegativeButton("Відміна", null)
            .show()
    }
}