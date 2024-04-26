package com.example.pi3_es_2024_time19

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class RecuperarSenhaActivity : AppCompatActivity() {

    private lateinit var etEmail: TextInputLayout
    private lateinit var buttonSend: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperarsenha)

        etEmail = findViewById(R.id.etEmail)
        buttonSend = findViewById(R.id.buttonSend)

        auth = FirebaseAuth.getInstance()

        buttonSend.setOnClickListener {
            val sEmail = etEmail.editText?.text.toString()
            sendPasswordReset(sEmail)
        }

        buttonSend.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //funcao recuperar senha
    private fun sendPasswordReset(emailAddress: String) {
        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    Toast.makeText(baseContext, "Email enviado!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}


