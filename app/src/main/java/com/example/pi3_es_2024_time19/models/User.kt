package com.example.pi3_es_2024_time19.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var age: Int = 0
) : Parcelable