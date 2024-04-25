package com.example.pi3_es_2024_time19.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3_es_2024_time19.adapters.CardAdapter
import com.example.pi3_es_2024_time19.databinding.FragmentPaymentBinding
import com.example.pi3_es_2024_time19.models.Card
import com.google.firebase.firestore.FirebaseFirestore

class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding
    private lateinit var cardList: MutableList<Card>
    private lateinit var rvCard: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPaymentBinding.inflate(
            inflater, container, false
        )

        val collectionRef = firestore.collection("cartoes")
        cardList = mutableListOf()

        collectionRef.get()
            .addOnSuccessListener { documents ->
                cardList.clear()
                cardList.addAll(documents.toObjects(Card::class.java))
                Log.i("PaymentFragment", "Dados recuperados do Firestore: $cardList")
                setupRecyclerView()
                Log.i("PaymentFragment", "RecyclerView configurado com sucesso.")
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Erro ao recuperar os dados: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i("PaymentFragment", "Erro ao recuperar os dados: ", exception)
            }

        //Initialize views here after inflating the layout
        rvCard = binding.rvCards
        rvCard.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    private fun setupRecyclerView() {
        cardAdapter = CardAdapter(cardList) { card ->
            removeCard(card)
        }
        rvCard.adapter = cardAdapter
        rvCard.layoutManager = LinearLayoutManager(context)
        Log.i("PaymentFragment", "RecyclerView Adapter configurado.")
    }

    private fun removeCard(card: Card) {
        val collectionRef = firestore.collection("cartoes")
        collectionRef.document(card.id).delete()
            .addOnSuccessListener {
                cardList.removeAll { it.id == card.id }
                Log.i("PaymentFragment", "Cartão removido: $cardList")
                cardAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Erro ao remover o cartão: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i("PaymentFragment", "Erro ao remover o cartão: ", exception)
            }
    }

    fun updateCardList(newCard: Card) {
        cardList.add(newCard)
        rvCard.adapter?.notifyItemInserted(cardList.size - 1)
    }

    /*private fun openRegisterCardActivity() {
        val intent = Intent(requireContext(), RegisterCardActivity::class.java)
        intent.putExtra("update_card_list", ::updateCardList)
        startActivity(intent)
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAddNewCard = binding.btnAddNewCard
        //openRegisterCardActivity()
    }
}
