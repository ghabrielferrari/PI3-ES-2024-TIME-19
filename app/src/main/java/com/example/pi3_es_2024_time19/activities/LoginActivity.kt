package com.example.pi3_es_2024_time19.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.pi3_es_2024_time19.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setContentView(binding.root)

        initFirebaseAuth()

        binding.btnLogin.setOnClickListener {
            val emailText = binding.tvEmail.text
            val passwordText = binding.tvPassword.text

            if (emailText?.isEmpty() == true) {
                showSnack("You Forgot Your Email Address")

            } else if (passwordText?.isEmpty() == true) {
                showSnack("You Forgot Your Password")

            } else {
                val email = binding.tvEmail.text.toString()
                val password = binding.tvPassword.text.toString()
                signIn(email, password)
            }
            hideKeyboard()
        }

        binding.btnCreateAccount.setOnClickListener({
            val intent: Intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        })

    }

    private fun signIn(email: String,password: String) {
        auth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User Logged In :)", Toast.LENGTH_SHORT).show()
                openMainActivity()
            } else {
                Toast.makeText(this, "(!) Login Failed - Check Connection", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun initFirebaseAuth() {
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    private fun openMainActivity() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun hideKeyboard() {
        // Get input manager and hide keyboard
        val imm = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun showSnack(message: String) {
        // Create Snackbar
        val snack = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setAction("OKAY", {}).show()
    }

    private fun setupBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
    }
}