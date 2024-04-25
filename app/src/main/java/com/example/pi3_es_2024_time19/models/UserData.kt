package com.example.pi3_es_2024_time19.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val uid: String,
    val nome_completo: String,
    val cpf: Number,
    val data_nascimento: String,
    val telefone: String
) : Parcelable