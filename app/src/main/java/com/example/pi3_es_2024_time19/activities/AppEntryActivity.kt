package com.example.pi3_es_2024_time19.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3_es_2024_time19.R
import com.example.pi3_es_2024_time19.databinding.ActivityAppEntryBinding
import com.example.pi3_es_2024_time19.utils.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class AppEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppEntryBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppEntryBinding.inflate((layoutInflater))
        setContentView(R.layout.activity_app_entry)

        initFirebaseAuth()
        verifyLogin()
    }

    private fun verifyLogin() {
        val handler = Handler()

        if (auth.currentUser != null) {
            handler.postDelayed({
                showToast("${auth.currentUser}")
                goToMainActivity()
            }, 2000)

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

    private fun initFirebaseAuth() {
        auth = Firebase.auth
    }
}