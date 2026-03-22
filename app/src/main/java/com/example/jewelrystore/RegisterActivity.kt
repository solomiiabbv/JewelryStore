package com.example.jewelrystore

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etBirthday = findViewById<EditText>(R.id.etBirthday)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnGoLogin = findViewById<Button>(R.id.btnGoLogin)
        val ivLogo = findViewById<ImageView>(R.id.ivLogo)

        ivLogo.setOnClickListener {
            Toast.makeText(this, "Ювелірний магазин", Toast.LENGTH_SHORT).show()
        }

        // Календар для дати народження
        etBirthday.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, { _, y, m, d ->
                val monthStr = (m + 1).toString().padStart(2, '0')
                val dayStr = d.toString().padStart(2, '0')
                etBirthday.setText("$dayStr/$monthStr/$y")
            }, year, month, day)
            dpd.show()
        }

        // Повернення на екран логіну
        btnGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Реєстрація
        btnRegister.setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val email = etEmail.text.toString()
            val username = etUsername.text.toString()
            val birthday = etBirthday.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                username.isEmpty() || birthday.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                Toast.makeText(this, "Заповніть усі поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Перевірка email на правильний формат
            if(!email.endsWith("@")){
                Toast.makeText(this, "Email повинен містити правильний формат (@gmail.com)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(password != confirmPassword){
                Toast.makeText(this, "Паролі не співпадають", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Зберігаємо дані користувача
            val userPref = getSharedPreferences("user", MODE_PRIVATE)
            userPref.edit().apply {
                putString("firstName", firstName)
                putString("lastName", lastName)
                putString("email", email)
                putString("username", username)
                putString("birthday", birthday)
                putString("password", password)
            }.apply()

            Toast.makeText(this, "Реєстрація успішна", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}