package com.example.jewelrystore

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductsActivity : AppCompatActivity() {

    private var productList = mutableListOf<Product>()
    private var fullProductList = mutableListOf<Product>() // Для пошуку
    private lateinit var adapter: ProductAdapter

    private val prefsName = "ProductsPrefs"
    private val keyProducts = "products"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val rvProducts: RecyclerView = findViewById(R.id.rvProducts)
        val btnClose: ImageButton = findViewById(R.id.btnClose)
        val btnAdd: Button = findViewById(R.id.btnAdd)
        val etSearch: EditText = findViewById(R.id.etSearch) // Поле пошуку
        val tvTitle: TextView = findViewById(R.id.tvTitle)
        tvTitle.text = "Список товарів"

        loadProducts()

        adapter = ProductAdapter(
            productList,
            onClick = { product ->
                Toast.makeText(this, "Вибрано: ${product.name}", Toast.LENGTH_SHORT).show()
            },
            onEditClick = { product, position ->
                showEditProductDialog(product, position)
            },
            onDeleteClick = { product, _ ->
                // Видаляємо з обох списків
                productList.remove(product)
                fullProductList.remove(product)
                adapter.notifyDataSetChanged()
                saveProducts()
            }
        )

        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = adapter
        btnClose.setOnClickListener { finish() }
        btnAdd.setOnClickListener { showAddProductDialog() }

        // Логіка пошуку
        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) { filter(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filter(query: String) {
        val filtered = if (query.isEmpty()) {
            fullProductList
        } else {
            fullProductList.filter { it.name.lowercase().contains(query.lowercase()) }
        }
        productList.clear()
        productList.addAll(filtered)
        adapter.notifyDataSetChanged()
    }

    private fun showAddProductDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_product_full, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val ivSelectImage = dialogView.findViewById<ImageView>(R.id.ivSelectImage)

        ivSelectImage.setImageResource(R.drawable.ic_launcher_foreground)

        AlertDialog.Builder(this)
            .setTitle("Додати товар")
            .setView(dialogView)
            .setPositiveButton("Додати") { _, _ ->
                val name = etName.text.toString()
                val price = etPrice.text.toString()

                // Створюємо новий товар (можна додати рандомну картинку з ваших drawable)
                val newProduct = Product(name, price, R.drawable.gold_ring)

                fullProductList.add(newProduct)
                filter("") // Оновити список на екрані
                saveProducts()
            }
            .setNegativeButton("Відміна", null)
            .show()
    }

    private fun showEditProductDialog(product: Product, position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_product_full, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val ivSelectImage = dialogView.findViewById<ImageView>(R.id.ivSelectImage)

        etName.setText(product.name)
        etPrice.setText(product.price)
        ivSelectImage.setImageResource(product.imageResId)

        AlertDialog.Builder(this)
            .setTitle("Редагувати")
            .setView(dialogView)
            .setPositiveButton("Зберегти") { _, _ ->
                product.name = etName.text.toString()
                product.price = etPrice.text.toString()
                adapter.notifyItemChanged(position)
                saveProducts()
            }
            .setNegativeButton("Відміна", null)
            .show()
    }

    private fun saveProducts() {
        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val serializedList = fullProductList.joinToString(";;") { product ->
            val imageName = try {
                resources.getResourceEntryName(product.imageResId)
            } catch (e: Exception) { "gold_ring" }
            "${product.name}|${product.price}|$imageName"
        }
        editor.putString(keyProducts, serializedList)
        editor.apply()
    }

    private fun loadProducts() {
        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val serializedList = prefs.getString(keyProducts, null)

        fullProductList.clear()

        if (!serializedList.isNullOrEmpty()) {
            val items = serializedList.split(";;")
            for (item in items) {
                val parts = item.split("|")
                if (parts.size >= 3) {
                    var resId = resources.getIdentifier(parts[2], "drawable", packageName)
                    if (resId == 0) resId = R.drawable.gold_ring // Запасний варіант
                    fullProductList.add(Product(parts[0], parts[1], resId))
                }
            }
        } else {
            fullProductList.addAll(getStartProducts())
            saveProducts()
        }
        productList.clear()
        productList.addAll(fullProductList)
    }

    private fun getStartProducts(): List<Product> = listOf(
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
}