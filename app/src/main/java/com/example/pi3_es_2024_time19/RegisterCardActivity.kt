package com.example.pi3_es_2024_time19.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3_es_2024_time19.databinding.ActivityRegisterCardBinding
import com.example.pi3_es_2024_time19.models.Card
import com.example.pi3_es_2024_time19.models.User
import com.example.pi3_es_2024_time19.utils.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterCardBinding

    private lateinit var numberCard: String
    private lateinit var fullName: String
    private lateinit var CPF: String
    private lateinit var expirationDate: String
    private lateinit var CCV: String

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var user: User
    private lateinit var intent: Intent

    companion object {
        const val EXTRA_CARD = "extra_card"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth

        val currentUser = firebaseAuth.currentUser
        initializeClickEvents(currentUser)
    }

    private fun initializeClickEvents(currentUser: FirebaseUser?) {
        currentUser?.let {
            user = User(
                id = it.uid,
                name = it.displayName ?: "",
                email = it.email ?: ""
            )
            binding.btnAddCard.setOnClickListener {
                Log.i("RegisterCardActivity", "Botão adicionar cartão clicado.")
                if (validateFields()) {
                    // Fields are valid, proceed with card registration
                } else {
                    Log.i("RegisterCardActivity", "Validação de campos falhou.")
                }
            }
        } ?: run {
        }
    }

    private fun validateFields(): Boolean {
        numberCard = binding.editNumberCard.text.toString()
        fullName = binding.editFullName.text.toString()
        CPF = binding.editCPF.text.toString()
        expirationDate = binding.editExpirationDate.text.toString()
        CCV = binding.editCCV.text.toString()

        if (numberCard.length != 16 || !numberCard.matches(Regex("\\d{16}"))) {
            showToast("Número de cartão inválido. Deve ter 16 dígitos.")
            return false
        }

        if (fullName.isBlank() || !fullName.matches(Regex("^[a-zA-ZÀ-ú ]+$")) || fullName.length > 50) {
            showToast("Nome deve ter apenas letras e no máximo 50 caracteres.")
            return false
        }

        if (CPF.length != 11 || !CPF.matches(Regex("\\d{11}"))) {
            showToast("CPF inválido. Deve ter 11 dígitos.")
            return false
        }

        if (!expirationDate.matches(Regex("^(0[1-9]|1[0-2])(24|25|26|27|28|29|30)$"))) {
            showToast("Data de vencimento inválida. Formato MM/YY.")
            return false
        }

        if (CCV.length != 3 || !CCV.matches(Regex("\\d{3}"))) {
            showToast("CCV inválido. Deve ter 3 dígitos.")
            return false
        }

        // Check uniqueness of numberCard
        return checkUniqueNumberCard(numberCard)
    }

    private fun checkUniqueNumberCard(numberCard: String): Boolean {
        var isNumberCardUnique = true

        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("cartoes")
        collection.whereEqualTo("numberCard", numberCard)
            .whereEqualTo("id", user.id)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    showToast("Número de cartão já cadastrado.")
                    isNumberCardUnique = false
                } else {
                    // Check uniqueness of fullName
                    checkUniqueName(fullName)
                }
            }
            .addOnFailureListener {
                showToast("Erro ao verificar número do cartão.")
                isNumberCardUnique = false
            }

        return isNumberCardUnique
    }

    private fun checkUniqueName(name: String): Boolean {
        var isNameUnique = true

        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("cartoes")
        collection.whereEqualTo("fullName", name)
            .whereEqualTo("id", user.id)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    showToast("Nome já cadastrado.")
                    isNameUnique = false
                } else {
                    // All fields are valid and unique
                    proceedWithCardRegistration()
                }
            }
            .addOnFailureListener {
                showToast("Erro ao verificar nome.")
                isNameUnique = false
            }

        return isNameUnique
    }

    private fun proceedWithCardRegistration() {
        val card = Card(user.id, numberCard, fullName, CPF, expirationDate, CCV)
        sendCardToPaymentFragment(card)
        showDialogSuccess(card)
    }

    private fun showDialogSuccess(card: Card) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Cartão Adicionado")
            .setMessage("Sucesso!")
            .setPositiveButton("OK") { _, _ ->
                intent.putExtra(EXTRA_CARD, card)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            .show()
    }

    private fun sendCardToPaymentFragment(card: Card) {
        intent = Intent()
        intent.putExtra(EXTRA_CARD, card)
        setResult(Activity.RESULT_OK, intent)
    }
}