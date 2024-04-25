package com.example.pi3_es_2024_time19

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3_es_2024_time19.databinding.ActivityRegisterCardBinding
import com.example.pi3_es_2024_time19.fragments.PaymentFragment
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
                if(validateFields()){
                    saveCardFirestore(user)
            }

        }
    }

    }

    private fun showDialogSucess(context: Context, onPositiveButtonClick: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Cartão Adicionado")
            .setMessage("Sucesso!")
            .setPositiveButton("OK") { _, _ ->
                onPositiveButtonClick()
            }
            .show()
    }

    private fun saveCardFirestore(user: User) {
        val card = Card(
            id = user.id,
            numberCard = numberCard,
            fullName = fullName,
            CPF = CPF,
            expirationDate = expirationDate,
            CCV = CCV
        )

        firestore
            .collection("cartoes")
            .document(card.id)
            .set(card)
            .addOnSuccessListener {
                showToast("Sucesso ao adicionar o cartao")
                showDialogSucess(this){
                    val fragment = PaymentFragment()
                    val transaction = supportFragmentManager.beginTransaction()
//                        transaction.replace(R.id.fragment_container, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
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

        if (numberCard.isNotEmpty()) {
            binding.textInputNumberCard.error = null
            if (fullName.isNotEmpty()) {
                binding.textInputFullName.error = null
                if (CPF.isNotEmpty()) {
                    binding.textInputCPF.error = null
                    if (expirationDate.isNotEmpty()) {
                        binding.textInputExpirationDate.error = null
                        if (CCV.isNotEmpty()) {
                            binding.textInputCCV.error = null
                            return true
                        } else {
                            binding.textInputCCV.error = "CVV é obrigatório"
                            return false
                        }
                    } else {
                        binding.textInputExpirationDate.error = "Vencimento é obrigatório"
                        return false
                    }
                } else {
                    binding.textInputCPF.error = "CPF é obrigatório"
                    return false
                }
            } else {
                binding.textInputFullName.error = "Nome completo é obrigatório"
                return false
            }
        } else {
            binding.textInputNumberCard.error = "Número do cartão é obrigatório"
            return false
        }
    }

}
