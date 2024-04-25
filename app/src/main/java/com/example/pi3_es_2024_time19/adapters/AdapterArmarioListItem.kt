package com.example.pi3_es_2024_time19.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3_es_2024_time19.models.Locker
import com.example.pi3_es_2024_time19.R

class AdapterArmarioListItem(
    private val lockers: MutableList<Locker>,
//    private val action1Listener:
) : RecyclerView.Adapter<AdapterArmarioListItem.ArmarioViewHolder>() {


    inner class ArmarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitle: TextView = itemView.findViewById(R.id.name)
        val tvStatus: TextView = itemView.findViewById(R.id.status)
        val btnAction1: Button = itemView.findViewById(R.id.btnAction1)
        val btnAction2: Button = itemView.findViewById(R.id.btnAction2)

        init {
            // quando clicar no botão, lançar outro listener para remover o item.
            TODO()
//            ivDeleteItem.setOnClickListener {
//                listener.delete(items[adapterPosition])
//            }
//            checkBox.setOnClickListener {
//                checkListener.onItemCheck(items[adapterPosition])
//            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArmarioViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ArmarioViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}