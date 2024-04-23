package com.example.pi3_es_2024_time19.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pi3_es_2024_time19.AppEntryActivity
import com.example.pi3_es_2024_time19.databinding.FragmentAccountBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFirebaseAuth()

        // Set current user
        user = auth.currentUser as FirebaseUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        binding.btnLogout.setOnClickListener {
            Toast.makeText(context, "User Logged Out", Toast.LENGTH_SHORT).show()
            auth.signOut()
            openEntryActivity()
        }
        return binding.root
    }


    private fun initFirebaseAuth() {
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    private fun openEntryActivity() {
        val intent = Intent(context, AppEntryActivity::class.java)
        startActivity(intent)
    }

}