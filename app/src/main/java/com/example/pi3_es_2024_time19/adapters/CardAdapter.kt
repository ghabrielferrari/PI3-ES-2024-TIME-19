package com.example.pi3_es_2024_time19.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3_es_2024_time19.R
import com.example.pi3_es_2024_time19.models.Card

class CardAdapter(
    private val cardList: MutableList<Card>,
    private val onDeleteCard: (Card) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCard: TextView = itemView.findViewById(R.id.textCard)
        val btnRemove: TextView = itemView.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.item_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]
        val lastFourDigits = card.numberCard.takeLast(4)
        holder.textCard.text = "MasterCard ****$lastFourDigits"

        holder.btnRemove.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Remover Cartão")
            builder.setMessage("Tem certeza que deseja remover este cartão?")

            builder.setPositiveButton("Sim") { _, _ ->
                onDeleteCard(card)
            }

            builder.setNegativeButton("Não") { _, _ ->
                // Não faz nada, apenas fecha o AlertDialog
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return cardList.size
    }
}
