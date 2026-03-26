package com.example.jewelrystore

import android.app.Activity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val rvProducts = findViewById<RecyclerView>(R.id.rvProducts)
        val btnClose = findViewById<ImageButton>(R.id.btnClose)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        checkPermissions()

        productList.addAll(
            listOf(
                Product("Gold Ring", "1200 UAH", R.drawable.gold_ring, null, "Rings", "5 g", "Gold", "Female", "16"),
                Product("Silver Bracelet", "800 UAH", R.drawable.silver_bracelet, null, "Bracelets", "7 g", "Silver", "Female", "-"),
                Product("Pearl Earrings", "950 UAH", R.drawable.pearl_earrings, null, "Earrings", "3 g", "Silver", "Female", "-"),
                Product("Diamond Pendant", "8000 UAH", R.drawable.diamond_pendant, null, "Pendants", "10 g", "Gold", "Female", "-"),
                Product("Rose Brooch", "450 UAH", R.drawable.rose_brooch, null, "Brooches", "2 g", "Silver", "Female", "-"),
                Product("Men's Watch Casio", "3500 UAH", R.drawable.mens_watch, null, "Watches", "50 g", "Metal", "Male", "-"),
                Product("Women's Watch Casio", "3200 UAH", R.drawable.womens_watch, null, "Watches", "45 g", "Metal", "Female", "-"),
                Product("Stone Necklace", "1800 UAH", R.drawable.stone_necklace, null, "Necklaces", "8 g", "Silver", "Female", "-"),
                Product("Silver Ring", "700 UAH", R.drawable.silver_ring, null, "Rings", "4 g", "Silver", "Female", "16"),
                Product("Beaded Bracelet", "650 UAH", R.drawable.beaded_bracelet, null, "Bracelets", "5 g", "Silver", "Female", "-")
            )
        )

        adapter = ProductAdapter(productList) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("name", product.name)
            intent.putExtra("price", product.price)
            intent.putExtra("weight", product.weight)
            intent.putExtra("metal", product.metal)
            intent.putExtra("gender", product.gender)
            intent.putExtra("size", product.size)
            intent.putExtra("category", product.category)
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
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val etWeight = dialogView.findViewById<EditText>(R.id.etWeight)
        val etMetal = dialogView.findViewById<EditText>(R.id.etMetal)
        val etGender = dialogView.findViewById<EditText>(R.id.etGender)
        val etSize = dialogView.findViewById<EditText>(R.id.etSize)
        val etCategory = dialogView.findViewById<EditText>(R.id.etCategory)
        val ivSelectImage = dialogView.findViewById<ImageView>(R.id.ivSelectImage)

        ivSelectImage.setOnClickListener { showImageSourceOptions() }

        AlertDialog.Builder(this)
            .setTitle("Add Product")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val newProduct = Product(
                    name = etName.text.toString(),
                    price = etPrice.text.toString(),
                    weight = etWeight.text.toString(),
                    metal = etMetal.text.toString(),
                    gender = etGender.text.toString(),
                    size = etSize.text.toString(),
                    category = etCategory.text.toString(),
                    imageUri = selectedImageUri,
                    imageResId = null
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

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as? Bitmap
            bitmap?.let {
                val tempUri = Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, it, null, null))
                selectedImageUri = tempUri
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            permissions.add(android.Manifest.permission.CAMERA)
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissions.isNotEmpty())
            requestPermissions(permissions.toTypedArray(), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
            Toast.makeText(this, "Permissions required to select image", Toast.LENGTH_SHORT).show()
        }
    }
}