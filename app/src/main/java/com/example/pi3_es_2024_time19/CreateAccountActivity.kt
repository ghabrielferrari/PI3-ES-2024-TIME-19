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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.core.FirestoreClient
import com.google.firebase.firestore.firestore
import java.util.Calendar

class CreateAccountActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private val binding by lazy {
        ActivityCreateAccountBinding.inflate(layoutInflater)
    }
    private var day = 0
    private var month = 0
    private var year = 0
    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var cpf: Number
    private lateinit var telefone: String
    private lateinit var nome_completo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnExit.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnDatePickerTrigger.setOnClickListener{
            pickDate()
        }

        binding.btnCreateAccount.setOnClickListener {
            // TODO Create Account
            signUp("breno19a.f@gmail.com", "123456")
        }
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
        val dateTextView = findViewById<TextView>(R.id.tvDateText)
        dateTextView.setText("$savedDay/$savedMonth/$savedYear")
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success, update UI with the signed-in user's information
                    val user = auth.currentUser as FirebaseUser
                    val user_data = getUserData(user)
                    addUserDataToCollection(user_data)

                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(this, "Sign up failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createAccount() {
        email = binding.tvEmail.text.toString()
        password = binding.tvPassword.text.toString()
        val confirmation_password = binding.tvConfirmPassword.toString()
        nome_completo = binding.tvCompleteName.text.toString()
        cpf = binding.tvCpf.text.toString().toInt()
        telefone = binding.tvPhoneNum.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, "WARNING! - Email field is empty", Toast.LENGTH_SHORT).show()
        } else {
            if (password.isEmpty()) {
                Toast.makeText(this, "WARNING! - Password field is empty", Toast.LENGTH_SHORT).show()
            } else {
                if (confirmation_password.isEmpty()) {
                    Toast.makeText(this, "WARNING! - Confirm your Password", Toast.LENGTH_SHORT).show()
                } else {
                    if (nome_completo.isEmpty()) {
                        Toast.makeText(this, "WARNING! - Enter your Full Name", Toast.LENGTH_SHORT).show()
                    } else {
                        if ((cpf as Int) < 10000000000) {
                            Toast.makeText(this, "WARNING! - CPF must have 11 digits", Toast.LENGTH_SHORT).show()
                        } else {
                            if (telefone.isEmpty()) {
                                Toast.makeText(this, "WARNING! - Type your phone number", Toast.LENGTH_SHORT).show()
                            } else {
                                if (savedYear == 0) {
                                    Toast.makeText(this, "WARNING! - Choose your birth date", Toast.LENGTH_SHORT).show()
                                } else {
                                    signUp(email, password)
                                }
                            }
                        }
                    }

                    if (password == confirmation_password) {

                    } else {
                        Toast.makeText(this, "Verify your Connection! - Could not Sign Up", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getUserData(user: FirebaseUser): HashMap<Any, Any> {

        val user_data = hashMapOf(
            "uid" to user?.uid,
            "cpf" to "FAKE CPF",
            "data_nascimento" to "2001-11-19",
            "telefone" to "+5551983551975"
        ) as HashMap<Any, Any>
        return user_data
    }

    private fun addUserDataToCollection(user_data: HashMap<Any, Any>) {
        db.collection("user_data")
            .add(user_data)
            .addOnSuccessListener {
                Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }

}