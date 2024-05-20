package com.example.pi3_es_2024_time19.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pi3_es_2024_time19.GetLocationActivity
import com.example.pi3_es_2024_time19.R
import com.example.pi3_es_2024_time19.adapters.AdapterArmarioListItem
import com.example.pi3_es_2024_time19.RentLockerActivity
import com.example.pi3_es_2024_time19.databinding.FragmentLockersBinding
import com.example.pi3_es_2024_time19.models.Locker
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class LockersFragment : Fragment() {

    private lateinit var binding: FragmentLockersBinding
    private lateinit var lockers: MutableList<Locker>
    private lateinit var db: FirebaseFirestore
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setBinding()
        setupRecyclerView()
        setFirestore()

        binding.btnAddNovoArmario.setOnClickListener {
            openGetLocationActivity()
        }

        //setDummyListItems()
        getAllLockers()

        return binding.root
    }

    private fun setBinding() {
        binding = FragmentLockersBinding.inflate(layoutInflater)
    }

    private fun openGetLocationActivity() {
        val intent = Intent(context, GetLocationActivity::class.java)
        startActivity(intent)
    }

    private fun setFirestore() {
        db = Firebase.firestore
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
                    "Erro conexão",
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