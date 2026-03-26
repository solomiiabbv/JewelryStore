package com.example.jewelrystore

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater

class ProductsActivity : AppCompatActivity() {

    private val productList = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter
    private var selectedImageUri: Uri? = null // тепер зберігаємо Uri обраного фото

    // Регістр для вибору файлу
    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedImageUri = data?.data
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val rvProducts = findViewById<RecyclerView>(R.id.rvProducts)
        val btnClose = findViewById<ImageButton>(R.id.btnClose)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        // Жовта кнопка
        btnAdd.setBackgroundColor(resources.getColor(android.R.color.holo_orange_light))
        btnAdd.setTextColor(resources.getColor(android.R.color.white))

        // Початкові товари (англійська)
        productList.addAll(
            listOf(
                Product("Gold Ring", "$1200", R.drawable.gold_ring, "Rings", "5 g", "Gold", "Female", "16"),
                Product("Silver Bracelet", "$800", R.drawable.silver_bracelet, "Bracelets", "7 g", "Silver", "Female", "-"),
                Product("Pearl Earrings", "$950", R.drawable.pearl_earrings, "Earrings", "3 g", "Silver", "Female", "-"),
                Product("Diamond Pendant", "$8000", R.drawable.diamond_pendant, "Pendants", "10 g", "Gold", "Female", "-"),
                Product("Rose Brooch", "$450", R.drawable.rose_brooch, "Brooches", "2 g", "Silver", "Female", "-"),
                Product("Men's Casio Watch", "$3500", R.drawable.mens_watch, "Watches", "50 g", "Metal", "Male", "-"),
                Product("Women's Casio Watch", "$3200", R.drawable.womens_watch, "Watches", "45 g", "Metal", "Female", "-"),
                Product("Stone Necklace", "$1800", R.drawable.stone_necklace, "Necklaces", "8 g", "Silver", "Female", "-"),
                Product("Silver Ring", "$700", R.drawable.silver_ring, "Rings", "4 g", "Silver", "Female", "16"),
                Product("Beaded Bracelet", "$650", R.drawable.beaded_bracelet, "Bracelets", "5 g", "Silver", "Female", "-")
            )
        )

        adapter = ProductAdapter(productList) { product ->
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

        btnClose.setOnClickListener { finish() }
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

        // Клік по картинці – вибір фото з файлів
        ivSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            selectImageLauncher.launch(intent)
        }

        AlertDialog.Builder(this)
            .setTitle("Add Product")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val newProduct = Product(
                    name = etName.text.toString(),
                    price = etPrice.text.toString(),
                    imageResId = selectedImageUri?.let { uri -> uriToResId(uri) } ?: R.drawable.gold_ring,
                    category = etCategory.text.toString(),
                    weight = etWeight.text.toString(),
                    metal = etMetal.text.toString(),
                    gender = etGender.text.toString(),
                    size = etSize.text.toString()
                )
                productList.add(newProduct)
                adapter.notifyItemInserted(productList.size - 1)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Проста функція для використання Uri (якщо потрібно конвертувати у drawable, поки залишимо дефолт)
    private fun uriToResId(uri: Uri): Int {
        // Для простоти поки повертаємо дефолтне зображення
        return R.drawable.gold_ring
    }
}