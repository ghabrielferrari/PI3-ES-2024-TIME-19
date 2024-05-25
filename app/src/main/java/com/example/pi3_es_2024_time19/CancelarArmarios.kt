package com.example.pi3_es_2024_time19

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi3_es_2024_time19.databinding.ActivityCancelarArmariosBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class CancelarArmarios : AppCompatActivity() {
    private lateinit var binding: ActivityCancelarArmariosBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var uid: String
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelarArmariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore
        auth = Firebase.auth

        deletarArmarios()

        val handler = Handler()
        handler.postDelayed({
            showVerificationDialog(
                "sucesso",
                "armarios cancelados",
                "ok",
                "",
                ::goToMainActivity
            )
        },
            1500
        )
    }

    private fun showVerificationDialog(title: String, message: String, positiveBtnText: String, negativeBtnText: String, positiveAction: () -> Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage(message)
            .setTitle(title)
            .setPositiveButton(positiveBtnText) { _, _ ->
                positiveAction()
            }
            .setNegativeButton(negativeBtnText) { dialog, _ ->
                dialog.dismiss()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun doNothing(): Boolean {
        return true
    }

    private fun deletarArmarios() {
        db.collection("locacao")
            .whereEqualTo("uid", auth.currentUser?.uid)
            .get()
            .continueWithTask { querySnapshotTask ->
                // Delete each document in the query results
                val batch = db.batch()
                querySnapshotTask.result?.documents?.forEach { document ->
                    batch.delete(document.reference)
                }
                batch.commit()
            }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}