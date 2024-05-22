package com.example.pi3_es_2024_time19.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3_es_2024_time19.HistoryActivity
import com.example.pi3_es_2024_time19.R
import com.example.pi3_es_2024_time19.models.Renting
import com.google.firebase.Timestamp
import kotlin.math.abs
import kotlin.math.round
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class HistoryAdapter(
    private val rentings: MutableList<Renting>
): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_renting_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rentings.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get item
        val item = rentings[position]
        // Set name
        holder.tvNomeArmario.text = item.nome_armario
        // Set duration
        val hours = item.timestamp_fim.toDate().hours - item.timestamp_inicio.toDate().hours
        val minutes = abs(item.timestamp_fim.toDate().minutes - item.timestamp_inicio.toDate().minutes)
        holder.tvDuracao.text = "${hours}h ${minutes}min"
        // Set price
        val preco = item.preco
        holder.tvPreco.text = "R$${preco}"
        holder.tvDateText.text = item.timestamp_inicio.toDate().toString()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNomeArmario: TextView = view.findViewById(R.id.tvNomeArmario)
        val tvDuracao: TextView = view.findViewById(R.id.tvDuracao)
        val tvPreco: TextView = view.findViewById(R.id.tvPreco)
        val tvDateText: TextView = view.findViewById(R.id.tvDateText)

        init {
            // Define click listener for the ViewHolder's View
        }
    }
}