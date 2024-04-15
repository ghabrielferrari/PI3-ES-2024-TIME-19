package com.example.pi3_es_2024_time19

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi3_es_2024_time19.databinding.ActivityAppEntryBinding

class AppEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppEntryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppEntryBinding.inflate((layoutInflater))
        setContentView(R.layout.activity_app_entry)

        verifyLogin()
    }

    private fun verifyLogin() {
        if (false) {
            TODO()
        } else {
            goToLogin()
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}