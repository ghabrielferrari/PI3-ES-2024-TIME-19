package com.example.pi3_es_2024_time19

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi3_es_2024_time19.databinding.ActivityAppEntryBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppEntryBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppEntryBinding.inflate((layoutInflater))
        setContentView(R.layout.activity_app_entry)

        initFirebaseAuth()
        verifyLogin()
    }

    private fun verifyLogin() {
        val handler = Handler()
        setLogText("Conectando...")

        if (auth.currentUser != null) {
            handler.postDelayed({
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

    private fun setLogText(text: String) {
        binding.logText.setText(text)
    }
}