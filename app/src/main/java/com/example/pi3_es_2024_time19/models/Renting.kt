package com.example.pi3_es_2024_time19.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Renting(
    val nome_armario: String,
    val timestamp_inicio: Timestamp,
    val timestamp_fim: Timestamp,
    val uid: String,
    val id_armario: String,
    val preco: Double
): Parcelable