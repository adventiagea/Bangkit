package com.dicoding.picodiploma.libas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.dicoding.picodiploma.libas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton: ImageButton = findViewById(R.id.button_login)
        loginButton.setOnClickListener{
            val intent = Intent(this, PredictionActivity::class.java)
            startActivity(intent)
        }

        val registerUser: TextView = findViewById(R.id.text_register)
        registerUser.setOnClickListener {
            val intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
        }

    }
}