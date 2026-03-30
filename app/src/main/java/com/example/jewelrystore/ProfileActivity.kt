package com.example.jewelrystore

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var ivProfilePhoto: ImageView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var tvBirthDate: TextView
    private lateinit var btnSaveProfile: Button
    private lateinit var btnBack: ImageButton

    private val PICK_IMAGE_REQUEST = 100
    private val CAMERA_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        ivProfilePhoto = findViewById(R.id.ivProfilePhoto)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        tvBirthDate = findViewById(R.id.tvBirthDate)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)
        btnBack = findViewById(R.id.btnBack)

        loadUserData()

        // Фото профілю
        ivProfilePhoto.setOnClickListener { showImagePicker() }

        // Вибір дати народження
        tvBirthDate.setOnClickListener { pickDate() }

        // Збереження даних
        btnSaveProfile.setOnClickListener { saveProfileData() }

        // Кнопка назад
        btnBack.setOnClickListener { finish() }
    }

    private fun showImagePicker() {
        val options = arrayOf("Галерея", "Камера")
        AlertDialog.Builder(this)
            .setTitle("Оберіть джерело")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> { // Галерея
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(intent, PICK_IMAGE_REQUEST)
                    }
                    1 -> { // Камера
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, CAMERA_REQUEST)
                    }
                }
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        ivProfilePhoto.setImageURI(it)
                        saveUriToInternalStorage(it)
                    }
                }
                CAMERA_REQUEST -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        ivProfilePhoto.setImageBitmap(it)
                        saveBitmapToInternalStorage(it)
                    }
                }
            }
        }
    }

    private fun saveUriToInternalStorage(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        bitmap?.let { saveBitmapToInternalStorage(it) }
        inputStream?.close()
    }

    private fun saveBitmapToInternalStorage(bitmap: Bitmap) {
        val file = File(filesDir, "profile_photo.jpg")
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    }

    private fun loadUserData() {
        val pref = getSharedPreferences("user", MODE_PRIVATE)
        etName.setText(pref.getString("firstName", "Адміністратор"))
        etEmail.setText(pref.getString("email", "admin@example.com"))
        tvBirthDate.text = pref.getString("birthDate", "Виберіть дату")

        val photoFile = File(filesDir, "profile_photo.jpg")
        if (photoFile.exists()) {
            ivProfilePhoto.setImageBitmap(BitmapFactory.decodeFile(photoFile.absolutePath))
        } else {
            ivProfilePhoto.setImageResource(R.drawable.ic_admin_placeholder)
        }
    }

    private fun saveProfileData() {
        val pref = getSharedPreferences("user", MODE_PRIVATE)
        pref.edit().apply {
            putString("firstName", etName.text.toString())
            putString("email", etEmail.text.toString())
            putString("birthDate", tvBirthDate.text.toString())
            apply()
        }
        Toast.makeText(this, "Дані збережено", Toast.LENGTH_SHORT).show()
    }

    private fun pickDate() {
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = "${dayOfMonth}/${month + 1}/$year"
                tvBirthDate.text = date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }
}