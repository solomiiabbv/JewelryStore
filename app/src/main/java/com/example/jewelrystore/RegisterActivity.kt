package com.example.jewelrystore

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Поля з XML
        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etBirthday = findViewById<EditText>(R.id.etBirthday)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // DatePicker для дати народження
        etBirthday.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    calendar.set(year, month, dayOfMonth)
                    etBirthday.setText(sdf.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Натискання кнопки Реєстрації
        btnRegister.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val birthday = etBirthday.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            // Перевірки
            when {
                firstName.isEmpty() -> etFirstName.error = "Введіть ім'я"
                lastName.isEmpty() -> etLastName.error = "Введіть прізвище"
                birthday.isEmpty() -> etBirthday.error = "Введіть дату народження"
                email.isEmpty() -> etEmail.error = "Введіть email"
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> etEmail.error = "Невірний формат email"
                username.isEmpty() -> etUsername.error = "Введіть логін"
                password.length < 6 -> etPassword.error = "Мінімум 6 символів"
                password != confirmPassword -> etConfirmPassword.error = "Паролі не співпадають"
                else -> {
                    // Зберігаємо дані в SharedPreferences
                    val sharedPref = getSharedPreferences("jewel_store_prefs", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("firstName", firstName)
                        putString("lastName", lastName)
                        putString("birthday", birthday)
                        putString("email", email)
                        putString("username", username)
                        putString("password", password)
                        putBoolean("isAuthorized", true)
                        apply()
                    }
                    Toast.makeText(this, "Реєстрація успішна!", Toast.LENGTH_SHORT).show()
                    finish() // Закриваємо екран реєстрації і повертаємось на логін
                }
            }
        }
    }
}