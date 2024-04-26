package com.example.pi3_es_2024_time19

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.text.Editable
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import com.example.pi3_es_2024_time19.databinding.ActivityCreateAccountBinding
import com.example.pi3_es_2024_time19.databinding.ActivityMainBinding
import com.example.pi3_es_2024_time19.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.core.FirestoreClient
import com.google.firebase.firestore.firestore
import java.util.Calendar
import com.example.pi3_es_2024_time19.models.UserData
import com.example.pi3_es_2024_time19.utils.showToast
import com.google.firebase.auth.auth

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
    private var db = Firebase.firestore

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
                showToast("SignUp")
                signUp()
            }

        }
    }

    private fun setupBinding() {
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
    }

    private fun initFirebaseAuth() {
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    private fun initFirebaseFirestore() {
        db = Firebase.firestore
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

    private fun signIn(email: String,password: String) {
        auth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User Logged In :)", Toast.LENGTH_SHORT).show()
                openMainActivity()
            } else {
                Toast.makeText(this, "(!) Login Failed - Check Connection", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signUp() {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success, update UI with the signed-in user's information
                    val user = auth.currentUser as FirebaseUser
                    saveUserDataToFirestore(user)
                    signIn(email, password)

                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(this, "Sign up failed.", Toast.LENGTH_SHORT).show()
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

        if (nomeCompletoText?.isEmpty() == true) {
            binding.tvCompleteName.error = "Digite seu Nome Completo"
            isValid = false
        }
        if (emailText?.isEmpty() == true) {
            binding.tvEmail.error = "Digite seu email"
            isValid = false
        }
        // Try to transform String to Long
        try {
            cpf = cpfText.toString().toLong()
        } catch (e: NumberFormatException) {}
        if (cpfText?.isEmpty() == true || cpf < 10000000000) {
            binding.tvCpf.error = "Digite seu CPF, deve ter 11 dígitos"
            isValid = false
        }
        if (telefoneText?.isEmpty() == true) {
            binding.tvPhoneNum.error = "Digite seu telefone"
            isValid = false
        }
        if (savedYear == 0) {
            binding.tvDateText.setTextColor(getColor(R.color.danger))
            isValid = false
        }
        if (passwordText?.isEmpty() == true) {
            binding.tvPassword.error = "Digite umma senha"
            isValid = false
        }
        if (confirmationPasswordText?.isEmpty() == true) {
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

    private fun saveUserDataToFirestore(user: FirebaseUser) {
        val user_data = UserData(
            uid = user.uid,
            nome_completo = nome_completo,
            cpf = cpf,
            data_nascimento = "$year-$month-$day",
            telefone = telefone
        )
        db.collection("user_data")
            .add(user_data)
            .addOnSuccessListener {
                showToast("Usuário criado com sucessso")
            }
            .addOnFailureListener {
                showToast("Não foi possível criar sua conta :(, tente novamente ou verifique sua internet")
            }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}