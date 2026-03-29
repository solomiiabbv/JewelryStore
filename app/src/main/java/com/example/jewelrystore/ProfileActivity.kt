package com.example.jewelrystore

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var tvBirthDate: TextView
    private lateinit var ivProfile: ImageView
    private lateinit var btnChangePhoto: Button
    private lateinit var btnSave: Button

    private var imageUri: Uri? = null

    private val PREFS = "profile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        tvBirthDate = findViewById(R.id.tvBirthDate)
        ivProfile = findViewById(R.id.ivProfile)
        btnChangePhoto = findViewById(R.id.btnChangePhoto)
        btnSave = findViewById(R.id.btnSave)

        loadProfile()

        // 📅 DatePicker
        tvBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    tvBirthDate.text = "$day/${month + 1}/$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 📸 Фото
        btnChangePhoto.setOnClickListener {
            val options = arrayOf("Камера", "Галерея")
            AlertDialog.Builder(this)
                .setTitle("Оберіть фото")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> openCamera()
                        1 -> openGallery()
                    }
                }.show()
        }

        // 💾 Зберегти
        btnSave.setOnClickListener {
            saveProfile()
            Toast.makeText(this, "Збережено!", Toast.LENGTH_SHORT).show()
        }
    }

    // ===== Камера =====
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                val uri = Uri.parse(
                    MediaStore.Images.Media.insertImage(
                        contentResolver,
                        bitmap,
                        null,
                        null
                    )
                )
                imageUri = uri
                ivProfile.setImageURI(uri)
            }
        }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    // ===== Галерея =====
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
                ivProfile.setImageURI(uri)
            }
        }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    // ===== Збереження =====
    private fun saveProfile() {
        val pref = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        pref.edit().apply {
            putString("name", etName.text.toString())
            putString("email", etEmail.text.toString())
            putString("birth", tvBirthDate.text.toString())
            putString("image", imageUri?.toString())
            apply()
        }
    }

    // ===== Завантаження =====
    private fun loadProfile() {
        val pref = getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        etName.setText(pref.getString("name", ""))
        etEmail.setText(pref.getString("email", ""))
        tvBirthDate.text = pref.getString("birth", "Дата народження")

        val image = pref.getString("image", null)
        if (image != null) {
            ivProfile.setImageURI(Uri.parse(image))
        }
    }
}