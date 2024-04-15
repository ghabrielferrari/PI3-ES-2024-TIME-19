package com.example.pi3_es_2024_time19

import android.content.Context
import android.content.Intent
import android.inputmethodservice.Keyboard
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.example.pi3_es_2024_time19.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlin.concurrent.timerTask

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val emailField = binding.tvEmail
            if (emailField.text?.isEmpty() == false) {
                // Create Snackbar
                val snack = Snackbar.make(binding.root, emailField.text.toString(), Snackbar.LENGTH_SHORT)
                    .setAction("OKAY", {})
                // Get input manager and hide keyboard
                val imm = getSystemService(InputMethodManager::class.java)
                imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                // Show Snackbar
                snack.show()

                openMainActivity()
            }
        }

        binding.btnCreateAccount.setOnClickListener({
            val intent: Intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        })

    }

    private fun openMainActivity() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}