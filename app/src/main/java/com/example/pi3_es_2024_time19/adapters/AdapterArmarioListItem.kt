package com.example.pi3_es_2024_time19.adapters

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.text.capitalize
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3_es_2024_time19.CaptureQrCode
import com.example.pi3_es_2024_time19.R
import com.example.pi3_es_2024_time19.models.Locker

class AdapterArmarioListItem(
    private val lockers: MutableList<Locker>
//    private val action1Listener: TODO
) : RecyclerView.Adapter<AdapterArmarioListItem.ArmarioViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArmarioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.armario_cliente_list_item, parent, false)
        return ArmarioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArmarioViewHolder, position: Int) {
        val currentListItem = lockers[position]
        holder.tvTitle.text = currentListItem.name
        holder.tvStatus.text = ">>> ${currentListItem.status} agora"
        if (currentListItem.isRented == true) {
//            holder.layoutLogo.setBackgroundColor(Color.parseColor("#00AA00"))
            holder.tvStatus.setTextColor(Color.parseColor("#00AA00"))
            holder.btnAction.text = "Cancelar"
            holder.btnAction.setTextColor(Color.parseColor("#ff0000"))
        } else {
            // Apenas gerente pode ver armários não alugados
            holder.layoutLogo.setBackgroundColor(Color.parseColor("#FFAA00"))
            //holder.tvStatus.setTextColor(Color.parseColor("#FFAA00"))
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
        val layoutLogo: LinearLayout = itemView.findViewById(R.id.layoutLocksLogo)

        init {

            btnAction.setOnClickListener {
                if (btnAction.text.toString().uppercase() == "liberar".uppercase()) {
                    val intent = Intent(tvTitle.context, CaptureQrCode::class.java)
                    tvTitle.context.startActivity(intent)
                }
                if (btnAction.text.toString().uppercase() == "cancelar".uppercase()) {
                    Log.d("BOTAO CANCELAR", "BOTAO CLICKADO")
                }
            }

        }
    }
}