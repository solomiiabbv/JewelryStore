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

        // Додаємо стартові 10 товарів
        productList.addAll(
            listOf(
                Product("Gold Ring", "1200 UAH", R.drawable.gold_ring),
                Product("Silver Bracelet", "800 UAH", R.drawable.silver_bracelet),
                Product("Pearl Earrings", "950 UAH", R.drawable.pearl_earrings),
                Product("Diamond Pendant", "8000 UAH", R.drawable.diamond_pendant),
                Product("Rose Brooch", "450 UAH", R.drawable.rose_brooch),
                Product("Men's Watch Casio", "3500 UAH", R.drawable.mens_watch),
                Product("Women's Watch Casio", "3200 UAH", R.drawable.womens_watch),
                Product("Stone Necklace", "1800 UAH", R.drawable.stone_necklace),
                Product("Silver Ring", "700 UAH", R.drawable.silver_ring),
                Product("Beaded Bracelet", "650 UAH", R.drawable.beaded_bracelet)
            )
        )

        adapter = ProductAdapter(productList) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("name", product.name)
            intent.putExtra("price", product.price)
            intent.putExtra("imageResId", product.imageResId)
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
                    etName.text.toString(),
                    etPrice.text.toString(),
                    selectedImageUri ?: Uri.parse("android.resource://${packageName}/${R.drawable.ic_launcher_foreground}")
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
                    0 -> openCamera()1 -> openGallery()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Permissions required to select image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}