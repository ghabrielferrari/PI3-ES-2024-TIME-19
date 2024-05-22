package com.example.pi3_es_2024_time19

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pi3_es_2024_time19.adapters.AdapterArmarioListItem
import com.example.pi3_es_2024_time19.adapters.HistoryAdapter
import com.example.pi3_es_2024_time19.databinding.ActivityHistoryBinding
import com.example.pi3_es_2024_time19.models.Locker
import com.example.pi3_es_2024_time19.models.Renting
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var rentings: MutableList<Renting>
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup binding
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Setup firebase
        auth = Firebase.auth
        db = Firebase.firestore

        // Setup list
        rentings = mutableListOf()

        binding.btnBack.setOnClickListener {
            navigateUpTo(Intent(this, MainActivity::class.java))
        }

        getAllRentings()
    }

    private fun getAllRentings() {
        showLoading()
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
                    rentings.add(
                        Renting(
                        doc.get("nome_armario").toString(),
                        doc.get("timestamp_inicio") as Timestamp,
                        doc.get("timestamp_fim") as Timestamp,
                        doc.get("uid").toString(),
                        doc.get("id_armario").toString(),
                            123.45
                        )
                    )
                }
                hideLoading()
                bindAdapter()
                if (rentings.isEmpty()) {
                    binding.tvNenhumArmario.visibility = View.VISIBLE
                } else {
                    binding.tvNenhumArmario.visibility = View.INVISIBLE
                }
                for (rent in rentings) {
                    println(rent)
                }
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

    private fun setupRecyclerView() {
        binding.rvRentings.layoutManager = LinearLayoutManager(this)
        binding.rvRentings.setHasFixedSize(true)
    }

    private fun bindAdapter() {
        val adapter = HistoryAdapter(rentings)
        binding.rvRentings.adapter = adapter
    }

    private fun showLoading() {
        binding.loadingSpinner.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingSpinner.visibility = View.INVISIBLE
    }

    private fun showDialog(title: String, message: String, positiveBtnText: String, negativeBtnText: String, positiveAction: () -> Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
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
            mediaPlayer = MediaPlayer.create(this, rawFile)
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
        }
        mediaPlayer.start()
    }

    private fun playErrorNotice() {
        playSound(R.raw.error_notice)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}