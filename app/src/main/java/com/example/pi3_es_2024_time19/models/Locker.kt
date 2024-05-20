package com.example.pi3_es_2024_time19.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Locker(
    val name: String,
    val status: String,
    val isRented: Boolean = false
): Parcelable