package com.example.jewelrystore

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val password = etPassword.text.toString()
            val confirm = etConfirmPassword.text.toString()

            if (password != confirm) {
                Toast.makeText(this, "Паролі не співпадають", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Реєстрація успішна", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}