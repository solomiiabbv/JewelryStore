package com.example.jewelrystore

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductsActivity : AppCompatActivity() {

    private val productList = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter
    private var selectedImageUri: Uri? = null
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        prefs = getSharedPreferences("products", MODE_PRIVATE)

        val tvTitle: TextView = findViewById(R.id.tvTitle)
        val rvProducts: RecyclerView = findViewById(R.id.rvProducts)
        val btnClose: ImageButton = findViewById(R.id.btnClose)
        val btnAdd: Button = findViewById(R.id.btnAdd)

        tvTitle.text = "Список товарів"

        // Перевірка прав
        checkPermissions()

        // Завантаження стартових та збережених товарів
        loadProducts()

        adapter = ProductAdapter(productList) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("name", product.name)
            intent.putExtra("price", product.price)
            intent.putExtra("imageUri", product.imageUri.toString())
            startActivity(intent)
        }

        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = adapter

        btnClose.setOnClickListener { finish() }

        btnAdd.setOnClickListener { showAddProductDialog() }
    }

    private fun showAddProductDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_product_full, null)
        val etName = dialogView.findViewById<android.widget.EditText>(R.id.etName)
        val etPrice = dialogView.findViewById<android.widget.EditText>(R.id.etPrice)
        val ivSelectImage = dialogView.findViewById<ImageView>(R.id.ivSelectImage)

        ivSelectImage.setOnClickListener { showImageSourceOptions() }

        AlertDialog.Builder(this)
            .setTitle("Додати товар")
            .setView(dialogView)
            .setPositiveButton("Додати") { _, _ ->
                if (etName.text.isNotEmpty() && etPrice.text.isNotEmpty()) {
                    val newProduct = Product(
                        etName.text.toString(),
                        etPrice.text.toString(),
                        selectedImageUri ?: Uri.parse("android.resource://${packageName}/${R.drawable.ic_launcher_foreground}")
                    )
                    productList.add(newProduct)
                    adapter.notifyItemInserted(productList.size - 1)
                    saveProducts() // збереження товарів
                    selectedImageUri = null
                } else {
                    Toast.makeText(this, "Введіть назву та ціну", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Відмінити", null)
            .show()
    }

    private fun showImageSourceOptions() {
        val options = arrayOf("Камера", "Галерея")
        AlertDialog.Builder(this)
            .setTitle("Оберіть джерело зображення")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }.show()
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as Bitmap
            val tempUri = Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, bitmap, null, null))
            selectedImageUri = tempUri
            Toast.makeText(this, "Фото з камери вибране", Toast.LENGTH_SHORT).show()
        }
        }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                Toast.makeText(this, "Фото з галереї вибране", Toast.LENGTH_SHORT).show()
            }
        }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.CAMERA)
        }
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissions.isNotEmpty()) {
            requestPermissions(permissions.toTypedArray(), 100)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Права потрібні для вибору фото", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- Збереження та завантаження товарів ---
    private fun saveProducts() {
        val editor = prefs.edit()
        val set = productList.map { "${it.name}||${it.price}||${it.imageUri}" }.toSet()
        editor.putStringSet("products_set", set)
        editor.apply()
    }

    private fun loadProducts() {
        // Стартові 10 товарів
        val startProducts = listOf(
            Product("Gold Ring", "1200 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.gold_ring}")),
            Product("Silver Bracelet", "800 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.silver_bracelet}")),
            Product("Pearl Earrings", "950 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.pearl_earrings}")),
            Product("Diamond Pendant", "8000 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.diamond_pendant}")),
            Product("Rose Brooch", "450 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.rose_brooch}")),
            Product("Men's Watch Casio", "3500 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.mens_watch}")),
            Product("Women's Watch Casio", "3200 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.womens_watch}")),
            Product("Stone Necklace", "1800 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.stone_necklace}")),
            Product("Silver Ring", "700 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.silver_ring}")),
            Product("Beaded Bracelet", "650 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.beaded_bracelet}"))
        )
        productList.addAll(startProducts)

        // Завантаження доданих товарів
        val set = prefs.getStringSet("products_set", emptySet()) ?: emptySet()
        for (item in set) {
            val parts = item.split("||")
            if (parts.size >= 3) {
                val name = parts[0]
                val price = parts[1]
                val uri = Uri.parse(parts[2])
                productList.add(Product(name, price, uri))
            }
        }
    }
}
}