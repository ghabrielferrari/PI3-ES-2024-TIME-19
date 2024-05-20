package com.example.pi3_es_2024_time19

import android.app.DatePickerDialog
import android.content.Intent
import android.media.MediaCodec
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3_es_2024_time19.databinding.ActivityCreateAccountBinding
import com.example.pi3_es_2024_time19.models.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern
import java.util.Calendar

class CreateAccountActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityCreateAccountBinding
    private var day = 0
    private var month = 0
    private var year = 0
    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0

    private lateinit var email: String
    private lateinit var password: String
    private var cpf: Long = 0
    private lateinit var telefone: String
    private lateinit var nome_completo: String

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setContentView(binding.root)

        // Initialize Firebase features objects
        initFirebaseAuth()
        initFirebaseFirestore()

        binding.btnExit.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnDatePickerTrigger.setOnClickListener{
            pickDate()
        }

        binding.btnCreateAccount.setOnClickListener {
            if (validateFields()) {
                signUp()
            }
        }
    }

    private fun setupBinding() {
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
    }

    private fun initFirebaseAuth() {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    private fun initFirebaseFirestore() {
        db = FirebaseFirestore.getInstance()
    }

    private fun getDateCalendar() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun pickDate() {
        getDateCalendar()
        DatePickerDialog(this, this, year, month, day).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedYear = year
        savedMonth = month + 1
        savedDay = dayOfMonth
        showToast("$savedDay/$savedMonth/$savedYear")
        val dateTextView = findViewById<TextView>(R.id.tvDateText)
        dateTextView.text = "$savedDay/$savedMonth/$savedYear"
    }

    private fun signUp() {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    if (user != null) {
                        saveUserDataToFirestore(user)
                        showToast("Usuário criado com sucesso")
                    } else {
                        showToast("Usuário não encontrado após criar a conta")
                    }
                } else {
                    // If sign up fails, display a message to the user.
                    showToast("Falha ao criar a conta: ${task.exception?.message}")
                }
            }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        val emailText = binding.tvEmail.text
        val passwordText = binding.tvPassword.text
        val confirmationPasswordText = binding.tvConfirmPassword.text
        val nomeCompletoText = binding.tvCompleteName.text
        val cpfText = binding.tvCpf.text
        val telefoneText = binding.tvPhoneNum.text

        if (nomeCompletoText.isNullOrEmpty()) {
            binding.tvCompleteName.error = "Digite seu Nome Completo"
            isValid = false
        }
        if (emailText.isNullOrEmpty()) {
            binding.tvEmail.error = "Digite seu email"
            isValid = false
        }
        if(!isEmailValid(emailText.toString())) {
            binding.tvEmail.error = "Email está incorreto"
            isValid = false
        }
        // Try to transform String to Long
        try {
            cpf = cpfText?.toString()?.toLong() ?: 0
        } catch (e: NumberFormatException) {
            isValid = false
        }
        if (cpfText.isNullOrEmpty() || cpf < 10000000000) {
            binding.tvCpf.error = "Digite seu CPF, deve ter 11 dígitos"
            isValid = false
        }
        if (telefoneText.isNullOrEmpty()) {
            binding.tvPhoneNum.error = "Digite seu telefone"
            isValid = false
        }
        if (savedYear == 0) {
            binding.tvDateText.setTextColor(getColor(R.color.danger))
            isValid = false
        }
        if (passwordText.isNullOrEmpty()) {
            binding.tvPassword.error = "Digite uma senha"
            isValid = false
        }
        if (confirmationPasswordText.isNullOrEmpty()) {
            binding.tvConfirmPassword.error = "Confirme sua senha"
            isValid = false
        }
        if (passwordText.toString() != confirmationPasswordText.toString()) {
            binding.tvPassword.error = "Senhas não são iguais"
            isValid = false
        }
        email = emailText.toString()
        password = passwordText.toString()
        nome_completo = nomeCompletoText.toString()
        telefone = telefoneText.toString()
        return isValid
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun saveUserDataToFirestore(user: FirebaseUser?) {
        val user_data = UserData(
            uid = user?.uid ?: "",
            nome_completo = nome_completo,
            cpf = cpf,
            data_nascimento = "$savedYear-$savedMonth-$savedDay",
            email= email,
            telefone = telefone,
            isManager = false
        )
        db.collection("user_data")
            .add(user_data)
            .addOnSuccessListener {
                showToast("Dados do usuário salvos no Firestore")
                openMainActivity()
            }
            .addOnFailureListener {
                showToast("Não foi possível salvar os dados do usuário no Firestore")
            }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}