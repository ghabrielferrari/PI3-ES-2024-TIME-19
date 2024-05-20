package com.example.pi3_es_2024_time19.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.pi3_es_2024_time19.AppEntryActivity
import com.example.pi3_es_2024_time19.CreateAccountActivity
import com.example.pi3_es_2024_time19.R
import com.example.pi3_es_2024_time19.databinding.FragmentAccountBinding
import com.example.pi3_es_2024_time19.models.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var user: FirebaseUser
    private var user_data: UserData = UserData()
    private var isManager: Boolean = false

    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFirebaseAuth()
        initFirebaseFirestore()

        // Set current user
        user = auth.currentUser as FirebaseUser
    }

    private fun signOut() {
        auth.signOut()
        playSound(R.raw.w98error_sfx)
        openEntryActivity()
    }

    private fun errorSignOut() {
        auth.signOut()
        openCreateAccountActivity()
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
                    showVerificationDialog(
                        "Verificar Email",
                        "Email enviado com sucesso.",
                        "",
                        "OK",
                        ::doNothing
                        )
                }
                .addOnFailureListener {
                    showVerificationDialog(
                        "Atenção",
                        "Seu email contém erro ou não existe. Para corrigir este erro considere criar sua conta com o email correto, deseja sair e criar uma nova conta?",
                        "Sim",
                        "Cancelar",
                        ::errorSignOut
                    )
                    playSound(R.raw.error_email)
                }
        }

        binding.btnLogout.setOnClickListener {
            Toast.makeText(context, "User Logged Out", Toast.LENGTH_SHORT).show()
            signOut()
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

    private fun openCreateAccountActivity() {
        val intent = Intent(context, CreateAccountActivity::class.java)
        startActivity(intent)
    }

    private fun getUserData() {
        showLoading()
        val query = db.collection("user_data")
            .whereEqualTo("uid", "${user.uid}")
            .get()
            .addOnSuccessListener{ documents ->
                hideLoading()
                if (documents.isEmpty) {
                    Log.d("QUERY FAILED", "Size of query is zero")
                } else {
                    for (doc in documents) {
                        user_data = doc.toObject(UserData::class.java)
                        isManager = doc.get("manager") as Boolean
                        Log.d("USER_DATA", "$user_data")
                    }
                    bindUserDataToView()
                }
            }
            .addOnFailureListener {
                hideLoading()
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

    private fun showVerificationDialog(title: String, message: String, positiveBtnText: String, negativeBtnText: String, positiveAction: () -> Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
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

    private fun playSound(rawFile: Int) {
        if (!this::mediaPlayer.isInitialized) {
            mediaPlayer = MediaPlayer.create(context, rawFile)
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
        }
        mediaPlayer.start()


    }

    private fun showLoading() {
        binding.loadingSpinner.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingSpinner.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }


}