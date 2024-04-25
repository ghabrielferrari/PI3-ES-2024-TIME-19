package com.example.pi3_es_2024_time19

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3_es_2024_time19.models.Locker

class AdapterArmarioListItem(
    private val lockers: MutableList<Locker>
//    private val action1Listener:
) : RecyclerView.Adapter<AdapterArmarioListItem.ArmarioViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterArmarioListItem.ArmarioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.armario_cliente_list_item, parent, false)
        return ArmarioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterArmarioListItem.ArmarioViewHolder, position: Int) {
        val currentListItem = lockers[position]
        holder.tvTitle.text = currentListItem.name
        holder.tvStatus.text = currentListItem.status
        if (currentListItem.isRented == true) {
//            holder.layoutLogo.setBackgroundColor(Color.parseColor("#00AA00"))
            holder.tvStatus.setTextColor(Color.parseColor("#00AA00"))
            holder.btnAction.text = "Info"
            holder.btnDanger.text = "Cancelar"
        } else {
            // Apenas gerente pode ver armários não alugados
            holder.layoutLogo.setBackgroundColor(Color.parseColor("#FFAA00"))
            holder.tvStatus.setTextColor(Color.parseColor("#FFAA00"))
            holder.btnDanger.visibility = View.INVISIBLE
            holder.btnAction.text = "Liberar"
        }
    }

    override fun getItemCount(): Int {
        return lockers.size
    }

    inner class ArmarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitle: TextView = itemView.findViewById(R.id.name)
        val tvStatus: TextView = itemView.findViewById(R.id.status)
        val btnAction: Button = itemView.findViewById(R.id.btnAction)
        val btnDanger: Button = itemView.findViewById(R.id.btnDanger)
        val layoutLogo: LinearLayout = itemView.findViewById(R.id.layoutLocksLogo)

        init {
//            btnAction.setOnClickListener {
//                TODO
//            }

//            btnDanger.setOnClickListener {
//                TODO
//            }
        }
    }
}