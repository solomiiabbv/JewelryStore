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

        adapter = ProductAdapter(
            productList,
            onClick = { product ->
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
            },
            onEditClick = { product, position ->
                showEditProductDialog(product, position)
            },
            onDeleteClick = { _, position ->
                productList.removeAt(position)
                adapter.notifyItemRemoved(position)
                saveProducts()
            }
        )

        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = adapter

        btnClose.setOnClickListener { finish() }

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

        var selectedImageUri: Uri? = null

        ivSelectImage.setOnClickListener { showImageSourceOptions { uri ->
            selectedImageUri = uri
            ivSelectImage.setImageURI(uri)
        } }

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
            }
            .setNegativeButton("Відміна", null)
            .show()
    }

    private fun showEditProductDialog(product: Product, position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_product_full, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val etCategory = dialogView.findViewById<EditText>(R.id.etCategory)
        val etWeight = dialogView.findViewById<EditText>(R.id.etWeight)
        val etMetal = dialogView.findViewById<EditText>(R.id.etMetal)
        val etGender = dialogView.findViewById<EditText>(R.id.etGender)
        val etSize = dialogView.findViewById<EditText>(R.id.etSize)
        val ivSelectImage = dialogView.findViewById<ImageView>(R.id.ivSelectImage)

        // Заповнюємо старі значення
        etName.setText(product.name)
        etPrice.setText(product.price)
        etCategory.setText(product.category)
        etWeight.setText(product.weight)
        etMetal.setText(product.metal)
        etGender.setText(product.gender)
        etSize.setText(product.size)
        ivSelectImage.setImageURI(product.imageUri)

        var dialogSelectedImageUri: Uri? = product.imageUri

        ivSelectImage.setOnClickListener { showImageSourceOptions { uri ->
            dialogSelectedImageUri = uri
            ivSelectImage.setImageURI(uri)
        } }

        AlertDialog.Builder(this)
            .setTitle("Редагувати товар")
            .setView(dialogView)
            .setPositiveButton("Зберегти") { _, _ ->
                val editedProduct = Product(
                    etName.text.toString(),
                    etPrice.text.toString(),
                    dialogSelectedImageUri
                        ?: Uri.parse("android.resource://${packageName}/${R.drawable.ic_launcher_foreground}"),
                    etCategory.text.toString(),
                    etWeight.text.toString(),
                    etMetal.text.toString(),
                    etGender.text.toString(),
                    etSize.text.toString()
                )
                productList[position] = editedProduct
                adapter.notifyItemChanged(position)
                saveProducts()
            }
            .setNegativeButton("Відміна", null)
            .show()
    }

    private fun showImageSourceOptions(onImageSelected: (Uri) -> Unit) {
        val options = arrayOf("Camera", "Gallery")
        AlertDialog.Builder(this)
            .setTitle("Оберіть джерело фото")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera(onImageSelected)
                    1 -> openGallery(onImageSelected)
                }
            }.show()
    }

    private fun openCamera(onImageSelected: (Uri) -> Unit) {
        val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
                onImageSelected(tempUri)
            }
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun openGallery(onImageSelected: (Uri) -> Unit) {
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) onImageSelected(uri)
        }
        galleryLauncher.launch("image/*")
    }private fun checkPermissions() {
        val permissions = mutableListOf<String>()
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.CAMERA)
        }
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissions.isNotEmpty()) requestPermissions(permissions.toTypedArray(), 100)
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
            Product("Gold Ring", "1200 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.gold_ring}"), "Rings", "5 g", "Gold", "Female", "16"),
            Product("Silver Bracelet", "800 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.silver_bracelet}"), "Bracelets", "7 g", "Silver", "Female", "onesize"),
            Product("Pearl Earrings", "950 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.pearl_earrings}"), "Earrings", "3 g", "Silver", "Female", "onesize"),
            Product("Diamond Pendant", "8000 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.diamond_pendant}"), "Pendants", "10 g", "Gold", "Female", "onesize"),
            Product("Rose Brooch", "450 UAH", Uri.parse("android.resource://${packageName}/${R.drawable.rose_brooch}"), "Brooches", "2 g", "Silver", "Female", "onesize")
        )

        productList.clear()

        if (!serializedList.isNullOrEmpty()) {
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
        }

        // Додаємо стартові товари, яких ще нема
        val existingNames = productList.map { it.name }
        startProducts.forEach { if (!existingNames.contains(it.name)) productList.add(it) }

        saveProducts()
    }
}