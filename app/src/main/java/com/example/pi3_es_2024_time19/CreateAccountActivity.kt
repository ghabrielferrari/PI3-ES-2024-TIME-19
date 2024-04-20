package com.example.pi3_es_2024_time19

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
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class CreateAccountActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private var day = 0
    private var month = 0
    private var year = 0
    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private lateinit var auth: FirebaseAuth

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
                    val user = auth.currentUser
                    // You can add further actions here after successful sign up
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(this, "Sign up failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}