package com.example.pi3_es_2024_time19.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
    var id: String = "",
    var numberCard: String = "",
    var fullName: String = "",
    var CPF: String = "",
    var expirationDate: String = "",
    var CCV: String = ""
) : Parcelable