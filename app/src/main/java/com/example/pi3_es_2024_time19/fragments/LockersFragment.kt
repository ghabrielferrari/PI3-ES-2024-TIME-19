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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pi3_es_2024_time19.GetLocationActivity
import com.example.pi3_es_2024_time19.R
import com.example.pi3_es_2024_time19.adapters.AdapterArmarioListItem
import com.example.pi3_es_2024_time19.RentLockerActivity
import com.example.pi3_es_2024_time19.databinding.FragmentLockersBinding
import com.example.pi3_es_2024_time19.models.Locker
import com.example.pi3_es_2024_time19.models.Renting
import com.example.pi3_es_2024_time19.models.UserData
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.sql.Time

class LockersFragment : Fragment() {

    private lateinit var binding: FragmentLockersBinding
    private lateinit var lockers: MutableList<Locker>
    private lateinit var rentings: MutableList<Renting>
    private lateinit var db: FirebaseFirestore
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var auth: FirebaseAuth
    private lateinit var userData: UserData
    private var isManager = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Setup binding
        binding = FragmentLockersBinding.inflate(layoutInflater)
        setupRecyclerView()

        // Setup lists
        lockers = mutableListOf()
        rentings = mutableListOf()

        // Setup firebase
        auth = Firebase.auth
        db = Firebase.firestore

        binding.btnAddNovoArmario.setOnClickListener {
            openGetLocationActivity()
        }
        getUserData()

        return binding.root
    }

    private fun openGetLocationActivity() {
        val intent = Intent(context, GetLocationActivity::class.java)
        startActivity(intent)
    }

    private fun setupRecyclerView() {
        binding.rvArmarios.layoutManager = LinearLayoutManager(context)
        binding.rvArmarios.setHasFixedSize(false)
    }

    private fun bindAdapter() {
        val adapter = AdapterArmarioListItem(lockers)
        binding.rvArmarios.adapter = adapter
    }

    private fun setDummyListItems() {
        lockers = mutableListOf()
        val li1 = Locker("Armário 1", "Alugando", true)
        val li2 = Locker("Armário 2", "Alugando", true)
        val li3 = Locker("Armário 3", "Livre", false)

        lockers.add(li1)
        lockers.add(li2)
        lockers.add(li3)
    }

    private fun getUserData() {
        showLoading()
        val query = db.collection("user_data")
            .whereEqualTo("uid", "${auth.currentUser?.uid}")
            .get()
            .addOnSuccessListener{ documents ->
                hideLoading()
                for (doc in documents) {
                    userData = doc.toObject(UserData::class.java)
                    isManager = doc.get("manager") as Boolean
                    Log.d("USER_DATA", "${userData}")
                    break
                }
                println("isManager=$isManager")
                if (isManager) {
                    binding.btnAddNovoArmario.visibility = View.INVISIBLE
                    getAllLockers()
                } else {
                    getAllRentings()
                }
                println("isManager = ${userData.isManager}")
            }
            .addOnFailureListener {
                hideLoading()
                Toast.makeText(context, "Erro ocorreu, verifique conexão e tente novamente!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getAllLockers() {
        showLoading()
        db.collection("armarios")
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    lockers.add(Locker(
                        doc.get("name").toString(),
                        doc.get("status").toString(),
                        doc.get("isRented") as Boolean
                    ))
                }
                bindAdapter()
                hideLoading()
            }
            .addOnFailureListener {
                hideLoading()
                showDialog(
                    "Erro na conexão",
                    "Você provavelmente está sem internet, se esse não for o caso, tente novamente mais tarde",
                    "",
                    "OK",
                    ::playErrorNotice
                    )
            }
    }

    private fun getAllRentings() {
        db.collection("locacao")
            .whereEqualTo("uid", auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { docs ->
                if (docs.isEmpty) {
                    binding.tvNenhumArmario.visibility = View.VISIBLE
                    hideLoading()
                    return@addOnSuccessListener
                } else {
                    binding.tvNenhumArmario.visibility = View.INVISIBLE
                }
                for (doc in docs) {
                    if ((doc.get("timestamp_fim") as Timestamp).compareTo(Timestamp.now()) > 0) {
                        lockers.add(Locker(
                            doc.get("nome_armario").toString(),
                            "Alugando",
                            true
                        ))
                    }
                    rentings.add(Renting(
                        doc.get("nome_armario").toString(),
                        doc.get("timestamp_inicio") as Timestamp,
                        doc.get("timestamp_fim") as Timestamp,
                        doc.get("uid").toString(),
                        doc.get("id_armario").toString(),
                        123.45
                    ))
                }
                hideLoading()
                bindAdapter()
                if (lockers.isEmpty()) {
                    binding.tvNenhumArmario.visibility = View.VISIBLE
                } else {
                    binding.tvNenhumArmario.visibility = View.INVISIBLE
                }
            }
            .addOnFailureListener {
                showDialog(
                    "Erro na conexão",
                    "Você provavelmente está sem internet, se esse não for o caso, tente novamente mais tarde",
                    "",
                    "OK",
                    ::playErrorNotice
                )
            }
    }

    private fun showDialog(title: String, message: String, positiveBtnText: String, negativeBtnText: String, positiveAction: () -> Unit) {
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
            mediaPlayer = MediaPlayer.create(requireContext(), rawFile)
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.seekTo(0)
            }
            mediaPlayer.start()
        }
    }

    private fun playErrorNotice() {
        playSound(R.raw.error_notice)
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
            mediaPlayer.release()
        }
    }

}