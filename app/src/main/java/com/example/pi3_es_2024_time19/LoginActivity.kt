package com.example.pi3_es_2024_time19

import android.content.Context
import android.content.Intent
import android.inputmethodservice.Keyboard
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import com.example.pi3_es_2024_time19.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlin.concurrent.timerTask

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var btnRecoveryPassword: Button
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setContentView(binding.root)

        initFirebaseAuth()

        btnRecoveryPassword = binding.btnRecoverPassword
        btnRecoveryPassword.setOnClickListener {
            startActivity(
                Intent(this, RecoveryPasswordActivity::class.java)
            )
        }

        binding.btnLogin.setOnClickListener {
            val emailText = binding.tvEmail.text
            val passwordText = binding.tvPassword.text

            if (emailText?.isEmpty() == true) {
                binding.tlEmail.error = "Digite seu email"

            }
            if (passwordText?.isEmpty() == true) {
                binding.tlPassword.error = "Digite sua senha"

            }
            if (emailText?.isNotEmpty() == true && passwordText?.isNotEmpty() == true) {
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
                playSound(R.raw.coin_sfx)
                openMainActivity()
            } else {
                binding.tlEmail.error = "."
                binding.tlPassword.error = "email ou senha incorreta"
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

    private fun playSound(rawFile: Int) {
        if (!this::mediaPlayer.isInitialized) {
            mediaPlayer = MediaPlayer.create(this, rawFile)
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
        } else {
            mediaPlayer.start()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}