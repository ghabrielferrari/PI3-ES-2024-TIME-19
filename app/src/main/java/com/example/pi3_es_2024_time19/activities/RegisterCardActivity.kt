package com.example.pi3_es_2024_time19.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3_es_2024_time19.R
import com.example.pi3_es_2024_time19.databinding.ActivityRegisterCardBinding
import com.example.pi3_es_2024_time19.models.Card
import com.example.pi3_es_2024_time19.models.User
import com.example.pi3_es_2024_time19.utils.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterCardActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterCardBinding.inflate(layoutInflater)
    }

    private lateinit var numberCard: String
    private lateinit var fullName: String
    private lateinit var CPF: String
    private lateinit var expirationDate: String
    private lateinit var CCV: String

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val currentUser = firebaseAuth.currentUser
        initializeClickEvents(currentUser)
    }

    private fun initializeClickEvents(currentUser: FirebaseUser?) {
        currentUser?.let {
            val user = User(
                id = it.uid,
                name = it.displayName ?: "",
                email = it.email ?: ""
            )
            binding.btnAddCard.setOnClickListener {
                Log.i("RegisterCardActivity", "Botão adicionar cartão clicado.")
                if (validateFields()) {
                    val card = Card(user.id, numberCard, fullName, CPF, expirationDate, CCV)
                    saveCardFirestore(card)
                    showDialogSuccess(card)
                } else {
                    Log.i("RegisterCardActivity", "Validação de campos falhou.")
                }
            }
        } ?: run {
            Log.i("RegisterCardActivity", "Usuário atual é nulo.")
        }
    }

    private fun showDialogSuccess(card: Card) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Cartão Adicionado")
            .setMessage("Sucesso!")
            .setPositiveButton("OK") { _, _ ->
                val intent = Intent()
                intent.putExtra(EXTRA_CARD, card)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            .show()
    }

    private fun saveCardFirestore(card: Card) {
        firestore
            .collection("cartoes")
            .document(card.id)
            .set(card)
            .addOnSuccessListener {
                showToast("Sucesso ao adicionar o cartao")
            }.addOnFailureListener {
                showToast("Erro ao fazer seu cadastro")
            }
    }

    private fun validateFields(): Boolean {
        numberCard = binding.editNumberCard.text.toString()
        fullName = binding.editFullName.text.toString()
        CPF = binding.editCPF.text.toString()
        expirationDate = binding.editExpirationDate.text.toString()
        CCV = binding.editCCV.text.toString()

        if (numberCard.isNotEmpty() && fullName.isNotEmpty() && CPF.isNotEmpty() &&
            expirationDate.isNotEmpty() && CCV.isNotEmpty()
        ) {
            return true
        }

        showToast("Todos os campos são obrigatórios.")
        return false
    }

    companion object {
        const val EXTRA_CARD = "extra_card"
    }
}
