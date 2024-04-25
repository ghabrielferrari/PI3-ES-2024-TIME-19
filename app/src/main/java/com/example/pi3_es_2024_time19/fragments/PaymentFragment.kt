package com.example.pi3_es_2024_time19.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3_es_2024_time19.activities.RegisterCardActivity
import com.example.pi3_es_2024_time19.adapters.CardAdapter
import com.example.pi3_es_2024_time19.databinding.FragmentPaymentBinding
import com.example.pi3_es_2024_time19.models.Card
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class PaymentFragment : Fragment(), RegisterCardActivity.OnCardAddedListener {

    private lateinit var binding: FragmentPaymentBinding
    private lateinit var cardList: MutableList<Card>
    private lateinit var collectionRef: CollectionReference
    private lateinit var rvCard: RecyclerView
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)

        collectionRef = firestore.collection("cartoes")
        cardList = mutableListOf()

        rvCard = binding.rvCards
        rvCard.layoutManager = LinearLayoutManager(context)
        rvCard.adapter = CardAdapter(cardList) { card ->
            removeCard(card)
        }

        loadCardsFromFirestore()

        return binding.root
    }


    private fun removeCard(card: Card) {
        collectionRef.document(card.id).delete()
            .addOnSuccessListener {
                cardList.removeAll { it.id == card.id }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Erro ao remover o cartão: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("PaymentFragment", "Erro ao remover o cartão: ", exception)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAddNewCard = binding.btnAddNewCard
        btnAddNewCard.setOnClickListener {
            startActivity(
                Intent(requireContext(), RegisterCardActivity::class.java)
            )
        }
    }

    private fun loadCardsFromFirestore() {
        collectionRef.get()
            .addOnSuccessListener { documents ->
                cardList.clear()
                cardList.addAll(documents.toObjects(Card::class.java))
                rvCard.adapter?.notifyDataSetChanged()
                Log.d("PaymentFragment", "Dados recuperados com sucesso.")
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Erro ao recuperar os dados: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("PaymentFragment", "Erro ao recuperar os dados: ", exception)
            }
    }

    private fun setupRecyclerView() {
        rvCard.adapter = CardAdapter(cardList) { card ->
            removeCard(card)
        }
        rvCard.layoutManager = LinearLayoutManager(context)
    }

    override fun onCardAdded(card: Card) {
        Log.i("PaymentFragment", "Cartão adicionado: $card")
        cardList.add(card)
        rvCard.adapter?.notifyItemInserted(cardList.size - 1)

        // Adicionar ao Firestore
        collectionRef.document(card.id).set(card)
            .addOnSuccessListener {
                Log.i("PaymentFragment", "Cartão adicionado ao Firestore com sucesso.")
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Erro ao adicionar o cartão ao Firestore: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i("PaymentFragment", "Erro ao adicionar o cartão ao Firestore: ", exception)
            }
    }



}