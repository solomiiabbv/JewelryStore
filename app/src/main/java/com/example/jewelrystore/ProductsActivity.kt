package com.example.jewelrystore

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductsActivity : AppCompatActivity() {

    private val productList = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter
    private var selectedImageUri: Uri? = null

    private val PREFS_NAME = "ProductsPrefs"
    private val KEY_PRODUCTS = "products"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val rvProducts: RecyclerView = findViewById(R.id.rvProducts)
        val btnClose: ImageButton = findViewById(R.id.btnClose)
        val btnAdd: Button = findViewById(R.id.btnAdd)
        val tvTitle: TextView = findViewById(R.id.tvTitle)

        tvTitle.text = "Список товарів"

        checkPermissions()
        loadProducts()

        adapter = ProductAdapter(productList) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("name", product.name)
            intent.putExtra("price", product.price)
            intent.putExtra("imageUri", product.imageUri.toString())
            intent.putExtra("category", product.category)
            intent.putExtra("weight", product.weight)
            intent.putExtra("metal", product.metal)
            intent.putExtra("gender", product.gender)
            intent.putExtra("size", product.size)
            startActivity(intent)
        }

        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = adapter

        btnClose.setOnClickListener { finish() }

        // Жовта кнопка "Додати товар"
        btnAdd.setBackgroundColor(resources.getColor(android.R.color.holo_orange_light))
        btnAdd.setOnClickListener { showAddProductDialog() }
    }

    private fun showAddProductDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_product_full, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val etCategory = dialogView.findViewById<EditText>(R.id.etCategory)
        val etWeight = dialogView.findViewById<EditText>(R.id.etWeight)
        val etMetal = dialogView.findViewById<EditText>(R.id.etMetal)
        val etGender = dialogView.findViewById<EditText>(R.id.etGender)
        val etSize = dialogView.findViewById<EditText>(R.id.etSize)
        val ivSelectImage = dialogView.findViewById<ImageView>(R.id.ivSelectImage)

        ivSelectImage.setOnClickListener { showImageSourceOptions() }

        AlertDialog.Builder(this)
            .setTitle("Додати товар")
            .setView(dialogView)
            .setPositiveButton("Додати") { _, _ ->
                val newProduct = Product(
                    etName.text.toString(),
                    etPrice.text.toString(),
                    selectedImageUri
                        ?: Uri.parse("android.resource://${packageName}/${R.drawable.ic_launcher_foreground}"),
                    etCategory.text.toString(),
                    etWeight.text.toString(),
                    etMetal.text.toString(),
                    etGender.text.toString(),
                    etSize.text.toString()
                )
                productList.add(newProduct)
                adapter.notifyItemInserted(productList.size - 1)
                saveProducts()
                selectedImageUri = null
            }
            .setNegativeButton("Відміна", null)
            .show()
    }

    private fun showImageSourceOptions() {
        val options = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(this)
            .setTitle("Оберіть джерело фото")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }.show()
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                val tempUri = Uri.parse(
                    MediaStore.Images.Media.insertImage(
                        contentResolver,
                        bitmap,
                        null,
                        null
                    )
                )
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
        if (requestCode == 100 && grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
            Toast.makeText(this, "Потрібні дозволи для вибору фото", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProducts() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val serializedList = productList.joinToString(";;") {
            "${it.name}|${it.price}|${it.imageUri}|${it.category}|${it.weight}|${it.metal}|${it.gender}|${it.size}"
        }
        editor.putString(KEY_PRODUCTS, serializedList)
        editor.apply()
    }

    private fun loadProducts() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val serializedList = prefs.getString(KEY_PRODUCTS, "")

        val startProducts = listOf(
            Product(
                "Gold Ring",
                "1200 UAH",
                Uri.parse("android.resource://${packageName}/${R.drawable.gold_ring}"),
                "Rings",
                "5 g",
                "Gold",
                "Female",
                "16"
            ),
            Product(
                "Silver Bracelet",
                "800 UAH",
                Uri.parse("android.resource://${packageName}/${R.drawable.silver_bracelet}"),
                "Bracelets",
                "7 g",
                "Silver",
                "Female",
                "onesize"
            ),
            Product(
                "Pearl Earrings",
                "950 UAH",
                Uri.parse("android.resource://${packageName}/${R.drawable.pearl_earrings}"),
                "Earrings",
                "3 g",
                "Silver",
                "Female",
                "onesize"
            ),
            Product(
                "Diamond Pendant",
                "8000 UAH",
                Uri.parse("android.resource://${packageName}/${R.drawable.diamond_pendant}"),
                "Pendants",
                "10 g",
                "Gold",
                "Female",
                "onesize"
            ),
            Product(
                "Rose Brooch",
                "450 UAH",
                Uri.parse("android.resource://${packageName}/${R.drawable.rose_brooch}"),
                "Brooches",
                "2 g",
                "Silver",
                "Female",
                "onesize"
            ),
            Product(
                "Men's Watch Casio",
                "3500 UAH",
                Uri.parse("android.resource://${packageName}/${R.drawable.mens_watch}"),
                "Watches",
                "50 g",
                "Metal",
                "Male",
                "onesize"
            ),
            Product(
                "Women's Watch Casio",
                "3200 UAH",
                Uri.parse("android.resource://${packageName}/${R.drawable.womens_watch}"),
                "Watches",
                "45 g",
                "Metal",
                "Female",
                "onesize"
            ),
            Product(
                "Stone Necklace",
                "1800 UAH",
                Uri.parse("android.resource://${packageName}/${R.drawable.stone_necklace}"),
                "Necklaces",
                "8 g",
                "Silver",
                "Female",
                "onesize"
            ),
            Product(
                "Silver Ring",
                "700 UAH",
                Uri.parse("android.resource://${packageName}/${R.drawable.silver_ring}"),
                "Rings",
                "4 g",
                "Silver",
                "Female",
                "16"
            ),
            Product(
                "Beaded Bracelet",
                "650 UAH",
                Uri.parse("android.resource://${packageName}/${R.drawable.beaded_bracelet}"),
                "Bracelets",
                "5 g",
                "Silver",
                "Female",
                "onesize"
            )
        )

        if (!serializedList.isNullOrEmpty()) {
            // Завантажуємо товари з SharedPreferences
            val items = serializedList.split(";;")
            for (item in items) {
                val parts = item.split("|")
                if (parts.size == 8) {
                    productList.add(
                        Product(
                            parts[0],
                            parts[1],
                            Uri.parse(parts[2]),
                            parts[3],
                            parts[4],
                            parts[5],
                            parts[6],
                            parts[7]
                        )
                    )
                }
            }

            // Перевіряємо, чи стартові товари вже є, якщо ні — додаємо
            val existingNames = productList.map { it.name }
            startProducts.forEach { product ->
                if (!existingNames.contains(product.name)) {
                    productList.add(product)
                }
            }
            saveProducts() // зберігаємо повний список
        } else {
            // SharedPreferences пустий — зберігаємо стартові товари
            productList.addAll(startProducts)
            saveProducts()
        }
    }
}