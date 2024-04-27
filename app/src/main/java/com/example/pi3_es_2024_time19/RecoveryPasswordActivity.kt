package com.example.pi3_es_2024_time19

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3_es_2024_time19.models.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RecoveryPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery_password)

        val editTelefoneRecoveryPassword: EditText = findViewById(R.id.editTelefoneRecoveryPassword)
        val btnRecoverPassword: Button = findViewById(R.id.btnRecoverPassword)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        btnRecoverPassword.setOnClickListener {
            val telefone = editTelefoneRecoveryPassword.text.toString()

            if (telefone.isNotEmpty()) {
                buscarNumeroTelefone(telefone)
            } else {
                showToast("Por favor, insira seu número de telefone.")
            }
        }
    }

    private fun buscarNumeroTelefone(telefone: String) {
        Log.i("RecoveryPassword", "Buscando número de telefone: $telefone")

        db.collection("user_data")
            .whereEqualTo("telefone", telefone)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    showToast("Número de telefone não encontrado.")
                    Log.i("RecoveryPassword", "Número de telefone não encontrado.")
                } else {
                    for (document in documents) {
                        val userData = document.toObject(UserData::class.java)
                        Log.i("RecoveryPassword", "Número de telefone encontrado: ${userData.telefone}")
                        // Enviar e-mail de recuperação de senha
                        enviarEmailRecuperacaoSenha(userData.email)
                        return@addOnSuccessListener
                    }
                }
            }
            .addOnFailureListener { e ->
                showToast("Erro ao buscar número de telefone: ${e.message}")
                Log.i("RecoveryPassword", "Erro ao buscar número de telefone", e) // Adicione este log de erro
            }
    }

    private fun enviarEmailRecuperacaoSenha(email: String) {
        Log.i("RecoveryPassword", "Enviando e-mail de recuperação de senha para: $email")

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("E-mail de recuperação de senha enviado para $email.")
                    Log.i("RecoveryPassword", "E-mail de recuperação de senha enviado com sucesso para: $email")
                    // Aqui você pode iniciar a LoginActivity após enviar o e-mail com sucesso
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    showToast("Erro ao enviar e-mail de recuperação de senha.")
                    Log.i("RecoveryPassword", "Erro ao enviar e-mail de recuperação de senha", task.exception)
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}