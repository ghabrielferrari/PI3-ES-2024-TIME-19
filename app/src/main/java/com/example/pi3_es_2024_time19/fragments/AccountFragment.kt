package com.example.pi3_es_2024_time19.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pi3_es_2024_time19.AppEntryActivity
import com.example.pi3_es_2024_time19.databinding.FragmentAccountBinding
import com.example.pi3_es_2024_time19.models.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.example.pi3_es_2024_time19.utils.showToast
import com.google.firebase.firestore.toObject

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var user: FirebaseUser
    private var user_data: UserData = UserData()
    private var isManager: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFirebaseAuth()
        initFirebaseFirestore()

        // Set current user
        user = auth.currentUser as FirebaseUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)

        binding.btnVerifyEmail.setOnClickListener {
            user.sendEmailVerification()
                .addOnSuccessListener {
                    Toast.makeText(context, "Email Enviado", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnLogout.setOnClickListener {
            Toast.makeText(context, "User Logged Out", Toast.LENGTH_SHORT).show()
            auth.signOut()
            openEntryActivity()
        }

        getUserData()

        return binding.root
    }


    private fun initFirebaseAuth() {
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    private fun initFirebaseFirestore() {
        db = Firebase.firestore
    }

    private fun openEntryActivity() {
        val intent = Intent(context, AppEntryActivity::class.java)
        startActivity(intent)
    }

    private fun getUserData() {
        val query = db.collection("user_data")
            .whereEqualTo("uid", "${user.uid}")
            .get()
            .addOnSuccessListener{ documents ->
                if (documents.isEmpty) {
                    Log.d("QUERY FAILED", "Size of query is zero")
                } else {
                    for (doc in documents) {
                        user_data = doc.toObject(UserData::class.java)
                        isManager = doc.get("isManager") as Boolean
                        Log.d("USER_DATA", "$user_data")
                    }
                    bindUserDataToView()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Erro ocorreu, verifique conexão e tente novamente!", Toast.LENGTH_SHORT).show()
            }

    }

    private fun bindUserDataToView() {
        binding.helloName.setText("Olá, ${user_data.nome_completo}")

        if (user.isEmailVerified) {
            binding.tvEmail.setText("${user.email} (Verificado)")
            binding.btnVerifyEmail.visibility = View.INVISIBLE

        } else {
            binding.tvEmail.setText("${user.email} (Não Verificado)")
            binding.btnVerifyEmail.visibility = View.VISIBLE
        }

        if (isManager) {
            binding.tvGerenteStatus.visibility = View.VISIBLE
        } else {
            binding.tvGerenteStatus.visibility = View.INVISIBLE
        }
    }

}