package com.example.jewelrystore

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val rvProducts: RecyclerView = findViewById(R.id.rvProducts)
        val btnClose: ImageButton = findViewById(R.id.btnClose)
        val btnAdd: Button = findViewById(R.id.btnAdd)

        // Перевірка прав
        checkPermissions()

        // Додаємо стартові 10 товарів з drawable
        productList.addAll(
            listOf(
                Product("Gold Ring", "1200 UAH", imageResId = R.drawable.gold_ring, category = "Rings", weight = "5 g", metal = "Gold", gender = "Female", size = "16"),
                Product("Silver Bracelet", "800 UAH", imageResId = R.drawable.silver_bracelet, category = "Bracelets", weight = "7 g", metal = "Silver", gender = "Female", size = "-"),
                Product("Pearl Earrings", "950 UAH", imageResId = R.drawable.pearl_earrings, category = "Earrings", weight = "3 g", metal = "Silver", gender = "Female", size = "-"),
                Product("Diamond Pendant", "8000 UAH", imageResId = R.drawable.diamond_pendant, category = "Pendants", weight = "10 g", metal = "Gold", gender = "Female", size = "-"),
                Product("Rose Brooch", "450 UAH", imageResId = R.drawable.rose_brooch, category = "Brooches", weight = "2 g", metal = "Silver", gender = "Female", size = "-"),
                Product("Men's Watch Casio", "3500 UAH", imageResId = R.drawable.mens_watch, category = "Watches", weight = "50 g", metal = "Metal", gender = "Male", size = "-"),
                Product("Women's Watch Casio", "3200 UAH", imageResId = R.drawable.womens_watch, category = "Watches", weight = "45 g", metal = "Metal", gender = "Female", size = "-"),
                Product("Stone Necklace", "1800 UAH", imageResId = R.drawable.stone_necklace, category = "Necklaces", weight = "8 g", metal = "Silver", gender = "Female", size = "-"),
                Product("Silver Ring", "700 UAH", imageResId = R.drawable.silver_ring, category = "Rings", weight = "4 g", metal = "Silver", gender = "Female", size = "16"),
                Product("Beaded Bracelet", "650 UAH", imageResId = R.drawable.beaded_bracelet, category = "Bracelets", weight = "5 g", metal = "Silver", gender = "Female", size = "-")
            )
        )

        // Adapter з click listener
        adapter = ProductAdapter(productList) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("name", product.name)
            intent.putExtra("price", product.price)
            intent.putExtra("category", product.category)
            intent.putExtra("weight", product.weight)
            intent.putExtra("metal", product.metal)
            intent.putExtra("gender", product.gender)
            intent.putExtra("size", product.size)

            // Відправляємо imageUri або imageResId
            if (product.imageUri != null) {
                intent.putExtra("imageUri", product.imageUri.toString())
            } else if (product.imageResId != null) {
                intent.putExtra("imageResId", product.imageResId)
            }
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
            .setTitle("Add Product")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val newProduct = Product(
                    name = etName.text.toString(),
                    price = etPrice.text.toString(),
                    imageUri = selectedImageUri,
                    category = "Custom",
                    weight = "-",
                    metal = "-",
                    gender = "-",
                    size = "-"
                )
                productList.add(newProduct)
                adapter.notifyItemInserted(productList.size - 1)
                selectedImageUri = null
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showImageSourceOptions() {
        val options = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(this)
            .setTitle("Choose Image Source")
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
                val tempUri = Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, bitmap, null, null))
                selectedImageUri = tempUri
                Toast.makeText(this, "Camera image selected", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Gallery image selected", Toast.LENGTH_SHORT).show()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Permissions required to select image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}