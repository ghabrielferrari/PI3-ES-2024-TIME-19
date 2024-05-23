package com.example.pi3_es_2024_time19.fragments

import android.app.Activity
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
import com.example.pi3_es_2024_time19.R
import com.example.pi3_es_2024_time19.activities.RegisterCardActivity
import com.example.pi3_es_2024_time19.adapters.CardAdapter
import com.example.pi3_es_2024_time19.databinding.FragmentPaymentBinding
import com.example.pi3_es_2024_time19.models.Card
import com.example.pi3_es_2024_time19.models.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding
    private lateinit var cardList: MutableList<Card>
    private lateinit var collectionRef: CollectionReference
    private lateinit var rvCard: RecyclerView
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userData: UserData
    private var isManager: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Setup binding
        binding = FragmentPaymentBinding.inflate(inflater, container, false)

        // Setup collection ref
        collectionRef = firestore.collection("cartoes")
        // Setup cardList
        cardList = mutableListOf()
        // Setup firebase auth
        auth = Firebase.auth
        db = Firebase.firestore

        rvCard = binding.rvCards
        rvCard.layoutManager = LinearLayoutManager(context)
        rvCard.adapter = CardAdapter(cardList) { card ->
            removeCard(card)
        }

        loadCardsFromFirestore()

        getUserData()

        return binding.root
    }

    // Get user_data from collection in firestore
    private fun getUserData() {
        showLoading()
        db.collection("user_data")
            .whereEqualTo("uid", "${auth.currentUser?.uid}")
            .get()
            .addOnSuccessListener{ documents ->
                hideLoading()
                if (documents.isEmpty) {
                    Log.d("QUERY FAILED", "Size of query is zero")
                } else {
                    for (doc in documents) {
                        userData = doc.toObject(UserData::class.java)
                        isManager = doc.get("manager") as Boolean
                        Log.d("USER_DATA (main actiivty)", "$userData")
                        break
                    }
                    if (isManager) {
                        binding.rvCards.visibility = View.INVISIBLE
                        binding.btnAddNewCard.visibility = View.INVISIBLE
                        binding.tvNenhumCartao.visibility = View.VISIBLE
                        binding.tvNenhumCartao.text = "Você é gerente, portanto, não pode usar nem cadastrar cartões."
                    } else {
                        binding.rvCards.visibility = View.VISIBLE
                        binding.btnAddNewCard.visibility = View.VISIBLE
                    }
                }
            }
            .addOnFailureListener {
                hideLoading()
                Toast.makeText(requireContext(), "Erro ocorreu, verifique conexão e tente novamente!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeCard(card: Card) {
        collectionRef.document(card.id).delete()
            .addOnSuccessListener {
                cardList.remove(card)
                rvCard.adapter?.notifyDataSetChanged()
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
            startActivityForResult(
                Intent(requireContext(), RegisterCardActivity::class.java),
                REQUEST_CODE_ADD_CARD
            )
        }
    }

    private fun loadCardsFromFirestore() {
        showLoading()
        collectionRef
            .whereEqualTo("uid", auth.currentUser?.uid)
            .get()
            .addOnSuccessListener { documents ->
                hideLoading()
                cardList.addAll(documents.toObjects(Card::class.java))
                rvCard.adapter?.notifyDataSetChanged()
                // Change view dinamically based on num of cardss
                if (cardList.isEmpty()) {
                    binding.tvNenhumCartao.visibility = View.VISIBLE
                } else {
                    binding.tvNenhumCartao.visibility = View.INVISIBLE
                }
            }
            .addOnFailureListener { exception ->
                hideLoading()
                Toast.makeText(
                    context,
                    "Erro ao recuperar os dados: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("PaymentFragment", "Erro ao recuperar os dados: ", exception)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADD_CARD && resultCode == Activity.RESULT_OK) {
            val card = data?.getParcelableExtra<Card>(RegisterCardActivity.EXTRA_CARD)
            card?.let {
                onCardAdded(it)
            }
            println("card null? = ${card == null}")
            if (card != null) {
                cardList.add(card)
            }
        }
    }

    private fun onCardAdded(card: Card) {
        Log.i("PaymentFragment", "Cartão adicionado: $card")
        cardList.add(card)
        rvCard.adapter?.notifyItemInserted(cardList.size - 1)

        // Gerar um ID único para o cartão
        val cardId = collectionRef.document().id
        card.id = cardId

        // Adicionar ao Firestore
        collectionRef
            .document(cardId)
            .set(card)
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

    private fun showLoading() {
        binding.loadingSpinner.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingSpinner.visibility = View.INVISIBLE
    }

    companion object {
        const val REQUEST_CODE_ADD_CARD = 1001
    }
}